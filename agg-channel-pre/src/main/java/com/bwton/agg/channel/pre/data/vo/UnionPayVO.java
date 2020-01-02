package com.bwton.agg.channel.pre.data.vo;

import com.bwton.agg.channel.pre.data.vo.UnionBaseVO;
import lombok.Data;

/**
 * 统一下单扫码支付（主扫）响应对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 16:39
 */
@Data
public class UnionPayVO extends UnionBaseVO {
    /** 唯一识别码 */
    private String uuid;
    /** 二维码链接。商户可用此参数自定义去生成二维码后展示出来进行扫码支付 */
    private String code_url;
    /** 二维码图片。此参数的值即是根据code_url生成的可以扫码支付的二维码图片地址 */
    private String code_img_url;
}
