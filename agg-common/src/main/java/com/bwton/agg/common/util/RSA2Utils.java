package com.bwton.agg.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 2048位，RSA/ECB/PKCS1Padding
 *
 * @author Saintcy
 */
public class RSA2Utils {
    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";
    private static final String CHARSET = "utf-8";
    private static final int KEY_SIZE = 2048;


    /**
     * 获取密钥
     *
     * @return
     * @throws Exception
     */
    public static KeyPair generateKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
    }

    /**
     * 获取私钥
     *
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
    }

    /**
     * 还原公钥
     *
     * @param key
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    /**
     * 还原公钥
     *
     * @param key
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        byte[] results = encryptByPrivateKey(data.getBytes(CHARSET), key.getBytes());
        return new String(Base64.encodeBase64(results));
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        if (data.length > (KEY_SIZE / 8 - 11)) {
            throw new IllegalBlockSizeException("Rsa encryptByPrivateKey Exception:Data must not be longer than " + (KEY_SIZE / 8 - 11) + " bytes.Data length:" + data.length);
        }

        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        byte[] encrypt = encryptByPublicKey(data.getBytes(CHARSET), key.getBytes());
        return new String(Base64.encodeBase64(encrypt));
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        if (data.length > (KEY_SIZE / 8 - 11)) {
            throw new IllegalBlockSizeException("Data must not be longer than " + (KEY_SIZE / 8 - 11) + " bytes.Data length:" + data.length);
        }

        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /*********************************************解密*********************************************************/

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        byte[] dataBytes = Base64.decodeBase64(data.getBytes());
        byte[] resultBytes = decryptByPrivateKey(dataBytes, key.getBytes());
        return new String(resultBytes, CHARSET);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) throws Exception {
        byte[] decode = Base64.decodeBase64(data.getBytes());
        byte[] byPublicKey = decryptByPublicKey(decode, key.getBytes("utf-8"));
        return new String(byPublicKey, CHARSET);
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /*********************************************签名*********************************************************/
    /**
     * 签名
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] content, String privateKey) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
            Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

        signature.initSign(priKey);
        signature.update(content);

        byte[] signed = signature.sign();

        return signed;
    }

    /**
     * 签名
     * 结果转base64
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String sign(String content, String privateKey, String charset) throws Exception {
        byte[] signed = sign(content.getBytes(charset), privateKey);

        return new String(Base64.encodeBase64(signed));
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean doCheck(byte[] content, byte[] sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] encodedKey = Base64.decodeBase64(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content);

        boolean verified = signature.verify(sign);
        return verified;
    }

    /**
     * RSA验签名检查
     * 签名为base64格式
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encoding) throws Exception {
        boolean verified = doCheck(content.getBytes(encoding), Base64.decodeBase64(sign), publicKey);

        return verified;
    }

    public static boolean doCheck(String content, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] encodedKey = Base64.decodeBase64(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes());

        boolean verified = signature.verify(Base64.decodeBase64(sign));
        return verified;
    }
}
