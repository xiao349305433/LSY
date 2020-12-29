package com.test1moudle.p.runner;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;

public class TestRunner3 extends CommonRunner {

    @Override
    public void onEventRun(Event event) {
        for(int i=0;i<100000;i++){
            LogUtils.i("TestRunner2==="+i);
        }
    }
}
