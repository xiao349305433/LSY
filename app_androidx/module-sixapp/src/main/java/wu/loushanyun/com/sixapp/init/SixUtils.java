package wu.loushanyun.com.sixapp.init;

import java.util.HashMap;

import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo2;

public class SixUtils {


    public static HashMap<String, String> SecondHashMap=new HashMap<>();
    public static HashMap<String, String> LorawanHashMap=new HashMap<>();
    public static String EnvName="";
    public static EnvironmentInfInfo2.DatasBean  DatasBean;
    public static  int EnvId;

    public static String getChanpingStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "压力传感器";
        } else if ("1".equals(code)) {
            result = "工业水表（信号输出)";
        } else if ("2".equals(code)) {
            result = "工业类电子类计量表";
        } else if ("3".equals(code)) {
            result = "远传物联网端";
        } else if ("4".equals(code)) {
            result = "远传表号接入";
        } else if ("5".equals(code)) {
            result = "11位手机号标志 ";
        }else if ("6".equals(code)) {
            result = "流量计";
        } else if ("7".equals(code)) {
            result = "井盖开关预警产品";
        } else if ("8".equals(code)) {
            result = "浸水判断预警产品";
        } else if ("9".equals(code)) {
            result = "电源开关类产品";
        } else if ("10".equals(code)) {
            result = "集中器";
        }
        return result;
    }

    public static String getChanpingPinYinByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "ylcgq-";
        } else if ("1".equals(code)) {
            result = "gysb-";
        } else if ("2".equals(code)) {
            result = "gyldzljlb-";
        } else if ("3".equals(code)) {
            result = "ycwlwd-";
        } else if ("4".equals(code)) {
            result = "ycbhjr-";
        } else if ("5".equals(code)) {
            result = "11wsjhbz-";
        }else if ("6".equals(code)) {
            result = "llj-";
        } else if ("7".equals(code)) {
            result = "jgkgyjcp-";
        } else if ("8".equals(code)) {
            result = "jspdyjcp-";
        } else if ("9".equals(code)) {
            result = "dykglcp-";
        } else if ("10".equals(code)) {
            result = "jzq-";
        }
        return result;
    }



}
