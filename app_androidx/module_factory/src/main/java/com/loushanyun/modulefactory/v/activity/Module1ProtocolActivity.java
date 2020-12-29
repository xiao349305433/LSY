package com.loushanyun.modulefactory.v.activity;

import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.m.ProtocolData;
import com.loushanyun.modulefactory.p.adapter.ProtocolViewBinder;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;

public class Module1ProtocolActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    private final String TAG = "com.factorysrtting_ble.global.v.activity.Module1ProtocolActivity";
    private TextView bluetoothName;
    private TextView itemSelector;
    private TextView itemGeneralSetting;
    private TextView dataNormal;
    private TextView dataUser;
    private RoundLinearLayout productForm;
    private TextView productFormView;
    private LinearLayout numberItem1;
    private LinearLayout numSheet1;
    private TextView numStart1;
    private TextView numEnding1;
    private ImageView numberSet1;
    private TextView numLength1;
    private TextView numName1;
    private View numberLine1;
    private LinearLayout numberItem2;
    private LinearLayout numSheet2;
    private TextView numStart2;
    private TextView numEnding2;
    private ImageView numberSet2;
    private TextView numLength2;
    private TextView numName2;
    private View numberLine2;
    private LinearLayout numberItem3;
    private LinearLayout numSheet3;
    private TextView numStart3;
    private TextView numEnding3;
    private ImageView numberSet3;
    private TextView numLength3;
    private TextView numName3;
    private View numberLine3;
    private LinearLayout numberItem4;
    private LinearLayout numSheet4;
    private TextView numStart4;
    private TextView numEnding4;
    private ImageView numberSet4;
    private TextView numLength4;
    private TextView numName4;
    private View numberLine4;
    private LinearLayout numberItem5;
    private LinearLayout numSheet5;
    private TextView numStart5;
    private TextView numEnding5;
    private ImageView numberSet5;
    private TextView numLength5;
    private TextView numName5;
    private View numberLine5;
    private LinearLayout numberItem6;
    private LinearLayout numSheet6;
    private TextView numStart6;
    private TextView numEnding6;
    private ImageView numberSet6;
    private TextView numLength6;
    private TextView numName6;
    private View numberLine6;
    private LinearLayout numberItem7;
    private LinearLayout numSheet7;
    private TextView numStart7;
    private TextView numEnding7;
    private ImageView numberSet7;
    private TextView numLength7;
    private TextView numName7;
    private View numberLine7;
    private LinearLayout numberItem8;
    private LinearLayout numSheet8;
    private TextView numStart8;
    private TextView numEnding8;
    private ImageView numberSet8;
    private TextView numLength8;
    private TextView numName8;
    private View numberLine8;
    private LinearLayout numberItem9;
    private LinearLayout numSheet9;
    private TextView numStart9;
    private TextView numEnding9;
    private ImageView numberSet9;
    private TextView numLength9;
    private TextView numName9;
    private View numberLine9;
    private LinearLayout numberItem10;
    private LinearLayout numSheet10;
    private TextView numStart10;
    private TextView numEnding10;
    private ImageView numberSet10;
    private TextView numLength10;
    private TextView numName10;
    private View numberLine10;
    private LinearLayout numberItem11;
    private LinearLayout numSheet11;
    private TextView numStart11;
    private TextView numEnding11;
    private ImageView numberSet11;
    private TextView numLength11;
    private TextView numName11;
    private View numberLine11;
    private TextView itemAdd;
    private RoundTextView setting;
    private RoundTextView loading;
    private RoundLinearLayout resultSetting;
    private RecyclerView resultSettingView;
    private RoundLinearLayout resultLoading;
    private RecyclerView resultLoadingView;
    private FrameLayout shadow;
    private RoundLinearLayout dialog;
    private TextView dialogName;
    private View dialogItemLine0;
    private TextView dialogItemTv0;
    private View dialogItemLine1;
    private TextView dialogItemTv1;
    private View dialogItemLine2;
    private TextView dialogItemTv2;
    private View dialogItemLine3;
    private TextView dialogItemTv3;


    private ArrayList<ProtocolData> userSettingList = new ArrayList<ProtocolData>();
    private MultiTypeAdapter userSettingAdapter;
    private ProtocolViewBinder userSettingViewBinder;

    private ArrayList<ProtocolData> userResultList = new ArrayList<ProtocolData>();
    private MultiTypeAdapter userResultAdapter;
    private ProtocolViewBinder userResultViewBinder;

    private Drawable arrow, blank;
    private String[] dialogItem = {"压力计", "工业水表", "流量计", "管井盖"};
    private int length;
    private int minIndex = 41;
    private int current;
    private int maxIndex = 51;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_module1_protocol;
        ba.mTitleText = "1号模组协议管理";
    }

    @Override
    protected void initView() {
        bluetoothName = (TextView) findViewById(R.id.bluetooth_name);
        itemSelector = (TextView) findViewById(R.id.item_selector);
        itemGeneralSetting = (TextView) findViewById(R.id.item_general_setting);
        dataNormal = (TextView) findViewById(R.id.data_normal);
        dataUser = (TextView) findViewById(R.id.data_user);
        productForm = (RoundLinearLayout) findViewById(R.id.product_form);
        productFormView = (TextView) findViewById(R.id.product_form_view);
        numberItem1 = (LinearLayout) findViewById(R.id.number_item_1);
        numSheet1 = (LinearLayout) findViewById(R.id.num_sheet_1);
        numStart1 = (TextView) findViewById(R.id.num_start_1);
        numEnding1 = (TextView) findViewById(R.id.num_ending_1);
        numberSet1 = (ImageView) findViewById(R.id.number_set_1);
        numLength1 = (TextView) findViewById(R.id.num_length_1);
        numName1 = (TextView) findViewById(R.id.num_name_1);
        numberLine1 = (View) findViewById(R.id.number_line_1);
        numberItem2 = (LinearLayout) findViewById(R.id.number_item_2);
        numSheet2 = (LinearLayout) findViewById(R.id.num_sheet_2);
        numStart2 = (TextView) findViewById(R.id.num_start_2);
        numEnding2 = (TextView) findViewById(R.id.num_ending_2);
        numberSet2 = (ImageView) findViewById(R.id.number_set_2);
        numLength2 = (TextView) findViewById(R.id.num_length_2);
        numName2 = (TextView) findViewById(R.id.num_name_2);
        numberLine2 = (View) findViewById(R.id.number_line_2);
        numberItem3 = (LinearLayout) findViewById(R.id.number_item_3);
        numSheet3 = (LinearLayout) findViewById(R.id.num_sheet_3);
        numStart3 = (TextView) findViewById(R.id.num_start_3);
        numEnding3 = (TextView) findViewById(R.id.num_ending_3);
        numberSet3 = (ImageView) findViewById(R.id.number_set_3);
        numLength3 = (TextView) findViewById(R.id.num_length_3);
        numName3 = (TextView) findViewById(R.id.num_name_3);
        numberLine3 = (View) findViewById(R.id.number_line_3);
        numberItem4 = (LinearLayout) findViewById(R.id.number_item_4);
        numSheet4 = (LinearLayout) findViewById(R.id.num_sheet_4);
        numStart4 = (TextView) findViewById(R.id.num_start_4);
        numEnding4 = (TextView) findViewById(R.id.num_ending_4);
        numberSet4 = (ImageView) findViewById(R.id.number_set_4);
        numLength4 = (TextView) findViewById(R.id.num_length_4);
        numName4 = (TextView) findViewById(R.id.num_name_4);
        numberLine4 = (View) findViewById(R.id.number_line_4);
        numberItem5 = (LinearLayout) findViewById(R.id.number_item_5);
        numSheet5 = (LinearLayout) findViewById(R.id.num_sheet_5);
        numStart5 = (TextView) findViewById(R.id.num_start_5);
        numEnding5 = (TextView) findViewById(R.id.num_ending_5);
        numberSet5 = (ImageView) findViewById(R.id.number_set_5);
        numLength5 = (TextView) findViewById(R.id.num_length_5);
        numName5 = (TextView) findViewById(R.id.num_name_5);
        numberLine5 = (View) findViewById(R.id.number_line_5);
        numberItem6 = (LinearLayout) findViewById(R.id.number_item_6);
        numSheet6 = (LinearLayout) findViewById(R.id.num_sheet_6);
        numStart6 = (TextView) findViewById(R.id.num_start_6);
        numEnding6 = (TextView) findViewById(R.id.num_ending_6);
        numberSet6 = (ImageView) findViewById(R.id.number_set_6);
        numLength6 = (TextView) findViewById(R.id.num_length_6);
        numName6 = (TextView) findViewById(R.id.num_name_6);
        numberLine6 = (View) findViewById(R.id.number_line_6);
        numberItem7 = (LinearLayout) findViewById(R.id.number_item_7);
        numSheet7 = (LinearLayout) findViewById(R.id.num_sheet_7);
        numStart7 = (TextView) findViewById(R.id.num_start_7);
        numEnding7 = (TextView) findViewById(R.id.num_ending_7);
        numberSet7 = (ImageView) findViewById(R.id.number_set_7);
        numLength7 = (TextView) findViewById(R.id.num_length_7);
        numName7 = (TextView) findViewById(R.id.num_name_7);
        numberLine7 = (View) findViewById(R.id.number_line_7);
        numberItem8 = (LinearLayout) findViewById(R.id.number_item_8);
        numSheet8 = (LinearLayout) findViewById(R.id.num_sheet_8);
        numStart8 = (TextView) findViewById(R.id.num_start_8);
        numEnding8 = (TextView) findViewById(R.id.num_ending_8);
        numberSet8 = (ImageView) findViewById(R.id.number_set_8);
        numLength8 = (TextView) findViewById(R.id.num_length_8);
        numName8 = (TextView) findViewById(R.id.num_name_8);
        numberLine8 = (View) findViewById(R.id.number_line_8);
        numberItem9 = (LinearLayout) findViewById(R.id.number_item_9);
        numSheet9 = (LinearLayout) findViewById(R.id.num_sheet_9);
        numStart9 = (TextView) findViewById(R.id.num_start_9);
        numEnding9 = (TextView) findViewById(R.id.num_ending_9);
        numberSet9 = (ImageView) findViewById(R.id.number_set_9);
        numLength9 = (TextView) findViewById(R.id.num_length_9);
        numName9 = (TextView) findViewById(R.id.num_name_9);
        numberLine9 = (View) findViewById(R.id.number_line_9);
        numberItem10 = (LinearLayout) findViewById(R.id.number_item_10);
        numSheet10 = (LinearLayout) findViewById(R.id.num_sheet_10);
        numStart10 = (TextView) findViewById(R.id.num_start_10);
        numEnding10 = (TextView) findViewById(R.id.num_ending_10);
        numberSet10 = (ImageView) findViewById(R.id.number_set_10);
        numLength10 = (TextView) findViewById(R.id.num_length_10);
        numName10 = (TextView) findViewById(R.id.num_name_10);
        numberLine10 = (View) findViewById(R.id.number_line_10);
        numberItem11 = (LinearLayout) findViewById(R.id.number_item_11);
        numSheet11 = (LinearLayout) findViewById(R.id.num_sheet_11);
        numStart11 = (TextView) findViewById(R.id.num_start_11);
        numEnding11 = (TextView) findViewById(R.id.num_ending_11);
        numberSet11 = (ImageView) findViewById(R.id.number_set_11);
        numLength11 = (TextView) findViewById(R.id.num_length_11);
        numName11 = (TextView) findViewById(R.id.num_name_11);
        numberLine11 = (View) findViewById(R.id.number_line_11);
        itemAdd = (TextView) findViewById(R.id.item_add);
        setting = (RoundTextView) findViewById(R.id.setting);
        loading = (RoundTextView) findViewById(R.id.loading);
        resultSetting = (RoundLinearLayout) findViewById(R.id.result_setting);
        resultSettingView = (RecyclerView) findViewById(R.id.result_setting_view);
        resultLoading = (RoundLinearLayout) findViewById(R.id.result_loading);
        resultLoadingView = (RecyclerView) findViewById(R.id.result_loading_view);
        shadow = (FrameLayout) findViewById(R.id.shadow);
        dialog = (RoundLinearLayout) findViewById(R.id.dialog);
        dialogName = (TextView) findViewById(R.id.dialog_name);
        dialogItemLine0 = (View) findViewById(R.id.dialog_item_line0);
        dialogItemTv0 = (TextView) findViewById(R.id.dialog_item_tv0);
        dialogItemLine1 = (View) findViewById(R.id.dialog_item_line1);
        dialogItemTv1 = (TextView) findViewById(R.id.dialog_item_tv1);
        dialogItemLine2 = (View) findViewById(R.id.dialog_item_line2);
        dialogItemTv2 = (TextView) findViewById(R.id.dialog_item_tv2);
        dialogItemLine3 = (View) findViewById(R.id.dialog_item_line3);
        dialogItemTv3 = (TextView) findViewById(R.id.dialog_item_tv3);

        itemSelector.setOnClickListener(this::onClick);
        itemGeneralSetting.setOnClickListener(this::onClick);
        productFormView.setOnClickListener(this::onClick);
        numberSet1.setOnClickListener(this::onClick);
        numberSet2.setOnClickListener(this::onClick);
        numberSet3.setOnClickListener(this::onClick);
        numberSet4.setOnClickListener(this::onClick);
        numberSet5.setOnClickListener(this::onClick);
        numberSet6.setOnClickListener(this::onClick);
        numberSet7.setOnClickListener(this::onClick);
        numberSet8.setOnClickListener(this::onClick);
        numberSet9.setOnClickListener(this::onClick);
        numberSet10.setOnClickListener(this::onClick);
        numberSet11.setOnClickListener(this::onClick);
        itemAdd.setOnClickListener(this::onClick);
        setting.setOnClickListener(this::onClick);
        loading.setOnClickListener(this::onClick);

        //-------------------------------------------------------//
        arrow = getResources().getDrawable(R.drawable.blue);
        arrow.setBounds(0, 0, arrow.getMinimumWidth(), arrow.getMinimumHeight());
        blank = getResources().getDrawable(R.drawable.blank);
        blank.setBounds(0, 0, blank.getMinimumWidth(), blank.getMinimumHeight());

        for (int i = 0; i < 5; i++) {
            userSettingList.add(new ProtocolData());
        }
        userSettingAdapter = new MultiTypeAdapter();
        userSettingAdapter.setItems(userSettingList);
        userSettingViewBinder = new ProtocolViewBinder();
        userSettingAdapter.register(ProtocolData.class, userSettingViewBinder);
        resultSettingView.setAdapter(userSettingAdapter);
        resultSettingView.setLayoutManager(new GridLayoutManager(this, 3));
        resultSettingView.setNestedScrollingEnabled(false);

        for (int i = 0; i < 8; i++) {
            userResultList.add(new ProtocolData());
        }
        userResultAdapter = new MultiTypeAdapter();
        userResultAdapter.setItems(userResultList);
        userResultViewBinder = new ProtocolViewBinder();
        userResultAdapter.register(ProtocolData.class, userResultViewBinder);
        resultLoadingView.setAdapter(userResultAdapter);
        resultLoadingView.setLayoutManager(new GridLayoutManager(this, 3));
        resultLoadingView.setNestedScrollingEnabled(false);
        removeAllItem();
        length = 0;
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_add:
                addItem();
                break;
            default:
        }
    }

    //调用一次增加一条
    public void addItem() {
        if (length <= maxIndex - minIndex) {
            length = length + 1;
        } else {
            sendMessageToast("添加出现错误");
        }
        switch (length) {
            case 11:
                numberItem11.setVisibility(View.VISIBLE);
                numberLine11.setVisibility(View.VISIBLE);
            case 10:
                numberItem10.setVisibility(View.VISIBLE);
                numberLine10.setVisibility(View.VISIBLE);
            case 9:
                numberItem9.setVisibility(View.VISIBLE);
                numberLine9.setVisibility(View.VISIBLE);
            case 8:
                numberItem8.setVisibility(View.VISIBLE);
                numberLine8.setVisibility(View.VISIBLE);
            case 7:
                numberItem7.setVisibility(View.VISIBLE);
                numberLine7.setVisibility(View.VISIBLE);
            case 6:
                numberItem6.setVisibility(View.VISIBLE);
                numberLine6.setVisibility(View.VISIBLE);
            case 5:
                numberItem5.setVisibility(View.VISIBLE);
                numberLine5.setVisibility(View.VISIBLE);
            case 4:
                numberItem4.setVisibility(View.VISIBLE);
                numberLine4.setVisibility(View.VISIBLE);
            case 3:
                numberItem3.setVisibility(View.VISIBLE);
                numberLine3.setVisibility(View.VISIBLE);
            case 2:
                numberItem2.setVisibility(View.VISIBLE);
                numberLine2.setVisibility(View.VISIBLE);
            case 1:
                numberItem1.setVisibility(View.VISIBLE);
                numberLine1.setVisibility(View.VISIBLE);
                break;
            case 0:
                break;
            default:
        }
    }

    //调用一次删除一条
    public void removeItem() {
        if (length > 0) {
            length = length - 1;
        } else {
            sendMessageToast("删除出现错误");
        }
        switch (length) {
            case 0:
                numberItem1.setVisibility(View.GONE);
                numberLine1.setVisibility(View.GONE);
            case 1:
                numberItem2.setVisibility(View.GONE);
                numberLine2.setVisibility(View.GONE);
            case 2:
                numberItem3.setVisibility(View.GONE);
                numberLine3.setVisibility(View.GONE);
            case 3:
                numberItem4.setVisibility(View.GONE);
                numberLine4.setVisibility(View.GONE);
            case 4:
                numberItem5.setVisibility(View.GONE);
                numberLine5.setVisibility(View.GONE);
            case 5:
                numberItem6.setVisibility(View.GONE);
                numberLine6.setVisibility(View.GONE);
            case 6:
                numberItem7.setVisibility(View.GONE);
                numberLine7.setVisibility(View.GONE);
            case 7:
                numberItem8.setVisibility(View.GONE);
                numberLine8.setVisibility(View.GONE);
            case 8:
                numberItem9.setVisibility(View.GONE);
                numberLine9.setVisibility(View.GONE);
            case 9:
                numberItem10.setVisibility(View.GONE);
                numberLine10.setVisibility(View.GONE);
            case 10:
                numberItem11.setVisibility(View.GONE);
                numberLine11.setVisibility(View.GONE);
                break;
            default:
        }
    }

    public void removeAllItem() {
        numberItem1.setVisibility(View.GONE);
        numberLine1.setVisibility(View.GONE);
        numberItem2.setVisibility(View.GONE);
        numberLine2.setVisibility(View.GONE);
        numberItem3.setVisibility(View.GONE);
        numberLine3.setVisibility(View.GONE);
        numberItem4.setVisibility(View.GONE);
        numberLine4.setVisibility(View.GONE);
        numberItem5.setVisibility(View.GONE);
        numberLine5.setVisibility(View.GONE);
        numberItem6.setVisibility(View.GONE);
        numberLine6.setVisibility(View.GONE);
        numberItem7.setVisibility(View.GONE);
        numberLine7.setVisibility(View.GONE);
        numberItem8.setVisibility(View.GONE);
        numberLine8.setVisibility(View.GONE);
        numberItem9.setVisibility(View.GONE);
        numberLine9.setVisibility(View.GONE);
        numberItem10.setVisibility(View.GONE);
        numberLine10.setVisibility(View.GONE);
        numberItem11.setVisibility(View.GONE);
        numberLine11.setVisibility(View.GONE);
    }

    public void showDialog() {
        int index = 0;
        for (; index < dialogItem.length; index++) {
            if (dialogItem[index].contentEquals(productFormView.getText())) {
                break;
            }
        }
        dialogItemTv0.setVisibility(View.VISIBLE);
        dialogItemLine0.setVisibility(View.VISIBLE);
        dialogItemTv1.setVisibility(View.VISIBLE);
        dialogItemLine1.setVisibility(View.VISIBLE);
        dialogItemTv2.setVisibility(View.VISIBLE);
        dialogItemLine2.setVisibility(View.VISIBLE);
        dialogItemTv3.setVisibility(View.VISIBLE);
        dialogItemLine3.setVisibility(View.VISIBLE);
        dialogItemTv0.setCompoundDrawables(blank, null, null, null);
        dialogItemTv1.setCompoundDrawables(blank, null, null, null);
        dialogItemTv2.setCompoundDrawables(blank, null, null, null);
        dialogItemTv3.setCompoundDrawables(blank, null, null, null);
        if (index == 0) {
            dialogItemTv0.setCompoundDrawables(arrow, null, null, null);
        } else if (index == 1) {
            dialogItemTv1.setCompoundDrawables(arrow, null, null, null);
        } else if (index == 2) {
            dialogItemTv2.setCompoundDrawables(arrow, null, null, null);
        } else if (index == 3) {
            dialogItemTv3.setCompoundDrawables(arrow, null, null, null);
        }
        shadow.setVisibility(View.VISIBLE);
        dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏所有弹窗样式布局
     */
    public void hideDialog() {
        dialogItemTv0.setVisibility(View.GONE);
        dialogItemLine0.setVisibility(View.GONE);
        dialogItemTv1.setVisibility(View.GONE);
        dialogItemLine1.setVisibility(View.GONE);
        dialogItemTv2.setVisibility(View.GONE);
        dialogItemLine2.setVisibility(View.GONE);
        dialogItemTv3.setVisibility(View.GONE);
        dialogItemLine3.setVisibility(View.GONE);
        dialog.setVisibility(View.GONE);
        shadow.setVisibility(View.GONE);
    }

}
