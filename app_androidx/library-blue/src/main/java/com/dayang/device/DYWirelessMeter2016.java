package com.dayang.device;

import com.xgg.blesdk.Utils;

import org.json.JSONObject;

public class DYWirelessMeter2016 extends DYMeter {
	
	public DYWirelessMeter2016()
	{
		m_id = 2;
	}

	//查询单表
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
	
	//查询单表
	@Override
	public JSONObject getQueryObject(byte output[]) {
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
				json.put("rtcode", "0");
				
//				long tmp = (int)output[9];
//				json.put("设备网号", tmp);
//				
//				tmp = (int)output[10];
//				json.put("路由器网号", tmp);
//				
//				tmp = (int)output[11];
//				json.put("路由器ID", tmp);
				
			    long tmp = 0;
				for (int i = 4; i > 0; i--)
				{
					tmp = (tmp * 256) + (int)(output[i+8] & 0xff);
				}
				json.put("sn", tmp);
				
				tmp = (int)output[13];
				json.put("设备网号", tmp);
				
				tmp = (int)output[14];
				json.put("路由器网号", tmp);
				
				tmp = (int)output[15];
				json.put("路由器ID", tmp);
				
				
				tmp = 0;
				long ltmp = 0;
				for (int i = 6; i > 0; i--)
				{
					ltmp = (ltmp * 256) + (long)(output[i+16] & 0xff);
				}
				json.put("正脉冲数", ltmp);
				
				ltmp = 0;
				for (int i = 4; i > 0; i--)
				{
					ltmp = (ltmp * 256) + (long)(output[i+22] & 0xff);
				}
				json.put("反脉冲数", ltmp);
				
				tmp = (int)(output[27]) & 0xff;
				if(tmp ==0 )
				    json.put("倍率", 0.001);
				else{
					if(tmp ==1 )
					    json.put("倍率", 0.01);
					if(tmp ==2 )
					    json.put("倍率", 0.1);
					if(tmp ==3 )
					    json.put("倍率", 1);
					if(tmp ==4 )
					    json.put("倍率", 10);
					if(tmp ==5 )
					    json.put("倍率", 100);
					if(tmp ==6 )
					    json.put("倍率", 1000);
					if(tmp ==7 )
					    json.put("倍率", 0.0001);
				}

				
				
				
				tmp = 0;
				for (int i = 2; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+31]) & 0xff);
				}
				json.put("剩余水量", tmp+"吨");
				
				tmp = (int)output[34] & 0xff;
				if (tmp == 0)
					json.put("状态", "正常");
				else
				{
					if ((tmp & 0x01) > 0)
						json.put("状态", "强磁");
					if ((tmp & 0x02) > 0)
						json.put("状态", "拆卸断线");
					if ((tmp & 0x04) > 0)
						json.put("状态", "倒流");
					if ((tmp & 0x10) > 0)
						json.put("状态", "阀坏");
					if ((tmp & 0x20) > 0)
						json.put("状态", "欠压");
				}
				
				tmp = (int)output[35] & 0xff;
				if (tmp == 1)
				{
					json.put("冻结时间", 0);
					json.put("冻结水量", 0);
				}
				else
				{
					json.put("冻结时间", tmp+"号");
				
					tmp = 0;
					for (int i = 4; i > 0; i--)
					{
						tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
						tmp = tmp + ((int)(output[i+27]) & 0xff);
					}
					json.put("冻结水量", tmp);
				}
				
				int cury = (int)output[36] & 0xff;
				int curm = (int)output[37] & 0xff;
				int curd = (int)output[38] & 0xff;
				int curh = (int)output[39] & 0xff;
				int curi = (int)output[40] & 0xff;
				int curs = (int)output[41] & 0xff;
				json.put("当前时间", String.format("%d-%d-%d %d:%d:%d", cury,curm,curd,curh,curi,curs));
				
				float volt = (float)(output[42] & 0xff);
				json.put("电池电压", (volt) / 10.0f);
				
				int stopdays = (int)(output[44] & 0xff) * 256 + (int)(output[43] & 0xff);
				json.put("停用天数",stopdays+"天");
				
				
				
				return json;
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
	
	//找表
	public byte[] FindMetersBytes()
	{
		//FE FE FE 68 01 00 08 09 16
	    byte[] d = new byte[9];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x08;
	    d[7] = (byte) 0x09;
	    d[8] = (byte) 0x16;
	    return d;
	}
	
	//找表成功
	public JSONObject FindMeter(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}
			if (output[7] == 0)		//找到表
			{
				json.put("rtcode", "0");
				json.put("desc", "找到水表");
				long tmp = 0;
				for (int i = 8; i > 0; i--)
				{
					tmp = tmp * 256;
					tmp = tmp + ((int)(output[i+7]) & 0xff);
				}
				json.put("表号", tmp);
			}
			else if (output[7] == -1)
			{
				json.put("rtcode", "-1");
				json.put("desc", "找表结束");
			}
		}
		catch (Exception e)
		{
			return json;
		}
		return json;
	}
	
	//监听断线消息 
	public byte[] MonitorDisconnBytes()
	{
		//FE FE FE 68 01 00 0a 07 16
	    byte[] d = new byte[9];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x0a;
	    d[7] = (byte) 0x0b;
	    d[8] = (byte) 0x16;
	    return d;
	}
	
	
}
