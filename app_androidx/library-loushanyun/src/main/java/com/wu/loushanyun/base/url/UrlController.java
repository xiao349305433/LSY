package com.wu.loushanyun.base.url;


import met.hx.com.base.base.application.AppContext;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

public class UrlController {

    private volatile static UrlController instance;

    public static UrlController getInstance() {
        if (instance == null) {
            synchronized (UrlController.class) {
                if (instance == null) {
                    instance = new UrlController();
                }
            }
        }
        return instance;
    }

    protected String getHead() {
        return AbSharedUtil.getString(AppContext.getInstance().getApplication(), "Head");
    }

    protected String getHost() {
        return AbSharedUtil.getString(AppContext.getInstance().getApplication(), "Host");
    }


    protected String getIP() {
        return AbSharedUtil.getString(AppContext.getInstance().getApplication(), "IP");
    }

    protected String getSocketIP() {
        return AbSharedUtil.getString(AppContext.getInstance().getApplication(), "SocketIP");
    }

    public void setIP(String head, String host) {
        AbSharedUtil.putString(AppContext.getInstance().getApplication(), "Head", head);
        AbSharedUtil.putString(AppContext.getInstance().getApplication(), "Host", host);
        AbSharedUtil.putString(AppContext.getInstance().getApplication(), "IP", head + host);
        if (URLUtils.HostTEST.equals(host)) {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "SocketIP", URLUtils.SocketIpTEST);
        } else {
            AbSharedUtil.putString(AppContext.getInstance().getApplication(), "SocketIP", URLUtils.SocketIpOFFICIAL);
        }
    }

}
