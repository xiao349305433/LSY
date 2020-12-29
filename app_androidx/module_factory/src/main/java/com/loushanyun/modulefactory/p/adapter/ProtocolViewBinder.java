package com.loushanyun.modulefactory.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.m.ProtocolData;

import met.hx.com.base.base.multitype.ItemViewBinder;

/**
 * 一号模组自定义协议网格视图的数据展示
 *
 * @author 喻南豪
 */
public class ProtocolViewBinder extends ItemViewBinder<ProtocolData, ProtocolViewBinder.ViewHolder> {
    private StringBuilder sb = new StringBuilder();
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_protocoldata, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ProtocolData item) {
        sb.delete(0,sb.length());
        for (int i = item.getLength(); i >= 0; i--) {
            sb.append('a');
        }
        holder.dataExample.setText(sb);
        sb.delete(0,sb.length());
        sb.append(item.getName());
        sb.append('(');
        sb.append(item.getLength());
        sb.append(')');
        holder.dataName.setText(sb);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dataExample;
        TextView dataName;

        public ViewHolder(View itemView) {
            super(itemView);
            dataExample = itemView.findViewById(R.id.data_example);
            dataName = itemView.findViewById(R.id.data_name);
        }
    }
}
