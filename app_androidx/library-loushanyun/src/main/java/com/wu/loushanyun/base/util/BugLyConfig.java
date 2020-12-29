package com.wu.loushanyun.base.util;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedHashMap;
import java.util.Map;

import met.hx.com.base.BuildConfig;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;

public class BugLyConfig {

    public static void initBugLy(Application application,String appId){
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Context context = application.getApplicationContext();
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppChannel("myChannel");  //设置渠道
        strategy.setAppVersion(AppUtils.getAppVersionName());      //App的版本
        strategy.setAppPackageName(AppUtils.getAppPackageName());  //App的包名
        strategy.setAppReportDelay(20000);
        strategy.setUploadProcess(true);
        strategy.setBuglyLogUpload(true);
        strategy.setRecordUserInfoOnceADay(true);
        strategy.setEnableANRCrashMonitor(true);
        strategy.setEnableUserInfo(true);
        strategy.setEnableNativeCrashMonitor(true);
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            public Map<String, String> onCrashHandleStart(int crashType, String errorType,
                                                          String errorMessage, String errorStack) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                try {
                    map.put("x-code", XActivityUtils.getTopActivity().getClass().getName());
                }catch (Exception e){

                }
                return map;
            }

            @Override
            public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
                                                           String errorMessage, String errorStack) {
                try {
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }

        });
        // 初始化Bugly// 调试时，将第三个参数改为true
        CrashReport.initCrashReport(context, appId, BuildConfig.DEBUG, strategy);
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);

    }

}
