package met.hx.com.base.base.activity;

import met.hx.com.base.base.BasePresenter;

/**
 * @param <> mvp结构的基类
 * @author huxu
 */
public abstract class BaseYesPresenterActivity<P extends BasePresenter> extends BaseActivity {


    public P mPresenter;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        mPresenter = (P) initPresenter();
        registerLifeCycle(mPresenter);
    }

    protected abstract P initPresenter();
}
