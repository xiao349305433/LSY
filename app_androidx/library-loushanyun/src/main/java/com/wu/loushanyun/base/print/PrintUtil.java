package com.wu.loushanyun.base.print;


import org.greenrobot.eventbus.EventBus;


public class PrintUtil {

    public static void print2(String sn, String chuangan, String factoryName, String id, String timeYear, String timeMonth, String timeDay) {
        EventBus.getDefault().post(new PrintToolBean2(0, sn, chuangan, factoryName, id, timeYear, timeMonth, timeDay));
    }

    public static void printChip2(String sn) {
        EventBus.getDefault().post(new PrintToolBean2( sn, 1));
    }

    public static void print3(String id, String timeYear, String timeMonth, String timeDay, String factoryName, String chuanganxinhao) {
        EventBus.getDefault().post(new PrintToolBean3(id, timeYear, timeMonth, timeDay, factoryName, chuanganxinhao));
    }

}
