package com.bwton.agg.merchant.dao;

import com.bwton.agg.merchant.data.entity.DeviceInfoEntity;

import java.util.List;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:31
 */
public interface DeviceInfoDao {
    DeviceInfoEntity getById(Integer id);

    List<DeviceInfoEntity> listByMerchantId(String merchantId);

    int insert(DeviceInfoEntity deviceInfoEntity);

    int update(DeviceInfoEntity deviceInfoEntity);
}
