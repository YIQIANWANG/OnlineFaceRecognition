package com.chenframework.common.utils.codec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA算法加密/解密工具类。
 */
@Slf4j
public abstract class RSAUtil {

    public static final String K_PUBLIC = "public";
    public static final String K_PRIVATE = "private";

    private static final String ALGORITHOM = "RSA";// 算法名称
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider(); // 默认的安全服务提供者
    private static final int KEY_SIZE = 1024; // 密钥大小
    private static KeyFactory keyFactory;

    private static KeyPairGenerator keyPairGen; // 公钥/秘钥对生成器

    private RSAUtil() {
    }

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException("RSAEncoder init fail!");
        }
    }

    /**
     * 使用指定的私钥解密数据。
     * @param privateKey 私钥
     * @param data       要解密的数据
     * @return 原始数据
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
        try {
            Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            ci.init(Cipher.DECRYPT_MODE, privateKey);
            return ci.doFinal(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("RSAEncoder.decrypt(PrivateKey,byte[]) doing failed !!", e);
        }

    }

    /**
     * 使用给定的私钥解密给定的字符串。
     * @param privateKey  私钥
     * @param encryptText 密文
     * @return 若私钥为null，或者 encryptText 为 null或空字符串则返回 null。 私钥不匹配时，返回null,否则返回原文
     * @see #decrypt(PrivateKey, byte[])
     */
    public static String decrypt(PrivateKey privateKey, String encryptText) {
        if (privateKey == null || StringUtils.isBlank(encryptText)) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encryptText.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (DecoderException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 使用指定的公钥加密数据。
     * @param publicKey 公钥
     * @param data      要加密的数据
     * @return 加密后的数据
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] data) {
        try {
            Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            ci.init(Cipher.ENCRYPT_MODE, publicKey);
            return ci.doFinal(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("RSAEncoder.encrypt(PublicKey,byte[]) doing failed !!", e);
        }
    }

    /**
     * 使用给定的公钥加密给定的字符串
     * @param publicKey 公钥
     * @param rawText   要加密的字符串
     * @return 若 publicKey 为null，或者rawText 为null 则返回null，否则返回密文
     * @see #encrypt(PublicKey, byte[])
     */
    public static String encrypt(PublicKey publicKey, String rawText) {
        if (publicKey == null || rawText == null) {
            return null;
        }
        byte[] en_data = encrypt(publicKey, rawText.getBytes());
        return new String(Hex.encodeHex(en_data));
    }

    /**
     * 生成公钥和私钥
     */
    public static Map<String, Object> getKeys() {
        Map<String, Object> map = new HashMap<>();

        keyPairGen.initialize(KEY_SIZE, new SecureRandom(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss").getBytes()));
        KeyPair keyPair = keyPairGen.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put(K_PUBLIC, publicKey);
        map.put(K_PRIVATE, privateKey);
        return map;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
     */
    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        BigInteger bM = new BigInteger(hexModulus);
        BigInteger bP = new BigInteger(hexPrivateExponent);
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bM, bP);
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     */
    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        BigInteger bM = new BigInteger(hexModulus);
        BigInteger bP = new BigInteger(hexPublicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bM, bP);
        try {
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
