package wu.loushanyun.com.moduletwoinit.p.adapter;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.ConverterDeviceData;

public class LSYDeviceBinderView extends ItemViewBinder<ConverterDeviceData, LSYDeviceBinderView.ViewHolder> {
    private String areaNumber;
    private String areaName;
    private int jumpType;
    private long id;


    public LSYDeviceBinderView(String areaNumber, int jumpType, long id, String areaName) {
        this.areaNumber = areaNumber;
        this.jumpType = jumpType;
        this.id = id;
        this.areaName = areaName;
    }

    private String keyWord;

    public void setKeyWord(String keyWrold) {
        this.keyWord = keyWrold;
        getAdapter().notifyDataSetChanged();
    }

    @NonNull
    @Override
    protected LSYDeviceBinderView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_device, parent, false);
        LSYDeviceBinderView.ViewHolder holder = new LSYDeviceBinderView.ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull LSYDeviceBinderView.ViewHolder holder, @NonNull ConverterDeviceData data) {
        if (data.isCanClick()) {
            holder.textMac.setVisibility(View.VISIBLE);
            holder.textXinhao.setVisibility(View.VISIBLE);
            holder.textSetting.getDelegate().setBackgroundColor(getActivity().getResources().getColor(R.color.l_five_Q));
            holder.textMac.setText(data.getDevice().getMacAddress());
            holder.textXinhao.setText(data.getDevice().getRssi() + "");
        } else {
            holder.textMac.setVisibility(View.GONE);
            holder.textXinhao.setVisibility(View.GONE);
            holder.textSetting.getDelegate().setBackgroundColor(getActivity().getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
        }
        holder.textSn.setText("物联SN：" + data.getSerialnumber());
        if (data.getConverter() != null) {
            holder.relativeSecond.setVisibility(View.VISIBLE);
            if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
                holder.textDetails.setVisibility(View.GONE);
                holder.textDelete.setVisibility(View.GONE);
            } else {
                holder.textDetails.setVisibility(View.VISIBLE);
                holder.textDelete.setVisibility(View.VISIBLE);
            }
            holder.textChangjiadaima.setText("厂家名称：" + data.getConverter().getFactoryName());
            if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
                holder.textChuanganxinhao.setText("传感信号：" + data.getConverter().getSensingSignal());
                holder.textChuchangriqi.setText("出厂日期：" + data.getConverter().getEquipmentTime());
            } else {
                holder.textChuanganxinhao.setText("传感信号：" + LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(data.getConverter().getSensingSignal())));
                holder.textChuchangriqi.setText("安装日期：" + data.getConverter().getEquipmentTime() + " 00:00:00");
            }
            holder.textBeizhu.setText("备注：" + data.getConverter().getRemark());
        } else {
            holder.relativeSecond.setVisibility(View.GONE);
        }
        String str = data.getSerialnumber();
        holder.setVisibility(false);
        if (XHStringUtil.isEmpty(keyWord, true)) {
            holder.textSn.setText("物联SN:" + str);
            holder.setVisibility(true);
        } else {
            Pattern p = Pattern.compile(keyWord);
            Matcher m = p.matcher(str);
            SpannableString spannableString = new SpannableString("物联SN:" + str);
            if (m.find()) {
                spannableString.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA300")), 5 + m.start(),
                        5 + m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.textSn.setText(spannableString);
                holder.setVisibility(true);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView relativeAll;
        private RelativeLayout relativeFirst;
        private TextView textSn;
        private TextView textMac;
        private TextView textXinhao;
        private RoundTextView textSetting;
        private RelativeLayout relativeSecond;
        private TextView textChangjiadaima;
        private TextView textChuanganxinhao;
        private TextView textChuchangriqi;
        private TextView textBeizhu;
        private RoundTextView textDetails;
        private RoundTextView textDelete;

        public ViewHolder(View view) {
            super(view);
            relativeAll = (CardView) view.findViewById(R.id.relative_all);
            relativeFirst = (RelativeLayout) view.findViewById(R.id.relative_first);
            textSn = (TextView) view.findViewById(R.id.text_sn);
            textMac = (TextView) view.findViewById(R.id.text_mac);
            textXinhao = (TextView) view.findViewById(R.id.text_xinhao);
            textSetting = (RoundTextView) view.findViewById(R.id.text_setting);
            relativeSecond = (RelativeLayout) view.findViewById(R.id.relative_second);
            textChangjiadaima = (TextView) view.findViewById(R.id.text_changjiadaima);
            textChuanganxinhao = (TextView) view.findViewById(R.id.text_chuanganxinhao);
            textChuchangriqi = (TextView) view.findViewById(R.id.text_chuchangriqi);
            textBeizhu = (TextView) view.findViewById(R.id.text_beizhu);
            textDetails = (RoundTextView) view.findViewById(R.id.text_details);
            textDelete = (RoundTextView) view.findViewById(R.id.text_delete);

            textSetting.setOnClickListener(this);
            textDetails.setOnClickListener(this);
            textDelete.setOnClickListener(this);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        @Override
        public void onClick(View v) {
            try {
                ConverterDeviceData converterDeviceData = getContentByViewHolder(this);
                if (v.getId() == R.id.text_setting) {
                    if (converterDeviceData.isCanClick()) {
                        try {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("sensoroDevice", converterDeviceData.getDevice());
                            bundle.putString("areaNumber", areaNumber);
                            bundle.putString("areaName", areaName);
                            if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
                                if (converterDeviceData.getConverter() != null) {
                                    bundle.putInt("jumpType", LSY2InitTypeCode.TypeFromOnCloudUpdateLocation);
                                } else {
                                    bundle.putInt("jumpType", LSY2InitTypeCode.TypeFromOnCloudInsideLocation);
                                }
                            } else {
                                bundle.putInt("jumpType", jumpType);
                            }
                            ARouter.getInstance().build(K.LSY2InitActivity).with(bundle).navigation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.showShort("未搜到该蓝牙");
                    }
                } else if (v.getId() == R.id.text_details) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sn", converterDeviceData.getSerialnumber());
                    bundle.putLong("id", id);
                    if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
                        if (converterDeviceData.getConverter() != null) {
                            bundle.putInt("jumpType", LSY2InitTypeCode.TypeFromOnCloudUpdateLocation);
                        } else {
                            bundle.putInt("jumpType", LSY2InitTypeCode.TypeFromOnCloudInsideLocation);
                        }
                    } else {
                        bundle.putInt("jumpType", jumpType);
                    }
                    ARouter.getInstance().build(K.LSY2LocationDetailActivity).with(bundle).navigation();
                } else if (v.getId() == R.id.text_delete) {
                    try {
                        converterDeviceData.getConverter().deleteAsync().listen(rowsAffected -> {
                            getAdapter().getItems().remove(getAdapterPosition());
                            getAdapter().notifyItemRemoved(getAdapterPosition());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
