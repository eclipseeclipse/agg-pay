package com.bwton.agg.merchant.dao;

import com.bwton.agg.merchant.data.entity.MerchantEntity;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:31
 */
public interface MerchantDao {
    MerchantEntity getById(String merchantId);

    int insert(MerchantEntity merchantEntity);

    int update(MerchantEntity merchantEntity);
}
