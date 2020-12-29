package wu.loushanyun.com.moduletwoinit.p.adapter;

import android.os.Bundle;
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
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;

public class OnCloudWuLianUploadDataBinder extends ItemViewBinder<WuLianUploadData, OnCloudWuLianUploadDataBinder.ViewHolder> {


    @Override
    protected void initEventRunner() {
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        super.onEventRunEnd(event, code);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_wulianlist_oncloud, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WuLianUploadData wuLianUploadData) {
        holder.textQuyuhao.setText("网格号:" + wuLianUploadData.getAreaNumber());
        holder.time.setText("保存时间:" + TimeUtils.milliseconds2String(Long.valueOf(wuLianUploadData.getTime())));
        holder.textQuyumingcheng.setText("网格名称:" + wuLianUploadData.getAreaName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView relativeAll;
        private TextView textQuyuhao;
        private TextView textQuyumingcheng;
        private TextView time;
        private RoundTextView textDetails;

        public ViewHolder(View view) {
            super(view);
            relativeAll = (CardView) view.findViewById(R.id.relative_all);
            textQuyuhao = (TextView) view.findViewById(R.id.text_quyuhao);
            textQuyumingcheng = (TextView) view.findViewById(R.id.text_quyumingcheng);
            time = (TextView) view.findViewById(R.id.time);
            textDetails = (RoundTextView) view.findViewById(R.id.text_details);


            textDetails.setOnClickListener(v -> {
                try {
                    WuLianUploadData wuLianUploadData = getContentByViewHolder(this);
                    Bundle bundle = new Bundle();
                    bundle.putString("areaNumber", wuLianUploadData.getAreaNumber());
                    bundle.putString("areaName", wuLianUploadData.getAreaName());
                    ARouter.getInstance().build(K.OnCloudUpdateLocationActivity).with(bundle).navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
