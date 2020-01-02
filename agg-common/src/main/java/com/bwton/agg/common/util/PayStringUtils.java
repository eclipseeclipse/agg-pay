package com.bwton.agg.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class PayStringUtils {

    /**
     * 获取前leftLen后rightLen
     *
     * @param str
     * @param leftLen  左边字符长度
     * @param rightLen 右边字符长度
     * @return
     */
    public static String leftAndRight(String str, int leftLen, int rightLen) {
        return org.apache.commons.lang3.StringUtils.left(str, leftLen)
                + org.apache.commons.lang3.StringUtils.right(str, rightLen);
    }

    public static void sortAES(List<String> targetList) {
        Collections.sort(targetList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null || o2 == null) {
                    return -1;
                }
                if (o1.length() > o2.length()) {
                    return 1;
                }
                if (o1.length() < o2.length()) {
                    return -1;
                }
                if (o1.compareTo(o2) > 0) {
                    return 1;
                }
                if (o1.compareTo(o2) < 0) {
                    return -1;
                }
                if (o1.compareTo(o2) == 0) {
                    return 0;
                }
                return 0;
            }
        });
    }

    public static void toZip(File out, File... srcFiles) throws IOException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(out));
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[2048];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            log.info("压缩完成，耗时：{}ms", (end - start));
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }
}
