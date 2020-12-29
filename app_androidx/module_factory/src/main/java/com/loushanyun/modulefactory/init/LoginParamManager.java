package com.loushanyun.modulefactory.init;

import com.google.gson.Gson;
import com.loushanyun.modulefactory.m.ProductRegister;
import com.tencent.bugly.crashreport.CrashReport;
import com.wu.loushanyun.BuildConfig;
import com.wu.loushanyun.base.url.URLUtils;

import java.util.Date;

import met.hx.com.base.base.application.AppContext;
import met.hx.com.librarybase.some_utils.ConstUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

public class LoginParamManager {


    private volatile static LoginParamManager instance;

    public static LoginParamManager getInstance() {
        if (instance == null) {
            synchronized (LoginParamManager.class) {
                if (instance == null) {
                    instance = new LoginParamManager();
                }
            }
        }
        return instance;
    }

    public ProductRegister getProductRegister() {
        ProductRegister loginData = null;
        Gson gson = new Gson();
        if (URLUtils.HostTEST.equals(URLUtils.getHost())) {
            String json = AbSharedUtil.getString(AppContext.getInstance().getApplication(), "loginJsonTest");
            loginData = gson.fromJson(json, ProductRegister.class);
        } else {
            String json = AbSharedUtil.getString(AppContext.getInstance().getApplication(), "loginJsonOffice");
            loginData = gson.fromJson(json, ProductRegister.class);
        }
        return loginData;
    }

    public void setProductRegister(String json) {
        if (URLUtils.HostTEST.equals(URLUtils.getHost())) {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "loginJsonTest", json);
            AbSharedUtil.putLong(AppContext.getInstance().getApplication(), "loginTimeTest", TimeUtils.date2Milliseconds(new Date()));
        } else {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "loginJsonOffice", json);
            AbSharedUtil.putLong(AppContext.getInstance().getApplication(), "loginTimeOffice", TimeUtils.date2Milliseconds(new Date()));
        }
        setCrashId();
    }
    public void setCrashId(){
        if(getProductRegister()!=null){
            if (URLUtils.HostTEST.equals(URLUtils.getHost())) {
                if(BuildConfig.DEBUG){
                    CrashReport.setUserId(String.valueOf(getProductRegister().getId()) + ",net,Debug");
                }else {
                    CrashReport.setUserId(String.valueOf(getProductRegister().getId()) + ",net,Release");
                }

            } else {
                if(BuildConfig.DEBUG){
                    CrashReport.setUserId(String.valueOf(getProductRegister().getId()) + ",com,Debug");
                }else {
                    CrashReport.setUserId(String.valueOf(getProductRegister().getId()) + ",com,Release");
                }
            }
        }
    }
    public void clear() {
        if (URLUtils.HostTEST.equals(URLUtils.getHost())) {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "loginJsonTest", "");
            AbSharedUtil.putLong(AppContext.getInstance().getApplication(), "loginTimeTest", 0);
        } else {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "loginJsonOffice", "");
            AbSharedUtil.putLong(AppContext.getInstance().getApplication(), "loginTimeOffice", 0);
        }
    }

    public boolean compareTime() {
        long time = 0;
        if (URLUtils.HostTEST.equals(URLUtils.getHost())) {
            time = AbSharedUtil.getLong(AppContext.getInstance().getApplication(), "loginTimeTest");
        } else {
            time = AbSharedUtil.getLong(AppContext.getInstance().getApplication(), "loginTimeOffice");
        }
        long hour = TimeUtils.getIntervalByNow(TimeUtils.milliseconds2Date(time), ConstUtils.HOUR);
        LogUtils.i(hour + "");
        if (hour >= 9) {
            return false;
        } else {
            setCrashId();
            return true;
        }
    }
}
