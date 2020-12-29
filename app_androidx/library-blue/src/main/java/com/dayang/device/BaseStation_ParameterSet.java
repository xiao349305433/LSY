package com.dayang.device;

import com.xgg.blesdk.Meter;

import org.json.JSONObject;

/**
 * 基站蓝牙参数设置
 * @author zhang.q.s
 *
 */
public abstract class BaseStation_ParameterSet extends Meter {
	
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
	 * 参数设置验证，读取能否成功
	 * @param output
	 * @return
	 */
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

}
