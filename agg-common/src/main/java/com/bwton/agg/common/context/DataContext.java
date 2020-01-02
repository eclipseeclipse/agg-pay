package com.bwton.agg.common.context;

import java.util.HashMap;

/**
 * @author DengQiongChuan
 * @date 2019-12-16 14:19
 */
public class DataContext extends HashMap<String, Object> {

    protected static final ThreadLocal<DataContext> threadLocal = new InheritableThreadLocal<DataContext>() {
        @Override
        protected DataContext initialValue() {
            return new DataContext();
        }
    };

    public static DataContext getCurrentContext() {
        return threadLocal.get();
    }

    /**
     * 从当前线程移除。在请求结束的时候调用。
     */
    public void unset() {
        threadLocal.remove();
    }

    public String getString(String key) {
        return (String)get(key);
    }
}
