package com.loushanyun.modulefactory.p.adapter;

import android.app.Activity;
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
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.loushanyun.modulefactory.R;
import com.sensoro.sensor.kit.entity.SensoroDevice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;

public class SensoroDeviceViewBinder  extends ItemViewBinder<SensoroDevice, SensoroDeviceViewBinder.ViewHolder> {
    private String keyWrold; // 关键字

    public void setKeyWrold(String keyWrold) {
        this.keyWrold = keyWrold;
    }


    private Activity activity;
    private String dumpString;

    public SensoroDeviceViewBinder(Activity activity,String dumpString){
        this.activity = activity;
        this.dumpString = dumpString;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_item_device, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SensoroDevice sensoroDevice) {
        String str;
        if (sensoroDevice.getSerialNumber() == null || sensoroDevice.getSerialNumber().equals("")) {
            str = sensoroDevice.getMacAddress();
        } else {
            str = sensoroDevice.getSerialNumber();
        }
        holder.setVisibility(false);
        if (XHStringUtil.isEmpty(keyWrold,true)){
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtv.setText(str);
            holder.setVisibility(true);
        }else {
            Pattern p = Pattern.compile(keyWrold);
            Matcher m = p.matcher(str);
            SpannableString spannableString = new SpannableString(str);
            while (m.find()) {
                if (str.contains(m.group())) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(0xffec8b44), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.txtv.setText(spannableString);
                holder.setVisibility(true);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView txtv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SensoroDevice sensoroDevice = (SensoroDevice) getAdapter().getItems().get(getAdapterPosition());
                    ARouter.getInstance().build(dumpString).withParcelable("sensoroDevice", sensoroDevice).navigation();
                }
            });
            txtv = (TextView) itemView.findViewById(R.id.device_sn);
        }

        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
