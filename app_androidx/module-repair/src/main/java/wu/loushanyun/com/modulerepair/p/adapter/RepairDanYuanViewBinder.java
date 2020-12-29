package wu.loushanyun.com.modulerepair.p.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ToastUtils;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.m.RepairDanYuanData;

public class RepairDanYuanViewBinder extends ItemViewBinder<RepairDanYuanData.DataBean, RepairDanYuanViewBinder.ViewHolder> {

    private OnRepairDanYuanListener onRepairDanYuanListener;
    private Context context;

    public RepairDanYuanViewBinder(Context context, OnRepairDanYuanListener onRepairDanYuanListener) {
        this.context = context;
        this.onRepairDanYuanListener = onRepairDanYuanListener;
    }

    public interface OnRepairDanYuanListener {
        void onRepairYanBiaoClick(RepairDanYuanData.DataBean oldDanYuanData, int position);

        void onRepairGengHuanClick(RepairDanYuanData.DataBean oldDanYuanData, int position);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_item_old_dan_yuan, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RepairDanYuanData.DataBean oldDanYuanData) {
        holder.deviceId.setText(oldDanYuanData.getMeterId());
        holder.meterNumber.setText(oldDanYuanData.getMeterNumber() + "");
        holder.textShijian.setText("离线时间:" + oldDanYuanData.getOffLineHours() + "小时");
        holder.textFactoryName.setText("厂家名称:" + oldDanYuanData.getManufacturersIdentification());
        if (oldDanYuanData.isDeviceGenghuanZhuangtai()) {
            holder.deviceGenghuanZhuangtai.setImageDrawable(context.getResources().getDrawable(R.drawable.m_repair_gou));
            holder.buttonGenghuan.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.l_five_Q));
        } else {
            holder.deviceGenghuanZhuangtai.setImageDrawable(context.getResources().getDrawable(R.drawable.m_repair_abnormal));
            holder.buttonGenghuan.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
        }
        if (oldDanYuanData.isDeviceBaocunZhuangtai()) {
            holder.deviceBaocunZhuangtai.setImageDrawable(context.getResources().getDrawable(R.drawable.m_repair_gou));
        } else {
            holder.deviceBaocunZhuangtai.setImageDrawable(context.getResources().getDrawable(R.drawable.m_repair_abnormal));
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView meterNumber;
        private TextView deviceId;
        private TextView textFactoryName;
        private TextView textShijian;
        private ImageView deviceGenghuanZhuangtai;
        private ImageView deviceBaocunZhuangtai;
        private RoundTextView buttonYanbiao;
        private RoundTextView buttonGenghuan;

        ViewHolder(View view) {
            super(view);
            meterNumber = (TextView) view.findViewById(R.id.meter_number);
            deviceId = (TextView) view.findViewById(R.id.device_id);
            textFactoryName = (TextView) view.findViewById(R.id.text_factory_name);
            textShijian = (TextView) view.findViewById(R.id.text_shijian);
            deviceGenghuanZhuangtai = (ImageView) view.findViewById(R.id.device_genghuan_zhuangtai);
            deviceBaocunZhuangtai = (ImageView) view.findViewById(R.id.device_baocun_zhuangtai);
            buttonYanbiao = (RoundTextView) view.findViewById(R.id.button_yanbiao);
            buttonGenghuan = (RoundTextView) view.findViewById(R.id.button_genghuan);

            buttonYanbiao.setOnClickListener(this);
            buttonGenghuan.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            try {
                RepairDanYuanData.DataBean oldDanYuanData = getContentByViewHolder(this);
                if (id == R.id.button_yanbiao) {
                    onRepairDanYuanListener.onRepairYanBiaoClick(oldDanYuanData, getAdapterPosition());
                } else if (id == R.id.button_genghuan) {
                    if (oldDanYuanData.isDeviceGenghuanZhuangtai()) {
                        onRepairDanYuanListener.onRepairGengHuanClick(oldDanYuanData, getAdapterPosition());
                    } else {
                        ToastUtils.showShort("此单元可以正常验表，无法进行更换");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
