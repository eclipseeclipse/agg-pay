package com.bwton.agg.trade.service.impl;

import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.common.enums.ChannelIdEnum;
import com.bwton.agg.common.enums.TradeTypeEnum;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.util.id.IdWorker;
import com.bwton.agg.trade.dao.TxChannelDao;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.agg.trade.service.TxChannelService;
import com.bwton.exception.BusinessException;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TxChannel Service
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Service
public class TxChannelServiceImpl implements TxChannelService {
    @Autowired
    TxChannelDao txChannelDao;
    @Autowired
    IdWorker idWorker;

    @Autowired
    private Gson gson;

    @Override
    public TxChannelEntity createPlaceOrder(TxTradeEntity txTradeEntity, TradeRouteVO tradeRouteVO) {
        TxChannelEntity txChannelEntity = buildTxChannelEntity(txTradeEntity, tradeRouteVO);
        txChannelDao.insert(txChannelEntity);
        return txChannelEntity;
    }

    @Override
    public TxChannelEntity updatePlaceOrder(TxChannelEntity txChannelEntity, ChannelPayVO channelPayVO) {
        txChannelEntity.setCodeUrl(channelPayVO.getCodeUrl());
        txChannelEntity.setCodeImgUrl(channelPayVO.getCodeImgUrl());
        txChannelDao.update(txChannelEntity);
        return txChannelEntity;
    }

    @Override
    public int updateTxChannelEntity(TxChannelEntity txChannelEntity) {
        return txChannelDao.update(txChannelEntity);
    }

    @Override
    public TxChannelEntity createProcessingTxChannel(TxTradeEntity txTradeEntity, MerchantVO merchantVO) {
        TxChannelEntity txChannelEntity = new TxChannelEntity();
        if(StringUtils.isNotBlank(txTradeEntity.getOrgTxTradeNo())) {
            TxChannelEntity orgTxChannelEntity = txChannelDao.getByTxTradeNo(txTradeEntity.getOrgTxTradeNo());
            if (orgTxChannelEntity == null) {
                throw new BusinessException("原渠道订单不存在");
            }
            txChannelEntity.setOrgTxSerialNo(orgTxChannelEntity.getTxSerialNo());
        }
        txChannelEntity.setTxSerialNo(idWorker.nextIdString());
        txChannelEntity.setTxTradeNo(txTradeEntity.getTxTradeNo());
        txChannelEntity.setTxChannelTime(txTradeEntity.getGmtCreate());
        txChannelEntity.setTotalFee(txTradeEntity.getTotalFee());
        txChannelEntity.setCashFee(txTradeEntity.getCashFee());
        txChannelEntity.setCouponFee(txTradeEntity.getCouponFee());
        txChannelEntity.setFeeRate(0f);
        txChannelEntity.setFeeType(txTradeEntity.getFeeType());
        txChannelEntity.setTxStatus(TxChannelStatusEnum.PROCESSING.getCode());
        txChannelEntity.setChannelId(merchantVO.getChannelId());
        txChannelEntity.setChannelMerId(merchantVO.getChannelMerId());
        txChannelEntity.setChannelGroup(ChannelIdEnum.getChannelGroup(merchantVO.getChannelId()));
        txChannelEntity.setGlobalSeq(txTradeEntity.getGlobalSeq());
        txChannelEntity.setGmtCreate(txChannelEntity.getGmtCreate());
        txChannelEntity.setGmtModified(txChannelEntity.getGmtModified());
        // 增加渠道交易类型 update by cairuimin 2019/12/16
        txChannelEntity.setTradeType(TradeTypeEnum.REFUND.getCode());
        txChannelDao.insert(txChannelEntity);

        return txChannelEntity;
    }

    @Override
    public TxChannelEntity getByTxSerialNo(String txSerialNo) {
        return txChannelDao.getByTxSerialNo(txSerialNo);
    }

    @Override
    public TxChannelEntity getByTxTradeNo(String txTradeNo) {
        return txChannelDao.getByTxTradeNo(txTradeNo);
    }

    private TxChannelEntity buildTxChannelEntity(TxTradeEntity txTradeEntity, TradeRouteVO tradeRouteVO) {
        TxChannelEntity channelEntity = new TxChannelEntity();
        channelEntity.setTxSerialNo(idWorker.nextIdString());
        channelEntity.setTxTradeNo(txTradeEntity.getTxTradeNo());
        channelEntity.setTxChannelTime(new Date());
        channelEntity.setTotalFee(txTradeEntity.getTotalFee());
        // trade_type(渠道的交易类型), cash_fee, coupon_fee, fee_type 待用户支付后，渠道通知了再更新
        channelEntity.setFeeRate(tradeRouteVO.getFeeRate());
        channelEntity.setTradeType(TradeTypeEnum.ACTIVE_SCAN_QR_CODE.getCode());
        channelEntity.setTxStatus(TxChannelStatusEnum.SAVED.getCode());
        channelEntity.setChannelId(tradeRouteVO.getChannelId());
        channelEntity.setChannelMerId(tradeRouteVO.getChannelMerId());
        channelEntity.setChannelGroup(ChannelIdEnum.getChannelGroup(tradeRouteVO.getChannelId()));
        // out_serial_no, out_end_time, out_settle_date, out_resp_code, out_resp_desc 待用户支付后，渠道通知了再更新
        // code_url, code_img_url 待同步请求渠道，渠道响应后更新
        channelEntity.setGlobalSeq(txTradeEntity.getGlobalSeq());
        Date currentDate = new Date();
        channelEntity.setGmtCreate(currentDate);
        channelEntity.setGmtModified(currentDate);
        channelEntity.setTradeType(txTradeEntity.getTradeType());

        // 请求保留域里放商户订单号和运营日期，为对账需要
        Map<String, String> reqReservedMap = new HashMap<>();
        reqReservedMap.put("out_trade_no", txTradeEntity.getMerOrderNo());
        if (StringUtils.isNotBlank(txTradeEntity.getOperationDate())) {
            reqReservedMap.put("operation_date", txTradeEntity.getOperationDate());
        }
        String reqReserved = gson.toJson(reqReservedMap);
        channelEntity.setReqReserved(reqReserved);
        return channelEntity;
    }
}
