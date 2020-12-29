package wu.loushanyun.com.modulerepair.p.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.m.RepairData;

public class RepairDataViewBinder extends ItemViewBinder<RepairData, RepairDataViewBinder.ViewHolder> {
    private OnRepairDataListener onRepairDataListener;
    private Context context;

    public RepairDataViewBinder(OnRepairDataListener onRepairDataListener, Context context) {
        this.onRepairDataListener = onRepairDataListener;
        this.context = context;
    }


    public interface OnRepairDataListener {
        void onJianYanRepairData(RepairData repairData);

        void onTiHuanRepairData(RepairData repairData);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_main_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RepairData repairData) {
        holder.roundTextSn.setText(repairData.getSn());
        holder.roundTextMoshi.setText(repairData.getChannel().replace("模式", ""));
        holder.textGonglv.setText("功率:" + repairData.getSendingPower());
        holder.textPinduan.setText("频段:" + repairData.getFrequencyBand());
        holder.textFasongpinlv.setText("发送频率:" + repairData.getFrequency());
        try {
            if (!"null".equals(String.valueOf(repairData.getSnr()))) {
                holder.textSnr.setText("SNR:" + String.valueOf(repairData.getSnr()));
                holder.roundTextShijian.setText(repairData.getOffLineTime() + "小时");
            } else {
                holder.textSnr.setText("SNR:" + "-");
                holder.roundTextShijian.setText("从未上送过数据");
            }
        } catch (Exception e) {

        }
        try {
            if (!"null".equals(String.valueOf(repairData.getRssi()))) {
                holder.textRssi.setText("RSSI:" + String.valueOf(repairData.getRssi()));
                holder.roundTextShijian.setText(repairData.getOffLineTime() + "小时");
            } else {
                holder.textRssi.setText("RSSI:" + "-");
                holder.roundTextShijian.setTextColor(context.getResources().getColor(R.color.base_R));
                holder.roundTextShijian.setText("从未上送过数据");
            }

        } catch (Exception e) {

        }
        holder.textKuopin.setText("扩频:" + repairData.getSf());
        holder.textChanpinxingshi.setText("产品形式:" + repairData.getProductForm() + "");
        holder.textBanben.setText("版本:" + repairData.getVersion());
        holder.textChuanganxinhao.setText("传感信号:" + repairData.getSensingSignal() + "");
        holder.textDingwei.setText(repairData.getRemark());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView roundTextSn;
        private TextView roundTextMoshi;
        private TextView roundTextShijian;
        private RoundTextView roundTextJianxiu;
        private RoundTextView roundTextTihuan;
        private TextView textGonglv;
        private TextView textPinduan;
        private TextView textFasongpinlv;
        private TextView textSnr;
        private TextView textRssi;
        private TextView textKuopin;
        private TextView textChanpinxingshi;
        private TextView textBanben;
        private TextView textChuanganxinhao;
        private TextView textDingwei;

        ViewHolder(View view) {
            super(view);
            roundTextSn = (TextView) view.findViewById(R.id.round_text_sn);
            roundTextMoshi = (TextView) view.findViewById(R.id.round_text_moshi);
            roundTextShijian = (TextView) view.findViewById(R.id.round_text_shijian);
            roundTextJianxiu = (RoundTextView) view.findViewById(R.id.round_text_jianxiu);
            roundTextTihuan = (RoundTextView) view.findViewById(R.id.round_text_tihuan);
            textGonglv = (TextView) view.findViewById(R.id.text_gonglv);
            textPinduan = (TextView) view.findViewById(R.id.text_pinduan);
            textFasongpinlv = (TextView) view.findViewById(R.id.text_fasongpinlv);
            textSnr = (TextView) view.findViewById(R.id.text_snr);
            textRssi = (TextView) view.findViewById(R.id.text_rssi);
            textKuopin = (TextView) view.findViewById(R.id.text_kuopin);
            textChanpinxingshi = (TextView) view.findViewById(R.id.text_chanpinxingshi);
            textBanben = (TextView) view.findViewById(R.id.text_banben);
            textChuanganxinhao = (TextView) view.findViewById(R.id.text_chuanganxinhao);
            textDingwei = (TextView) view.findViewById(R.id.text_dingwei);

            view.setOnClickListener(v -> {
                try {
                    RepairData repairData = getContentByViewHolder(this);
                    ARouter.getInstance().build(K.LocationDetailActivity).withString("oldSn", repairData.getSn()).withString("productForm", repairData.getProductForm()).navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            roundTextJianxiu.setOnClickListener(v -> {
                try {
                    onRepairDataListener.onJianYanRepairData(getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            roundTextTihuan.setOnClickListener(v -> {
                try {
                    onRepairDataListener.onTiHuanRepairData(getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
