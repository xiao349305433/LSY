package wu.loushanyun.com.fivemoduleapp.v.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.RefreshYuanChengBiaoHao;
import wu.loushanyun.com.fivemoduleapp.p.adapter.SaveDataViewBinder;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;

@Route(path = K.UpLoadDataActivity)
public class UpLoadDataActivity extends BaseNoPresenterActivity {
    private RecyclerView recyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_up_load_data;
        ba.mTitleText = "同步娄山云";
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = new Items();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(YuanChuanSaveData.class, new SaveDataViewBinder());
        recyclerView.setAdapter(multiTypeAdapter);
        multiTypeAdapter.setItems(items);
        getData();
    }

    private void getData(){
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<YuanChuanSaveData>>) emitter -> {
                    LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<YuanChuanSaveData> arrayList = LitePal.where("loginid = ?", loginid + "")
                            .find(YuanChuanSaveData.class);
                    emitter.onNext(arrayList);
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    items.clear();
                    items.addAll(list);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshYuanChengBiaoHao(RefreshYuanChengBiaoHao refreshYuanChengBiaoHao) {
        getData();
    }
    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
