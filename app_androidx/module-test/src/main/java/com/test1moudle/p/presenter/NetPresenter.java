package com.test1moudle.p.presenter;


import com.test1moudle.init.TestEventCode;
import com.test1moudle.p.runner.TestRunner;

import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * Created by baixiaokang on 16/4/22.
 */
public class NetPresenter extends NetContract.Presenter{

    @Override
    protected void initPresenter() {
        registerEventRunner(TestEventCode.HTTP_Test, new TestRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        LogUtils.i("哈哈哈返回的");
        if (code == TestEventCode.HTTP_Test) {
            mView.test((Integer) event.getReturnParamAtIndex(0));
        }
    }

    public void test() {
        for (int i = 0; i < 100; i++) {
            pushEvent(TestEventCode.HTTP_Test);
        }
    }
}

