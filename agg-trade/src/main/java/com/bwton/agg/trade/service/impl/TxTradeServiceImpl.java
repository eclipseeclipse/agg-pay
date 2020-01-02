package com.bwton.agg.trade.service.impl;

import com.bwton.agg.bean.bo.OrderCloseBO;
import com.bwton.agg.bean.bo.OrderPayBO;
import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.common.constant.DateConstants;
import com.bwton.agg.common.enums.TradeTypeEnum;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.enums.TxTradeStatusEnum;
import com.bwton.agg.common.util.TimeUtils;
import com.bwton.agg.common.util.id.IdWorker;
import com.bwton.agg.trade.dao.TxTradeDao;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.agg.trade.service.TxChannelService;
import com.bwton.agg.trade.service.TxTradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Service
@Slf4j
public class TxTradeServiceImpl implements TxTradeService {
    @Autowired
    private TxTradeDao txTradeDao;
    @Autowired
    private TxChannelService txChannelService;
    @Autowired
    private IdWorker idWorker;
    @Value("${trade.expire.time}")
    private Integer expireTime;

    @Override
    public TxTradeEntity createPlaceOrder(OrderPayBO orderPayBO, TradeRouteVO tradeRouteVO) {
        TxTradeEntity txTradeEntity = buildTxTradeEntity(orderPayBO, tradeRouteVO);
        txTradeDao.insert(txTradeEntity);
        return txTradeEntity;
    }

    @Override
    public TxTradeEntity updatePlaceOrder(TxTradeEntity txTradeEntity, ChannelPayVO channelPayVO) {
        txTradeEntity.setCodeUrl(channelPayVO.getCodeUrl());
        txTradeEntity.setCodeImgUrl(channelPayVO.getCodeImgUrl());
        txTradeDao.update(txTradeEntity);
        return txTradeEntity;
    }

    @Override
    public int updateTxTradeEntity(TxTradeEntity txTradeEntity) {
        return txTradeDao.update(txTradeEntity);
    }

    @Override
    public TxTradeEntity createCloseTxTrade(OrderCloseBO orderCloseBO, TxTradeEntity orgTxTrade, MerchantVO merchantVO) {
        TxTradeEntity txTradeEntity = new TxTradeEntity();
        txTradeEntity.setTxTradeNo(idWorker.nextIdString());
        txTradeEntity.setOrgTxTradeNo(orgTxTrade.getTxTradeNo());
        txTradeEntity.setMerchantId(orderCloseBO.getMchId());
        txTradeEntity.setDeviceNo(orderCloseBO.getDeviceInfo());
        txTradeEntity.setMerOrderNo(orderCloseBO.getOutTradeNo());
        txTradeEntity.setTradeType(TradeTypeEnum.CLOSE.getCode());
        txTradeEntity.setTotalFee(orgTxTrade.getTotalFee());
        txTradeEntity.setCashFee(orgTxTrade.getCashFee());
        txTradeEntity.setCouponFee(orgTxTrade.getCouponFee());
        txTradeEntity.setFeeType(orgTxTrade.getFeeType());
        Date now = new Date();
        txTradeEntity.setMerTxTime(TimeUtils.dateToStr(now, DateConstants.YYYYMMDDHHMMSS));
        txTradeEntity.setMerTimeExpire(TimeUtils.addMinutes(now, expireTime, DateConstants.YYYYMMDDHHMMSS));
        txTradeEntity.setMerOrderDesc("关闭订单");
        txTradeEntity.setTxStatus(TxTradeStatusEnum.PROCESSING.getCode());
        txTradeEntity.setOperationDate(TimeUtils.dateToStr(now, DateConstants.YYYYMMDD));
        txTradeEntity.setProductId(orgTxTrade.getProductId());
        txTradeEntity.setMerchantIp(orgTxTrade.getMerchantIp());
        txTradeEntity.setGlobalSeq(orderCloseBO.getGlobalSeq());
        txTradeEntity.setGmtCreate(now);
        txTradeEntity.setGmtModified(now);
        txTradeDao.insert(txTradeEntity);
        txChannelService.createProcessingTxChannel(txTradeEntity,merchantVO);
        log.info("关闭订单>>tx_trade、tx_channel 交易入库成功");
        return txTradeEntity;
    }

    @Override
    public TxTradeEntity getByMerOrderNoAndMerchantId(String outTradeNo, String merchantId) {
        TxTradeEntity queryEntity = new TxTradeEntity();
        queryEntity.setMerOrderNo(outTradeNo);
        queryEntity.setMerchantId(merchantId);
        return txTradeDao.getByMerOrderNoAndMerchantId(queryEntity);
    }

    @Override
    public TxTradeEntity getByTxTradeNoAndMerchantId(String txTradeNo, String merchantId) {
        TxTradeEntity queryEntity = new TxTradeEntity();
        queryEntity.setTxTradeNo(txTradeNo);
        queryEntity.setMerchantId(merchantId);
        return txTradeDao.getByTxTradeNoAndMerchantId(queryEntity);
    }

    @Override
    public TxChannelEntity createRefundTxTrade(OrderRefundBO orderRefundBO, TxTradeEntity orgTxTrade, MerchantVO merchantVO) {
        TxTradeEntity txTradeEntity = new TxTradeEntity();
        txTradeEntity.setTxTradeNo(idWorker.nextIdString());
        txTradeEntity.setOrgTxTradeNo(orgTxTrade.getTxTradeNo());
        txTradeEntity.setMerchantId(orderRefundBO.getMchId());
        txTradeEntity.setDeviceNo(orderRefundBO.getDeviceInfo());
        txTradeEntity.setMerOrderNo(orderRefundBO.getOutRefundNo());
        txTradeEntity.setTradeType(TradeTypeEnum.REFUND.getCode());
        txTradeEntity.setTotalFee(orderRefundBO.getRefundFee());
        txTradeEntity.setCashFee(orderRefundBO.getRefundFee());
        txTradeEntity.setCouponFee(0L);
        txTradeEntity.setFeeType(orgTxTrade.getFeeType());
        Date now = new Date();
        txTradeEntity.setMerTxTime(TimeUtils.dateToStr(now, DateConstants.YYYYMMDDHHMMSS));
        txTradeEntity.setMerTimeExpire(TimeUtils.addMinutes(now, expireTime, DateConstants.YYYYMMDDHHMMSS));
        txTradeEntity.setMerOrderDesc("订单退款");
        txTradeEntity.setTxStatus(TxTradeStatusEnum.PROCESSING.getCode());
        txTradeEntity.setOperationDate(TimeUtils.dateToStr(now, DateConstants.YYYYMMDD));
        txTradeEntity.setProductId(orgTxTrade.getProductId());
        txTradeEntity.setMerchantIp(orgTxTrade.getMerchantIp());
        txTradeEntity.setGlobalSeq(orderRefundBO.getGlobalSeq());
        txTradeEntity.setGmtCreate(now);
        txTradeEntity.setGmtModified(now);
        txTradeDao.insert(txTradeEntity);
        return txChannelService.createProcessingTxChannel(txTradeEntity,merchantVO);
    }

    @Override
    public Integer accountRefundTotalByOrgSerialNo(String txTradeNo) {
        String account = txTradeDao.accountRefundTotalByOrgSerialNo(txTradeNo, TxTradeStatusEnum.PAID.getCode(), TxChannelStatusEnum.REFUND.getCode());
        return StringUtils.isEmpty(account) ? 0:Integer.parseInt(account);
    }

    private TxTradeEntity buildTxTradeEntity(OrderPayBO orderPayBO, TradeRouteVO tradeRouteVO) {
        TxTradeEntity tradeEntity = new TxTradeEntity();
        tradeEntity.setTxTradeNo(idWorker.nextIdString());
        tradeEntity.setMerchantId(orderPayBO.getMchId());
        tradeEntity.setDeviceNo(orderPayBO.getDeviceInfo());
        tradeEntity.setMerOrderNo(orderPayBO.getOutTradeNo());
        tradeEntity.setTradeType(TradeTypeEnum.ACTIVE_SCAN_QR_CODE.getCode());
        tradeEntity.setTotalFee(orderPayBO.getTotalFee());
        // cash_fee, coupon_fee, fee_type需要用户支付后，渠道通知后再更新
        String merTxTime = StringUtils.isNotBlank(orderPayBO.getTimeStart())
                ? orderPayBO.getTimeStart() : DateFormatUtils.format(new Date(), DateConstants.YYYYMMDDHHMMSS);
        String merTimeExpire = StringUtils.isNotBlank(orderPayBO.getTimeExpire())
                ? orderPayBO.getTimeExpire() : TimeUtils.addMinutes(tradeRouteVO.getOrderValidTime(), DateConstants.YYYYMMDDHHMMSS);
        tradeEntity.setMerTxTime(merTxTime);
        tradeEntity.setMerTimeExpire(merTimeExpire);
        tradeEntity.setMerOrderDesc(orderPayBO.getBody());
        tradeEntity.setTxStatus(TxTradeStatusEnum.WAIT_PAY.getCode());
        tradeEntity.setOperationDate(orderPayBO.getOperationDate());
        tradeEntity.setProductId(orderPayBO.getProductId());
        tradeEntity.setMerchantIp(orderPayBO.getMchCreateIp());
        tradeEntity.setNotifyUrl(orderPayBO.getNotifyUrl());
        tradeEntity.setReqReserved(orderPayBO.getAttach());
        // code_url, code_img_url 同步请求渠道后，渠道返回后再更新
        tradeEntity.setGlobalSeq(orderPayBO.getGlobalSeq());
        Date currentDate = new Date();
        tradeEntity.setGmtCreate(currentDate);
        tradeEntity.setGmtModified(currentDate);
        return tradeEntity;
    }
}
