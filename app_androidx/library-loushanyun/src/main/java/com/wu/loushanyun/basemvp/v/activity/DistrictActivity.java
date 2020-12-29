package com.wu.loushanyun.basemvp.v.activity;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.R;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.m.ProvinceCitysBean;
import com.wu.loushanyun.basemvp.p.ProvinceUtil;
import com.wu.loushanyun.basemvp.p.adapter.CityTextViewBinder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;

import static com.wu.loushanyun.basemvp.v.activity.ProvincesActivity.RESULT_DATA;

@Route(path = K.DistrictActivity)
public class DistrictActivity extends BaseNoPresenterActivity {
    private RecyclerView recycleView;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<String> stringArrayList;
    private List<ProvinceCitysBean.ProvinceBean.CityListBeanX.CityListBean> provinceBeanList;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_loushanyun_city_province;
        ba.mTitleText = "选择区域";
    }

    @Override
    protected void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        stringArrayList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(String.class, new CityTextViewBinder(new CityTextViewBinder.OnClickTextListener() {
            @Override
            public void OnClickText(String s, int position) {
                //将计算的结果回传给第一个Activity
                Intent reReturnIntent = new Intent();
                reReturnIntent.putExtra("area", s);
                for(int i=0;i<provinceBeanList.size();i++){
                    if(s.equals(provinceBeanList.get(i).getName())){
                        reReturnIntent.putExtra("areaCode", provinceBeanList.get(i).getId());
                    }
                }
                setResult(RESULT_DATA, reReturnIntent);
                finish();
            }
        }));
        multiTypeAdapter.setItems(stringArrayList);
        recycleView.setAdapter(multiTypeAdapter);
        loadDisData(getIntent().getStringExtra("province"), getIntent().getStringExtra("city"));
    }

    public void loadDisData(String provinceName, String cityName) {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<ProvinceCitysBean.ProvinceBean.CityListBeanX.CityListBean>>) emitter -> {
                    emitter.onNext(ProvinceUtil.getAllDistrict(this, provinceName, cityName));
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(provinceBeanList -> {
                    this.provinceBeanList=provinceBeanList;
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < provinceBeanList.size(); i++) {
                        arrayList.add(provinceBeanList.get(i).getName());
                    }
                    stringArrayList.addAll(arrayList);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }
}
