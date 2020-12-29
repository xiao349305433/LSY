package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.p.runner.UploadYuanChuanRunner;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LFiveCode;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;
import wu.loushanyun.com.libraryfive.p.runner.MRepairModifyThirdEquipmentRunner;
import wu.loushanyun.com.module_initthree.init.InitCode;

@Route(path = K.UpLoadDataInfoActivity)
public class UpLoadDataInfoActivity extends BaseNoPresenterActivity {
    private TextView sn;
    private TextView textQuyuhao;
    private TextView time;
    private TextView dataInit;
    private TextView dataLeft;
    private TextView dataRight;
    private TextView timeFactory;
    private ImageView lv1;
    private ImageView lv2;
    private ImageView lv3;
    private ImageView lv4;
    private RoundTextView upload;
    private YuanChuanSaveData yuanChuanSaveData;
    private String[] mPhotoPath = new String[4];
    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";
    private RepairUpdateData repairUpdateData;
    private long repairUpdateDataId;

    private boolean isRepaired;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_five_activity_up_load_data_info;
        ba.mTitleText = "物联网集中器定位详情";
    }

    private void getAllData() {
        long yuanChuanSaveDataId = getIntent().getLongExtra("yuanChuanSaveDataId", 0);
        repairUpdateDataId = getIntent().getLongExtra("repairUpdateDataId", 0);
        isRepaired = getIntent().getBooleanExtra("isRepaired", false);
        if (isRepaired) {
            if (repairUpdateDataId != 0) {
                repairUpdateData = LitePal.find(RepairUpdateData.class, repairUpdateDataId);
                yuanChuanSaveData = new Gson().fromJson(repairUpdateData.getYuanChuanSaveDataJson(), YuanChuanSaveData.class);
            } else {
                sendMessageToast("没找到本地数据，请删除后重新保存");
                finish();
            }
        } else {
            yuanChuanSaveData = LitePal.find(YuanChuanSaveData.class, yuanChuanSaveDataId);
            if (yuanChuanSaveData == null) {
                sendMessageToast("没找到本地数据，请删除后重新保存");
                finish();
            }
        }

    }

    @Override
    protected void initView() {
        sn = (TextView) findViewById(R.id.sn);
        textQuyuhao = (TextView) findViewById(R.id.text_quyuhao);
        time = (TextView) findViewById(R.id.time);
        dataInit = (TextView) findViewById(R.id.data_init);
        dataLeft = (TextView) findViewById(R.id.data_left);
        dataRight = (TextView) findViewById(R.id.data_right);
        timeFactory = (TextView) findViewById(R.id.time_factory);
        lv1 = (ImageView) findViewById(R.id.lv1);
        lv2 = (ImageView) findViewById(R.id.lv2);
        lv3 = (ImageView) findViewById(R.id.lv3);
        lv4 = (ImageView) findViewById(R.id.lv4);
        upload = (RoundTextView) findViewById(R.id.upload);

        Gson gson = new Gson();

        lv1.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE1, fileProvider);
        });
        lv2.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE2, fileProvider);
        });
        lv3.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE3, fileProvider);
        });
        lv4.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE4, fileProvider);
        });
        if (isRepaired) {
            upload.setText("更换");
        } else {
            upload.setText("定位");
        }
        upload.setOnClickListener(v -> {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(mPhotoPath[0]);
            arrayList.add(mPhotoPath[1]);
            arrayList.add(mPhotoPath[2]);
            arrayList.add(mPhotoPath[3]);
            yuanChuanSaveData.setJsonImage(gson.toJson(arrayList));
            LogUtils.i("厂家" + yuanChuanSaveData.getFactoryName());
            if (isRepaired) {
                pushEvent(LFiveCode.MRepairModifyThirdEquipmentRunner, yuanChuanSaveData, repairUpdateData.getOldSn());
            } else {
                pushEvent(InitCode.UploadYuanChuanRunner, yuanChuanSaveData);
            }

        });
        sn.setText("物联SN：" + yuanChuanSaveData.getSn());
        if (repairUpdateDataId == 0) {
            textQuyuhao.setVisibility(View.VISIBLE);
        } else {
            textQuyuhao.setVisibility(View.GONE);
        }
        textQuyuhao.setText("网格号：" + yuanChuanSaveData.getGridCode());
        time.setText("保存时间：" + yuanChuanSaveData.getTime());

        ArrayList<String> arrayList = gson.fromJson(yuanChuanSaveData.getJsonImage(), new TypeToken<ArrayList<String>>() {
        }.getType());
        mPhotoPath[0] = arrayList.get(0);
        mPhotoPath[1] = arrayList.get(1);
        mPhotoPath[2] = arrayList.get(2);
        mPhotoPath[3] = arrayList.get(3);
        GlideUtil.display(this, lv1, Base64.decode(arrayList.get(0).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, lv2, Base64.decode(arrayList.get(1).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, lv3, Base64.decode(arrayList.get(2).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, lv4, Base64.decode(arrayList.get(3).getBytes(), Base64.DEFAULT));

        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(yuanChuanSaveData.getAttrMapJson());
        } catch (JSONException e) {
            Log.i("yunanhao", "JSON解析失败");
        }
        double softVersion = Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(json.optString(MapParams.硬件版本))));
        pushEvent(InitCode.GetChangJiaBiaoShiRunner, json.optString(MapParams.厂家标识));
        StringBuilder sb = new StringBuilder();
        sb.append("表号：");
        sb.append(json.optString(MapParams.总线起止表号_起));
        sb.append("~");
        sb.append(json.optString(MapParams.总线起止表号_止));
        sb.append("\n扩频因子：");

        if (softVersion >= 1.07) {
            sb.append(json.optString(SAtInstructParams.sAtInstructSpreadingFactor));
        } else {
            sb.append(LouShanYunUtils.getKPYZReadStringByCode(json.optString(MapParams.扩频因子)));
        }
        sb.append("\n信道参数：");
        if (softVersion >= 1.07) {
            sb.append(json.optString(SAtInstructParams.sAtInstructChannel));
        } else {
            sb.append("0".equalsIgnoreCase(json.optString(MapParams.信道参数)) ? "模式A" : "模式B");
        }
        sb.append("\n信号强度(Rssi)：");
        sb.append(json.optString(MapParams.信号强度));
        sb.append("\n信噪比(Snr)：");
        sb.append(json.optString(MapParams.信噪比));
        sb.append("\n发送功率：");
        if (softVersion >= 1.07) {
            sb.append(json.optString(SAtInstructParams.sAtInstructSendingPower));
        } else {
            sb.append(json.optString(MapParams.发送功率) + "dbm");
        }
        dataInit.setText(sb.toString());
        sb = new StringBuilder();
        sb.append("\n使用类型：");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(json.optString(MapParams.采集场景)));
        sb.append("\n产品形式：");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(json.optString(MapParams.产品形式)));
        sb.append("\n传感信号：");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(json.optString(MapParams.传感信号, "0"))));
        sb.append("\n无线频率：");
        sb.append(LouShanYunUtils.getWuXianPinLv(json.optString(MapParams.无线频率)));
        sb.append("\n发送频率：");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(json.optString(MapParams.发送频率, "0"))));
        sb.append("\n电源类型：");
        sb.append(LouShanYunUtils.getDianYuanLeiXin(json.optString(MapParams.电源类型)));
        dataLeft.setText(sb);
        sb = new StringBuilder();
        sb.append("\n物联电池：");
        sb.append(("0".equalsIgnoreCase(json.optString(MapParams.底板状态_自备电池状态)) ? "正常" : "异常"));
        sb.append("\n计量状态：");
        sb.append("正常");
        sb.append("\n通讯状态：");
        sb.append("正常");
        sb.append("\n厂家标识：-");
        sb.append("\n硬件版本：");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(json.optString(MapParams.硬件版本))));
        sb.append("\n软件版本：");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(json.optString(MapParams.软件版本))));
        dataRight.setText(sb);
        sb = new StringBuilder();
        sb.append("\n出厂时间：20");
        sb.append(json.optString(MapParams.出厂时间_年));
        sb.append("-");
        sb.append(json.optString(MapParams.出厂时间_月));
        sb.append("-");
        sb.append(json.optString(MapParams.出厂时间_日));
        sb.append(" 00:00:00");
        timeFactory.setText(sb.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE1) {
                GlideUtil.display(this, lv1, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[0] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE2) {
                GlideUtil.display(this, lv2, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[1] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE3) {
                GlideUtil.display(this, lv3, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[2] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE4) {
                GlideUtil.display(this, lv4, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[3] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            }
        }

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.UploadYuanChuanRunner, new UploadYuanChuanRunner());
        registerEventRunner(InitCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
        registerEventRunner(LFiveCode.MRepairModifyThirdEquipmentRunner, new MRepairModifyThirdEquipmentRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.UploadYuanChuanRunner) {
            if (event.isSuccess()) {
                int code1 = (int) event.getReturnParamAtIndex(0);
                if (code1 == 0) {
                    sendMessageToast((String) event.getReturnParamAtIndex(1));
//                    yuanChuanSaveData.deleteAsync().listen(rowsAffected -> {
//                        EventBus.getDefault().post(new RefreshYuanChengBiaoHao());
//                        finish();
//                    });
                } else {
                    sendMessageToast((String) event.getReturnParamAtIndex(1), true);
                }

            }
        } else if (code == InitCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                String message = dataRight.getText().toString();
                yuanChuanSaveData.setFactoryName(name);
                dataRight.setText(message.replaceAll("厂家标识：-", "厂家标识：" + name));
            }
        } else if (code == LFiveCode.MRepairModifyThirdEquipmentRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                if (codeReturn == 0) {
                    saveUpdateData();
                    sendMessageToast("更换成功", true);
                } else {
                    sendMessageToast((String) event.getReturnParamAtIndex(1), true);
                }
            }
        }
    }

    /**
     * 删除数据
     */
    private void saveUpdateData() {
//        LitePal.deleteAsync(RepairUpdateData.class, repairUpdateData.getBaseObjId()).listen(new UpdateOrDeleteCallback() {
//            @Override
//            public void onFinish(int rowsAffected) {
//                EventBus.getDefault().post(new RefreshMainRepair());
//                finish();
//            }
//        });
    }
}
