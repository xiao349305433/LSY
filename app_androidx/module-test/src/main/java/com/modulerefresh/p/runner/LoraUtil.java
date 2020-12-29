package com.modulerefresh.p.runner;

import org.litepal.exceptions.GlobalException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import met.hx.com.librarybase.some_utils.EncryptUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;

public class LoraUtil {
    /**
     * 校验权限
     *
     * @param signature 签名
     * @param appSecret appSecret
     * @param timeStamp 时间戳
     * @param method    请求方法，大写
     * @param url       请求URL
     * @param body      body
     */
    private static void valid(String signature, String appSecret, String timeStamp, String method, String url, String body) {
        String concat = new String(hmacsha256(timeStamp.concat(method).concat(url).concat(body).getBytes(),
                appSecret.getBytes()));
        if (!signature.equals(concat)) {
            throw new GlobalException("权限认证失败");
        }

    }

    /**
     * lora加密算法
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] hmacsha256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new GlobalException("加密失败:{}");
        }
    }

    public static String getX_ACCESS_ID() {
        return "SADASKDHSDAJKHDSA";
    }

    public static String getAppSecret() {
        return "loushanyun_upload";
    }

    public static String getX_NONCE() {
        return String.valueOf(TimeUtils.getCurTimeMills());
    }
}
