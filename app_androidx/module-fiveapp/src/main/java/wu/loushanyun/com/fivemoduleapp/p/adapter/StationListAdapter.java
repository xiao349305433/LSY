package wu.loushanyun.com.fivemoduleapp.p.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.BaseStation;
import wu.loushanyun.com.jizhanModule.BaseLocationMapActivity;

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.ViewHolder> {
    private List<BaseStation> list;
    private Context context;

    public StationListAdapter(List<BaseStation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public StationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_five_station_item, parent, false);
        StationListAdapter.ViewHolder viewHolder = new StationListAdapter.ViewHolder(view);
        viewHolder.setContext(parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StationListAdapter.ViewHolder holder, int position) {
        if (position % 2 == 1) {
            holder.contentStation.setBackgroundColor(0x00000000);
        } else {
            holder.contentStation.setBackgroundColor(0xffffffff);
        }
        if (list.get(position).getImageUrl().equals("0")) {
            holder.ivStationPic.setImageDrawable(context.getResources().getDrawable(R.drawable.camera));
        } else {
            holder.ivStationPic.setImageDrawable(context.getResources().getDrawable(R.drawable.photo));
        }
        //TODO
//        holder.ivStationPic.setImageResource();
        holder.tvStationName.setText(list.get(position).getBaseName());
        holder.tvStationStatus.setText("状态:" + list.get(position).getStatus());
        holder.tvStationSn.setText(list.get(position).getSn());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout contentStation;
        public ImageView ivStationPic;
        public TextView tvStationName;
        public TextView tvStationStatus;
        public TextView tvStationSn;
        public Context context;

        public ViewHolder(View convertView) {
            super(convertView);
            contentStation = (LinearLayout) convertView.findViewById(R.id.content_station);
            ivStationPic = (ImageView) convertView.findViewById(R.id.iv_station_pic);
            tvStationName = (TextView) convertView.findViewById(R.id.tv_station_name);
            tvStationStatus = (TextView) convertView.findViewById(R.id.tv_station_status);
            tvStationSn = (TextView) convertView.findViewById(R.id.tv_station_sn);
            contentStation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseStation baseStation = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, BaseLocationMapActivity.class);
                    intent.putExtra("sn", baseStation.getSn());
                    intent.putExtra("baseName", baseStation.getBaseName());
                    intent.putExtra("tags", baseStation.getTags());
                    intent.putExtra("status", baseStation.getStatus());
                    intent.putExtra("remark", baseStation.getRemark());
                    intent.putExtra("baseId", Integer.parseInt(baseStation.getId()));
                    intent.putExtra("longitude", Double.parseDouble(baseStation.getLongitude()));
                    intent.putExtra("latitude", Double.parseDouble(baseStation.getLatitude()));
                    intent.putExtra("imageUrl", baseStation.getImageUrl());
                    intent.putExtra("channel", baseStation.getChannel());
                    context.startActivity(intent);
                }
            });
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}
