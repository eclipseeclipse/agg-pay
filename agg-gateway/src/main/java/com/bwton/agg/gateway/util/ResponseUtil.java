package com.bwton.agg.gateway.util;

import com.bwton.agg.bean.bo.BaseRespBO;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.util.XmlUtils;

/**
 * @author DengQiongChuan
 * @date 2019-12-15 14:40
 */
public class ResponseUtil {

    public static String buildResponseXml(AggPayExceptionEnum exceptionEnum) {
        BaseRespBO respBO = new BaseRespBO();
        respBO.setVersion(CommonConstants.VERSION);
        respBO.setCharset(CommonConstants.UTF_8);
        respBO.setStatus(exceptionEnum.getCode());
        respBO.setMessage(exceptionEnum.getMsg());
        return XmlUtils.toXml(respBO, BaseRespBO.class);
    }

}
