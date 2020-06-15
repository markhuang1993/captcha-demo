package com.mark.captchademo.util;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5CryptoUtil {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private Md5CryptoUtil() {
        throw new AssertionError();
    }
    
    public static String encrypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(str.getBytes(CHARSET));
        final byte[] resultByte = messageDigest.digest();
        return byteArr2HexStr(resultByte);
    }
    
    public static String byteArr2HexStr(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final int b : bytes) {
            int temp = b;
            if (temp < 0) {
                temp = temp + 256;
            } else if (temp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(temp, 16));
        }
        return sb.toString();
    }
}
