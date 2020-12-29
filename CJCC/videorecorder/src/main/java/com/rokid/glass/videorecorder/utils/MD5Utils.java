package com.rokid.glass.videorecorder.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.08 11:54
 */
public class MD5Utils {


    public static String getFileMD5(File file) {
        if(file == null || !file.exists() ) return null;
        MessageDigest md = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        int len;

        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            while((len = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
        }  catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }

        BigInteger bigInteger = new BigInteger(1, md.digest());
        return bigInteger.toString(16);
    }

    public static String getFileMD5(String filePath) {
        if(TextUtils.isEmpty(filePath)) return null;

        File file = new File(filePath);

        return getFileMD5(file);
    }

    public static String generateMD5(LinkedHashMap<String, String> params, String secretValue) {
        String[] keys = (String[])params.keySet().toArray(new String[0]);
        StringBuilder query = new StringBuilder();
        String[] var4 = keys;
        int var5 = keys.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String key = var4[var6];
            String value = (String)params.get(key);
            query.append(key).append("=").append(value).append("&");
        }

        query.append("secret").append("=").append(secretValue);
        Logger.d(new String[]{"query str " + query.toString()});
        byte[] bytes = encryptMD5(query.toString());
        return byte2hex(bytes);
    }

    public static byte[] encryptMD5(String data) {
        byte[] bytes = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException var3) {
            Logger.i(new String[]{"Exception: " + var3.getCause()});
        } catch (UnsupportedEncodingException var4) {
            Logger.i(new String[]{"Exception: " + var4.getCause()});
        }

        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sign.append("0");
            }

            sign.append(hex.toUpperCase());
        }

        Logger.d(new String[]{"generate sign is " + sign.toString()});
        return sign.toString();
    }

    public static final String MD5(String pwd) {
        char[] md5String = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 15];
                str[k++] = md5String[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }

    public static final String MD5LowerCase(String pwd) {
        char[] md5String = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 15];
                str[k++] = md5String[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }

    public static String getMD5String(String str) {
        byte[] bytes = encryptMD5(str);
        return byte2hex(bytes);
    }
}
