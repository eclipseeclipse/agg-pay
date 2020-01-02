package com.bwton.agg.common.enums;

/**
 * D 为deduction的首字母。
 *
 * @author DengQiongChuan.
 */
public enum RedisKeyEnum {

    /** 申请退款同步锁 */
    D_REFUND_APPLY_SEQ("d.r.a.s"),
    /** 关闭订单同步锁 */
    D_CLOSE_ORDER_SEQ("d.c.a.s"),

    /** 统一下单锁。key:merchantId:merOrderNo */
    LOCK_PAY("lock.pay"),

    /** 平台交易记录锁 */
    LOCK_TX_TRADE("lock.t.t"),

    /** 渠道交易记录锁 */
    LOCK_TX_CHANNEL("lock.t.c"),

    ;

    private String name;

    RedisKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
