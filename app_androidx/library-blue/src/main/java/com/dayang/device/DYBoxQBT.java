package com.dayang.device;

import com.xgg.blesdk.Box;

import org.json.JSONObject;

public class DYBoxQBT extends Box {
	public DYBoxQBT()
	{
		m_str_writechara_gd = "0000ff11-0000-1000-8000-00805f9b34fb";
		m_str_readchara_gd = "0000ff12-0000-1000-8000-00805f9b34fb";
	}
	
	@Override
	//检查收到的数据是否合乎规则
    //返回值
    //-1：不合规则，全删除
    //0:未收完，不确定是否合乎规则
    //1:合规则，可以进入下一步了
    //－2:校验错误
    //-3：收到的命令号与当前命令号不一致
    protected int CheckInput()
    {
    	if (m_bytes == null)
    		return -1;
    	try
    	{
    		if (m_bytes[0] != -2)		//协议头不对
    			return -1;
    		if (m_bytes.length < 2)
    			return 0;
    		if (m_bytes[1] != -2)		//协议头不对
    			return -1;
    		if (m_bytes.length < 3)
    			return 0;
    		if (m_bytes[2] != -2)		//协议头不对
    			return -1;
    		if (m_bytes.length < 4)
    			return 0;
    		if (m_bytes[3] != 0x68)		//协议头不对
    			return -1;
    		if (m_bytes.length <= 5)
    			return 0;
    		/*int length = m_bytes[5] + m_bytes[4];
    		if (length + 8 < m_bytes.length)	//现在收到的字节数目不够
    			return 0;*/
    		/*if (m_bytes[6] != (byte)(m_curcmd | 0x80) )		//答非所问
    			return -3;*/
    		if (m_bytes[m_bytes.length-2] != 0x16)		//协议尾不对
    			return 0;
    		//字节足够多了，开始校验
    		byte cs = 0;
    		for (int i = 3; i < m_bytes.length-3; i++)
    		{
    			cs += m_bytes[i];
    		}
    		if (cs == m_bytes[ m_bytes.length-3])		//校验正确
    		{
    			return 1;
    		}
    		else
    			return -2;
    	}
    	catch (Exception e)
    	{
    		return -1;
    	}
    }
	
	@Override
	public int clearBytes()
	{
		if (m_bytes == null)
    		return 0;
		try
    	{
			int rt = CheckInput();
			if (rt < 0)		//校验不正确
			{
				m_bytes = null;
				return 0;
			}
			else if (rt == 0)
			{
				return 0;
			}
			else 		//校验正确
			{
				//移除掉第一帧
				//看看如果还有字节，返回1，否则返回0
				int length = m_bytes[5]* 256 + m_bytes[4];
				if (m_bytes.length <= length + 8)
				{
					m_bytes = null;
					return 0;
				}
				else
				{
					int newlength = m_bytes.length - length - 8;
					byte newbyte[] = new byte[newlength];
					for (int i = 0; i < newlength;i++)
						newbyte[i] = m_bytes[i+length+8];
					m_bytes = newbyte;
					return 1;
				}
			}
    	}
		catch (Exception e)
		{
			m_bytes = null;
			return 0;
		}
	}
	

	/**
	 * 光电直读广播读表
	 * @return
	 */
	public byte[] getRadioMeterInfoBytes(){
		byte[] d = new byte[19];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x10;
	    d[5] = (byte) 0xAA;
	    d[6] = (byte) 0xAA;
	    d[7] = (byte) 0xAA;
	    d[8] = (byte) 0xAA;
	    d[9] = (byte) 0xAA;
	    d[10] = (byte) 0xAA;
	    d[11] = (byte) 0xAA;
	    d[12] = (byte) 0x01;
	    d[13] = (byte) 0x03;
	    d[14] = (byte) 0x90;
	    d[15] = (byte) 0x1F;
	    d[16] = (byte) 0x00;
	    d[17] = (byte) 0xD1;
	    d[18] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 光电直读广播读表信息返回
	 * @param output
	 * @return
	 */
	public JSONObject getRadioMeterInfo(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}else {
				json.put("表计类型", "水表");
				long tmp = 0;
				String s = "";
				for (int i = 7; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+4] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("ID", s);
				tmp = 0;
				s= "";
				for (int i = 4; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+16] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("当前累计流量", s);
				tmp = 0;
				s = "";
				for (int i = 4; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+21] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("日累计流量", s);
			}
		}catch (Exception e)
		{
			
		}
		return json;
	}
	
	
	/**
	 * 光电直读sn读表
	 * @return
	 */
	public byte[] RadioMeterInfo(String ph1,String ph2,String ph3,String ph4,String ph5,String ph6)
	{
		byte[] d = new byte[16];  
	    d[0] = (byte) 0x68;
	    d[1] = (byte) 0x10;
	    d[2] = (byte) Integer.parseInt(ph1,16);
	    d[3] = (byte) Integer.parseInt(ph2,16);
	    d[4] = (byte) Integer.parseInt(ph3,16);
	    d[5] = (byte) Integer.parseInt(ph4,16);
	    d[6] = (byte) Integer.parseInt(ph5,16);
	    d[7] = (byte) Integer.parseInt(ph6,16);
	    d[8] = (byte) 0x00;
	    d[9] = (byte) 0x01;
	    d[10] = (byte) 0x03;
	    d[11] = (byte) 0x90;
	    d[12] = (byte) 0x1F;
	    d[13] = (byte) 0x00;
	    byte cs = 0;
	    for (int i = 0; i < 14; i++)
 	    {
	    	cs += d[i];
	    }
	    d[14] = cs;
//	    d[14] = (byte) 0xE7;
	    d[15] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 光电直读ID读表信息返回
	 * @param output
	 * @return
	 */
	public JSONObject getRadioMeterIDInfo(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}else {
				json.put("表计类型", "水表");
				long tmp = 0;
				String s = "";
				for (int i = 7; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+4] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("ID", s);
				tmp = 0;
				s= "";
				for (int i = 4; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+16] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("当前累计流量", s);
				tmp = 0;
				s = "";
				for (int i = 4; i > 0 ; i--)
				{
					String hexString = Integer.toHexString((int)(output[i+21] & 0xff));
					if (Integer.parseInt(hexString) < 10) {
						hexString = "0"+hexString;
					}
					s = s + hexString;
				}
				json.put("日累计流量", s);
			}
		}catch (Exception e)
		{
			
		}
		return json;
	}
	
	/**
	 * 光电直读表底数设置
	 * @return
	 */
	public byte[] setRadioMeterDataBytes(String ph1,String ph2,String ph3,String ph4,String ph5,String ph6,String ph7,String ph8,String ph9,String ph10){
		byte[] d = new byte[24];
	    d[0] = (byte) 0xFE;
	    d[1] = (byte) 0xFE;
	    d[2] = (byte) 0xFE;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x10;
	    d[5] = (byte) Integer.parseInt(ph1,16);
	    d[6] = (byte) Integer.parseInt(ph2,16);
	    d[7] = (byte) Integer.parseInt(ph3,16);
	    d[8] = (byte) Integer.parseInt(ph4,16);
	    d[9] = (byte) Integer.parseInt(ph5,16);
	    d[10] = (byte) Integer.parseInt(ph6,16);
	    d[11] = (byte) 0x00;
	    d[12] = (byte) 0x16;
	    d[13] = (byte) 0x08;
	    d[14] = (byte) 0xA0;
	    d[15] = (byte) 0x16;
	    d[16] = (byte) 0x00;
	    d[17] = (byte) 0x00;
	    d[18] = (byte) Integer.parseInt(ph7,16);
	    d[19] = (byte) Integer.parseInt(ph8,16);
	    d[20] = (byte) Integer.parseInt(ph9,16);
	    d[21] = (byte) Integer.parseInt(ph10,16);
	    byte cs = 0;
	    for (int i = 3; i < 22; i++)
 	    {
	    	cs += d[i];
	    }
	    d[22] = cs;
	    d[23] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 光电直读设置表底数信息返回
	 * @param output
	 * @return
	 */
	public JSONObject getRadioMeterDataInfo(byte output[])
	{
		JSONObject json = new JSONObject();
		try
		{
			if (output == null)
			{
				json.put("状态", "未能读取到数据");
				return json;
			}else {
				json.put("状态", "成功");
				return json;
			}
		}catch (Exception e)
		{
			
		}
		return json;
	}
}
