package com.bwton.agg.common.util;

import com.bwton.agg.common.enums.RedisKeyEnum;

/**
 * @author DengQiongChuan.
 */
public class RedisKeyUtils {

    /**
     * 返回格式 redisKey:id
     * 例如，getKey(RedisKeyEnum.D_ACTIVITY_LIMIT_TOTAL_COUNT, 123) 返回 d.act.l.t.c:123
     */
    public static String getKey(RedisKeyEnum redisKeyEnum, long id) {
        return getKey(redisKeyEnum, String.valueOf(id));
    }

    /**
     * 返回格式 redisKey:field:field
     * 例如，getKey(RedisKeyEnum.D_ACTIVITY_LIMIT_TOTAL_COUNT, "123", "234") 返回 d.act.l.t.c:123:234
     */
    public static String getKey(RedisKeyEnum redisKeyEnum, String... fields) {
        if(fields == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(redisKeyEnum.getName());
        for(String field : fields) {
            sb.append(":").append(field);
        }
        return sb.toString();
    }

}
