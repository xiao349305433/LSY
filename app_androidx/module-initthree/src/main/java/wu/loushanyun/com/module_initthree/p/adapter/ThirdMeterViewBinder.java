package wu.loushanyun.com.module_initthree.p.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;

import java.math.BigDecimal;

import met.hx.com.base.base.basebinder.BaseItemViewBinder;
import met.hx.com.base.base.basebinder.ChildContentHolder;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.module_initthree.R;


public class ThirdMeterViewBinder extends BaseItemViewBinder<SaveDataMeter, ThirdMeterViewBinder.MeterHolder> {
    private Activity context;
    private OnZhiShuListener onZhiShuListener;
    private OnChaoBiaoListener onChaoBiaoListener;
    private MDDialog mdDialog;
    private TextView textDialogTitle;
    private LinearLayout layoutId;
    private EditText editId;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private RoundTextView roundSetting;
    private SaveDataMeter saveDataMeter;

    public ThirdMeterViewBinder(Activity context) {
        super(context);
        this.context = context;
    }


    public OnZhiShuListener getOnZhiShuListener() {
        return onZhiShuListener;
    }
    public interface OnZhiShuListener {
        void onZhiShu(SaveDataMeter saveDataMeter);
    }

    public void setOnZhiShuListener(OnZhiShuListener onZhiShuListener) {
        this.onZhiShuListener = onZhiShuListener;
    }


    public OnChaoBiaoListener getOnChaoBiaoListener() {
        return onChaoBiaoListener;
    }

    public void setOnChaoBiaoListener(OnChaoBiaoListener onChaoBiaoListener) {
        this.onChaoBiaoListener = onChaoBiaoListener;
    }

    public interface OnChaoBiaoListener {
        void onChaoBiao(SaveDataMeter saveDataMeter);
    }

    @Override
    protected ChildContentHolder onCreateContentChildHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_meter_md_item, parent, false);
        View view = inflater.inflate(R.layout.l_loushanyun_meter_zhishu_dialog, null);

        textDialogTitle = (TextView) view.findViewById(R.id.text_dialog_title);
        editId = (EditText) view.findViewById(R.id.edit_id);
        beilvSelect = (Spinner) view.findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) view.findViewById(R.id.edit_maichong_start);
        roundSetting = (RoundTextView) view.findViewById(R.id.round_setting);
        layoutId=view.findViewById(R.id.layout_id);
        mdDialog = new MDDialog.Builder(context).setContentView(view)
                .setShowTitle(false)
                .setShowButtons(false)
                .create();


        roundSetting.setOnClickListener(view1 -> {
            if (XHStringUtil.isEmpty(saveDataMeter.getHub(), false)) {
                ToastUtils.showShort("请未读取到该表信息，无法置数");
                return;
            }
            if (XHStringUtil.isEmpty(editMaichongStart.getText().toString(), false)) {
                ToastUtils.showShort("请填写脉冲数");
                return;
            }

            try {
                saveDataMeter.getMeterMap().put(MapParams.倍率, String.valueOf(LouShanYunUtils.getBLWriteIntByValue(String.valueOf(beilvSelect.getSelectedItem()))));
                saveDataMeter.getMeterMap().put(MapParams.脉冲数, editMaichongStart.getText().toString().trim());
                if (!saveDataMeter.getType().equals("3")){  //非3号模组初始化才要显示
                    saveDataMeter.getMeterMap().put(MapParams.设备ID, editId.getText().toString().trim());
                }else {
                    saveDataMeter.getMeterMap().put(MapParams.设备ID,saveDataMeter.getUserId());
                }
                saveDataMeter.setMeterMap(saveDataMeter.getMeterMap());
                onZhiShuListener.onZhiShu(saveDataMeter);
                mdDialog.dismiss();
            } catch (Exception e) {
                ToastUtils.showLong("参数填写不正确，请填写正整数");
            }

        });

        return  new MeterHolder(root);
    }

    @Override
    protected void onBindContentChildHolder(@NonNull MeterHolder holder, @NonNull SaveDataMeter meter) {
        holder.meterNumber.setText(" 表号:" + meter.getMeterNumber());
        boolean isGood=true;
        if (!XHStringUtil.isEmpty(meter.getUserId(), true) && !XHStringUtil.isEmpty(meter.getMeterNumber(), true)) {
            holder.deviceId.setText("表ID:" + meter.getUserId());
            try {
                BigDecimal bigDecimal = new BigDecimal(meter.getImpulseInitial());
                holder.resultNumber.setText("读数(m³):" + bigDecimal.divide(new BigDecimal(meter.getPulseConstant())).toPlainString());
            } catch (Exception e) {
                holder.resultNumber.setText("读数(m³):");
            }
            Drawable nav_up = context.getResources().getDrawable(R.drawable.l_loushanyun_normal);
            holder.imageMeterStatus.setImageDrawable(nav_up);
        } else {
            holder.deviceId.setText("表ID:");
            holder.resultNumber.setText("读数(m³):");
            Drawable nav_up = context.getResources().getDrawable(R.drawable.l_loushanyun_abnormal);
            holder.imageMeterStatus.setImageDrawable(nav_up);
            isGood=false;
        }
        if (!XHStringUtil.isEmpty(meter.getHub(), false)) {
            Drawable nav_up = context.getResources().getDrawable(R.drawable.l_loushanyun_normal);
            holder.imageHubStatus.setImageDrawable(nav_up);
        } else {
            Drawable nav_up = context.getResources().getDrawable(R.drawable.l_loushanyun_abnormal);
            holder.imageHubStatus.setImageDrawable(nav_up);
            isGood=false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表状态:");
        if ("1".equals(meter.getMeterMap().get(MapParams.表强磁状态))) {
            stringBuilder.append("强磁,");
        }
        if ("1".equals(meter.getMeterMap().get(MapParams.表电池状态))) {
            stringBuilder.append("欠压,");
        }
        if ("1".equals(meter.getMeterMap().get(MapParams.表流向状态))) {
            stringBuilder.append("倒流,");
        }
        if ("1".equals(meter.getMeterMap().get(MapParams.表拆卸状态))) {
            stringBuilder.append("拆卸,");
        }
        String s = stringBuilder.toString();
        if (s.equals("表状态:")) {
            if(isGood){
                stringBuilder.append("正常,");
            }else {
                stringBuilder.append("异常,");
            }
        }
        s = stringBuilder.toString();
        holder.resultMeterStatus.setText(s.substring(0, s.length() - 1));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mdDialog != null) {
            mdDialog.dismiss();
        }
    }


    class  MeterHolder extends ChildContentHolder  {
        private CardView cardMain11;
        private RoundTextView meterNumber;
        private TextView deviceId;
        private TextView resultNumber;
        private TextView hubState;
        private ImageView imageHubStatus;
        private TextView communicationState;
        private ImageView imageMeterStatus;
        private TextView resultMeterStatus;
        private RoundTextView textZhishu;
        private RoundTextView textChaobiao;

        MeterHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            meterNumber = (RoundTextView) view.findViewById(R.id.meter_number);
            deviceId = (TextView) view.findViewById(R.id.device_id);
            resultNumber = (TextView) view.findViewById(R.id.result_number);
            hubState = (TextView) view.findViewById(R.id.hub_state);
            imageHubStatus = (ImageView) view.findViewById(R.id.image_hub_status);
            communicationState = (TextView) view.findViewById(R.id.communication_state);
            imageMeterStatus = (ImageView) view.findViewById(R.id.image_meter_status);
            resultMeterStatus = (TextView) view.findViewById(R.id.result_meter_status);
            textZhishu = (RoundTextView) view.findViewById(R.id.text_zhishu);
            textChaobiao = (RoundTextView) view.findViewById(R.id.text_chaobiao);
            if (onZhiShuListener == null) {
                textZhishu.setVisibility(View.GONE);
            }else {
                textZhishu.setVisibility(View.VISIBLE);
            }

//            textChaobiao.setOnClickListener(v -> {
//                if (onChaoBiaoListener != null) {
//                    try {
//                        onChaoBiaoListener.onChaoBiao(getContentByViewHolder(getParent()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            textZhishu.setOnClickListener(view1 -> {
//                try {
//                    if (onZhiShuListener == null) {
//                        ToastUtils.showShort("无法使用置数功能");
//                    } else {
//                        saveDataMeter = getContentByViewHolder(this);
//                        if ("0".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(0);
//                        } else if ("1".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(1);
//                        } else if ("2".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(2);
//                        } else if ("3".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(3);
//                        } else if ("4".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(4);
//                        } else if ("5".equals(saveDataMeter.getMeterMap().get(MapParams.倍率))) {
//                            beilvSelect.setSelection(5);
//                        }
//                        if (saveDataMeter.getType().equals("3")){ //3号模组初始化才要隐藏
//                            layoutId.setVisibility(View.GONE);
//                            textDialogTitle.setText("3号模组初始化" + saveDataMeter.getMeterNumber() + "号表置数");
//                        }else {
//                            textDialogTitle.setText("集中器给" + saveDataMeter.getMeterNumber() + "号表置数");
//                        }
//
//
//                        editId.setText(saveDataMeter.getUserId());
//                        editMaichongStart.setText(saveDataMeter.getImpulseInitial());
//                        mdDialog.show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });


        }
    }
}
