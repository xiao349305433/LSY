package com.wu.loushanyun.basemvp.p.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
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

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;

public class SNDeviceViewBinder extends ItemViewBinder<SensoroDevice, SNDeviceViewBinder.ViewHolder> {

    private OnSensoroDeviceListener onSensoroDeviceListener;
    private String keyWord;

    public void setKeyWord(String keyWrold) {
        this.keyWord = keyWrold;
        getAdapter().notifyDataSetChanged();
    }

    public SNDeviceViewBinder(OnSensoroDeviceListener onSensoroDeviceListener) {
        this.onSensoroDeviceListener = onSensoroDeviceListener;
    }

    public interface OnSensoroDeviceListener {
        void onSensoroDevice(SensoroDevice sensoroDevice);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_blue_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SensoroDevice sensoroDevice) {
        String str = sensoroDevice.getSerialNumber();
        holder.textBlueSn.setText("SN号:"+str);
        holder.textBlueMac.setText("MAC地址:"+sensoroDevice.macAddress);
        holder.textRssi.setText("信号:"+sensoroDevice.rssi+"");
        holder.setVisibility(false);
        if (XHStringUtil.isEmpty(keyWord, true)) {
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textBlueSn.setText("SN号:"+str);
            holder.setVisibility(true);
        } else {
            Pattern p = Pattern.compile(keyWord);
            Matcher m = p.matcher(str);
            SpannableString spannableString = new SpannableString(str);
            while (m.find()) {
                if (str.contains(m.group())) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(0xffec8b44), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.textBlueSn.setText("SN号:"+spannableString);
                holder.setVisibility(true);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relative;
        private TextView textBlueSn;
        private TextView textBlueMac;
        private TextView textRssi;


        ViewHolder(View view) {
            super(view);
            relative = (RelativeLayout) view.findViewById(R.id.relative);
            textBlueSn = (TextView) view.findViewById(R.id.text_blue_sn);
            textBlueMac = (TextView) view.findViewById(R.id.text_blue_mac);
            textRssi = (TextView) view.findViewById(R.id.text_rssi);

            relative.setOnClickListener(v -> {
                try {
                    onSensoroDeviceListener.onSensoroDevice(getContentByViewHolder(this));
                    XLog.i("数据--==="+getContentByViewHolder(this).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

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
    }
}
