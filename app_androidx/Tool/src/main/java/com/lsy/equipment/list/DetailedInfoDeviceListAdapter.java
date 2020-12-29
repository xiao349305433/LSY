package com.lsy.equipment.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsy.login.R;

public class DetailedInfoDeviceListAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private List<ItemBean> list = null;


	public DetailedInfoDeviceListAdapter(List<ItemBean> list, Context context) {
		this.list = list;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
	} 
	


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
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
			convertView = inflater.inflate(R.layout.item_detailed_information, null);
			holder.tv_device_sn = (TextView) convertView.findViewById(R.id.tv_device_sn);
			holder.tv_FrequencyPoint_info = (TextView) convertView.findViewById(R.id.tv_FrequencyPoint_info);
			holder.tv_loraTxPower_info = (TextView) convertView.findViewById(R.id.tv_loraTxPower_info);
			holder.tv_loraSpectrum_info = (TextView) convertView.findViewById(R.id.tv_loraSpectrum_info);
			holder.tv_loraSendFrequence_info = (TextView) convertView.findViewById(R.id.tv_loraSendFrequence_info);
			holder.tv_loraSignalRatio_info = (TextView) convertView.findViewById(R.id.tv_loraSignalRatio_info);
			holder.tv_loraSignalStrength_info = (TextView) convertView.findViewById(R.id.tv_loraSignalStrength_info);
			holder.tv_loraSpreadSpectrum_info = (TextView) convertView.findViewById(R.id.tv_loraSpreadSpectrum_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag(); 
		}
		
		//组装数据
		 ItemBean itemBean = list.get(position);
		 holder.tv_device_sn.setText(itemBean.Device_sn);
		 holder.tv_FrequencyPoint_info.setText(itemBean.FrequencyPoint);
		 holder.tv_loraTxPower_info.setText(itemBean.Power);
		 holder.tv_loraSpectrum_info.setText(itemBean.Spectrum);
		 holder.tv_loraSendFrequence_info.setText(itemBean.SendFrequence);
		 holder.tv_loraSignalRatio_info.setText(itemBean.SignalRatio);
		 holder.tv_loraSignalStrength_info.setText(itemBean.SignalStrength);
		 holder.tv_loraSpreadSpectrum_info.setText(itemBean.SpreadSpectrum);
		
		
		return convertView;
	}
	
	static class ViewHolder {
        TextView tv_device_sn;
        TextView tv_FrequencyPoint_info;
        TextView tv_loraTxPower_info;
        TextView tv_loraSpectrum_info;
        TextView tv_loraSendFrequence_info;
        TextView tv_loraSignalRatio_info;
        TextView tv_loraSignalStrength_info;
        TextView tv_loraSpreadSpectrum_info;
        
	}


}
