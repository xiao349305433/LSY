package wu.loushanyun.com.modulerepair.p.adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.modulerepair.R;

public class SensoroDeviceViewBinder extends ItemViewBinder<SensoroDevice, SensoroDeviceViewBinder.ViewHolder> {

    private OnSensoroDeviceClickListener onSensoroDeviceClickListener;
    private String buttonText="详情";


    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setOnSensoroDeviceClickListener(OnSensoroDeviceClickListener onSensoroDeviceClickListener) {
        this.onSensoroDeviceClickListener = onSensoroDeviceClickListener;
    }

    public interface OnSensoroDeviceClickListener{
        void onSensoroDeviceClick(SensoroDevice sensoroDevice);
    }

    @NonNull
    @Override
    protected SensoroDeviceViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_repair_neighbor_blue_item, parent, false);
        SensoroDeviceViewBinder.ViewHolder holder = new SensoroDeviceViewBinder.ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull SensoroDeviceViewBinder.ViewHolder holder, @NonNull SensoroDevice sensoroDevice) {
        holder.tvName.setText("物联SN:"+sensoroDevice.getSerialNumber());
        holder.tvDetail.setText(buttonText);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RoundTextView tvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(onSensoroDeviceClickListener!=null){
                            onSensoroDeviceClickListener.onSensoroDeviceClick(getContentByViewHolder(ViewHolder.this));
                        }else {
                            SensoroDevice sensoroDevice = (SensoroDevice) getAdapter().getItems().get(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("sensoroDevice", sensoroDevice);
                            ARouter.getInstance().build(K.NeighborDetailsActivity).with(bundle).navigation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }
}

