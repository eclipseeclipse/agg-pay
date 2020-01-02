package com.bwton.agg.trade.base;

import com.bwton.agg.bean.bo.BaseReqBO;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.util.SignUtils;
import com.bwton.agg.common.util.XmlUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeMap;

/**
 * 描述: 
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/16 
 */
public class BaseTest {

    public <T,K> String post(T bean, Class<T> type , Class<K> resultType , String url) throws IOException {

        String sign = SignUtils.sign(HttpConstants.secureKey, bean);
        setSign(bean,sign);
        String reqXml = XmlUtils.toXml(bean, type);

        System.err.println(url+"》》》请求参数》》》"+reqXml);
        String respBoby = HttpUtils.postForXML(HttpConstants.baseUrl+url, reqXml,
                5000, 8000);
        if (respBoby == null) {
            return "请求渠道失败";
        }
        System.err.println(url+"》》》响应结果》》》"+respBoby);
        TreeMap<String, String> respMap = XmlUtils.toMap(respBoby);
        if (respMap == null) {
            return "渠道返回结果转化XML异常";
        }
        String respSign = SignUtils.sign4Map(HttpConstants.secureKey, respMap);
        if (!respSign.equals(respMap.get("sign"))) {
            return "渠道返回结果验签失败";
        }
        return respBoby;
    }




 public <T> void setSign(T bean, String sign){
     Field[] field = bean.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
     try {
         for (int j = 0; j < field.length; j++) { // 遍历所有属性
             String name = field[j].getName(); // 获取属性的名字
             name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
             if(name.equals("sign")){
                 bean.getClass().getMethod("set"+name,String.class).invoke(bean,sign);
             }
         }
     } catch (NoSuchMethodException e) {
         e.printStackTrace();
     } catch (SecurityException e) {
         e.printStackTrace();
     } catch (IllegalAccessException e) {
         e.printStackTrace();
     } catch (IllegalArgumentException e) {
         e.printStackTrace();
     } catch (InvocationTargetException e) {
         e.printStackTrace();
     }
 }

    public <T>  String  getSign(T bean){
        Field[] field = bean.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                if (name.equals("sign")){
                    name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    Method m = bean.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(bean); // 调用getter方法获取属性值
                   return value;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  void getBaseRequest(BaseReqBO baseReqBO){
        baseReqBO.setVersion(CommonConstants.VERSION);
        baseReqBO.setCharset(CommonConstants.UTF_8);
        baseReqBO.setSignType(CommonConstants.DEFAULT_SIGN_TYPE);
        baseReqBO.setMchId(HttpConstants.mchId);
        baseReqBO.setDeviceInfo("12345678");
        baseReqBO.setNonceStr(String.valueOf(System.currentTimeMillis()));
        baseReqBO.setService("test");
    }
}
