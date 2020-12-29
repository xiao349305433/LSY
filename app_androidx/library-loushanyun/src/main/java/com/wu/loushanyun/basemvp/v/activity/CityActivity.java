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

@Route(path = K.CityActivity)
public class CityActivity extends BaseNoPresenterActivity {
    private RecyclerView recycleView;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<String> stringArrayList;
    private String cityBean;


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_loushanyun_city_province;
        ba.mTitleText = "选择城市";
    }

    @Override
    protected void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        stringArrayList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(String.class, new CityTextViewBinder(new CityTextViewBinder.OnClickTextListener() {
            @Override
            public void OnClickText(String s, int position) {
                CityActivity.this.cityBean=s;
                Intent intent=new Intent(CityActivity.this,DistrictActivity.class);
                intent.putExtra("province", getIntent().getStringExtra("province"));
                intent.putExtra("city", s);
                startActivityForResult(intent,RESULT_DATA);
            }
        }));
        multiTypeAdapter.setItems(stringArrayList);
        recycleView.setAdapter(multiTypeAdapter);
        loadCityData(getIntent().getStringExtra("province"));
    }

    public void loadCityData(String provinceName) {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<ProvinceCitysBean.ProvinceBean.CityListBeanX>>) emitter -> {
                    emitter.onNext(ProvinceUtil.getAllCity(this, provinceName));
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(cityListBeanXList -> {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < cityListBeanXList.size(); i++) {
                        arrayList.add(cityListBeanXList.get(i).getName());
                    }
                    stringArrayList.addAll(arrayList);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_DATA && data != null) {
            String area = data.getStringExtra("area");
            String areaCode = data.getStringExtra("areaCode");
            Intent intent = new Intent();
            intent.putExtra("city", cityBean);
            intent.putExtra("area", area);
            intent.putExtra("areaCode", areaCode);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
