package com.bwton.agg.channel.pre.service.impl;

import com.bwton.agg.bean.bo.ChannelCloseBO;
import com.bwton.agg.bean.bo.ChannelPayBO;
import com.bwton.agg.bean.bo.ChannelQueryBO;
import com.bwton.agg.bean.bo.ChannelRefundBO;
import com.bwton.agg.bean.bo.ChannelRefundQueryBO;
import com.bwton.agg.bean.vo.*;
import com.bwton.agg.channel.pre.config.ChannelConfig;
import com.bwton.agg.channel.pre.data.bo.UnionCloseBO;
import com.bwton.agg.channel.pre.data.bo.UnionPayBO;
import com.bwton.agg.channel.pre.data.bo.UnionPayQueryBO;
import com.bwton.agg.channel.pre.data.bo.UnionRefundBO;
import com.bwton.agg.channel.pre.data.bo.UnionRefundQueryBO;
import com.bwton.agg.channel.pre.data.vo.*;
import com.bwton.agg.channel.pre.data.vo.UnionPayVO;
import com.bwton.agg.channel.pre.enums.UnionInterfaceEnum;
import com.bwton.agg.channel.pre.enums.UnionTradeState;
import com.bwton.agg.channel.pre.service.IChannelPreService;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.enums.TxChannelStatusEnum;
import com.bwton.agg.common.util.AggHttpClient;
import com.bwton.agg.common.util.HttpUtils;
import com.bwton.agg.common.util.SignUtils;
import com.bwton.agg.common.util.XmlUtils;
import com.bwton.core.web.CommonResult;
import com.bwton.exception.BusinessException;
import com.bwton.lang.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DataBindingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_BUSINESS_RESULT_CODE_NOT_RETURN;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_CHANNEL_RETURN_ERROR;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_GATAWAY_CODE_NOT_RETURN;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_HTTP_BODY_EMPTY;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_HTTP_EXCEPTION;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_JAXB_EXCEPTION;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_RETURN_OBJ_IS_EMPTY;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_SIGN_FAIL;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_PRE_UNKNOW_EXCEPTION;


/**
 * 统一下单扫码支付（主扫）
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:27
 */
//todo 大量重复的渠道返回处理代码可以重构
@Service("unionPayService")
@Slf4j
public class UnionPayServiceImpl implements IChannelPreService {

    @Autowired
    private Gson gson;

    @Autowired
    private ChannelConfig channelConfig;

    @Autowired
    private AggHttpClient httpClient;

    @Override
    public Result<ChannelPayVO> pay(ChannelPayBO channelPayBO) {
        try {
            UnionPayBO unionPayBO = buildUnionPayBO(channelPayBO);
            String sign = SignUtils.sign(channelPayBO.getSecureKey(), unionPayBO);
            unionPayBO.setSign(sign);
            String reqXml = XmlUtils.toXml(unionPayBO, UnionPayBO.class);
            //todo 替换http请求方法,发送和返回的一定要在日志中打出来；
            String respBoby = HttpUtils.post4Xml(httpClient, channelPayBO.getBaseUrl(), reqXml);
            if (StringUtils.isBlank(respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_HTTP_BODY_EMPTY);
            }
            // 验签
            if (!checkSign(channelPayBO.getSecureKey(), respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_SIGN_FAIL);
            }

            UnionPayVO unionPayVO = XmlUtils.fromXml(respBoby, UnionPayVO.class);
            if (unionPayVO == null) {
                return CommonResult.failure(CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }

            ChannelPayVO channelPayVO = new ChannelPayVO();
            //todo 错误码和状态的判断代码结构进行调整
            if (StringUtils.isBlank(unionPayVO.getStatus())) {
                return CommonResult.failure(CHANNEL_PRE_GATAWAY_CODE_NOT_RETURN);
            }
            // status != 0（通信失败），渠道返回失败时，取渠道错误码信息
            if (!CommonConstants.UNION_SUCCESS_CODE.equals(unionPayVO.getStatus())) {
                channelPayVO.setOutRespCode(unionPayVO.getCode());
                channelPayVO.setOutRespDesc(unionPayVO.getMessage());
                return new CommonResult<>(false, unionPayVO.getCode(), unionPayVO.getMessage(), channelPayVO);
            }
            if (StringUtils.isBlank(unionPayVO.getResult_code())) {
                return CommonResult.failure(CHANNEL_PRE_BUSINESS_RESULT_CODE_NOT_RETURN);
            }
            // result_code != 0（业务结果失败），渠道返回失败时，取渠道错误码信息
            if (!CommonConstants.UNION_SUCCESS_CODE.equals(unionPayVO.getResult_code())) {
                channelPayVO.setOutRespCode(unionPayVO.getErr_code());
                channelPayVO.setOutRespDesc(unionPayVO.getErr_msg());
                return new CommonResult<>(false, unionPayVO.getErr_code(), unionPayVO.getErr_msg(), channelPayVO);
            }

            //  status 和 result_code 都为 0
            String codeImgUrl = unionPayVO.getCode_img_url();
            if (StringUtils.isNotBlank(codeImgUrl)) {
                codeImgUrl = codeImgUrl.replace(channelConfig.getQrcodeUrl4UnionSource(), channelConfig.getQrcodeUrl4UnionTarget());
            }
            channelPayVO.setCodeUrl(unionPayVO.getCode_url());
            channelPayVO.setCodeImgUrl(codeImgUrl);
            channelPayVO.setOutRespCode(CommonConstants.UNION_SUCCESS_CODE);
            channelPayVO.setOutRespDesc(CommonConstants.UNION_SUCCESS_MESSAGE);
            return CommonResult.success(channelPayVO);
        } catch (DataBindingException e) {
            log.error("调用银联统一下单接口时发生JAXB异常", e);
            return CommonResult.failure(CHANNEL_PRE_JAXB_EXCEPTION);
        } catch (BusinessException e) {
            log.error("调用银联统一下单接口时发生http异常", e);
            return CommonResult.failure(CHANNEL_PRE_HTTP_EXCEPTION.getCode(),
                    CHANNEL_PRE_HTTP_EXCEPTION.getMsg() + "[" + e.getCode() + "]");
        } catch (Exception e) {
            log.error("调用银联统一下单接口时发生未知异常", e);
            return CommonResult.failure(CHANNEL_PRE_UNKNOW_EXCEPTION);
        }
    }

    @Override
    public Result<ChannelQueryVO> query(ChannelQueryBO channelQueryBO) {
        try {
            UnionPayQueryBO unionPayQueryBO = buildUnionPayQueryBO(channelQueryBO);
            String sign = SignUtils.sign(channelQueryBO.getSecureKey(), unionPayQueryBO);
            unionPayQueryBO.setSign(sign);
            String reqXml = XmlUtils.toXml(unionPayQueryBO, UnionPayQueryBO.class);
            String respBoby = HttpUtils.post4Xml(httpClient, channelQueryBO.getBaseUrl(), reqXml);
            if (StringUtils.isBlank(respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_HTTP_BODY_EMPTY);
            }
            // 验签
            if (!checkSign(channelQueryBO.getSecureKey(), respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_SIGN_FAIL);
            }

            UnionPayQueryVO unionPayQueryVO = XmlUtils.fromXml(respBoby, UnionPayQueryVO.class);
            if (unionPayQueryVO == null) {
                return CommonResult.failure(CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }

            ChannelQueryVO channelQueryVO = new ChannelQueryVO();
            if (CommonConstants.UNION_SUCCESS_CODE.equals(unionPayQueryVO.getStatus())) {
                // status ==0 && (result_code ==0 || err_code ==00) 均代表成功,
                // 但是 err_code ==00 时 不会返回订单信息
                if (CommonConstants.UNION_SUCCESS_CODE.equals(unionPayQueryVO.getResult_code())) {
                    channelQueryVO.setOutRespCode(unionPayQueryVO.getTrade_state());
                    channelQueryVO.setOutRespDesc(unionPayQueryVO.getTrade_state_desc());
                    UnionTradeState unionTradeState = UnionTradeState.get(unionPayQueryVO.getTrade_state());
                    switch (unionTradeState) {
                        case SUCCESS:
                            channelQueryVO = buildChannelQueryVO4Success(unionPayQueryVO);
                            channelQueryVO.setTxStatus(TxChannelStatusEnum.PAID.getCode());
                            return CommonResult.success(channelQueryVO);
                        case PAYERROR:
                            channelQueryVO.setTxStatus(TxChannelStatusEnum.FAILED.getCode());
                            return CommonResult.success(channelQueryVO);
                        case CLOSED:
                            channelQueryVO.setTxStatus(TxChannelStatusEnum.CLOSED.getCode());
                            return CommonResult.success(channelQueryVO);
                        case REFUND:
                            channelQueryVO.setTxStatus(TxChannelStatusEnum.REFUND.getCode());
                            return CommonResult.success(channelQueryVO);
                        case NOTPAY:
                        default:
                            channelQueryVO.setTxStatus(TxChannelStatusEnum.PROCESSING.getCode());
                            return CommonResult.success(channelQueryVO);
                    }

                    // status ==0 && (result_code ==04 || err_code ==06) 代表渠道处理中,
                    // err_code ==00  代表交易成功，但是不会返回支付订单信息，此处先做处理中
                } else if (CommonConstants.UNION_ERR_CODE_04.equals(unionPayQueryVO.getErr_code())
                        || CommonConstants.UNION_ERR_CODE_06.equals(unionPayQueryVO.getErr_code())
                        || CommonConstants.UNION_ERR_CODE_00.equals(unionPayQueryVO.getErr_code())) {
                    channelQueryVO.setTxStatus(TxChannelStatusEnum.PROCESSING.getCode());
                    return CommonResult.success(channelQueryVO);
                } else {
                    channelQueryVO.setTxStatus(TxChannelStatusEnum.FAILED.getCode());
                    channelQueryVO.setOutRespCode(unionPayQueryVO.getErr_code());
                    channelQueryVO.setOutRespDesc(unionPayQueryVO.getErr_msg());
                    return CommonResult.success(channelQueryVO);
                }
            }
            channelQueryVO.setTxStatus(TxChannelStatusEnum.PROCESSING.getCode());
            return CommonResult.success(channelQueryVO);
        } catch (DataBindingException e) {
            log.error("调用银联查询接口时发生JAXB异常", e);
            return CommonResult.failure(CHANNEL_PRE_JAXB_EXCEPTION);
        } catch (BusinessException e) {
            log.error("调用银联查询接口时发生http异常", e);
            return CommonResult.failure(CHANNEL_PRE_HTTP_EXCEPTION.getCode(),
                    CHANNEL_PRE_HTTP_EXCEPTION.getMsg() + "[" + e.getCode() + "]");
        } catch (Exception e) {
            log.error("调用银联查询接口时发生未知异常", e);
            return CommonResult.failure(CHANNEL_PRE_UNKNOW_EXCEPTION);
        }
    }

    @Override
    public Result<ChannelCloseVO> close(ChannelCloseBO channelCloseBO) {
        try {
            UnionCloseBO unionCloseBO = buildUnionCloseBO(channelCloseBO);
            String sign = SignUtils.sign(channelCloseBO.getSecureKey(), unionCloseBO);
            unionCloseBO.setSign(sign);
            String reqXml = XmlUtils.toXml(unionCloseBO, UnionCloseBO.class);
            String respBoby = HttpUtils.post4Xml(httpClient, channelCloseBO.getBaseUrl(), reqXml);
            if (StringUtils.isBlank(respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_HTTP_BODY_EMPTY);
            }
            // 验签
            if (!checkSign(channelCloseBO.getSecureKey(), respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_SIGN_FAIL);
            }

            UnionCloseVO unionCloseVO = XmlUtils.fromXml(respBoby, UnionCloseVO.class);
            if (unionCloseVO == null) {
                return CommonResult.failure(CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }

            ChannelCloseVO channelCloseVO = new ChannelCloseVO();
            // status ==0 渠道请求已受理
            if (CommonConstants.UNION_SUCCESS_CODE.equals(unionCloseVO.getStatus())) {
                //  status ==0 && (result_code ==0 || err_code ==00) 均代表成功
                if (CommonConstants.UNION_SUCCESS_CODE.equals(unionCloseVO.getResult_code())) {
                    channelCloseVO.setOutRespCode(CommonConstants.UNION_SUCCESS_CODE);
                    channelCloseVO.setOutRespDesc(CommonConstants.UNION_SUCCESS_MESSAGE);
                    return CommonResult.success(channelCloseVO);
                    //04106_1211006 订单不存在  可以代表关单成功
                } else if (CommonConstants.UNION_ERR_CODE_00.equals(unionCloseVO.getErr_code())
                        || CommonConstants.UNION_ERR_CODE_ORDER_NOT_EXISTS.equals(unionCloseVO.getErr_code())) {
                    channelCloseVO.setOutRespCode(unionCloseVO.getErr_code());
                    channelCloseVO.setOutRespDesc(unionCloseVO.getErr_msg());
                    return CommonResult.success(channelCloseVO);
                    //  status ==0 && (result_code ==04 || err_code ==06) 代表渠道处理中,
                } else if (CommonConstants.UNION_ERR_CODE_04.equals(unionCloseVO.getErr_code())
                        || CommonConstants.UNION_ERR_CODE_06.equals(unionCloseVO.getErr_code())) {
                    channelCloseVO.setOutRespCode(unionCloseVO.getErr_code());
                    channelCloseVO.setOutRespDesc(unionCloseVO.getErr_msg());
                    return new CommonResult<>(false, unionCloseVO.getErr_code(), unionCloseVO.getErr_msg(), channelCloseVO);
                } else {
                    channelCloseVO.setOutRespCode(unionCloseVO.getErr_code());
                    channelCloseVO.setOutRespDesc(unionCloseVO.getErr_msg());
                    return new CommonResult<>(false, unionCloseVO.getErr_code(), unionCloseVO.getErr_msg(), channelCloseVO);
                }
                // status !=0 渠道请求未受理
            } else {
                channelCloseVO.setOutRespCode(unionCloseVO.getErr_code());
                channelCloseVO.setOutRespDesc(unionCloseVO.getErr_msg());
                return new CommonResult<>(false, unionCloseVO.getErr_code(), unionCloseVO.getErr_msg(), channelCloseVO);
            }
        }  catch (DataBindingException e) {
            log.error("调用银联统一下单接口时发生JAXB异常", e);
            return CommonResult.failure(CHANNEL_PRE_JAXB_EXCEPTION);
        } catch (BusinessException e) {
            log.error("调用银联统一下单接口时发生http异常", e);
            return CommonResult.failure(CHANNEL_PRE_HTTP_EXCEPTION.getCode(),
                    CHANNEL_PRE_HTTP_EXCEPTION.getMsg() + "[" + e.getCode() + "]");
        } catch (Exception e) {
            log.error("调用银联统一下单接口时发生未知异常", e);
            return CommonResult.failure(CHANNEL_PRE_UNKNOW_EXCEPTION);
        }
    }

    @Override
    public Result<ChannelRefundVO> refund(ChannelRefundBO channelRefundBO) {
        try {
            UnionRefundBO unionRefundBO = buildUnionRefundBO(channelRefundBO);
            String sign = SignUtils.sign(channelRefundBO.getSecureKey(), unionRefundBO);
            unionRefundBO.setSign(sign);
            String reqXml = XmlUtils.toXml(unionRefundBO, UnionRefundBO.class);
            String respBoby = HttpUtils.post4Xml(httpClient, channelRefundBO.getBaseUrl(), reqXml);
            if (StringUtils.isBlank(respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_HTTP_BODY_EMPTY);
            }
            // 验签
            if (!checkSign(channelRefundBO.getSecureKey(), respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_SIGN_FAIL);
            }

            UnionRefundVO unionRefundVO = XmlUtils.fromXml(respBoby, UnionRefundVO.class);
            if (unionRefundVO == null) {
                return CommonResult.failure(CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }

            ChannelRefundVO channelRefundVO = new ChannelRefundVO();
            if (StringUtils.isBlank(unionRefundVO.getStatus())) {
                return CommonResult.failure(CHANNEL_PRE_GATAWAY_CODE_NOT_RETURN);
            }
            // status != 0（通信失败），渠道返回失败时，取渠道错误码信息
            if (!CommonConstants.UNION_SUCCESS_CODE.equals(unionRefundVO.getStatus())) {
                channelRefundVO.setOutRespCode(unionRefundVO.getCode());
                channelRefundVO.setOutRespDesc(unionRefundVO.getMessage());
                return new CommonResult<>(false, unionRefundVO.getCode(), unionRefundVO.getMessage(), channelRefundVO);
            }
            if (StringUtils.isBlank(unionRefundVO.getResult_code())) {
                return CommonResult.failure(CHANNEL_PRE_BUSINESS_RESULT_CODE_NOT_RETURN);
            }
            // result_code != 0（业务结果失败），渠道返回失败时，取渠道错误码信息
            if (!CommonConstants.UNION_SUCCESS_CODE.equals(unionRefundVO.getResult_code())) {
                channelRefundVO.setOutRespCode(unionRefundVO.getErr_code());
                channelRefundVO.setOutRespDesc(unionRefundVO.getErr_msg());
                return new CommonResult<>(false, unionRefundVO.getErr_code(), unionRefundVO.getErr_msg(), channelRefundVO);
            }

            //  status 和 result_code 都为 0
            channelRefundVO.setTransactionId(unionRefundVO.getTransaction_id());
            channelRefundVO.setOutTradeNo(unionRefundVO.getOut_trade_no());
            channelRefundVO.setOutRefundNo(unionRefundVO.getOut_refund_no());
            channelRefundVO.setRefundId(unionRefundVO.getRefund_id());
            channelRefundVO.setRefundChannel(unionRefundVO.getRefund_channel());
            channelRefundVO.setRefundFee(unionRefundVO.getRefund_fee());
            channelRefundVO.setCouponRefundFee(unionRefundVO.getCoupon_refund_fee());
            channelRefundVO.setOutRespCode(CommonConstants.UNION_SUCCESS_CODE);
            channelRefundVO.setOutRespDesc(CommonConstants.UNION_SUCCESS_MESSAGE);
            return CommonResult.success(channelRefundVO);
        } catch (DataBindingException e) {
            log.error("调用银联统一下单接口时发生JAXB异常", e);
            return CommonResult.failure(CHANNEL_PRE_JAXB_EXCEPTION);
        } catch (BusinessException e) {
            log.error("调用银联统一下单接口时发生http异常", e);
            return CommonResult.failure(CHANNEL_PRE_HTTP_EXCEPTION.getCode(),
                    CHANNEL_PRE_HTTP_EXCEPTION.getMsg() + "[" + e.getCode() + "]");
        } catch (Exception e) {
            log.error("调用银联统一下单接口时发生未知异常", e);
            return CommonResult.failure(CHANNEL_PRE_UNKNOW_EXCEPTION);
        }
    }

    @Override
    public Result<ChannelRefundQueryVO> refundQuery(ChannelRefundQueryBO channelRefundQueryBO) {
        try {
            UnionRefundQueryBO unionRefundQueryBO = buildUnionRefundQueryBO(channelRefundQueryBO);
            String sign = SignUtils.sign(channelRefundQueryBO.getSecureKey(), unionRefundQueryBO);
            unionRefundQueryBO.setSign(sign);
            String reqXml = XmlUtils.toXml(unionRefundQueryBO, UnionRefundQueryBO.class);
            String respBoby = HttpUtils.post4Xml(httpClient, channelRefundQueryBO.getBaseUrl(), reqXml);
            if (StringUtils.isBlank(respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_HTTP_BODY_EMPTY);
            }
            // 验签
            if (!checkSign(channelRefundQueryBO.getSecureKey(), respBoby)) {
                return CommonResult.failure(CHANNEL_PRE_SIGN_FAIL);
            }

            UnionRefundQueryVO unionRefundQueryVO =getUnionRefundQueryVO(respBoby);
            if (unionRefundQueryVO == null) {
                return CommonResult.failure(CHANNEL_PRE_RETURN_OBJ_IS_EMPTY);
            }

            ChannelRefundQueryVO channelRefundQueryVO = new ChannelRefundQueryVO();
            //status 和 result_code都为0是，表示查询成功。否则返回失败
            if (CommonConstants.UNION_SUCCESS_CODE.equals(unionRefundQueryVO.getStatus())
                    && CommonConstants.UNION_SUCCESS_CODE.equals(unionRefundQueryVO.getResult_code())){
                channelRefundQueryVO=buildRefundQuerySucessResp(unionRefundQueryVO,channelRefundQueryVO);
            } else {
                return CommonResult.failure(CHANNEL_PRE_CHANNEL_RETURN_ERROR);
            }
            return CommonResult.success(channelRefundQueryVO);
        } catch (DataBindingException e) {
            log.error("调用银联统一下单接口时发生JAXB异常", e);
            return CommonResult.failure(CHANNEL_PRE_JAXB_EXCEPTION);
        } catch (BusinessException e) {
            log.error("调用银联统一下单接口时发生http异常", e);
            return CommonResult.failure(CHANNEL_PRE_HTTP_EXCEPTION.getCode(),
                    CHANNEL_PRE_HTTP_EXCEPTION.getMsg() + "[" + e.getCode() + "]");
        } catch (Exception e) {
            log.error("调用银联统一下单接口时发生未知异常", e);
            return CommonResult.failure(CHANNEL_PRE_UNKNOW_EXCEPTION);
        }
    }

    private UnionPayQueryBO buildUnionPayQueryBO(ChannelQueryBO queryBO) {
        UnionPayQueryBO unionQueryBO = new UnionPayQueryBO();
        unionQueryBO.setOut_trade_no(queryBO.getTxSerialNo());
        unionQueryBO.setTransaction_id(queryBO.getOutSerialNo());
        unionQueryBO.setService(UnionInterfaceEnum.PAY_QUERY.getMethod());
        unionQueryBO.setVersion(CommonConstants.UNION_VERSION);
        unionQueryBO.setCharset(CommonConstants.UTF_8);
        unionQueryBO.setSign_type(queryBO.getSignType());
        unionQueryBO.setMch_id(queryBO.getChannelMerId());
        unionQueryBO.setNonce_str(String.valueOf(System.currentTimeMillis()));
        return unionQueryBO;
    }

    private UnionRefundQueryBO buildUnionRefundQueryBO(ChannelRefundQueryBO queryBO) {
        UnionRefundQueryBO unionRefundQueryBO = new UnionRefundQueryBO();
        unionRefundQueryBO.setOut_trade_no(queryBO.getOutTradeNo());
        unionRefundQueryBO.setOut_refund_no(queryBO.getOutRefundNo());
        unionRefundQueryBO.setService(UnionInterfaceEnum.REFUND_QUERY.getMethod());
        unionRefundQueryBO.setVersion(CommonConstants.UNION_VERSION);
        unionRefundQueryBO.setCharset(CommonConstants.UTF_8);
        unionRefundQueryBO.setSign_type(queryBO.getSignType());
        unionRefundQueryBO.setMch_id(queryBO.getChannelMerId());
        unionRefundQueryBO.setNonce_str(String.valueOf(System.currentTimeMillis()));
        return unionRefundQueryBO;
    }

    private UnionCloseBO buildUnionCloseBO(ChannelCloseBO channelCloseBO) {
        UnionCloseBO bo = new UnionCloseBO();
        bo.setService(channelCloseBO.getService());
        bo.setVersion(CommonConstants.VERSION);
        bo.setCharset(channelCloseBO.getCharset());
        bo.setSign_type(channelCloseBO.getSignType());
        bo.setOut_trade_no(channelCloseBO.getOutTradeNo());
        bo.setMch_id(channelCloseBO.getMchId());
        bo.setNonce_str(channelCloseBO.getNonceStr());
        return bo;
    }

    private UnionRefundBO buildUnionRefundBO(ChannelRefundBO reqBO) {
        UnionRefundBO bo = new UnionRefundBO();
        bo.setOut_trade_no(reqBO.getOutTradeNo());
        bo.setTransaction_id(reqBO.getTransactionId());
        bo.setOut_refund_no(reqBO.getOutRefundNo());
        bo.setTotal_fee(reqBO.getTotalFee());
        bo.setRefund_fee(reqBO.getRefundFee());
        bo.setOp_user_id(reqBO.getOpUserId());
        bo.setRefund_channel(reqBO.getRefundChannel());
        bo.setService(reqBO.getService());
        bo.setVersion(CommonConstants.VERSION);
        bo.setCharset(reqBO.getCharset());
        bo.setSign_type(reqBO.getSignType());
        bo.setNonce_str(reqBO.getNonceStr());
        bo.setMch_id(reqBO.getMchId());
        return bo;
    }

    private UnionPayBO buildUnionPayBO(ChannelPayBO channelPayBO) {
        UnionPayBO bo = new UnionPayBO();
        bo.setService(channelPayBO.getService());
        bo.setVersion(CommonConstants.VERSION);
        bo.setCharset(channelPayBO.getCharset());
        bo.setSign_type(channelPayBO.getSignType());
        bo.setMch_id(channelPayBO.getMchId());
        bo.setNonce_str(String.valueOf(System.currentTimeMillis()));
        bo.setSign(channelPayBO.getSign());
        bo.setSign_agentno(channelPayBO.getSignAgentNo());
        bo.setGroupno(channelPayBO.getGroupNo());

        bo.setOut_trade_no(channelPayBO.getOutTradeNo());
        bo.setDevice_info(channelPayBO.getDeviceInfo());
        bo.setBody(channelPayBO.getBody());
        bo.setAttach(channelPayBO.getAttach());
        bo.setTotal_fee(channelPayBO.getTotalFee());
        bo.setMch_create_ip(channelPayBO.getMchCreateIp());
        bo.setNotify_url(channelPayBO.getNotifyUrl());
        bo.setTime_start(channelPayBO.getTimeStart());
        bo.setTime_expire(channelPayBO.getTimeExpire());
        bo.setOp_user_id(channelPayBO.getOpUserId());
        bo.setProduct_id(channelPayBO.getProductId());
        return bo;
    }

    private ChannelQueryVO buildChannelQueryVO4Success(UnionPayQueryVO unionPayQueryVO) {
        ChannelQueryVO channelQueryVO = new ChannelQueryVO();
        Long totalFee = unionPayQueryVO.getTotal_fee();
        Long couponFee = unionPayQueryVO.getCoupon_fee() != null ? unionPayQueryVO.getCoupon_fee() : 0;
        Long cashFee = totalFee - couponFee;

        String outEndTime = unionPayQueryVO.getTime_end();
        String outSettleDate = null;
        if (StringUtils.isNotBlank(outEndTime)) {
            outSettleDate = outEndTime.substring(0, 8);
        }
        channelQueryVO.setOutTradeType(unionPayQueryVO.getTrade_type());
        channelQueryVO.setOutSerialNo(unionPayQueryVO.getOut_trade_no());
        channelQueryVO.setOutThirdPartyNo(unionPayQueryVO.getOut_transaction_id());
        channelQueryVO.setTxSerialNo(unionPayQueryVO.getOut_trade_no());
        channelQueryVO.setTotalFee(totalFee);
        channelQueryVO.setCashFee(cashFee);
        channelQueryVO.setCouponFee(couponFee);
        channelQueryVO.setFeeType(unionPayQueryVO.getFee_type());
        channelQueryVO.setOutEndTime(outEndTime);
        channelQueryVO.setOutSettleDate(outSettleDate);
        return channelQueryVO;
    }

    /**
     * 验签。true验签通过，false验签不通过
     */
    private boolean checkSign(String secureKey, String respXml) {
        TreeMap<String, String> xmlMap = XmlUtils.toMap(respXml);
        if (xmlMap == null) {
            return false;
        }
        String xmlSign = xmlMap.get(CommonConstants.SIGN);
        if (StringUtils.isBlank(xmlSign)) {
            return false;
        }
        String respSign = SignUtils.sign4Map(secureKey, xmlMap);
        return xmlSign.equals(respSign);
    }

    /**
     * XML列表数据 转类对象 特殊处理
     * @param respXml 响应报文体
     * @return UnionRefundQueryVO
     */
    private UnionRefundQueryVO getUnionRefundQueryVO(String respXml){
        UnionRefundQueryVO unionRefundQueryVO=XmlUtils.fromXml(respXml, UnionRefundQueryVO.class);
        TreeMap<String, String> xmlMap = XmlUtils.toMap(respXml);
        if (xmlMap != null) {
          if (StringUtils.isNotBlank(xmlMap.get(CommonConstants.REFUND_COUNT))){
              List<UnionRefundQuerySingleVO> list=setFieldValueByFieldName(xmlMap,Integer.valueOf(xmlMap.get(CommonConstants.REFUND_COUNT)));
              unionRefundQueryVO.setList(list);
              log.info("退款查询接口XML解析后的对象:{}",gson.toJson(unionRefundQueryVO));
              return unionRefundQueryVO;
          }
        }
        return unionRefundQueryVO;
    }

    /**
     *  TreeMap 转 list<Object>
     * @param  xmlMap
     * @return  List<UnionRefundQuerySingleVO>
     */
    private  List<UnionRefundQuerySingleVO> setFieldValueByFieldName(TreeMap<String, String> xmlMap,Integer count){
        List<UnionRefundQuerySingleVO> list=new ArrayList<>();
        for (int j=0;j<count;j++){
            try {
                UnionRefundQuerySingleVO object=new UnionRefundQuerySingleVO();
                Field[] fields = object.getClass().getDeclaredFields();
                for(int i=0;i<fields.length;i++){
                    Field field = fields[i];
                    //字段名称
                    String name = field.getName();
                    String key = name+"_"+j;
                    String methodName = name.substring(0,1).toUpperCase()+name.substring(1);
                    String fieldValue=xmlMap.get(key);
                    Method method = object.getClass().getMethod("set" + methodName,String.class);
                    method.invoke(object,fieldValue);
                }
                list.add(object);
            }catch (Exception e){
                log.error("退款查询接口设置属性值出现异常",e);
            }
        }
        return list;
    }

    /**
     *  封装退款查询同步应答成功的报文数据
     * @param  channelRefundQueryVO
     * @param  unionRefundQueryVO
     * @return
     */
    private  ChannelRefundQueryVO buildRefundQuerySucessResp(UnionRefundQueryVO unionRefundQueryVO,ChannelRefundQueryVO channelRefundQueryVO){
        if(unionRefundQueryVO ==null){
            return channelRefundQueryVO;
        }
        channelRefundQueryVO.setTradeType(unionRefundQueryVO.getTrade_type());
        channelRefundQueryVO.setOutTradeNo(unionRefundQueryVO.getOut_trade_no());
        channelRefundQueryVO.setOutTransactionId(unionRefundQueryVO.getOut_transaction_id());
        channelRefundQueryVO.setRefundCount(unionRefundQueryVO.getRefund_count());
        channelRefundQueryVO.setTransactionId(unionRefundQueryVO.getTransaction_id());
        List<ChannelRefundSingleQueryVO> list=new ArrayList<>();
        unionRefundQueryVO.getList().forEach(unionRefundQuerySingleVO -> {
            ChannelRefundSingleQueryVO channelRefundSingleQueryVO=new ChannelRefundSingleQueryVO();
            channelRefundSingleQueryVO.setOutRefundNo(unionRefundQuerySingleVO.getOut_refund_no());
            channelRefundSingleQueryVO.setRefundId(unionRefundQuerySingleVO.getRefund_id());
            channelRefundSingleQueryVO.setOutRefundId(unionRefundQuerySingleVO.getOut_refund_id());
            channelRefundSingleQueryVO.setRefundChannel(unionRefundQuerySingleVO.getRefund_channel());
            channelRefundSingleQueryVO.setRefundFee(unionRefundQuerySingleVO.getRefund_fee());
            channelRefundSingleQueryVO.setCouponRefundFee(unionRefundQuerySingleVO.getRefund_fee());
            channelRefundSingleQueryVO.setMdiscount(unionRefundQuerySingleVO.getMdiscount());
            channelRefundSingleQueryVO.setRefundTime(unionRefundQuerySingleVO.getRefund_time());
            channelRefundSingleQueryVO.setRefundStatus(unionRefundQuerySingleVO.getRefund_status());
            channelRefundSingleQueryVO.setSettleKey(unionRefundQuerySingleVO.getSettle_key());
            list.add(channelRefundSingleQueryVO);
        });
        channelRefundQueryVO.setList(list);
        log.info("退款查询接口解析应答报文结果：{}",gson.toJson(channelRefundQueryVO));
        return channelRefundQueryVO;
    }
}
