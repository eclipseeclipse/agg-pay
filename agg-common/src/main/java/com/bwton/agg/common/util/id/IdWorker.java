package com.bwton.agg.common.util.id;

/**
 * @Author:wangjunjie
 * @Describleption:
 * @Date: Created in 10:13  2018/9/4
 * @Modified By:
 */
/**
 * ID生成器
 *
 * @author Saintcy
 */
public interface IdWorker {
    /**
     * 按名字获取下一个id值
     *
     * @param name 流水名称
     * @return
     */
    long nextId(String name);

    /**
     * 用全局统一的名字获取id值
     *
     * @return
     */
    long nextId();

    /**
     * 返回string格式的id
     *
     * @param name 流水名称
     * @return
     */
    default String nextIdString(String name) {
        return nextId(name) + "";
    }

    /**
     * 返回string格式的id
     *
     * @return
     */
    default String nextIdString() {
        return nextId() + "";
    }
}

