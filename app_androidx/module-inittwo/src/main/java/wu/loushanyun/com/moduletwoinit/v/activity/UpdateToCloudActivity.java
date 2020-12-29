package wu.loushanyun.com.moduletwoinit.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

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
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.adapter.WuLianUploadDataBinder;
import wu.loushanyun.com.moduletwoinit.p.runner.SaveOnetoOneMeterRunner;

@Route(path = K.UpdateToCloudActivity)
public class UpdateToCloudActivity extends BaseNoPresenterActivity  {
    private RecyclerView recyclerview;
    private WuLianUploadDataBinder binder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<WuLianUploadData> wuLianUploadData;

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.SaveOnetoOneMeterRunner, new SaveOnetoOneMeterRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.SaveOnetoOneMeterRunner) {
            Log.i("yunanhao", "onEventRunEnd");
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_wulianlist;
        ba.mTitleText = "未上送信息";
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);


        multiTypeAdapter = new MultiTypeAdapter();
        binder = new WuLianUploadDataBinder();
        multiTypeAdapter.register(WuLianUploadData.class, binder);
        wuLianUploadData = new ArrayList<>();
        multiTypeAdapter.setItems(wuLianUploadData);
        recyclerview.setAdapter(multiTypeAdapter);
        loadData();
    }

    private void loadData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<WuLianUploadData>>) emitter -> {
                    LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                    List<WuLianUploadData> arrayList = LitePal.findAll(WuLianUploadData.class);
                    emitter.onNext(arrayList);
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    wuLianUploadData.clear();
                    wuLianUploadData.addAll(list);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }


}
