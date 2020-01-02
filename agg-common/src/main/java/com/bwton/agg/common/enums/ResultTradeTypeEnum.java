package com.bwton.agg.common.enums;

/**
 * 描述: 将渠道的交易类型转为聚合支付平台的交易类型返回给商户。
 *
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/17 
 */
public enum ResultTradeTypeEnum {

    // 支付宝 01
    ALIPAY("pay.alipay.jspay", "01"),

    // 微信 02
    WXPAY("pay.weixin.jspay", "02"),

    // 银联 03
    UNIONPAY("pay.unionpay.native", "03"),


    UNDEFINED("undefined", "-9"),
    ;

    // 渠道交易类型
    private String outTradeType;
    // 聚合支付平台交易类型
    private String platTradeType;

    ResultTradeTypeEnum(String outTradeType, String platTradeType){
        this.outTradeType = outTradeType;
        this.platTradeType = platTradeType;
    }

    public static ResultTradeTypeEnum getEnum(String outTradeType){
        for(ResultTradeTypeEnum resultTradeTypeEnum : ResultTradeTypeEnum.values()){
            if(outTradeType.equals(resultTradeTypeEnum.getOutTradeType())){
                return resultTradeTypeEnum;
            }
        }
        return UNDEFINED;
    }

    public String getOutTradeType() {
        return outTradeType;
    }

    public String getPlatTradeType() {
        return platTradeType;
    }
}
