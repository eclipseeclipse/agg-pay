package com.bwton.agg.trade.api.controller;

import com.bwton.agg.bean.bo.BaseReqBO;
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
import com.bwton.agg.common.BaseController;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.context.DataContext;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.exception.AggPayException;
import com.bwton.agg.enums.InterfaceTypeEnum;
import com.bwton.agg.trade.service.OrderService;
import com.bwton.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.PARAM_ERROR;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    /**
     * 统一下单接口
     * @return
     */
    @RequestMapping(value = "/create", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderPayResultBO createOrder(@Validated @RequestBody OrderPayBO orderPayBO, @RequestHeader(value = "sequence", required = false) String sequence){
        this.validation(orderPayBO);
        orderPayBO.setGlobalSeq(sequence);
        Result<OrderPayResultBO> result = orderService.placeOrder(orderPayBO);
        OrderPayResultBO bo = result.getResult();
        if (result.isSuccess()) {
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.SYS_SUCCESS.getCode());
        } else {
            if (bo == null) bo = new OrderPayResultBO();
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
            bo.setErrCode(result.getErrcode());
            bo.setErrMsg(result.getErrmsg());
        }
        bo.setMchId(orderPayBO.getMchId());
        bo.setDeviceInfo(orderPayBO.getDeviceInfo());
        return bo;
    }

    /**
     * 查询订单接口
     * @return
     */
    @RequestMapping(value = "/query", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderQueryResultBO queryOrder(@Validated @RequestBody OrderQueryBO orderQueryBO){
        this.validation(orderQueryBO);
        if(StringUtils.isEmpty(orderQueryBO.getOutTradeNo()) && StringUtils.isEmpty(orderQueryBO.getTransactionId())){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "平台交易号与商户订单号至少一个必填");
        }
        Result<OrderQueryResultBO> result = orderService.query(orderQueryBO);
        OrderQueryResultBO bo = result.getResult();
        if (result.isSuccess()) {
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.SYS_SUCCESS.getCode());
        } else {
            if (bo == null) bo = new OrderQueryResultBO();
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
            bo.setErrCode(result.getErrcode());
            bo.setErrMsg(result.getErrmsg());
        }
        bo.setMchId(orderQueryBO.getMchId());
        bo.setDeviceInfo(orderQueryBO.getDeviceInfo());
        return bo;
    }
    /**
     * 关闭订单接口
     * @return
     */
    @RequestMapping(value = "/close", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderCloseResultBO closeOrder(@Validated @RequestBody OrderCloseBO orderCloseBO){
        this.validation(orderCloseBO);
        Result<OrderCloseResultBO> result = orderService.close(orderCloseBO);
        OrderCloseResultBO bo = result.getResult();
        if (result.isSuccess()) {
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.SYS_SUCCESS.getCode());
        } else {
            if (bo == null) bo = new OrderCloseResultBO();
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
            bo.setErrCode(result.getErrcode());
            bo.setErrMsg(result.getErrmsg());
        }
        bo.setMchId(orderCloseBO.getMchId());
        bo.setDeviceInfo(orderCloseBO.getDeviceInfo());
        return bo;
    }
    /**
     * 退款接口
     * @return
     */
    @RequestMapping(value = "/refund", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderRefundResultBO refundOrder(@Validated @RequestBody OrderRefundBO orderRefundBO){
        this.validation(orderRefundBO);
        orderRefundBO.setGlobalSeq(String.valueOf(DataContext.getCurrentContext().get(CommonConstants.HTTP_HEADER_GLOBAL_SEQ)));
        if(StringUtils.isEmpty(orderRefundBO.getOutTradeNo()) && StringUtils.isEmpty(orderRefundBO.getTransactionId())){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "平台交易号与商户订单号至少一个必填");
        }
        Result<OrderRefundResultBO> result = orderService.refund(orderRefundBO);
        OrderRefundResultBO bo = result.getResult();
        if (result.isSuccess()) {
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.SYS_SUCCESS.getCode());
        } else {
            if (bo == null) bo = new OrderRefundResultBO();
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
            bo.setErrCode(result.getErrcode());
            bo.setErrMsg(result.getErrmsg());
        }
        bo.setMchId(orderRefundBO.getMchId());
        bo.setDeviceInfo(orderRefundBO.getDeviceInfo());
        return bo;
    }

    /**
     * 退款查询接口
     * @return
     */
    @RequestMapping(value = "/refund/query", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderRefundQueryResultBO queryRefundOrder(@Validated @RequestBody OrderRefundQueryBO orderRefundQueryBO){
        this.validation(orderRefundQueryBO);
        if(StringUtils.isEmpty(orderRefundQueryBO.getRefundId())  && StringUtils.isEmpty(orderRefundQueryBO.getOutRefundNo())){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "商户退款单号、平台退款单号至少一个必填");
        }
        Result<OrderRefundQueryResultBO> result = orderService.refundQuery(orderRefundQueryBO);
        OrderRefundQueryResultBO bo = result.getResult();
        if (result.isSuccess()) {
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.SYS_SUCCESS.getCode());
        } else {
            if (bo == null) bo = new OrderRefundQueryResultBO();
            bo.setStatus(AggPayExceptionEnum.SYS_SUCCESS.getCode());
            bo.setResultCode(AggPayExceptionEnum.FAIL.getCode());
            bo.setErrCode(result.getErrcode());
            bo.setErrMsg(result.getErrmsg());
        }
        bo.setMchId(orderRefundQueryBO.getMchId());
        bo.setDeviceInfo(orderRefundQueryBO.getDeviceInfo());
        return bo;
    }



    private void validation(BaseReqBO baseReqBO){
        if(!baseReqBO.getSignType().equals(CommonConstants.DEFAULT_SIGN_TYPE)){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "签名类型错误");
        }
        if(!baseReqBO.getVersion().equals(CommonConstants.VERSION)){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "版本信息错误");
        }
        if(InterfaceTypeEnum.UNDEFINED.equals(InterfaceTypeEnum.getEnum(baseReqBO.getService()))){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "接口类型错误");
        }
        if(!baseReqBO.getCharset().equals(CommonConstants.UTF_8)){
            throw new AggPayException(PARAM_ERROR.getCode(), PARAM_ERROR.getCode(), "字符集错误");
        }
    }
}
