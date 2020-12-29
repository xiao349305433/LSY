package wu.loushanyun.com.moduletwoinit.p.adapter;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;

import org.greenrobot.eventbus.EventBus;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.ConverterDeviceData;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;

public class WuLianUploadConverterDataBinder extends ItemViewBinder<ConverterDeviceData, WuLianUploadConverterDataBinder.ViewHolder> {

    private String areaNumber;

    public WuLianUploadConverterDataBinder(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_wulian_converter_list, parent, false);
        WuLianUploadConverterDataBinder.ViewHolder holder = new WuLianUploadConverterDataBinder.ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ConverterDeviceData data) {
        if (data.isCanClick()) {
            holder.relativeAll.setBackgroundColor(Color.WHITE);
        }
        if (data.getConverter() == null) {
            holder.changjiadaima.setVisibility(View.GONE);
            holder.chuanganxinhao.setVisibility(View.GONE);
            holder.chuchangriqi.setVisibility(View.GONE);
            holder.anzhuangshijian.setVisibility(View.GONE);
            holder.beizhu.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.wulian.setText("物联SN：" + data.getSerialnumber());
        } else {
            holder.changjiadaima.setVisibility(View.VISIBLE);
            holder.chuanganxinhao.setVisibility(View.VISIBLE);
            holder.chuchangriqi.setVisibility(View.VISIBLE);
            holder.anzhuangshijian.setVisibility(View.GONE);
            holder.beizhu.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.wulian.setText("物联SN：" + data.getSerialnumber());
            holder.changjiadaima.setText("厂家名称：" + data.getConverter().getFactoryName());
            holder.chuanganxinhao.setText("传感信号：" + LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(data.getConverter().getSensingSignal())));
            holder.chuchangriqi.setText("出厂日期：" + data.getConverter().getEquipmentTime() + " 00:00:00");
            holder.anzhuangshijian.setText("安装时间：00:00:00");
            holder.beizhu.setText("备注：" + data.getConverter().getRemark());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeAll;
        private TextView wulian;
        private TextView changjiadaima;
        private TextView chuanganxinhao;
        private TextView chuchangriqi;
        private TextView anzhuangshijian;
        private TextView beizhu;
        private TextView delete;


        public ViewHolder(View view) {
            super(view);
            relativeAll = (RelativeLayout) view.findViewById(R.id.relative_all);
            wulian = (TextView) view.findViewById(R.id.wulian);
            changjiadaima = (TextView) view.findViewById(R.id.changjiadaima);
            chuanganxinhao = (TextView) view.findViewById(R.id.chuanganxinhao);
            chuchangriqi = (TextView) view.findViewById(R.id.chuchangriqi);
            anzhuangshijian = (TextView) view.findViewById(R.id.anzhuangshijian);
            beizhu = (TextView) view.findViewById(R.id.beizhu);
            delete = (TextView) view.findViewById(R.id.delete);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ConverterDeviceData data = getContentByViewHolder(ViewHolder.this);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("sensoroDevice", data.getDevice());
                        bundle.putString("areaNumber", areaNumber);
                        bundle.putParcelable("onetoOneConverter", data.getConverter());
                        ARouter.getInstance().build(K.LSY2InitActivity).with(bundle).navigation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ConverterDeviceData converterDeviceData = getContentByViewHolder(ViewHolder.this);
                        converterDeviceData.getConverter().deleteAsync().listen(rowsAffected -> {
                            getAdapter().getItems().remove(getAdapterPosition());
                            getAdapter().notifyDataSetChanged();
                        });
                        EventBus.getDefault().post(new RefreshTwoLocationEvent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
