package com.bwton.agg.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
    public static final String AES = "AES";
    public static final int SIZE = 4096;

    /**
     * 对文件AES加密
     *
     * @param sourceFile
     * @param targetFile
     * @param password
     * @return
     */
    public static int fileEncrypt(File sourceFile, File targetFile, String password) {
        return fileEncrypt(sourceFile, targetFile, password, AES);
    }

    public static int fileEncrypt(File sourceFile, File targetFile, String password, String encryptType) {
        if (StringUtils.isBlank(encryptType)) {
            //加密类型不能为空
            return -1;
        }
        if (!StringUtils.equalsAny(encryptType, AES)) {
            //暂不支持该加密类型
            return -2;
        }
        byte[] newkey = new byte[16];

        for (int i = 0; i < newkey.length && i < password.getBytes().length; ++i) {
            newkey[i] = password.getBytes()[i];
        }
        SecretKeySpec key = new SecretKeySpec(newkey, "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            //算法不存在
            return -4;
        } catch (NoSuchPaddingException e) {
            //填充方法不存在
            return -5;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            //密钥不合法
            return -6;
        }
        return writeToFile(sourceFile, targetFile, cipher);
    }


    /**
     * 对文件AES解密
     *
     * @param sourceFile
     * @param targetFile
     * @param password
     * @return
     */
    public static int fileDecrypt(File sourceFile, File targetFile, String password) {
        return fileDecrypt(sourceFile, targetFile, password, AES);
    }

    public static int fileDecrypt(File sourceFile, File targetFile, String password, String encryptType) {
        if (StringUtils.isBlank(encryptType)) {
            //加密类型不能为空
            return -1;
        }
        if (!StringUtils.equalsAny(encryptType, AES)) {
            //暂不支持该加密类型
            return -2;
        }
        byte[] newkey = new byte[16];

        for (int i = 0; i < newkey.length && i < password.getBytes().length; ++i) {
            newkey[i] = password.getBytes()[i];
        }
        SecretKeySpec key = new SecretKeySpec(newkey, "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            //算法不存在
            return -4;
        } catch (NoSuchPaddingException e) {
            //填充方法不存在
            return -5;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            //密钥不合法
            return -6;
        }
        return writeToFile(sourceFile, targetFile, cipher);
    }

    private static int writeToFile(File sourceFile, File targetFile, Cipher cipher) {
        try (FileInputStream fileInputStream = new FileInputStream(sourceFile);
             FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
            byte[] sByte = new byte[SIZE];
            int readlen;
            while ((readlen = fileInputStream.read(sByte, 0, SIZE)) > 0) {
                cipherOutputStream.write(sByte, 0, readlen);
            }
            return 0;
        } catch (IOException e) {
            //文件读取或写入错误
            return -3;
        }
    }

}
