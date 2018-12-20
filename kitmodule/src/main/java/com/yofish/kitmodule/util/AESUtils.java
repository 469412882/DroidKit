package com.yofish.kitmodule.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密算法
 * <p>
 * Created by hch on 2018/12/19.
 */
public class AESUtils {
    private final static String DEF_SECRET_KEY = "9188123123123345";
    private final static String DEF_SECRET_V = "9188123123123345";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_NAME = "AES";

    private static byte[] encrypt(String cbcIv, String key, byte[] content) throws Exception {
        SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(cbcIv.getBytes());
        cipher.init(1, sksSpec, iv);
        byte[] encrypted = cipher.doFinal(content);
        return encrypted;
    }

    private static byte[] decrypt(String cbcIv, String key, byte[] encrypted) throws Exception {
        SecretKeySpec skeSpect = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(cbcIv.getBytes());
        cipher.init(2, skeSpect, iv);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String decrypt(String key, String str) {
        try {
            byte[] decbytes = decrypt(DEF_SECRET_V, key, Base64.decode(str, Base64.DEFAULT));
            return new String(decbytes, "utf-8").trim();
        } catch (Exception e) {
            return null;
        }
    }
    public static String encrypt(String key, String str) {
        try {
            byte[] encbytes = encrypt(DEF_SECRET_V, key, str.getBytes("utf-8"));
            return Base64.encodeToString(encbytes, Base64.DEFAULT).trim();
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String str) {
        return decrypt(DEF_SECRET_KEY, str);
    }
    public static String encrypt(String str) {
        return encrypt(DEF_SECRET_KEY, str);
    }
}
