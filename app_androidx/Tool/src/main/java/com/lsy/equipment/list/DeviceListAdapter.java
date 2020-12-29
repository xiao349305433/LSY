package com.lsy.equipment.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsy.login.R;
import com.sensoro.sensor.kit.entity.SensoroDevice;

public class DeviceListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<SensoroDevice> mList = new ArrayList<SensoroDevice>();
	private LayoutInflater mInflater;
	private OnTextItemClickListener mOnTextItemClickListener;
	public DeviceListAdapter(Context mContext, OnTextItemClickListener mOnTextItemClickListener) {
		this.mContext = mContext;
		this.mOnTextItemClickListener = mOnTextItemClickListener;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(ArrayList<SensoroDevice> data) {
		mList = data; 
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_device, null);
			holder.snTextView = (TextView) convertView.findViewById(R.id.device_sn);
			holder.tv_environment_check = (TextView) convertView.findViewById(R.id.tv_environment_check);
			holder.tv_moshi = (TextView) convertView.findViewById(R.id.tv_moshi);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag(); 
		}
		if (holder.snTextView == null /*|| holder.rssiTextView == null*/) {
			return null;
		}
        SensoroDevice sensoroDevice = mList.get(position);
        if (sensoroDevice.getSerialNumber() == null || sensoroDevice.getSerialNumber().equals("")) {
            holder.snTextView.setText(mList.get(position).getMacAddress());
        } else {
            holder.snTextView.setText(mList.get(position).getSerialNumber());
        }
        holder.snTextView.setText(mList.get(position).getSerialNumber());
        holder.tv_environment_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTextItemClickListener.onTextCheckClick(position);
            }
        });
        holder.tv_moshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTextItemClickListener.onTextMoShiClick(sensoroDevice,position);
            }
        });
		return convertView;
	}
	
	/**
     * 删除按钮的监听接口
     */
    public interface OnTextItemClickListener {
        void onTextCheckClick(int i);
        void onTextMoShiClick(SensoroDevice sensoroDevice,int i);
    }

	static class ViewHolder {
        TextView snTextView;
        TextView tv_environment_check;
        TextView tv_moshi;
	}


}
