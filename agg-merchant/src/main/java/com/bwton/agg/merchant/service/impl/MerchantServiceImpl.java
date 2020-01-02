package com.bwton.agg.merchant.service.impl;

import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.merchant.dao.ChannelMerchantDao;
import com.bwton.agg.merchant.dao.MerchantDao;
import com.bwton.agg.merchant.dao.MerchantRouteDao;
import com.bwton.agg.merchant.data.entity.ChannelMerchantEntity;
import com.bwton.agg.merchant.data.entity.MerchantEntity;
import com.bwton.agg.merchant.data.entity.MerchantRouteEntity;
import com.bwton.agg.merchant.service.MerchantService;
import com.bwton.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

/**
 * @author DengQiongChuan
 * @date 2019-12-09 22:08
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchantRouteDao merchantRouteDao;

    @Autowired
    private ChannelMerchantDao channelMerchantDao;

    @Override
    public String getMerchantSecureKey(String merchantId) {
        MerchantEntity merchantEntity = merchantDao.getById(merchantId);
        if (merchantEntity == null) {
            return null;
        } else {
            return merchantEntity.getSecureKey();
        }
    }

    @Override
    public TradeRouteVO getTradeRoute(String merchantId) {
        MerchantEntity merchantEntity = merchantDao.getById(merchantId);
        if (merchantEntity == null) {
            throw new BusinessException(AggPayExceptionEnum.MERCHANT_ID_NOT_EXISTS);
        }
        MerchantRouteEntity merchantRouteEntity = merchantRouteDao.getByMerchantId(merchantEntity.getId());
        if (merchantRouteEntity == null) {
            throw new BusinessException(AggPayExceptionEnum.MERCHANT_NOT_CONFIG_ROUTE);
        }
        ChannelMerchantEntity channelMerchantEntity =
                channelMerchantDao.getById(merchantRouteEntity.getChannelMerchantId());
        return buildTradeRouteVO(merchantEntity, channelMerchantEntity);
    }

    @Override
    public ChannelMerchantVO getChannelMerchant(Integer channelId, String channelMerchantId) {
        ChannelMerchantEntity channelMerchantEntity =
                channelMerchantDao.getByChannelIdAndChannelMerId(channelId, channelMerchantId);
        if (channelMerchantEntity == null) {
            return null;
        }
        ChannelMerchantVO channelMerchantVO = new ChannelMerchantVO();
        BeanCopier.create(ChannelMerchantEntity.class, ChannelMerchantVO.class, false)
                .copy(channelMerchantEntity, channelMerchantVO, null);
        return channelMerchantVO;
    }

    @Override
    public MerchantVO getChannelMerchantById(String merchantId) {
        ChannelMerchantEntity channelMerchantEntity =
                channelMerchantDao.getByMerchantId(merchantId);
        if (channelMerchantEntity == null) {
            return null;
        }
        MerchantVO merchantVO = new MerchantVO();
        BeanUtils.copyProperties(channelMerchantEntity,merchantVO);
        return merchantVO;
    }

    private TradeRouteVO buildTradeRouteVO(MerchantEntity merchantEntity, ChannelMerchantEntity channelMerchantEntity) {
        TradeRouteVO tradeRouteVO = new TradeRouteVO();
        tradeRouteVO.setMerchantId(merchantEntity.getId());
        tradeRouteVO.setBusinessType(merchantEntity.getBusinessType());
        tradeRouteVO.setOrderValidTime(merchantEntity.getOrderValidTime());
        tradeRouteVO.setSingleLimit(merchantEntity.getSingleLimit());
        tradeRouteVO.setDayLimit(merchantEntity.getDayLimit());
        tradeRouteVO.setCreditCardLimit(merchantEntity.getCreditCardLimit());
        tradeRouteVO.setChannelId(channelMerchantEntity.getChannelId());
        tradeRouteVO.setChannelMerId(channelMerchantEntity.getChannelMerId());
        tradeRouteVO.setFeeRate(channelMerchantEntity.getFeeRate());
        tradeRouteVO.setSecureKey(channelMerchantEntity.getSecureKey());
        tradeRouteVO.setSecurePath(channelMerchantEntity.getSecurePath());
        tradeRouteVO.setRegion(channelMerchantEntity.getRegion());
        tradeRouteVO.setBaseUrl(channelMerchantEntity.getBaseUrl());
        tradeRouteVO.setPlatPriKey(channelMerchantEntity.getPlatPriKey());
        tradeRouteVO.setPlatPubKey(channelMerchantEntity.getPlatPubKey());
        tradeRouteVO.setChannelPubKey(channelMerchantEntity.getPlatPubKey());
        tradeRouteVO.setSignType(merchantEntity.getSignType());
        return tradeRouteVO;
    }
}
