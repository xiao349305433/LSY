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

@Route(path = K.ProvincesActivity)
public class ProvincesActivity extends BaseNoPresenterActivity {
    private RecyclerView recycleView;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<String> stringArrayList;
    public static final int RESULT_DATA = 1001;

    private String province;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_loushanyun_activity_province;
        ba.mTitleText = "选择省份";
    }

    @Override
    protected void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        stringArrayList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(String.class, new CityTextViewBinder(new CityTextViewBinder.OnClickTextListener() {
            @Override
            public void OnClickText(String s, int position) {
                ProvincesActivity.this.province = s;
                Intent intent=new Intent(ProvincesActivity.this,CityActivity.class);
                intent.putExtra("province", s);
                startActivityForResult(intent,RESULT_DATA);
            }
        }));
        multiTypeAdapter.setItems(stringArrayList);
        recycleView.setAdapter(multiTypeAdapter);
        loadProData();
    }

    /**
     * 解析34个省市直辖区数据
     */
    public void loadProData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<ProvinceCitysBean.ProvinceBean>>) emitter -> {
                    emitter.onNext(ProvinceUtil.getAllProvince(this));
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(provinceBeanList -> {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < provinceBeanList.size(); i++) {
                        arrayList.add(provinceBeanList.get(i).getName());
                    }
                    stringArrayList.addAll(arrayList);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DATA && data != null) {
            String area = data.getStringExtra("area");
            String city = data.getStringExtra("city");
            String areaCode = data.getStringExtra("areaCode");
            Intent intent = new Intent();
            intent.putExtra("province", province);
            intent.putExtra("city", city);
            intent.putExtra("area", area);
            intent.putExtra("areaCode", areaCode);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
