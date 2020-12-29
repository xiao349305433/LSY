package com.test1moudle.v.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.test1moudle.R
import met.hx.com.base.base.BaseAttribute
import met.hx.com.base.base.activity.BaseNoPresenterActivity
import met.hx.com.base.baseconfig.C
import met.hx.com.base.baseevent.Event

@Route(path = C.DemoKotlinActivity)
class DemoKotlinActivity : BaseNoPresenterActivity() {
    override fun initEventListener() {
    }

    override fun onInitAttribute(ba: BaseAttribute?) {
        ba?.mTitleText = "Kotlin示例代码"
        ba?.mActivityLayoutId = R.layout.m_test_refresh_test
        ba?.mTitleRightText = "暂无"
    }

    override fun onEventRunEnd(event: Event?, code: Int) {
    }

    override fun initView() {
    }

}