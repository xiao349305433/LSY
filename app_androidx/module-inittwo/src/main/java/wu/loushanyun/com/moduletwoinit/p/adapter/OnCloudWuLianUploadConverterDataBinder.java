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

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.OnCloudConverterDeviceData;

public class OnCloudWuLianUploadConverterDataBinder extends ItemViewBinder<OnCloudConverterDeviceData, OnCloudWuLianUploadConverterDataBinder.ViewHolder> {

    private String areaNumber;
    private String areaName;

    public OnCloudWuLianUploadConverterDataBinder(String areaNumber, String areaName) {
        this.areaNumber = areaNumber;
        this.areaName = areaName;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_wulian_converter_list_cloud, parent, false);
        OnCloudWuLianUploadConverterDataBinder.ViewHolder holder = new OnCloudWuLianUploadConverterDataBinder.ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull OnCloudConverterDeviceData data) {
        if (!data.isCloud()) {
            holder.changjiadaima.setVisibility(View.GONE);
            holder.chuanganxinhao.setVisibility(View.GONE);
            holder.chuchangriqi.setVisibility(View.GONE);
            holder.beizhu.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.wulian.setText("物联SN：" + data.getDevice().getSerialNumber());
            if (data.getDevice() != null) {
                holder.relativeAll.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.changjiadaima.setVisibility(View.VISIBLE);
            holder.chuanganxinhao.setVisibility(View.VISIBLE);
            holder.chuchangriqi.setVisibility(View.VISIBLE);
            holder.beizhu.setVisibility(View.VISIBLE);
            holder.wulian.setText("物联SN：" + data.getEquipmentSN());
            holder.changjiadaima.setText("厂家名称：" + data.getManufacturersName());
            holder.chuanganxinhao.setText("传感信号：" + data.getSensingSignal());
            holder.chuchangriqi.setText("出厂日期：" + TimeUtils.milliseconds2String(data.getEquipmentTime()));
            holder.beizhu.setText("备注：" + data.getRemark());
            if (data.getDevice() != null) {
                holder.relativeAll.setBackgroundColor(Color.WHITE);
            } else {
                holder.relativeAll.setBackgroundColor(getActivity().getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeAll;
        private TextView wulian;
        private TextView changjiadaima;
        private TextView chuanganxinhao;
        private TextView chuchangriqi;
        private TextView beizhu;
        private TextView delete;


        public ViewHolder(View view) {
            super(view);
            relativeAll = (RelativeLayout) view.findViewById(R.id.relative_all);
            wulian = (TextView) view.findViewById(R.id.wulian);
            changjiadaima = (TextView) view.findViewById(R.id.changjiadaima);
            chuanganxinhao = (TextView) view.findViewById(R.id.chuanganxinhao);
            chuchangriqi = (TextView) view.findViewById(R.id.chuchangriqi);
            beizhu = (TextView) view.findViewById(R.id.beizhu);
            delete = (TextView) view.findViewById(R.id.delete);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        OnCloudConverterDeviceData data = getContentByViewHolder(ViewHolder.this);
                        if (data.isCloud()) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("sensoroDevice", data.getDevice());
                            bundle.putString("sn", data.getEquipmentSN());
                            bundle.putString("areaName", areaName);
                            bundle.putString("areaNumber", areaNumber);
                            ARouter.getInstance().build(K.OnCloudWulianwangduanInitActivity).with(bundle).navigation();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("sensoroDevice", data.getDevice());
                            bundle.putString("areaNumber", areaNumber);
                            ARouter.getInstance().build(K.OnCloudTwoWulianwangduanInitActivity).with(bundle).navigation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
