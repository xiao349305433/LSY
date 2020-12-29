package wu.loushanyun.com.moduletwoinit.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.InsidePublicMeter;

public class LSY4ListBinderView extends ItemViewBinder<InsidePublicMeter, LSY4ListBinderView.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_lsy4_list, parent, false);
        LSY4ListBinderView.ViewHolder holder = new LSY4ListBinderView.ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull InsidePublicMeter item) {
        holder.textSn.setText("SN号:"+item.getSn());
        if(("1").equals(item.getSensingSignalModule())){
            holder.textAddress.setText("传感模式:"+"累计脉冲");
        }else if(("2").equals(item.getSensingSignalModule())){
            holder.textAddress.setText("传感模式:"+"状态切换");
        }else if(("3").equals(item.getSensingSignalModule())){
            holder.textAddress.setText("传感模式:"+"数字信号");
        }
        holder.textTime.setText("激活时间:"+TimeUtils.milliseconds2String(Long.valueOf(item.getJiHuoTime())));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView relativeAll;
        private TextView textSn;
        private TextView textTime;
        private TextView textAddress;
        private RoundTextView upload;
        private RoundTextView delete;

        public ViewHolder(View view) {
            super(view);
            relativeAll = (CardView) view.findViewById(R.id.relative_all);
            textSn = (TextView) view.findViewById(R.id.text_sn);
            textTime = (TextView) view.findViewById(R.id.text_time);
            textAddress = (TextView) view.findViewById(R.id.text_address);
            upload = (RoundTextView) view.findViewById(R.id.upload);
            delete = (RoundTextView) view.findViewById(R.id.delete);
            upload.setOnClickListener(this::onClick);
            delete.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.upload) {
                try {
                    InsidePublicMeter insidePublicMeter = getContentByViewHolder(ViewHolder.this);
                    ARouter.getInstance().build(K.LSY4LocationActivity).withLong("insidePublicMeterId", insidePublicMeter.getBaseObjId())
                            .withString("oldSn", "").navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (i == R.id.delete) {
                try {
                    InsidePublicMeter insidePublicMeter = getContentByViewHolder(ViewHolder.this);
                    insidePublicMeter.deleteAsync().listen(rowsAffected -> {
                        getAdapter().getItems().remove(getAdapterPosition());
                        getAdapter().notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
