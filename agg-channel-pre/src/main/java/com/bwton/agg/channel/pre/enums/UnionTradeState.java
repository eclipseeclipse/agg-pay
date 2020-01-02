package com.bwton.agg.channel.pre.enums;

/**
 * 银联交易状态
 *
 * @author DengQiongChuan
 * @date 2019-12-13 13:38
 */
public enum UnionTradeState {
    SUCCESS("SUCCESS", "支付/退款成功"),

    REFUND("REFUND", "转入退款"),

    NOTPAY("NOTPAY", "未支付"),

    CLOSED("CLOSED", "已关闭"),

    /** 支付失败(其他原因，如银行返回失败) */
    PAYERROR("PAYERROR", "支付失败"),

    FAIL("FAIL", "退款失败"),

    PROCESSING("PROCESSING", "退款处理中"),

    /** 未确定，需要商户原退款单号重新发起 */
    NOTSURE("NOTSURE", "未确定"),

    /**
     * 转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，
     * 资金回流到商户的现金帐号，需要商户人工干预，通过线下或者平台转账的方式进行退款。
     */
    CHANGE("CHANGE", "转入代发"),

    /** 未定义的状态 */
    UNDEFINED("UNDEFINED", "未定义的状态"),
    ;

    private String code;
    private String name;

    UnionTradeState(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UnionTradeState get(String code){
        for(UnionTradeState unionTradeState : UnionTradeState.values()){
            if(code.equals(unionTradeState.getCode())){
                return unionTradeState;
            }
        }
        return UNDEFINED;
    }

}
