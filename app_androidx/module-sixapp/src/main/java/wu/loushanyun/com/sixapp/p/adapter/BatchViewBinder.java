package wu.loushanyun.com.sixapp.p.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.GetBatchInfo;
import wu.loushanyun.com.sixapp.m.GetModelInfo;

public class BatchViewBinder extends ItemViewBinder<GetModelInfo.DatasBean, BatchViewBinder.ViewHolder> {
    private OnBatchListener onBatchListener;


    public BatchViewBinder(OnBatchListener onBatchListener) {
        this.onBatchListener = onBatchListener;
    }

    public interface OnBatchListener {
        void setBatch(GetModelInfo.DatasBean item);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_six_batch_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GetModelInfo.DatasBean item) {
        holder.batchtv.setText(item.getGoodsName()+"-"+item.getGoodsModel());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView batchtv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            batchtv = (TextView) itemView.findViewById(R.id.batch_tv);

            batchtv.setOnClickListener(v -> {
                try {
                    onBatchListener.setBatch(getContentByViewHolder(this));
                    XLog.i("batch--===" + getContentByViewHolder(this).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
