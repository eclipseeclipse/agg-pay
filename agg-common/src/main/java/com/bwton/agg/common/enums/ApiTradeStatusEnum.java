package com.bwton.agg.common.enums;

/**
 * 接口订单状态枚举
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/16
 */
public enum ApiTradeStatusEnum {

    SUCCESS("SUCCESS", "支付/退款成功"),
    REFUND("REFUND", "转入退款"),
    NOTPAY("NOTPAY", "未支付"),
    CLOSED("CLOSED", "已关闭"),
    // 支付失败(其他原因，如银行返回失败)
    PAYERROR("PAYERROR", "支付失败"),
    FAIL("FAIL", "退款失败"),
    PROCESSING("PROCESSING", "退款处理中"),
    // 未确定，需要商户原退款单号重新发起
    NOTSURE("NOTSURE", "未确定"),
    // 转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，
    // 需要商户人工干预，通过线下或者平台转账的方式进行退款。
    CHANGE("CHANGE", "转入代发"),


    UNDEFINED("UNDEFINED", "未定义"),
    ;
    private String name;
    private String desc;

    ApiTradeStatusEnum(String name, String desc){
        this.name = name;
        this.desc = desc;
    }


    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取枚举
     * @param txTradeStatusEnum tx_trade交易状态枚举
     * @param isPay 是否是支付接口。true-是，false-不是
     * @return
     */
    public static ApiTradeStatusEnum getEnum(TxTradeStatusEnum txTradeStatusEnum, boolean isPay) {
        switch (txTradeStatusEnum) {
            case WAIT_PAY:
                return ApiTradeStatusEnum.NOTPAY;
            case PAID:
                return ApiTradeStatusEnum.SUCCESS;
            case PROCESSING:
                return ApiTradeStatusEnum.PROCESSING;
            case REFUND:
                return ApiTradeStatusEnum.REFUND;
            case NOTSURE:
                return ApiTradeStatusEnum.NOTSURE;
            case CLOSED:
                return ApiTradeStatusEnum.CLOSED;
            case FAILED:
                return isPay ? ApiTradeStatusEnum.PAYERROR : ApiTradeStatusEnum.FAIL;
            case THIRD_PAID:
                return ApiTradeStatusEnum.CHANGE;
            default:
        }
        return UNDEFINED;
    }
}
