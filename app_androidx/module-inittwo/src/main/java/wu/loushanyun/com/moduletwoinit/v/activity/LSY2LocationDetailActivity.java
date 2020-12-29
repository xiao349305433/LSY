package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LFiveCode;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.p.runner.ModifyOnetoOneEquipmentRunner;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.InsideSaveOnetoOneData;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.runner.GetIotInfoRunner;

@Route(path = K.LSY2LocationDetailActivity)
public class LSY2LocationDetailActivity extends BaseNoPresenterActivity {
    private CardView cardViewQuyu;
    private TextView textQuyuDetails;
    private TextView textMeterDetails;
    private RoundLinearLayout roundTextFive;
    private TextView textImageTishi;
    private ImageView ivSelector;
    private RoundTextView roundTextSave;

    private long id;
    private String sn;
    private int jumpType;

    private String oldSn;
    private String onetoOneConverterJson;
    private OnetoOneConverter onetoOneConverterReplace;
    private long deleteId;

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetIotInfoRunner, new GetIotInfoRunner());
        registerEventRunner(LFiveCode.ModifyOnetoOneEquipmentRunner, new ModifyOnetoOneEquipmentRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetIotInfoRunner) {
            if (event.isSuccess()) {
                cardViewQuyu.setVisibility(View.GONE);
                onetoOneConverterReplace = (OnetoOneConverter) event.getReturnParamAtIndex(1);
                if (onetoOneConverterReplace != null) {
                    textMeterDetails.setText(onetoOneConverterReplace.printAll());
                    if (onetoOneConverterReplace != null) {
                        GlideUtil.display(this, ivSelector, onetoOneConverterReplace.getImagePath());
                    }
                }
            }
        }else if (code == LFiveCode.ModifyOnetoOneEquipmentRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg, true);
                switch (codeReturn) {
                    case 0:
//                        LitePal.deleteAsync(RepairUpdateData.class, deleteId).listen(new UpdateOrDeleteCallback() {
//                            @Override
//                            public void onFinish(int rowsAffected) {
//                                EventBus.getDefault().post(new RefreshMainRepair());
//                                finish();
//                            }
//                        });
                        break;
                }
            }

        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_lsy2_detail;
        ba.mTitleText = "定位信息详情";
    }

    private void getAllData() {
        id = getIntent().getLongExtra("id", 0);
        sn = getIntent().getStringExtra("sn");
        jumpType = getIntent().getIntExtra("jumpType", 0);

        oldSn = getIntent().getExtras().getString("oldSn");
        onetoOneConverterJson = getIntent().getExtras().getString("onetoOneConverterJson");
        deleteId = getIntent().getExtras().getLong("deleteId", 0);
    }

    @Override
    protected void initView() {
        cardViewQuyu = (CardView) findViewById(R.id.card_view_quyu);
        textQuyuDetails = (TextView) findViewById(R.id.text_quyu_details);
        textMeterDetails = (TextView) findViewById(R.id.text_meter_details);
        roundTextFive = (RoundLinearLayout) findViewById(R.id.round_text_five);
        textImageTishi = (TextView) findViewById(R.id.text_image_tishi);
        ivSelector = (ImageView) findViewById(R.id.iv_selector);
        roundTextSave = (RoundTextView) findViewById(R.id.round_text_save);



        loadData();
        roundTextSave.setOnClickListener(v -> {
            saveUpload(onetoOneConverterReplace);
        });

        ivSelector.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onetoOneConverterReplace.getImagePath() != null) {
                    ArrayList list = new ArrayList(1);
                    list.add(onetoOneConverterReplace.getImagePath());
                    ARouter.getInstance().build(C.FullViewPictureActivity)
                            .withStringArrayList("paths", list)
                            .withBoolean("hasDelete", false)
                            .navigation(LSY2LocationDetailActivity.this);
                }
                return true;
            }
        });
    }

    private void OnClick(View view) {
        if(view.getId()==R.id.iv_selector){
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, 99, "wu.loushanyun.com.fivemoduleapp.fileprovider");
        }
    }

    private void loadData() {
        if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
            roundTextSave.setVisibility(View.GONE);
            textImageTishi.setVisibility(View.GONE);
            pushEvent(MInitTwoCode.GetIotInfoRunner, sn);
        } else if (jumpType == LSY2InitTypeCode.TypeFromCreateLocation || jumpType == LSY2InitTypeCode.TypeFromUpdateLocation) {
            roundTextSave.setVisibility(View.GONE);
            textImageTishi.setVisibility(View.GONE);
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<ArrayList<Object>>) emitter -> {
                        LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                        WuLianUploadData uploadData = LitePal.find(WuLianUploadData.class, id);
                        int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                        List<OnetoOneConverter> arrayList = LitePal.where("sn = ? and loginid = ?", sn, loginid + "")
                                .find(OnetoOneConverter.class);
                        ArrayList<Object> arrayList1 = new ArrayList<>();
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                        if (arrayList.size() != 0) {
                            arrayList1.add(uploadData);
                            arrayList1.add(arrayList.get(0));
                            emitter.onNext(arrayList1);
                            emitter.onComplete();
                        } else {
                            emitter.onComplete();
                        }
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(list -> {
                        WuLianUploadData wuLianUploadData = (WuLianUploadData) list.get(0);
                        onetoOneConverterReplace = (OnetoOneConverter) list.get(1);
                        if (wuLianUploadData != null) {
                            textQuyuDetails.setText(wuLianUploadData.printAll());
                        } else {
                            cardViewQuyu.setVisibility(View.GONE);
                        }
                        if (onetoOneConverterReplace != null) {
                            textMeterDetails.setText(onetoOneConverterReplace.printAll());
                            if (onetoOneConverterReplace != null) {
                                GlideUtil.display(this, ivSelector, onetoOneConverterReplace.getImagePath());
                            }
                        }
                    }));
        } else if (jumpType == LSY2InitTypeCode.TypeFromReplace) {
            roundTextSave.setVisibility(View.VISIBLE);
            textImageTishi.setVisibility(View.VISIBLE);
            cardViewQuyu.setVisibility(View.GONE);
            ivSelector.setOnClickListener(this::OnClick);
            if (!XHStringUtil.isEmpty(onetoOneConverterJson, false)) {
                InsideSaveOnetoOneData insideSaveOnetoOneData = new Gson().fromJson(onetoOneConverterJson, InsideSaveOnetoOneData.class);
                onetoOneConverterReplace = insideSaveOnetoOneData.getConverter().get(0);
                if (onetoOneConverterReplace != null) {
                    textMeterDetails.setText(onetoOneConverterReplace.printAll());
                    if (onetoOneConverterReplace != null) {
                        GlideUtil.display(this, ivSelector, onetoOneConverterReplace.getImagePath());
                    }
                }
            }
        }

    }
    private void saveUpload(OnetoOneConverter onetoOneConverter) {
        if (XHStringUtil.isEmpty(onetoOneConverter.getImage(), true)) {
            sendMessageToast("请选择一张图片");
            return;
        }
        InsideSaveOnetoOneData data = new InsideSaveOnetoOneData();
        List<OnetoOneConverter> onetoOneConverterList = new ArrayList<>();
        onetoOneConverterList.add(onetoOneConverter);
        data.setConverter(onetoOneConverterList);
        data.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
        data.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
        pushEvent(LFiveCode.ModifyOnetoOneEquipmentRunner, oldSn, new Gson().toJson(data));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 99) {
                String imagePath = Matisse.obtainPathResult(intent).get(0);
                GlideUtil.display(this, ivSelector, imagePath);
                String data = PictureUtil.bitmapToString(imagePath);
                onetoOneConverterReplace.setImage(data);
                onetoOneConverterReplace.setImagePath(imagePath);
            }
        }
    }
}
