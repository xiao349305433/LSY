package com.test1moudle.p.presenter;


import com.modulerefresh.p.runner.FileUploadRunner;
import com.test1moudle.init.TestEventCode;
import com.test1moudle.p.runner.TestRunner;

import met.hx.com.base.baseevent.Event;

/**
 * Created by baixiaokang on 16/5/4.
 *
 * @author huxu
 */

public class MainPresenter extends MainContract.Presenter {
    @Override
    protected void initPresenter() {
        registerEventRunner(TestEventCode.HTTP_Test, new FileUploadRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
    }

    @Override
    public void test(String path) {
        pushEvent(TestEventCode.HTTP_Test,path);
    }


}


