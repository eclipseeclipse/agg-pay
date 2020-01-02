package com.bwton.agg.trade.service.impl;

import com.bwton.agg.api.ChannelPreRest;
import com.bwton.agg.api.MerchantRest;
import com.bwton.agg.bean.bo.ChannelCloseBO;
import com.bwton.agg.bean.bo.ChannelMerchantQueryBO;
import com.bwton.agg.bean.bo.ChannelPayBO;
import com.bwton.agg.bean.bo.ChannelRefundBO;
import com.bwton.agg.bean.bo.ChannelRefundQueryBO;
import com.bwton.agg.bean.bo.OrderCloseBO;
import com.bwton.agg.bean.bo.OrderCloseResultBO;
import com.bwton.agg.bean.bo.OrderPayBO;
import com.bwton.agg.bean.bo.OrderPayResultBO;
import com.bwton.agg.bean.bo.OrderQueryBO;
import com.bwton.agg.bean.bo.OrderQueryResultBO;
import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.bo.OrderRefundQueryBO;
import com.bwton.agg.bean.bo.OrderRefundQueryResultBO;
import com.bwton.agg.bean.bo.OrderRefundResultBO;
import com.bwton.agg.bean.vo.ChannelCloseVO;
import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.ChannelRefundQueryVO;
import com.bwton.agg.bean.vo.ChannelRefundVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.common.configuration.CommonConfig;
import com.bwton.agg.common.configuration.RedissonConfig;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.constant.DateConstants;
import com.bwton.agg.common.data.vo.UnionNotifyVO;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.enums.ApiTradeStatusEnum;
import com.bwton.agg.common.enums.ChannelIdEnum;
import com.bwton.agg.common.enums.RedisKeyEnum;
import com.bwton.agg.common.enums.ResultTradeTypeEnum;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.enums.TxTradeStatusEnum;
import com.bwton.agg.common.exception.AggPayException;
import com.bwton.agg.common.util.RedisKeyUtils;
import com.bwton.agg.enums.InterfaceTypeEnum;
import com.bwton.agg.trade.dao.TxChannelDao;
import com.bwton.agg.trade.dao.TxTradeDao;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.agg.trade.service.OrderService;
import com.bwton.agg.trade.service.TxChannelService;
import com.bwton.agg.trade.service.TxTradeService;
import com.bwton.core.web.CommonResult;
import com.bwton.exception.BusinessException;
import com.bwton.lang.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_MERCHANT_NO_NOT_EXISTS;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.LOCK_EXCEPTION;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.MERCHANT_ROUTE_FAIL;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.ORDER_IS_EXIST;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SINGLE_LIMIT_NOT_PERMIT;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SYS_ERROR;

/**
 * @Description 订单服务
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ChannelPreRest channelPreRest;
    @Autowired
    private MerchantRest merchantRest;
    @Autowired
    private TxTradeService txTradeService;
    @Autowired
    private TxChannelService txChannelService;
    @Autowired
    private RedissonConfig redisson;
    @Autowired
    private TxTradeDao txTradeDao;
    @Autowired
    private TxChannelDao txChannelDao;

    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private Gson gson;
    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public Result<OrderPayResultBO> placeOrder(OrderPayBO orderPayBO) {
        String lockKey = RedisKeyUtils.getKey(
                RedisKeyEnum.D_CLOSE_ORDER_SEQ, orderPayBO.getMchId(), orderPayBO.getOutTradeNo());
        //todo 更换redis写法参照bmp-auth
        RLock lock = redisson.getLock(lockKey);
        boolean tryLock;
        try {
            // 尝试获取锁
            tryLock = lock.tryLock(commonConfig.getLockWaitTime(), commonConfig.getLockLeaseTime(), TimeUnit.SECONDS);
            if (!tryLock) {
                return CommonResult.failure(LOCK_EXCEPTION);
            }
            // 检查是否为重复订单
            TxTradeEntity txTradeExists = txTradeService.getByMerOrderNoAndMerchantId(
                    orderPayBO.getOutTradeNo(), orderPayBO.getMchId());
            if (txTradeExists != null) {
                return CommonResult.failure(ORDER_IS_EXIST);
            }
            // 获取交易路由
            Result<TradeRouteVO> tradeRouteResult = merchantRest.getTradeRoute(orderPayBO.getMchId());
            if (!tradeRouteResult.isSuccess()) {
                return CommonResult.failure(MERCHANT_ROUTE_FAIL);
            }
            TradeRouteVO tradeRouteVO = tradeRouteResult.getResult();
            //todo 验证下金额类型为int型的最大支持金额，明确是否需要修改
            if (tradeRouteVO.getSingleLimit() != null && orderPayBO.getTotalFee() != null
                    && tradeRouteVO.getSingleLimit() < orderPayBO.getTotalFee()) {
                log.info("当前交易超出单笔限额，限额额度:{}", tradeRouteVO.getSingleLimit());
                return CommonResult.failure(SINGLE_LIMIT_NOT_PERMIT);
            }
            // 创建订单
            TxTradeEntity txTradeNew = txTradeService.createPlaceOrder(orderPayBO, tradeRouteVO);
            TxChannelEntity txChannelNew = txChannelService.createPlaceOrder(txTradeNew, tradeRouteVO);
            // 调渠道前置
            ChannelPayBO channelPayBO = buildChannelPayBO(txTradeNew, txChannelNew, tradeRouteVO);
            Result<ChannelPayVO> channelPayResult = channelPreRest.placeOrder(channelPayBO);
            if (!channelPayResult.isSuccess()) {
                //todo 错误码是否可以获取channel的错误？
                return CommonResult.failure(channelPayResult.getErrcode(), channelPayResult.getErrmsg());
            }
            // 更新订单
            ChannelPayVO channelPayVO = channelPayResult.getResult();
            txChannelNew.setTxStatus(TxChannelStatusEnum.WAIT_PAY.getCode());
            txChannelService.updatePlaceOrder(txChannelNew, channelPayVO);
            txTradeService.updatePlaceOrder(txTradeNew, channelPayVO);
            // 返回商户支付二维码
            OrderPayResultBO resultBO = new OrderPayResultBO();
            resultBO.setCodeUrl(channelPayVO.getCodeUrl());
            resultBO.setCodeImgUrl(channelPayVO.getCodeImgUrl());
            return CommonResult.success(resultBO);
        } catch (Exception e) {
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        } finally {
            redisson.unLock(lock);
        }
    }

    @Override
    public Result<OrderQueryResultBO> query(OrderQueryBO orderQueryBO) {
        try{
            TxTradeEntity orgTxTrade = null;
            //out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先,有限查询不到时，使用out_trade_no查询。
            if (StringUtils.isNotEmpty(orderQueryBO.getTransactionId())) {
                orgTxTrade = txTradeService.getByTxTradeNoAndMerchantId(orderQueryBO.getTransactionId(), orderQueryBO.getMchId());
            } else if (StringUtils.isNotEmpty(orderQueryBO.getOutTradeNo())) {
                orgTxTrade = txTradeService.getByMerOrderNoAndMerchantId(orderQueryBO.getOutTradeNo(), orderQueryBO.getMchId());
            }
            if (orgTxTrade == null) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_IS_NOT_EXIST);
            }
            // 查tx_channel
            TxChannelEntity txChannelEntity = txChannelService.getByTxTradeNo(orgTxTrade.getTxTradeNo());
            if (txChannelEntity == null) {
                return CommonResult.failure(AggPayExceptionEnum.CHANNEL_ORDER_IS_NOT_EXIST);
            }
            TxTradeStatusEnum txTradeStatusEnum = TxTradeStatusEnum.getEnum(orgTxTrade.getTxStatus());

            OrderQueryResultBO orderQueryVO = new OrderQueryResultBO();
            orderQueryVO.setTradeState(ApiTradeStatusEnum.getEnum(txTradeStatusEnum, true).getName());
            // 支付成功时返回以下信息
            if (TxTradeStatusEnum.PAID == txTradeStatusEnum) {
                orderQueryVO.setTradeType(ResultTradeTypeEnum.getEnum(txChannelEntity.getOutTradeType()).getPlatTradeType());
                orderQueryVO.setFeeType(orgTxTrade.getFeeType());
                orderQueryVO.setTotalFee(String.valueOf(orgTxTrade.getTotalFee()));
                orderQueryVO.setCashFee(String.valueOf(orgTxTrade.getCashFee()));
                orderQueryVO.setCouponFee(String.valueOf(orgTxTrade.getCouponFee()));
                orderQueryVO.setAttach(orgTxTrade.getReqReserved());
                orderQueryVO.setMchId(orderQueryBO.getMchId());
                orderQueryVO.setTransactionId(orgTxTrade.getTxTradeNo());
                orderQueryVO.setOutTradeNo(orgTxTrade.getMerOrderNo());
                orderQueryVO.setOutTransactionId(txChannelEntity.getOutThirdPartyNo());
                orderQueryVO.setTimeEnd(txChannelEntity.getOutEndTime());
            }
            return CommonResult.success(orderQueryVO);
        } catch (Exception e) {
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        }
    }

    @Override
    @Transactional
    public Result<OrderCloseResultBO> close(OrderCloseBO orderCloseBO) {
        //生成redis同步锁键值
        String redisKey = RedisKeyUtils.getKey(RedisKeyEnum.D_CLOSE_ORDER_SEQ, orderCloseBO.getMchId(), orderCloseBO.getOutTradeNo());
        RLock lock = redisson.getLock(redisKey);
        try {
            // 尝试获取锁
            boolean tryLock = lock.tryLock(commonConfig.getLockWaitTime(), commonConfig.getLockLeaseTime(), TimeUnit.SECONDS);
            if (!tryLock) {
                return CommonResult.failure(LOCK_EXCEPTION);
            }

            // 原交易订单是否存在
            TxTradeEntity orgTxTrade = txTradeService.getByMerOrderNoAndMerchantId(orderCloseBO.getOutTradeNo(), orderCloseBO.getMchId());
            if (orgTxTrade == null ) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_IS_NOT_EXIST);
            }
            //订单已关闭则幂等返回
            if (TxTradeStatusEnum.CLOSED.getCode().equals(orgTxTrade.getTxStatus())) {
                return CommonResult.success(new OrderCloseResultBO());
            }
            //待支付、支付失败的订单允许关闭订单
            if (!TxTradeStatusEnum.WAIT_PAY.getCode().equals(orgTxTrade.getTxStatus())
                    && !TxTradeStatusEnum.FAILED.getCode().equals(orgTxTrade.getTxStatus())) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_NOT_ALLOW_CLOSE);
            }

            // 渠道订单是否存在
            TxChannelEntity txChannelEntity = txChannelService.getByTxTradeNo(orgTxTrade.getTxTradeNo());
            if (txChannelEntity == null) {
                return CommonResult.failure(AggPayExceptionEnum.CHANNEL_ORDER_IS_NOT_EXIST);
            }
            //待支付、支付失败的订单允许关闭订单
            if (!TxChannelStatusEnum.WAIT_PAY.getCode().equals(txChannelEntity.getTxStatus())
                    && !TxChannelStatusEnum.FAILED.getCode().equals(txChannelEntity.getTxStatus())) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_NOT_ALLOW_CLOSE);
            }

            Result<TradeRouteVO> tradeRouteResult = merchantRest.getTradeRoute(orderCloseBO.getMchId());
            if (!tradeRouteResult.isSuccess()) {
                return CommonResult.failure(MERCHANT_ROUTE_FAIL);
            }
            TradeRouteVO tradeRouteVO = tradeRouteResult.getResult();
            //组装请求渠道前置报文参数
            ChannelCloseBO channelCloseBO = buildChannelCloseBO(tradeRouteVO, txChannelEntity);
            //请求渠道前置
            Result<ChannelCloseVO> closeResult = channelPreRest.close(channelCloseBO);

            //处理渠道返回结果
            if (!closeResult.isSuccess()) {
                return CommonResult.failure(tradeRouteResult.getErrcode(), tradeRouteResult.getErrmsg());
            }
            ChannelCloseVO channelCloseVO = closeResult.getResult();
            closeOrder(orgTxTrade.getTxTradeNo(), txChannelEntity.getTxSerialNo(), channelCloseVO.getOutRespCode(), channelCloseVO.getOutRespDesc());
            return CommonResult.success(new OrderCloseResultBO());
        } catch (Exception e) {
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        } finally {
            redisson.unLock(lock);
        }
    }


    @Override
    @Transactional
    public Result<OrderRefundResultBO> refund(OrderRefundBO orderRefundBO) {
        //生成redis同步锁键值
        String redisKey = RedisKeyUtils.getKey(RedisKeyEnum.D_REFUND_APPLY_SEQ, orderRefundBO.getMchId(), orderRefundBO.getOutTradeNo());
        RLock lock = redisson.getLock(redisKey);
        try {
            //申请redis锁
            boolean tryLock = lock.tryLock(commonConfig.getLockWaitTime(), commonConfig.getLockLeaseTime(), TimeUnit.SECONDS);
            if (!tryLock) {
                throw new BusinessException(AggPayExceptionEnum.LOCK_FAIL);
            }
            //获取原订单信息
            TxTradeEntity orgTxTrade = txTradeService.getByMerOrderNoAndMerchantId(orderRefundBO.getOutTradeNo(), orderRefundBO.getMchId());
            if (orgTxTrade == null) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_IS_NOT_EXIST);
            }
            //如果原订单不是支付成功的状态，不允许退款
            if (!orgTxTrade.getTxStatus().equals(TxTradeStatusEnum.PAID.getCode())) {
                return CommonResult.failure(AggPayExceptionEnum.ORG_ORDER_STATUS_NOT_SUCCESS);
            }

            log.info("申请退款交易>>查询订单结->> {}",gson.toJson(orgTxTrade));
            if (!TxTradeStatusEnum.PAID.getCode().equals(orgTxTrade.getTxStatus())) {
                log.error("订单号：{}，", orgTxTrade.getTxTradeNo() + AggPayExceptionEnum.ORG_ORDER_STATUS_NOT_CORRECT.getMsg());
                return CommonResult.failure(AggPayExceptionEnum.ORG_ORDER_STATUS_NOT_CORRECT);
            }

            //校验 -- 该支付订单退款金额 与订单金额比较。如果退款金额大于支付订单金额，则直接返回申请退款失败.update by cairuimin 2019/12/16
            //已退款金额
            Integer orgRefundTotal = txTradeService.accountRefundTotalByOrgSerialNo(orgTxTrade.getTxTradeNo());
            //本次退款金额
            Long refundFee = orderRefundBO.getRefundFee();
            if ( (refundFee+ orgRefundTotal) > orgTxTrade.getCashFee()) {
                log.error("订单号：{}，", orgTxTrade.getTxTradeNo() + AggPayExceptionEnum.REFUND_FEE_OVERSTEP.getMsg());
                return CommonResult.failure(AggPayExceptionEnum.REFUND_FEE_OVERSTEP);
            }
            //获取商户信息
            Result<MerchantVO> merchantVOResult = merchantRest.getChannelMerchantById(orderRefundBO.getMchId());
            if (!merchantVOResult.isSuccess()) {
                return CommonResult.failure(AggPayExceptionEnum.MERCHANT_NOT_CONFIG_ROUTE);
            }
            MerchantVO merchantVO=merchantVOResult.getResult();

            //生成退款处理中记录
            TxChannelEntity txChannelEntity = txTradeService.createRefundTxTrade(orderRefundBO, orgTxTrade, merchantVO);

            Result<TradeRouteVO> tradeRouteResult = merchantRest.getTradeRoute(orderRefundBO.getMchId());
            if (!tradeRouteResult.isSuccess()) {
                return CommonResult.failure(MERCHANT_ROUTE_FAIL);
            }

            //拼装本次请求
            ChannelRefundBO channelRefundBO= new ChannelRefundBO();
            TradeRouteVO tradeRouteVO = tradeRouteResult.getResult();
            buildChannelRefundBO(channelRefundBO,tradeRouteVO,orderRefundBO,merchantVO,txChannelEntity);

            //请求渠道前置
            Result<ChannelRefundVO> respDTOResult = channelPreRest.refund(channelRefundBO);
            //处理渠道返回结果
            if (respDTOResult.isSuccess()) {
                OrderRefundResultBO orderCloseResultBO = new OrderRefundResultBO();
                BeanUtils.copyProperties(respDTOResult.getResult(),orderCloseResultBO);
                return CommonResult.success(orderCloseResultBO);
            } else {
                log.info("申请退款>>调用渠道失败");
                return CommonResult.failure(AggPayExceptionEnum.CHANNEL_PRE_PROCESS_FAIL);
            }
        } catch (Exception e) {
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        } finally {
            redisson.unLock(lock);
        }
    }

    private void buildChannelRefundBO(ChannelRefundBO channelRefundBO, TradeRouteVO tradeRouteVO, OrderRefundBO orderRefundBO, MerchantVO merchantVO, TxChannelEntity txChannelEntity) {
        BeanUtils.copyProperties(tradeRouteVO,channelRefundBO);
        BeanUtils.copyProperties(orderRefundBO,channelRefundBO);

        channelRefundBO.setMchId(merchantVO.getChannelMerId());
        //渠道商户号
        channelRefundBO.setOpUserId(merchantVO.getChannelMerId());
        channelRefundBO.setSecureKey(merchantVO.getSecureKey());
        channelRefundBO.setChannelGroup(ChannelIdEnum.getChannelGroup(merchantVO.getChannelId()));
        channelRefundBO.setService(InterfaceTypeEnum.REFUND.getMethod());
        //请求渠道需要的 out_trade_no 和 out_refund_no
        channelRefundBO.setOutTradeNo(txChannelEntity.getOrgTxSerialNo());
        channelRefundBO.setOutRefundNo(txChannelEntity.getTxSerialNo());
        channelRefundBO.setTotalFee(txChannelEntity.getTotalFee());
        channelRefundBO.setCharset(CommonConstants.UTF_8);
        channelRefundBO.setBaseUrl(merchantVO.getBaseUrl());
        channelRefundBO.setSecureKey(merchantVO.getSecureKey());
        channelRefundBO.setNonceStr(String.valueOf(System.currentTimeMillis()));
    }

    @Override
    @Transactional
    public Result<OrderRefundQueryResultBO> refundQuery(OrderRefundQueryBO orderRefundQueryBO) {
        try{
            TxTradeEntity txTrade = txTradeService.getByMerOrderNoAndMerchantId(orderRefundQueryBO.getOutRefundNo(), orderRefundQueryBO.getMchId());
            if (txTrade == null) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_IS_NOT_EXIST);
            }
            TxTradeEntity orgTxtrade = txTradeDao.getByTxTradeNo(txTrade.getOrgTxTradeNo());
            if (orgTxtrade == null) {
                return CommonResult.failure(AggPayExceptionEnum.ORDER_IS_NOT_EXIST);
            }
            OrderRefundQueryResultBO orderRefundQueryResultBO = new OrderRefundQueryResultBO();
            //平台订单号
            orderRefundQueryResultBO.setTransactionId(orgTxtrade.getTxTradeNo());
            //商户订单号
            orderRefundQueryResultBO.setOutTradeNo(orgTxtrade.getMerOrderNo());
            //商户退款单号
            orderRefundQueryResultBO.setOutRefundNo(txTrade.getMerOrderNo());
            //平台退款单号
            orderRefundQueryResultBO.setRefundId(txTrade.getTxTradeNo());
            orderRefundQueryResultBO.setRefundFee(String.valueOf(txTrade.getCashFee()));
            //现金券退款金额
            orderRefundQueryResultBO.setCouponRefundFee(String.valueOf(txTrade.getCouponFee()));
            orderRefundQueryResultBO.setRefundTime(txTrade.getMerTxTime());
            orderRefundQueryResultBO.setRefundStatus(String.valueOf(txTrade.getTxStatus()));

            //去渠道表查一下，如果是tx_status是成功 - 2 SUCCESS的状态，就返回退款成功，
            // 4：PROCESSING—退款处理中，5-渠道未确定，返回NOTSURE未确定，需要商户原退款单号重新发起
            // 7：SUCCESS失败，返回失败，8：CHANGE转入代发，退款失败需线下退款
            TxChannelEntity txChannelEntity = txChannelDao.getByTxTradeNo(txTrade.getTxTradeNo());
            if (txChannelEntity == null) {
                return CommonResult.failure(AggPayExceptionEnum.CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }
            // set refund_status退款状态
            if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.PAID.getCode())) {
                orderRefundQueryResultBO.setRefundStatus(CommonConstants.SUCCESS);
            } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.FAILED.getCode())) {
                orderRefundQueryResultBO.setRefundStatus(CommonConstants.FAIL);
            } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.PROCESSING.getCode())) {
                //处理中状态，去渠道查证一下
                String status = processTxChannelRefund(txChannelEntity);
                orderRefundQueryResultBO.setRefundStatus(StringUtils.isBlank(status)?CommonConstants.PROCESSING:status);
            } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.UNKNOWN.getCode())) {
                orderRefundQueryResultBO.setRefundStatus(CommonConstants.NOTSURE);
            } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.THIRD_PAID.getCode())) {
                orderRefundQueryResultBO.setRefundStatus(CommonConstants.CHANGE);
            }
            return CommonResult.success(orderRefundQueryResultBO);
        }catch (Exception e){
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        }
    }

    private String processTxChannelRefund(TxChannelEntity txChannelEntity) {
        TxTradeEntity txTradeEntity = txTradeDao.getByTxTradeNo(txChannelEntity.getTxTradeNo());
        if (txTradeEntity == null) {
            log.error("查询异常：txChannelEntity关联的txTrade为空");
            return null;
        }
        Integer txChannelStatus = txChannelEntity.getTxStatus();
        if (TxChannelStatusEnum.PROCESSING.getCode().equals(txChannelStatus)) {
            Result<ChannelMerchantVO> channelMerchantVOResult = merchantRest.getChannelMerchant(
                    new ChannelMerchantQueryBO(txChannelEntity.getChannelId(), txChannelEntity.getChannelMerId()));
            if (!channelMerchantVOResult.isSuccess()) {
                log.error(CHANNEL_MERCHANT_NO_NOT_EXISTS.getMsg());
            }
            ChannelMerchantVO channelMerchantVO = channelMerchantVOResult.getResult();
            ChannelRefundQueryBO channelRefundQueryBO = buildChannelRefundQueryBO(txChannelEntity, channelMerchantVO);
            Result<ChannelRefundQueryVO> channelRefundQueryVOResult = channelPreRest.refundQuery(channelRefundQueryBO);

            ChannelRefundQueryVO channelRefundQueryVO = channelRefundQueryVOResult.getResult();
            // created by wjl,2020/1/2,v1.0.0 当前退款查询到的列表为空，直接执行下个查询
            if(channelRefundQueryVO.getList().isEmpty()){
                log.info("退款查询接口解析到的退款列表为空");
                return null;
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
        // set refund_status退款状态
        if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.PAID.getCode())) {
            return CommonConstants.SUCCESS;
        } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.FAILED.getCode())) {
            return CommonConstants.FAIL;
        } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.PROCESSING.getCode())) {
            return CommonConstants.PROCESSING;
        } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.REFUND.getCode())) {
            return CommonConstants.PROCESSING;
        } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.UNKNOWN.getCode())) {
            return CommonConstants.NOTSURE;
        } else if (txChannelEntity.getTxStatus().equals(TxChannelStatusEnum.THIRD_PAID.getCode())) {
            return CommonConstants.CHANGE;
        }
        return null;
    }

    private ChannelRefundQueryBO buildChannelRefundQueryBO(TxChannelEntity txChannelEntity, ChannelMerchantVO channelMerchantVO) {
        ChannelRefundQueryBO channelRefundQueryBO = new ChannelRefundQueryBO();
        BeanUtils.copyProperties(channelMerchantVO,channelRefundQueryBO);
        channelRefundQueryBO.setOutTradeNo(txChannelEntity.getOrgTxSerialNo());
        channelRefundQueryBO.setOutRefundNo(txChannelEntity.getTxSerialNo());
        channelRefundQueryBO.setChannelGroup(ChannelIdEnum.getChannelGroup(channelMerchantVO.getChannelId()));
        return channelRefundQueryBO;
    }

    @Transactional
    @Override
    public void updateOrderStatus(TxChannelEntity txChannelEntity, TxTradeEntity txTradeEntity) {
        txChannelDao.update(txChannelEntity);
        txTradeDao.update(txTradeEntity);
    }

    public Result<Boolean> updateOrder(UnionNotifyVO unionNotifyVO) {
        return CommonResult.success();
    }

    private OrderPayResultBO buildOrderPayResultBO(OrderPayBO orderPayBO) {
        OrderPayResultBO resultBO = new OrderPayResultBO();
        resultBO.setVersion(orderPayBO.getVersion());
        resultBO.setCharset(orderPayBO.getCharset());
        resultBO.setSignType(orderPayBO.getSignType());
        resultBO.setMchId(orderPayBO.getMchId());
        resultBO.setDeviceInfo(orderPayBO.getDeviceInfo());
        return resultBO;
    }

    private ChannelPayBO buildChannelPayBO(
            TxTradeEntity txTradeEntity, TxChannelEntity txChannelEntity, TradeRouteVO tradeRouteVO) {
        ChannelPayBO channelPayBO = new ChannelPayBO();
        channelPayBO.setGlobalSeq(txChannelEntity.getGlobalSeq());
        channelPayBO.setChannelGroup(ChannelIdEnum.getChannelGroup(tradeRouteVO.getChannelId()));

        /* 渠道商户信息 */
        channelPayBO.setService(InterfaceTypeEnum.PLACE_ORDER.getMethod());
        channelPayBO.setCharset(CommonConstants.UTF_8);
        channelPayBO.setSignType(tradeRouteVO.getSignType());
        channelPayBO.setMchId(tradeRouteVO.getChannelMerId());
        channelPayBO.setChannelId(tradeRouteVO.getChannelId());
        channelPayBO.setBaseUrl(tradeRouteVO.getBaseUrl());
        channelPayBO.setPlatPriKey(tradeRouteVO.getPlatPriKey());
        channelPayBO.setChannelPubKey(tradeRouteVO.getChannelPubKey());
        channelPayBO.setSecureKey(tradeRouteVO.getSecureKey());
        channelPayBO.setSecurePath(tradeRouteVO.getSecurePath());

        /* 渠道订单信息 */
        channelPayBO.setOutTradeNo(txChannelEntity.getTxSerialNo());
        channelPayBO.setDeviceInfo(txTradeEntity.getDeviceNo());
        channelPayBO.setBody(txTradeEntity.getMerOrderDesc());
        channelPayBO.setTotalFee(txChannelEntity.getTotalFee());
        channelPayBO.setMchCreateIp(txTradeEntity.getMerchantIp());
        channelPayBO.setNotifyUrl(commonConfig.getNotifyUrl());
        // 发给渠道的时间：订单生成时间取 tx_channel.gmt_create, 超时时间取 tx_trade.mer_time_expire
        channelPayBO.setTimeStart(DateFormatUtils.format(txChannelEntity.getGmtCreate(), DateConstants.YYYYMMDDHHMMSS));
        channelPayBO.setTimeExpire(txTradeEntity.getMerTimeExpire());
        channelPayBO.setOpUserId(tradeRouteVO.getChannelMerId());
        channelPayBO.setProductId(txTradeEntity.getProductId());

        // 请求保留域里放商户订单号和运营日期，为对账需要
        channelPayBO.setAttach(txChannelEntity.getReqReserved());

        return channelPayBO;
    }

    private void closeOrder(String txTradeNo, String txSerialNo, String code , String msg){
        //更新tx_trade
        TxTradeEntity updateTxTradeEntity = new TxTradeEntity();
        updateTxTradeEntity.setTxTradeNo(txTradeNo);
        updateTxTradeEntity.setTxStatus(TxTradeStatusEnum.CLOSED.getCode());
        int num=txTradeService.updateTxTradeEntity(updateTxTradeEntity);

        if(num==1){
            //更新tx_channel
            TxChannelEntity updateTxChannelEntity=new TxChannelEntity();
            updateTxChannelEntity.setTxSerialNo(txSerialNo);
            updateTxChannelEntity.setTxStatus(TxChannelStatusEnum.CLOSED.getCode());
            updateTxChannelEntity.setOutRespCode(code);
            updateTxChannelEntity.setOutRespDesc(msg);
            txChannelService.updateTxChannelEntity(updateTxChannelEntity);
        }
    }

    private ChannelCloseBO  buildChannelCloseBO(TradeRouteVO tradeRouteVO,TxChannelEntity orgTxChannel){
        ChannelCloseBO channelCloseBO=new ChannelCloseBO();
        channelCloseBO.setGlobalSeq(orgTxChannel.getGlobalSeq());
        channelCloseBO.setVersion(CommonConstants.VERSION);
        channelCloseBO.setNonceStr(String.valueOf(System.currentTimeMillis()));
        channelCloseBO.setChannelGroup(ChannelIdEnum.getChannelGroup(tradeRouteVO.getChannelId()));

        /* 渠道商户信息 */
        channelCloseBO.setService(InterfaceTypeEnum.CLOSE.getMethod());
        channelCloseBO.setCharset(CommonConstants.UTF_8);
        channelCloseBO.setSignType(tradeRouteVO.getSignType());
        channelCloseBO.setMchId(tradeRouteVO.getChannelMerId());
        channelCloseBO.setChannelId(tradeRouteVO.getChannelId());
        channelCloseBO.setBaseUrl(tradeRouteVO.getBaseUrl());
        channelCloseBO.setPlatPriKey(tradeRouteVO.getPlatPriKey());
        channelCloseBO.setChannelPubKey(tradeRouteVO.getChannelPubKey());
        channelCloseBO.setSecureKey(tradeRouteVO.getSecureKey());
        channelCloseBO.setSecurePath(tradeRouteVO.getSecurePath());
        channelCloseBO.setOutTradeNo(orgTxChannel.getTxSerialNo());
        return channelCloseBO;
    }


}
