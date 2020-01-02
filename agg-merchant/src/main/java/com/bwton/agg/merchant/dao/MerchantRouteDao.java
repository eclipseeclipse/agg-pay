package com.bwton.agg.merchant.dao;

import com.bwton.agg.merchant.data.entity.MerchantRouteEntity;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:31
 */
public interface MerchantRouteDao {
    MerchantRouteEntity getById(Integer id);

    MerchantRouteEntity getByMerchantId(String merchantId);

    int insert(MerchantRouteEntity merchantRouteEntity);

    int update(MerchantRouteEntity merchantRouteEntity);
}
