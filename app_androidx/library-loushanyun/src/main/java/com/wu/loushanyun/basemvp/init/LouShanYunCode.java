package com.wu.loushanyun.basemvp.init;

import com.wu.loushanyun.base.url.URLUtils;

import java.util.ArrayList;

import met.hx.com.base.basemvp.init.BaseCode;

public class LouShanYunCode extends BaseCode {
    public static final int GetChangJiaBiaoShiRunner = ++CODE_INC;
    public static final int GetModule4ParseRuleDetailRunner = ++CODE_INC;
    public static final int GetModule4ParseRuleRunner = ++CODE_INC;
    public static final int MChipGetNewsInfoRunner = ++CODE_INC;
    public static final int ArrearsVerificationRunner = ++CODE_INC;
    public static final int MChipSetRxDelayRunner = ++CODE_INC;
    public static final int SelectOrdernumberRunner = ++CODE_INC;
    public static final int MChipCreateUserId = ++CODE_INC;

    public static ArrayList<String> getAllZhiZaoShang(int cjbs) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (URLUtils.getIP().equals(URLUtils.HttpHead + URLUtils.HostOFFICIAL)) {
            if (cjbs == 27) {
                arrayList.add("娄山云软件部测试");
            } else {
                arrayList.add("武汉楚天汉仪");
            }
        } else {
            arrayList.add("武汉楚天汉仪");
        }
        return arrayList;
    }

    public static String getBiaoShi(String zhizaoshang) {
        if (zhizaoshang.equals("武汉楚天汉仪")) {
            if (URLUtils.getIP().equals(URLUtils.HttpHead + URLUtils.HostOFFICIAL)) {
                return "5";
            }
        } else if (zhizaoshang.equals("娄山云软件部测试")) {
            return "4";
        }
        return "";
    }
}
