package com.dayang.device;


import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;

import com.xgg.blesdk.Meter;

public abstract class DYMeter extends Meter{
	//设置倍率
	public byte[] SetRate(String sn,int type,int rate)
	{
		//FE FE FE 68 0C 00 09 01 15 27 00 00 00 00 00 00 02 06 5A 16 //设置倍率
		byte[] d = new byte[20];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0c;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x09;			//从这里开始，12个字节0c
	    d[7] = (byte) type;			//类型
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 0x02;
	    d[17] = (byte) rate;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[19] = (byte) 0x16;
	    return d;  
	}
	//设置倍率返回
	public JSONObject SetRateReturn(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//查询成功
			{
				json.put("状态", "设置成功");
			}
			else
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
		}
		catch (Exception e)
		{
			
		}
		return json;
	}
	
	//设置底数
	public byte[] SetValue(String sn,int type,int value)
	{
		byte[] d = new byte[23];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0f;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x09;			//从这里开始，15个字节0f 
	    d[7] = (byte) type;			//类型
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 0x04;
	    d[17] = (byte) (value & 0xff);
	    d[18] = (byte) ((value & 0xff00) / 0x0100);
	    d[19] = (byte) ((value & 0xff0000) / 0x010000);
	    d[20] = (byte) ((value & 0xff000000) / 0x01000000);
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    { 
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[22] = (byte) 0x16;
	    return d;
	}
	//设置倍率返回
	public JSONObject SetValueReturn(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//查询成功
			{
				json.put("状态", "设置成功");
			}
			else
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
		}
		catch (Exception e)
		{
			
		}
		return json;
	}

	//设置时间
	public byte[] SetDatetime(String sn,int type,int year,int month,int day,int hour,int minute,int second)
	{
		byte[] d = new byte[25];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x11;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x09;			//从这里开始，15个字节0f
	    d[7] = (byte) type;			//类型
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 0x05;
	    d[17] = (byte) (year);
	    d[18] = (byte) (month);
	    d[19] = (byte) (day);
	    d[20] = (byte) (hour);
	    d[21] = (byte) (minute);
	    d[22] = (byte) (second);
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[24] = (byte) 0x16;
	    return d;  
	}
	
	
	//设置时间
	public byte[] SetSensorDatetime(int year,int month,int day,int week,int hour,int minute,int second)
	{
		byte[] d = new byte[16];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x08;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x03;
	    d[7] = (byte) (year);
	    d[8] = (byte) (month);
	    d[9] = (byte) (day);
	    d[10] = (byte) (week);
	    d[11] = (byte) (hour);
	    d[12] = (byte) (minute);
	    d[13] = (byte) (second);
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[15] = (byte) 0x16;
	    return d;  
	}
	
	//设置时间返回
	public JSONObject SetDatetimeReturn(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//查询成功
			{
				json.put("状态", "设置成功");
			}
			else
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
		}
		catch (Exception e)
		{
			
		}
		return json;
	}

	//设置冻结日
	public byte[] SetFreezedate(String sn,int type,int date)
	{
		byte[] d = new byte[20];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0c;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x09;			//从这里开始，15个字节0f
	    d[7] = (byte) type;			//类型
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 0x03;			//设置冻结日
	    d[17] = (byte) (date);
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[length + 7] = (byte) 0x16;
	    return d;  
	}
	//设置冻结日返回
	public JSONObject SetFreezedateReturn(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//查询成功
			{
				json.put("状态", "设置成功");
			}
			else
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
		}
		catch (Exception e)
		{
			
		}
		return json;
	}

	//读取历史数据
	public byte[] GetHistoryData(String sn,int type,int monthorday,int count)
	{
		byte[] d = new byte[21];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0d;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x05;			//从这里开始，15个字节0f
	    d[7] = (byte) type;			//类型
	    
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 1;			//位置默认为1
	    d[17] = (byte)count;
	    d[18] = (byte)monthorday ;		//0是日历史，1是月历史
	    
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[length + 7] = (byte) 0x16;
	    return d;
	}
	//获取历史数据的json
	public JSONObject GetHistoryDataReturn(int style,int start,int outcount,byte output[]){
		JSONObject json = new JSONObject();
		try
		{
			if (output == null){
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] != 0){
				json.put("状态", "读取失败");
				return json;
			}
			int count = (int)(output[17] & 0xff);
			json.put("开始位置", start);
			json.put("需要条目数", outcount);
			json.put("实际条目数", count);
			JSONArray array = new JSONArray();
			for (int i = 0; i < count; i++){
				int data = (int)(output[17 + i*2 + 2] & 0xff) * 256 + (int)(output[17 + i*2 + 1] & 0xff) ;
				array.put(data);
			}
			json.put("数据", array);
		}
		catch (Exception e){
			
		}
		return json;
	}

	//清除历史数据
	public byte[] ClearHistoryData(String sn,int type)
	{
		byte[] d = new byte[19];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0b;
	    d[5] = (byte) 0x00;
	    
	    d[6] = (byte) 0x09;			//从这里开始，12个字节0c
	    d[7] = (byte) type;			//类型
	    try							//8-15为sn号
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    d[16] = 0x06;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[length + 7] = (byte) 0x16;
	    return d;  
	}
	//设置倍率返回
	public JSONObject ClearHistortDataReturn(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//查询成功
			{
				json.put("状态", "设置成功");
			}
			else
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
		}
		catch (Exception e)
		{
			
		}
			return json;
		}
	
	//设置开关阀
	public byte[] switchValve(String bh,int type,int switchStatu){
			//SN 为1的表的开关阀  FE FE FE 68 13 00 03 01 01 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 01 1A 16
			byte[] d = new byte[12];
			d[0] = (byte) 0xfe;
		    d[1] = (byte) 0xfe;
		    d[2] = (byte) 0xfe;
		    d[3] = (byte) 0x68;
		    d[4] = (byte) 0x04;
		    d[5] = (byte) 0x00; 
		    
		    d[6] = (byte) 0x03;
		    d[7] = (byte) (type + 1);
		    if(type+1 == 1){
		    	
		    }else if (type+1 == 3) {//总线远传
		    	try							//8-15为sn号
			    {
		    		d[8] = (byte) Integer.parseInt(bh);
				    if (switchStatu == 0) {
				    	d[9] = (byte) 0x00;
					}else {
						d[9] = (byte) 0x01;
					}
			    }
			    catch (Exception e)
			    {
			    }
			}
		    int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[11] = (byte) 0x16;
			return d;
		}
		
	//设置开关阀返回
	public JSONObject SetSwitchReturn(byte output[])
		{
			JSONObject json = new JSONObject();
			try
			{
				if (output == null)
				{
					json.put("状态", "未能读取到数据");
					return json;
				}
				if (output[7] == 0)		//查询成功
				{
					json.put("状态", "操作成功");
				}
				else
				{
					json.put("状态", "未能正常关闭");
					return json;
				}
			}
			catch (Exception e)
			{
				
			}
			return json;
		}
		
		
		
		
	/**
	 * 设置工业压力传感器相关参数
	 * @return
	 */
	public byte[] setIndustrialPressureSensor(String sn,int caliber,int lowPressure,int highPressure){
			byte[] d  = new byte[21];
			d[0] = (byte) 0xfe;
			d[1] = (byte) 0xfe;
			d[2] = (byte) 0xfe;
			d[3] = (byte) 0x68;
			d[4] = (byte) 0x0d;//有效长度
			d[5] = (byte) 0x00;
			d[6] = (byte) 0x00;//设置蓝牙参数命令
			d[7] = (byte) 0x00;//工业压力传感器
			try							//8-12为ID号
		    {
				long isn = Long.parseLong(sn);
		    	for (int i =0; i < 5; i++)
		    	{
		    		d[i + 8] = (byte) (isn & 0xff);
		    		isn = isn >> 8; 
		    	}
		    }
		    catch (Exception e)
		    {
		    }
			d[13] = (byte) caliber;
			
//	    	for (int i =0; i < 2; i++)
//	    	{
//	    		d[i + 14] = (byte) (lowPressure & 0xff);
//	    		lowPressure = lowPressure >> 8;
//	    	}
//	    	for (int i =0; i < 2; i++)
//	    	{
//	    		d[i + 16] = (byte) (highPressure & 0xff);
//	    		highPressure = highPressure >> 8;
//	    	}
	    	
			d[14] = (byte) (lowPressure & 0xff);
		    d[15] = (byte) ((lowPressure & 0xff00) / 0x0100);
		    d[16] = (byte) (highPressure & 0xff);
		    d[17] = (byte) ((highPressure & 0xff00) / 0x0100);
		    
		    d[18] = (byte) 0x00;
			int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[20] = (byte) 0x16;
			return d;
		}
	
	/**
	 * 工业三磁敏大表参数设置
	 * @param sn
	 * @param caliber
	 * @param value
	 * @param rate
	 * @return
	 */
	public byte[] setMagnetoMeter(String sn,int caliber,int value,int rate){
			byte[] d = new byte[22];
			d[0] = (byte) 0xfe;
			d[1] = (byte) 0xfe;
			d[2] = (byte) 0xfe;
			d[3] = (byte) 0x68;
			d[4] = (byte) 0x0e;
			d[5] = (byte) 0x00;
			d[6] = (byte) 0x00;
			d[7] = (byte) 0x01;
			try							//8-12为ID号
		    {
				long isn = Long.parseLong(sn);
		    	for (int i =0; i < 5; i++)
		    	{
		    		d[i + 8] = (byte) (isn & 0xff);
		    		isn = isn >> 8; 
		    	}
		    }
		    catch (Exception e)
		    {
		    }
			d[13] = (byte) caliber;
			d[14] = (byte) (value & 0xff);
		    d[15] = (byte) ((value & 0xff00) / 0x0100);
		    d[16] = (byte) ((value & 0xff0000) / 0x010000);
		    d[17] = (byte) ((value & 0xff000000) / 0x01000000);
			d[18] = (byte) rate;
			d[19] = (byte) 0x00;//初始化默认正常
			int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[21] = (byte) 0x16;
			return d;
		}
	
	
	/**
	 * 流量计水表初始化（包含汇中水表和正反脉冲）
	 * @param sn
	 * @param caliber
	 * @param frequency
	 * @param skinMeterNumber
	 * @param rate
	 * @param state
	 * @return
	 */
	public byte[] setsinkMeterInit(int type,String sn,int caliber,int frequency,String skinMeterNumber,int rate,int state){
			if (type == 0 ) {
				byte[] d = new byte[20];
				d[0] = (byte) 0xfe;
				d[1] = (byte) 0xfe;
				d[2] = (byte) 0xfe;
				d[3] = (byte) 0x68;
				d[4] = (byte) 0x0c;
				d[5] = (byte) 0x00;//这两个字节为有效长度
				d[6] = (byte) 0x00;
				try							//7-11为ID号
			    {
					long isn = Long.parseLong(sn);
			    	for (int i =0; i < 5; i++)
			    	{
			    		d[i + 7] = (byte) (isn & 0xff);
			    		isn = isn >> 8; 
			    	}
			    }
			    catch (Exception e)
			    {
			    }
				d[12] = (byte) caliber;
				d[13] = (byte) frequency;
				d[14] = (byte) Integer.parseInt(skinMeterNumber,16);
				d[15] = (byte) rate;
				d[16] = (byte) state;//初始化默认正常
				d[17] = (byte) 0XA5;//汇中水表结束
				int length = d[5] * 256 + d[4];
			    byte cs = 0;
			    for (int i = 4; i < length + 6; i++)
			    {
			    	cs += d[i];
			    }
			    d[length + 6] = cs;
			    d[19] = (byte) 0x16;
			    return d;
			}else {
				byte[] d1 = new byte[23];
				d1[0] = (byte) 0xfe;
				d1[1] = (byte) 0xfe;
				d1[2] = (byte) 0xfe;
				d1[3] = (byte) 0x68;
				d1[4] = (byte) 0x0f;
				d1[5] = (byte) 0x00;//这两个字节为有效长度
				d1[6] = (byte) 0x00;
				try							//7-11为ID号
			    {
					long isn = Long.parseLong(sn);
			    	for (int i =0; i < 5; i++)
			    	{
			    		d1[i + 7] = (byte) (isn & 0xff);
			    		isn = isn >> 8; 
			    	}
			    }
			    catch (Exception e)
			    {
			    }
				d1[12] = (byte) caliber;
				d1[13] = (byte) frequency;
				d1[14] = (byte) (Integer.parseInt(skinMeterNumber,16) & 0xff);
				d1[15] = (byte) ((Integer.parseInt(skinMeterNumber,16) & 0xff00) / 0x0100);
				d1[16] = (byte) ((Integer.parseInt(skinMeterNumber,16) & 0xff0000) / 0x010000);
				d1[17] = (byte) ((Integer.parseInt(skinMeterNumber,16) & 0xff000000) / 0x01000000);
				d1[18] = (byte) rate;
				d1[19] = (byte) state;//初始化默认正常
				d1[20] = (byte) 0XB5;//正反脉冲结束
				int length = d1[5] * 256 + d1[4];
			    byte cs = 0;
			    for (int i = 4; i < length + 6; i++)
			    {
			    	cs += d1[i];
			    }
			    d1[length + 6] = cs;
			    d1[22] = (byte) 0x16;
			    return d1;
			}
		}
	
	/**
	 * 流量计参数设置（包含正反脉冲和汇中水表）
	 * @param type
	 * @param batteryType
	 * @param accessType
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public byte[] setFlowMeter(int type,int batteryType,int accessType,String year,String month,String day){
		byte[] d = new byte[17];
		d[0] = (byte) 0xfe;
		d[1] = (byte) 0xfe;
		d[2] = (byte) 0xfe;
		d[3] = (byte) 0x68;
		d[4] = (byte) 0x09;
		d[5] = (byte) 0x00;//
		d[6] = (byte) 0x01;
		d[7] = (byte) 0x02;//流量计
		if (type == 0) {
			d[8] = (byte) 0x06;//0x06:RS485接口
		}else {
			d[8] = (byte) 0x02;//0x02:正反脉冲
		}
		d[9] = (byte) batteryType;
		d[10] = (byte) accessType;
		d[11] = (byte) Integer.parseInt(year,16);
		d[12] = (byte) Integer.parseInt(month,16);
		d[13] = (byte) Integer.parseInt(day,16);
		if (type == 0) {
			d[14] = (byte) 0XA6;//汇中水表特有结束标志
		}else {
			d[14] = (byte) 0XB6;//正反脉冲特有结束标志
		}
		int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[16] = (byte) 0x16;
		return d;
	}
		
		/**
		 * 第三方01系统接入参数设置
		 * @param startBh
		 * @param endBh
		 * @return
		 */
		public byte[] set01System(int startBh,int endBh){
			byte[] d = new byte[13];
			d[0] = (byte) 0xfe;
			d[1] = (byte) 0xfe;
			d[2] = (byte) 0xfe;
			d[3] = (byte) 0x68;
			d[4] = (byte) 0x05;
			d[5] = (byte) 0x00;
			d[6] = (byte) 0x00;
			d[7] = (byte) 0x04;
			d[8] = (byte) startBh;
			d[9] = (byte) endBh;
			d[10] = (byte) 0x00;
			int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[12] = (byte) 0x16;
			return d;
		}
		
	
	
	
}
