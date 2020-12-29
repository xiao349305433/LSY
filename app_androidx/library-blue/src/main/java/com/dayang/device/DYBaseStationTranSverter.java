package com.dayang.device;

import com.xgg.blesdk.Utils;

import org.json.JSONObject;

public class DYBaseStationTranSverter extends DYMeter{
	
	public DYBaseStationTranSverter() {
		m_id = 3;
	}

	@Override
	public byte[] getQueryBytes(String sn) {
		String tmp = "FEFEFE68190002010100000000000000010000000000000020160117204112AD16";
		int length = tmp.length() / 2;  
	    char[] hexChars = tmp.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (Utils.charToByte(hexChars[pos]) << 4 | Utils.charToByte(hexChars[pos + 1]));  
	    }  
	    
	    try
	    {
	    	int isn = Integer.parseInt(sn);
	    	for (int i =0; i < 8; i++)
	    	{
	    		d[i + 8] = (byte) (isn & 0xff);
	    		d[i + 16] = (byte) (isn & 0xff);
	    		isn = isn >> 8;
	    	}
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    
	    length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    return d;  
	}

	@Override
	public JSONObject getQueryObject(byte[] output) {
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//
			{
				
			}
			else if (output[7] == 1)	
			{
				json.put("rtcode", "1");
				json.put("desc", "操作失败");
				return json;
			}
			else if (output[7] == 2)	
			{
				json.put("rtcode", "2");
				json.put("desc", "无效命令字");
				return json;
			}
			else if (output[7] == 3)	
			{
				json.put("rtcode", "3");
				json.put("desc", "操作超时");
				return json;
			}
			else if (output[7] == 4)	
			{
				json.put("rtcode", "4");
				json.put("desc", "无效参数");
				return json;
			}
		}
		catch (Exception e)
		{
			return json;
		}

    	return null;
	}

}
