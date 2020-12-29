package com.test1moudle.p.runner;


import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;

public class TestRunner extends CommonRunner {
    private int returnCode=0;


    @Override
    public void onEventRun(Event event) {
        LogUtils.i("runner内运行");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.addReturnParam(returnCode++);
        event.setSuccess(true);
    }
}
