package com.bwton.agg.common.util;

public class NumberUtils {
    public static int compare(Integer x, Integer y) {
        if (x == null) {
            x = 0;
        }
        if (y == null) {
            y = 0;
        }
        return Integer.compare(x, y);
    }

    public static int compare(Long x, Long y) {
        if (x == null) {
            x = 0L;
        }
        if (y == null) {
            y = 0L;
        }
        return Long.compare(x, y);
    }

    public static int compare(Long x, Integer y) {
        if (x == null) {
            x = 0L;
        }
        if (y == null) {
            y = 0;
        }
        return Long.compare(x, y);
    }

    public static int compare(Integer x, Byte y) {
        if (x == null) {
            x = 0;
        }
        if (y == null) {
            y = 0;
        }
        return Integer.compare(x, y);
    }

    public static int div(Integer x, Integer y) {
        if (x == null) {
            return 0;
        }
        if (y == null || y == 0) {
            return 0;
        }
        return x / y;
    }
}
