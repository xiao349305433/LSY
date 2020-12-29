package wu.loushanyun.com.modulerepair.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.wu.loushanyun.base.config.K;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.p.adapter.RepairUpdateDataViewBinder;

@Route(path = K.UploadChangeDataActivity)
public class UploadChangeDataActivity extends BaseNoPresenterActivity {
    private ArrayList<RepairUpdateData> arrayList;
    private MultiTypeAdapter multiTypeAdapter;
    private RecyclerView recyclerview;
    private TextView textEmpty;
    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_upload_change;
        ba.mTitleText = "上传端设备更换数据";
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        textEmpty = (TextView) findViewById(R.id.text_empty);

        multiTypeAdapter = new MultiTypeAdapter();
        arrayList = new ArrayList<>();
        multiTypeAdapter.setItems(arrayList);
        multiTypeAdapter.register(RepairUpdateData.class, new RepairUpdateDataViewBinder(this));
        recyclerview.setAdapter(multiTypeAdapter);
        findData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMainRepair refreshMainRepair) {
        findData();
    }

    private void findData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<RepairUpdateData>>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在查找", UploadChangeDataActivity.this.getClass().getName());
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<RepairUpdateData> list = LitePal.where("loginid = ?", loginid + "").find(RepairUpdateData.class);
                    emitter.onNext(list);
                    LoadingDialogUtil.dismissByEvent(UploadChangeDataActivity.this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    XLog.i("数据==" + list.toString());
                    if(list.size()>0){
                        arrayList.clear();
                        arrayList.addAll(list);
                        multiTypeAdapter.notifyDataSetChanged();
                        textEmpty.setVisibility(View.GONE);
                    }else {
                        textEmpty.setVisibility(View.VISIBLE);
                    }

                }));
    }
}
