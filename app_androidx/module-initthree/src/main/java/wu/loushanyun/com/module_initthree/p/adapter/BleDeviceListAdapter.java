package wu.loushanyun.com.module_initthree.p.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import wu.loushanyun.com.module_initthree.R;

public class BleDeviceListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<BluetoothDevice> mLeDevices;
	private ArrayList<Integer> RSSIs;
	private ArrayList<String> scanRecords;

	public BleDeviceListAdapter(Context context) {
		mLeDevices = new ArrayList<BluetoothDevice>();
		RSSIs = new ArrayList<Integer>();
		scanRecords = new ArrayList<String>();
		this.mInflater = LayoutInflater.from(context);
	}

	public void addDevice(BluetoothDevice device, int RSSI, String scanRecord) {
//		if (mLeDevices.size() == 0/* && !TextUtils.isEmpty(device.getName())*/) {
//			this.mLeDevices.add(device);
//			this.RSSIs.add(RSSI);
//			this.scanRecords.add(scanRecord);
//		} else {
//			for (int i = 0; i < mLeDevices.size(); i++) {
//				if (/*mLeDevices.get(i).getAddress().equals(device.getAddress())
//						&& */mLeDevices.get(i).getName().equals(device.getName())) {
//					RSSIs.set(i, RSSI);
//					scanRecords.set(i, scanRecord);
//					break;// 跳出循环
//				}
//				if (i == (mLeDevices.size() - 1)
//						&& !TextUtils.isEmpty(device.getName())) {// 最后一条
//					this.mLeDevices.add(device);
//					this.RSSIs.add(RSSI);
//					this.scanRecords.add(scanRecord);
//				}
//			}
//		}
		
		if (!mLeDevices.contains(device)) {
			this.mLeDevices.add(device);
			this.RSSIs.add(RSSI);
			this.scanRecords.add(scanRecord);
		} else {
			for (int i = 0; i < mLeDevices.size(); i++) {                           
				BluetoothDevice d = mLeDevices.get(i);
				if (device.getAddress().equals(d.getAddress())) {
					RSSIs.set(i, RSSI);
					scanRecords.set(i, scanRecord);
					continue;// 跳出循环
				}
			}
		}
	}

	public BluetoothDevice getDevice(int position) {
		return mLeDevices.get(position);
	}

	@Override
	public int getCount() {
		return mLeDevices.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLeDevices.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewholder;
		if (view == null) {
			view = mInflater.inflate(R.layout.m_init_item_devicelist, null);
			viewholder = new ViewHolder();
			viewholder.devicename = (TextView) view
					.findViewById(R.id.tv_devicelist_name);
			view.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) view.getTag();
		}
		String name = mLeDevices.get(position).getName();
		String adress = mLeDevices.get(position).getAddress();
		if (TextUtils.isEmpty(name)){
			viewholder.devicename.setText(adress);
		}else{
			viewholder.devicename.setText(name);
		}
		return view;
		
	}

	static class ViewHolder {
		TextView devicename;
	}

	public void clear() {
		// TODO Auto-generated method stub
		mLeDevices.clear();
		RSSIs.clear();
		scanRecords.clear();
		this.notifyDataSetChanged();
	}

}

