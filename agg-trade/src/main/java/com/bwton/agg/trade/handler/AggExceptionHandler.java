package com.bwton.agg.trade.handler;

import com.bwton.agg.bean.bo.BaseRespBO;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.exception.AggPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.Field;

/**
 * @author DengQiongChuan
 * @date 2019-12-14 14:27
 */
@ControllerAdvice
@Slf4j
public class AggExceptionHandler {

    @ExceptionHandler(value = AggPayException.class)
    @ResponseBody
    public BaseRespBO aggPayExceptionHandler(AggPayException e, HttpServletRequest req){
        log.error("交易服务捕获到异常[AggPayException]", e);
        String merchantId = req.getHeader(CommonConstants.HTTP_HEADER_MERCHANT_ID);
        String deviceInfo = req.getHeader(CommonConstants.HTTP_HEADER_DEVICE_INFO);

        BaseRespBO bo = new BaseRespBO();
        bo.setStatus(StringUtils.isNotBlank(e.getErrType()) ? e.getErrType() : AggPayExceptionEnum.SYS_ERROR.getCode());
        bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
        bo.setMchId(merchantId);
        if (StringUtils.isNotBlank(deviceInfo)) {
            bo.setDeviceInfo(deviceInfo);
        }
        bo.setErrCode(e.getErrCode());
        bo.setErrMsg(e.getErrMsg());
        return bo;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseRespBO validExceptionHandler(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String errFieldName = fieldError.getField();
        String errFieldMsg = fieldError.getDefaultMessage();

        Object target = bindingResult.getTarget();
        Class clazz = target.getClass();
        String fieldXmlElementName = null;
        //TODO 与之前的转换放到一个类里面
        while (null != clazz) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getName().equals(errFieldName)) {
                    boolean accessFlag = field.isAccessible();
                    field.setAccessible(true);
                    XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                    if (xmlElement != null) {
                        fieldXmlElementName = xmlElement.name();
                    }
                    field.setAccessible(accessFlag);
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
        String errFileName = fieldXmlElementName == null ? errFieldName : fieldXmlElementName;

        BaseRespBO bo = new BaseRespBO();
        bo.setStatus(AggPayExceptionEnum.PARAM_ERROR.getCode());
        bo.setMessage(errFileName + ":" + errFieldMsg);
        return bo;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseRespBO businessExceptionHandler(Exception e){
        log.error("交易服务捕获到异常[未知]", e);
        BaseRespBO bo = new BaseRespBO();
        bo.setStatus(AggPayExceptionEnum.SYS_ERROR.getCode());
        bo.setMessage(AggPayExceptionEnum.SYS_ERROR.getMsg());
        return bo;
    }

}
