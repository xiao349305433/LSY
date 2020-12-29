package met.hx.com.base.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;

/**
 * 打开Fragment时使用（工具类）
 */
@Route(path = C.SingleFragmentActivity)
public class SingleFragmentActivity extends BaseNoPresenterActivity {
    public final static String FragmentName = "FragmentName";
    @Autowired
    public String path;
    @Autowired
    public Bundle bundle;
    private  Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fragment = (Fragment) ARouter.getInstance().build(path).with(bundle).navigation();
            addLocalFragment(fragment, R.id.flcontainer, bundle);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.flcontainer);
            if (fragment instanceof View.OnTouchListener) {
                frameLayout.setOnTouchListener((View.OnTouchListener) fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastManager.getInstance(SingleFragmentActivity.this).showToast("未知异常，请安装最新版金融通。");
        }
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(fragment!=null){
            fragment.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.base_activity_singlefragment;
        ba.mStatusBarActivityEnabled=false;
        ba.mHasTitle = false;
    }

    @Override
    protected void initView() {

    }

}
