package com.bwton.agg.trade.service.impl;

import com.bwton.agg.api.MerchantRest;
import com.bwton.agg.bean.bo.ChannelMerchantQueryBO;
import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.common.configuration.CommonConfig;
import com.bwton.agg.common.configuration.RedissonConfig;
import com.bwton.agg.common.constant.DateConstants;
import com.bwton.agg.common.data.vo.UnionNotifyVO;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.enums.RedisKeyEnum;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.exception.AggPayException;
import com.bwton.agg.common.util.RedisKeyUtils;
import com.bwton.agg.common.util.SignUtils;
import com.bwton.agg.common.util.XmlUtils;
import com.bwton.agg.enums.ChannelEnum;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.agg.trade.service.ChannelNotifyService;
import com.bwton.agg.trade.service.TxChannelService;
import com.bwton.agg.trade.service.TxTradeService;
import com.bwton.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bwton.agg.common.constant.CommonConstants.UNION_SUCCESS_CODE;
import static com.bwton.agg.common.constant.CommonConstants.UNION_SUCCESS_MESSAGE;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.XML_TO_MAP_EXCEPTION;

/**
 * @author DengQiongChuan
 * @date 2019-12-11 17:49
 */
@Service
@Slf4j
public class ChannelNotifyServiceImpl implements ChannelNotifyService {
    @Autowired
    private MerchantRest merchantRest;

    @Autowired
    private TxTradeService txTradeService;

    @Autowired
    private TxChannelService txChannelService;

    @Autowired
    private RedissonConfig redisson;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    @Transactional
    public boolean unionNotify(String notifyXml) {
        UnionNotifyVO unionNotifyVO = XmlUtils.fromXml(notifyXml, UnionNotifyVO.class);
        if (!UNION_SUCCESS_CODE.equals(unionNotifyVO.getStatus())
                || !UNION_SUCCESS_CODE.equals(unionNotifyVO.getResult_code())) {
            return false;
        }
        Result<ChannelMerchantVO> channelMerchantVOResult = merchantRest.getChannelMerchant(
                new ChannelMerchantQueryBO(ChannelEnum.UNION.getId(), unionNotifyVO.getMch_id()));
        if (!channelMerchantVOResult.isSuccess()) {
            return false;
        }
        ChannelMerchantVO channelMerchantVO = channelMerchantVOResult.getResult();
        try {
            // 验签
            String sign = SignUtils.sign4Map(channelMerchantVO.getSecureKey(), XmlUtils.toMap(notifyXml));
            if (!StringUtils.equalsIgnoreCase(sign, unionNotifyVO.getSign())) {
                return false;
            }
        } catch (RuntimeException e) {
            log.error(XML_TO_MAP_EXCEPTION.getMsg(), e);
            return false;
        }
        // 校验订单号和金额是否与DB中的一致
        TxChannelEntity txChannelEntityFromDB = txChannelService.getByTxSerialNo(unionNotifyVO.getOut_trade_no());
        if (!StringUtils.equals(txChannelEntityFromDB.getTxSerialNo(), unionNotifyVO.getOut_trade_no())
                || !txChannelEntityFromDB.getTotalFee().equals(unionNotifyVO.getTotal_fee())) {
            return false;
        }
        // 加锁并更新tx_channel和tx_trade
        String lockKey = RedisKeyUtils.getKey(RedisKeyEnum.LOCK_TX_CHANNEL, unionNotifyVO.getOut_trade_no());
        RLock lock = redisson.getLock(lockKey);
        boolean tryLock;
        try {
            tryLock = lock.tryLock(commonConfig.getLockWaitTime(), commonConfig.getLockLeaseTime(), TimeUnit.SECONDS);
            if (!tryLock) {
                return false;
            }
            TxChannelEntity txChannelEntity4Update = buildTxChannelEntity(unionNotifyVO);
            int num = txChannelService.updateTxChannelEntity(txChannelEntity4Update);
            if (num == 1) {
                TxTradeEntity txTradeEntity4Update = buildTxTradeEntity(txChannelEntity4Update);
                txTradeEntity4Update.setTxTradeNo(txChannelEntityFromDB.getTxTradeNo());
                txTradeService.updateTxTradeEntity(txTradeEntity4Update);
            }
        } catch (Exception e) {
            throw new AggPayException(AggPayExceptionEnum.SYS_ERROR, e);
        } finally {
            redisson.unLock(lock);
        }
        return true;
    }

    private TxChannelEntity buildTxChannelEntity(UnionNotifyVO unionNotifyVO) {
        Integer txStatus = UNION_SUCCESS_CODE.equals(unionNotifyVO.getPay_result())
                ? TxChannelStatusEnum.PAID.getCode() : TxChannelStatusEnum.FAILED.getCode();
        String outEndTime = unionNotifyVO.getTime_end();
        if (StringUtils.isBlank(outEndTime)) {
            outEndTime = DateFormatUtils.format(new Date(), DateConstants.YYYYMMDDHHMMSS);
        }
        String respDesc = UNION_SUCCESS_CODE.equals(unionNotifyVO.getPay_result())
                ? UNION_SUCCESS_MESSAGE : unionNotifyVO.getPay_result();

        Long totalFee = unionNotifyVO.getTotal_fee();
        Long cashFee = unionNotifyVO.getCash_fee();
        Long couponFee = unionNotifyVO.getCoupon_fee();
        if (cashFee == null) {
            if (couponFee == null) {
                cashFee = totalFee;
            } else {
                cashFee = totalFee - couponFee;
            }
        }
        TxChannelEntity entity = new TxChannelEntity();
        entity.setTxSerialNo(unionNotifyVO.getOut_trade_no());
        entity.setOutTradeType(unionNotifyVO.getTrade_type());
        entity.setCashFee(cashFee);
        entity.setCouponFee(couponFee);
        entity.setFeeType(unionNotifyVO.getFee_type());
        entity.setTxStatus(txStatus);
        entity.setOutSerialNo(unionNotifyVO.getTransaction_id());
        entity.setOutThirdPartyNo(unionNotifyVO.getOut_transaction_id());
        entity.setOutEndTime(outEndTime);
        entity.setOutSettleDate(outEndTime.substring(0, 8));
        entity.setOutRespCode(unionNotifyVO.getPay_result());
        entity.setOutRespDesc(respDesc);
        return entity;
    }

    private TxTradeEntity buildTxTradeEntity(TxChannelEntity txChannelEntity) {
        TxTradeEntity entity = new TxTradeEntity();
        entity.setTxStatus(txChannelEntity.getTxStatus());
        entity.setCashFee(txChannelEntity.getCashFee());
        entity.setCouponFee(txChannelEntity.getCouponFee());
        entity.setFeeType(txChannelEntity.getFeeType());
        return entity;
    }
}
