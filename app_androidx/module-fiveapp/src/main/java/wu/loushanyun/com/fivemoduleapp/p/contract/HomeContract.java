package wu.loushanyun.com.fivemoduleapp.p.contract;


import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.baseinterface.BaseView;

/**
 * Created by baixiaokang on 16/4/22.
 */
public interface HomeContract {

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<View> {
        @Override
        public void onAttached() {

        }
    }
}

