package com.bwton.agg.trade.service.impl;

import com.bwton.agg.api.ChannelPreRest;
import com.bwton.agg.api.MerchantRest;
import com.bwton.agg.bean.bo.ChannelMerchantQueryBO;
import com.bwton.agg.bean.bo.ChannelQueryBO;
import com.bwton.agg.bean.bo.ChannelRefundQueryBO;
import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.ChannelQueryVO;
import com.bwton.agg.bean.vo.ChannelRefundQueryVO;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.constant.DateConstants;
import com.bwton.agg.common.enums.ChannelIdEnum;
import com.bwton.agg.common.enums.TradeTypeEnum;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.enums.TxTradeStatusEnum;
import com.bwton.agg.enums.InterfaceTypeEnum;
import com.bwton.agg.trade.config.TxTradeConfig;
import com.bwton.agg.trade.dao.TxChannelDao;
import com.bwton.agg.trade.dao.TxTradeDao;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.agg.trade.service.JobOffsetService;
import com.bwton.agg.trade.service.OrderService;
import com.bwton.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_MERCHANT_NO_NOT_EXISTS;

/**
 * 定时任务补偿服务。到渠道查询交易结果，然后更新平台交易数据
 *
 * @author DengQiongChuan
 * @date 2019-12-12 20:43
 */
@Service
@Slf4j
public class JobOffsetServiceImpl implements JobOffsetService {
    @Autowired
    private TxTradeConfig config;

    @Autowired
    private TxChannelDao txChannelDao;

    @Autowired
    private TxTradeDao txTradeDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChannelPreRest channelPreRest;

    @Autowired
    private MerchantRest merchantRest;

    // 渠道交易支付补偿。N 分钟内的支付
    @Override
    public boolean txChannelPayOffset() {
        try{
            Date startTime = DateUtils.addMinutes(new Date(), -config.getTxChannelOffsetTime());
            List<TxChannelEntity> txChannelEntities = txChannelDao.listByTxStatusAndTime(
                    startTime, TxChannelStatusEnum.WAIT_PAY.getCode(), TradeTypeEnum.ACTIVE_SCAN_QR_CODE.getCode());
            processTxChannel(txChannelEntities);
            return true;
        }catch (Exception e){
            log.error("渠道交易支付补偿出现异常", e);
            return false;
        }

    }

    /**
     *  渠道交易退款补偿。退款补偿单独处理。退到银行卡是非实时的，每个银行的处理速度不同，一般发起退款后1-3个工作日内到账。
     */
    @Override
    public boolean txChannelRefundOffset() {
        log.info("====== 退款查询渠道补偿开始 =====");
        Date startTime = DateUtils.addHours(new Date(), -config.getTxchannelRefundHours());
        Integer pageSize = config.getTxChannelPageSize();
        List<TxChannelEntity> txChannelEntities =
                txChannelDao.listRefundByTxChannelTimeAndTxStatus(startTime, TxChannelStatusEnum.PROCESSING.getCode(), pageSize);

        int totalCount = txChannelEntities.size();
        log.info("{}待补偿的渠道交易支付记录数:{}", DateFormatUtils.format(new Date(), DateConstants.YYYYMMDDHHMMSS), totalCount);

        processTxChannelRefund(txChannelEntities);
        log.info("====== 退款查询渠道补偿结束 =====");
        return true;
    }

    private void processTxChannel(List<TxChannelEntity> txChannelEntities) {
        if (txChannelEntities == null || txChannelEntities.size() == 0) {
            return;
        }
        for (TxChannelEntity txChannelEntity : txChannelEntities) {
            Result<ChannelMerchantVO> channelMerchantVOResult = merchantRest.getChannelMerchant(
                    new ChannelMerchantQueryBO(txChannelEntity.getChannelId(), txChannelEntity.getChannelMerId()));
            if (!channelMerchantVOResult.isSuccess()) {
                log.error(CHANNEL_MERCHANT_NO_NOT_EXISTS.getMsg());
                continue;
            }
            ChannelMerchantVO channelMerchantVO = channelMerchantVOResult.getResult();
            ChannelQueryBO channelQueryBO = buildChannelQueryBO(txChannelEntity, channelMerchantVO);
            Result<ChannelQueryVO> channelQueryVOResult = channelPreRest.query(channelQueryBO);
            if (!channelQueryVOResult.isSuccess()) {
                continue;
            }
            //渠道返回订单未支付关闭订单
            ChannelQueryVO channelQueryVO = channelQueryVOResult.getResult();
            if(StringUtils.isNotEmpty(channelQueryVO.getTxSerialNo())){
                channelQueryVO.setTxSerialNo(txChannelEntity.getTxSerialNo());
            }
            TxChannelStatusEnum statusEnum = TxChannelStatusEnum.get(channelQueryVO.getTxStatus());
            switch (statusEnum) {
                case PAID:
                    TxChannelEntity txChannelEntity4Success = buildTxChannelEntity4Success(channelQueryVO);
                    TxTradeEntity txTradeEntity4Success =
                            buildTxTradeEntity4Success(txChannelEntity.getTxTradeNo(), txChannelEntity4Success);
                    orderService.updateOrderStatus(txChannelEntity4Success,txTradeEntity4Success);
                    break;
                case FAILED:
                    TxChannelEntity txChannelEntity4Fail = buildTxChannelEntity4Fail(channelQueryVO);
                    TxTradeEntity txTradeEntity4Fail = buildTxTradeEntity4Fail(txChannelEntity.getTxTradeNo());
                    orderService.updateOrderStatus(txChannelEntity4Fail,txTradeEntity4Fail);
                    break;
                // 处理中、已关闭、退款成功状态不做更新
                default:
                    break;
            }
        }
    }

    private void processTxChannelRefund(List<TxChannelEntity> txChannelEntities) {
        if (txChannelEntities == null || txChannelEntities.size() == 0) {
            return;
        }
        for (TxChannelEntity txChannelEntity : txChannelEntities) {
            TxTradeEntity txTradeEntity = txTradeDao.getByTxTradeNo(txChannelEntity.getTxTradeNo());
            if (txTradeEntity == null) {
                log.error("查询异常：txChannelEntity关联的txTrade为空");
                return;
            }
            Integer txChannelStatus = txChannelEntity.getTxStatus();
            if (TxChannelStatusEnum.PROCESSING.getCode().equals(txChannelStatus)) {
                Result<ChannelMerchantVO> channelMerchantVOResult = merchantRest.getChannelMerchant(
                        new ChannelMerchantQueryBO(txChannelEntity.getChannelId(), txChannelEntity.getChannelMerId()));
                if (!channelMerchantVOResult.isSuccess()) {
                    log.error(CHANNEL_MERCHANT_NO_NOT_EXISTS.getMsg());
                    continue;
                }
                ChannelMerchantVO channelMerchantVO = channelMerchantVOResult.getResult();
                ChannelRefundQueryBO channelRefundQueryBO = buildChannelRefundQueryBO(txChannelEntity, channelMerchantVO);
                Result<ChannelRefundQueryVO> channelRefundQueryVOResult = channelPreRest.refundQuery(channelRefundQueryBO);
                if (!channelRefundQueryVOResult.isSuccess()) {
                    continue;
                }
                ChannelRefundQueryVO channelRefundQueryVO = channelRefundQueryVOResult.getResult();
                // created by wjl,2020/1/2,v1.0.0 当前退款查询到的列表为空，直接执行下个查询
                if(channelRefundQueryVO.getList().isEmpty()){
                    continue;
                }
                // created by wjl,2020/1/2,v1.0.0  当前使用退款单号查询，列表中只会存在一条数据。若后期使用交易订单号查询，则可能存在多个列表
                String refundStatus = channelRefundQueryVO.getList().get(0).getRefundStatus();
                Date now = new Date();
                //根据渠道补偿返回，修改数据 tx_trade 和 tx_channel的状态
                switch (refundStatus) {
                    case CommonConstants.SUCCESS:
                        txChannelEntity.setTxStatus(TxChannelStatusEnum.PAID.getCode());
                        txChannelEntity.setGmtModified(now);
                        txTradeEntity.setTxStatus(TxTradeStatusEnum.PAID.getCode());
                        txTradeEntity.setGmtModified(now);
                        orderService.updateOrderStatus(txChannelEntity,txTradeEntity);
                        //原订单状态修改为：转入退款
                        TxChannelEntity orgTxChannelEntity = txChannelDao.getByTxSerialNo(txChannelEntity.getOrgTxSerialNo());
                        if (orgTxChannelEntity != null) {
                            orgTxChannelEntity.setTxStatus(TxChannelStatusEnum.REFUND.getCode());
                            txChannelDao.update(orgTxChannelEntity);
                        }
                        break;
                    case CommonConstants.FAIL:
                        txChannelEntity.setTxStatus(TxChannelStatusEnum.FAILED.getCode());
                        txChannelEntity.setGmtModified(now);
                        txTradeEntity.setTxStatus(TxTradeStatusEnum.FAILED.getCode());
                        txTradeEntity.setGmtModified(now);
                        orderService.updateOrderStatus(txChannelEntity,txTradeEntity);
                        break;
                    case CommonConstants.PROCESSING:
                        // do nothing
                        break;
                    case CommonConstants.NOTSURE:
                        log.info("渠道返回是未确定状态,需要商户原退款单号重新发起");
                        //如果渠道返回是未确定状态， 需要商户原退款单号重新发起
                        OrderRefundBO orderRefundBO = new OrderRefundBO();
                        //组装base信息
                        orderRefundBO.setVersion(CommonConstants.VERSION);
                        orderRefundBO.setCharset(CommonConstants.UTF_8);
                        orderRefundBO.setSignType(CommonConstants.DEFAULT_SIGN_TYPE);
                        orderRefundBO.setMchId(txTradeEntity.getMerchantId());
                        orderRefundBO.setDeviceInfo(txTradeEntity.getDeviceNo());
                        orderRefundBO.setNonceStr(String.valueOf(System.currentTimeMillis()));
                        orderRefundBO.setOutTradeNo(txTradeEntity.getOrgTxTradeNo());
                        orderRefundBO.setOutRefundNo(txTradeEntity.getTxTradeNo());
                        orderRefundBO.setRefundFee(txTradeEntity.getCashFee());
                        orderRefundBO.setService(InterfaceTypeEnum.REFUND.getMethod());
                        log.info("商户原退款单号重新发起请求参数,orderRefundBO={}",orderRefundBO);
                        orderService.refund(orderRefundBO);
                        break;
                    case CommonConstants.CHANGE:
                        txChannelEntity.setTxStatus(TxChannelStatusEnum.THIRD_PAID.getCode());
                        txChannelEntity.setGmtModified(now);
                        txTradeEntity.setTxStatus(TxTradeStatusEnum.THIRD_PAID.getCode());
                        txTradeEntity.setGmtModified(now);
                        orderService.updateOrderStatus(txChannelEntity,txTradeEntity);
                        break;
                    default:
                        // do nothing
                        break;
                }
            }
        }
    }

    private ChannelRefundQueryBO buildChannelRefundQueryBO(TxChannelEntity txChannelEntity, ChannelMerchantVO channelMerchantVO) {
        ChannelRefundQueryBO channelRefundQueryBO = new ChannelRefundQueryBO();
        BeanUtils.copyProperties(channelMerchantVO,channelRefundQueryBO);
        channelRefundQueryBO.setOutTradeNo(txChannelEntity.getOrgTxSerialNo());
        channelRefundQueryBO.setOutRefundNo(txChannelEntity.getTxSerialNo());
        channelRefundQueryBO.setChannelGroup(ChannelIdEnum.getChannelGroup(channelMerchantVO.getChannelId()));
        return channelRefundQueryBO;
    }

    private ChannelQueryBO buildChannelQueryBO(TxChannelEntity txChannelEntity, ChannelMerchantVO channelMerchantVO) {
        ChannelQueryBO channelQueryBO = new ChannelQueryBO();
        channelQueryBO.setTxSerialNo(txChannelEntity.getTxSerialNo());
        channelQueryBO.setGlobalSeq(txChannelEntity.getGlobalSeq());
        channelQueryBO.setChannelId(txChannelEntity.getChannelId());
        channelQueryBO.setBaseUrl(channelMerchantVO.getBaseUrl());
        channelQueryBO.setSignType(channelMerchantVO.getSignType());
        channelQueryBO.setPlatPriKey(channelMerchantVO.getPlatPriKey());
        channelQueryBO.setChannelPubKey(channelMerchantVO.getChannelPubKey());
        channelQueryBO.setChannelMerId(txChannelEntity.getChannelMerId());
        channelQueryBO.setSecureKey(channelMerchantVO.getSecureKey());
        channelQueryBO.setSecurePath(channelMerchantVO.getSecurePath());
        channelQueryBO.setChannelGroup(ChannelIdEnum.getChannelGroup(txChannelEntity.getChannelId()));
        return channelQueryBO;
    }

    private TxChannelEntity buildTxChannelEntity4Success(ChannelQueryVO channelQueryVO) {
        TxChannelEntity entity = new TxChannelEntity();
        entity.setTxSerialNo(channelQueryVO.getTxSerialNo());
        entity.setTxStatus(TxChannelStatusEnum.PAID.getCode());
        entity.setOutTradeType(channelQueryVO.getOutTradeType());
        entity.setCashFee(channelQueryVO.getCashFee());
        entity.setCouponFee(channelQueryVO.getCouponFee());
        entity.setFeeType(channelQueryVO.getFeeType());
        entity.setOutSerialNo(channelQueryVO.getOutSerialNo());
        entity.setOutThirdPartyNo(channelQueryVO.getOutThirdPartyNo());
        entity.setOutEndTime(channelQueryVO.getOutEndTime());
        entity.setOutSettleDate(channelQueryVO.getOutSettleDate());
        entity.setOutRespCode(channelQueryVO.getOutRespCode());
        entity.setOutRespDesc(channelQueryVO.getOutRespDesc());
        return entity;
    }

    private TxChannelEntity buildTxChannelEntity4Fail(ChannelQueryVO channelQueryVO) {
        TxChannelEntity entity = new TxChannelEntity();
        entity.setTxSerialNo(channelQueryVO.getTxSerialNo());
        entity.setTxStatus(TxChannelStatusEnum.FAILED.getCode());
        entity.setOutRespCode(channelQueryVO.getOutRespCode());
        entity.setOutRespDesc(channelQueryVO.getOutRespDesc());
        return entity;
    }

    private TxTradeEntity buildTxTradeEntity4Success(String txTradeNo, TxChannelEntity txChannelEntity) {
        TxTradeEntity entity = new TxTradeEntity();
        entity.setTxTradeNo(txTradeNo);
        entity.setCashFee(txChannelEntity.getCashFee());
        entity.setCouponFee(txChannelEntity.getCouponFee());
        entity.setFeeType(txChannelEntity.getFeeType());
        entity.setTxStatus(TxTradeStatusEnum.PAID.getCode());
        return entity;
    }

    private TxTradeEntity buildTxTradeEntity4Fail(String txTradeNo) {
        TxTradeEntity entity = new TxTradeEntity();
        entity.setTxTradeNo(txTradeNo);
        entity.setTxStatus(TxTradeStatusEnum.FAILED.getCode());
        return entity;
    }

}
