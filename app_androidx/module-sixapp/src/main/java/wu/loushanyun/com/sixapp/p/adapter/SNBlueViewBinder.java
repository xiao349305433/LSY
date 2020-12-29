package wu.loushanyun.com.sixapp.p.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.m.SNMeter;

public class SNBlueViewBinder extends ItemViewBinder<SensoroDevice, SNBlueViewBinder.ViewHolder> {

    private OnSensoroDeviceListener onSensoroDeviceListener;
    private String keyWord;
    private Context context;
    public void setKeyWord(String keyWrold) {
        this.keyWord = keyWrold;
        if(getAdapter()!=null){
            getAdapter().notifyDataSetChanged();
        }

    }


    public SNBlueViewBinder(OnSensoroDeviceListener onSensoroDeviceListener) {
        this.onSensoroDeviceListener = onSensoroDeviceListener;
    }

    public interface OnSensoroDeviceListener {
        void onSensoroDevice(SensoroDevice sensoroDevice);

        void showPop(SensoroDevice sensoroDevice);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root=inflater.inflate(wu.loushanyun.com.sixapp.R.layout.m_six_ble_item,parent, false);

        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SensoroDevice sensoroDevice) {
        String str = sensoroDevice.getSerialNumber();
        holder.textBlueSn.setText("SN号:"+sensoroDevice.sn);
        holder.textBlueMac.setText("MAC地址:"+sensoroDevice.macAddress);
//        if(snMeter.isOk){
//            GlideUtil.display(context,holder.imgIsOk,R.mipmap.item_0k1,R.mipmap.item_0k1);
//        }else {
//            GlideUtil.display(context,holder.imgIsOk,R.mipmap.item_wrong1,R.mipmap.item_wrong1);
//        }


  //      holder.textRssi.setText("信号:"+sensoroDevice.rssi+"");
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
        private LinearLayout linearLayout;
        private TextView textBlueSn;
        private TextView textBlueMac;
       // private TextView textRssi;
        private ImageView imgIsOk;
        private LinearLayout wuxian;

        ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(wu.loushanyun.com.sixapp.R.id.item_linear);
            textBlueSn = (TextView) view.findViewById(wu.loushanyun.com.sixapp.R.id.item_blue_sn);
            textBlueMac = (TextView) view.findViewById(wu.loushanyun.com.sixapp.R.id.item_blue_mac);
            imgIsOk=view.findViewById(R.id.item_isok);
            wuxian= view.findViewById(R.id.item_wuxian);
         //   textRssi = (TextView) view.findViewById(R.id.text_rssi);

            linearLayout.setOnClickListener(v -> {
                try {
                    onSensoroDeviceListener.onSensoroDevice(getContentByViewHolder(this));
                    XLog.i("数据--==="+getContentByViewHolder(this).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            wuxian.setOnClickListener(v -> {
                try {
                    onSensoroDeviceListener.showPop(getContentByViewHolder(this));
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
