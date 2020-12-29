package met.hx.com.base.base.fragment;

import met.hx.com.base.base.BasePresenter;

/**
 * mvp模式
 *
 * @author huxu
 **/
public abstract class BaseYesPresenterFragment<P extends BasePresenter> extends BaseFragment {

    public P mPresenter;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        mPresenter = (P) initPresenter();
        registerLifeCycle(mPresenter);
    }

    protected abstract BasePresenter initPresenter();
}
