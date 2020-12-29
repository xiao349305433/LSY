package com.dayang.device;

import com.xgg.blesdk.Box;

import org.json.JSONObject;

public class DYBox extends Box {
	public DYBox()
	{
		m_str_writechara = "0000ffe1-0000-1000-8000-00805f9b34fb";
		m_str_readchara = "0000ffe1-0000-1000-8000-00805f9b34fb";
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
    		int length = m_bytes[5]* 256 + m_bytes[4];
    		if (length + 8 > m_bytes.length)	//现在收到的字节数目不够
    			return 0;
    		/*if (m_bytes[6] != (byte)(m_curcmd | 0x80) )		//答非所问
    			return -3;*/
    		if (m_bytes[m_bytes.length-1] != 0x16)		//协议尾不对
    			return -1;
    		//字节足够多了，开始校验
    		byte cs = 0;
    		for (int i = 4; i < m_bytes.length-2; i++)
    		{
    			cs += m_bytes[i];
    		}
    		if (cs == m_bytes[m_bytes.length-2])		//校验正确
    		{
    			return 1;
    		}
    		else
    			return 1;//renturn -2 强制使用了1
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
	 * 汇中水表(更改仪表通信号)
	 * @param startNumber
	 * @param newNumber
	 * @return
	 */
	public byte[] getChangeCommunicationInfoBytes(String startNumber,String newNumber){
		byte[] d = new byte[12];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x04;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x02;
	    d[7] = (byte) Integer.parseInt(startNumber,16);
	    d[8] = (byte) Integer.parseInt(newNumber,16);
	    d[9] = (byte) 0xA7;
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
	
	/**
	 * 设置汇中水表(更改仪表通信号)数据返回
	 * @param output
	 * @return
	 */
	public JSONObject getChangeCommunicationInfo(byte output[])
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
				json.put("当前通信号", Integer.toHexString(output[9] & 0xFF));
				json.put("更改后通信号", Integer.toHexString(output[11] & 0xFF));
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
	
	/**
	 * 读取流量计设置参数命令
	 * @param type
	 * @return
	 */
	public byte[] getFlowMeterSetInfoBytes(int type){
		byte[] d = new byte[10];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x03;
	    if (type == 0 ) {
	    	d[7] = (byte) 0xA8;
		}else {
			d[7] = (byte) 0xB7;
		}
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 解析流量计设置参数数据返回
	 * @param output
	 * @return
	 */
	public JSONObject getFlowMeterSetInfo(byte output[]){

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
				json.put("状态", "查询成功");
				long tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+8] & 0xff);
				}
				json.put("设备ID", tmp);
				tmp = (int)output[14] & 0xff;
				if (tmp == 0) {
					json.put("口径", "0");
				}else if (tmp == 1) {
					json.put("口径", "1");
				}else {
					json.put("口径", "2");
				}
				tmp = (int)output[15] & 0xff;
				if (tmp == 0) {
					json.put("上传频率", "0");
				}else if (tmp == 1) {
					json.put("上传频率", "1");
				}else if (tmp == 1) {
					json.put("上传频率", "2");
				}else if (tmp == 1) {
					json.put("上传频率", "3");
				}else if (tmp == 1) {
					json.put("上传频率", "4");
				}else {
					json.put("上传频率", "5");
				}
				tmp = (int)output[8] & 0xff;
				if (tmp == 0) {
					String dataSingle = Integer.toHexString(output[16] & 0xff);//需要到现场根据实际情况来更改
					json.put("仪表通信号", dataSingle);
					tmp = (int)output[17] & 0xff;
					if (tmp == 0) {
						json.put("倍率", "0");
					}else if (tmp == 1) {
						json.put("倍率", "1");
					}else if (tmp == 2) {
						json.put("倍率", "2");
					}else if (tmp == 3) {
						json.put("倍率", "3");
					}else {
						json.put("倍率", "4");
					}
				}else {
					tmp = (int)output[16] & 0xff;
					if (tmp == 0) {
						json.put("倍率", "0");
					}else if (tmp == 1) {
						json.put("倍率", "1");
					}else if (tmp == 2) {
						json.put("倍率", "2");
					}else if (tmp == 3) {
						json.put("倍率", "3");
					}else {
						json.put("倍率", "4");
					}
				}
				
				
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
	
	
	/**
	 * 读取设备信息(包含汇中水表和正反脉冲)
	 * @param type
	 * @return
	 */
	public byte[] getFlowMeterEquipmentInfoBytes(int type){
		byte[] d = new byte[10];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x04;
	    if (type == 0 ) {
	    	d[7] = (byte) 0xA9;
		}else {
			d[7] = (byte) 0xB8;
		}
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 解析读取设备信息(包含汇中水表和正反脉冲)数据返回
	 * @param output
	 * @return
	 */
	public JSONObject getFlowMeterEquipmentInfo(byte output[])
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
				long tmp = (int)output[8] & 0xff;
				if (tmp == 0) {
					json.put("产品名称", "0");
				}else {
					if (tmp == 1)
						json.put("产品名称", "1");
					else if (tmp == 2)
						json.put("产品名称", "2");
					else if (tmp == 3)
						json.put("产品名称", "3");
					else
						json.put("产品名称", "4");
				}
				tmp = (int)output[9] & 0xff;
				if (tmp == 0) {
					json.put("传感信号", "0");
				}else {
					if (tmp == 1)
						json.put("传感信号", "1");
					else if (tmp == 2)
						json.put("传感信号", "2");
					else if (tmp == 3)
						json.put("传感信号", "3");
					else if (tmp == 4)
						json.put("传感信号", "4");
					else
						json.put("传感信号", "5");
				}
				tmp = (int)output[10] & 0xff;
				if (tmp == 0) {
					json.put("设备电池类型", "0");
				}else {
					json.put("设备电池类型", "1");
				}
				tmp = (int)output[11] & 0xff;
				if (tmp == 0) {
					json.put("接入类型", "0");
				}else {
					json.put("接入类型", "1");
				}
				json.put("硬件版本信息", output[12]);
				json.put("软件版本信息", output[13]);
				String year = Integer.toHexString(output[14] & 0xff);
				String month = Integer.toHexString(output[15] & 0xff);
				String day = Integer.toHexString(output[16] & 0xff);
				json.put("设备出厂时间", "20" + year+"年"+month+"月"+day+"日");
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
	
	/**
	 * 读取流量计当前设备状态及数据
	 * @return
	 */
	public byte[] getCurrentStateEquipmentInfoBytes(int type,String dataSignal ){//还需修改
		if (type == 0) {
			byte[] d = new byte[11];
			d[0] = (byte) 0xfe;
		    d[1] = (byte) 0xfe;
		    d[2] = (byte) 0xfe;
		    d[3] = (byte) 0x68;
		    d[4] = (byte) 0x03;
		    d[5] = (byte) 0x00;
		    d[6] = (byte) 0x05;
	    	d[7] = (byte) Integer.parseInt(dataSignal,16);
	    	d[8] = (byte) 0xAA;
		    int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[10] = (byte) 0x16;
			return d;
		}else {
			byte[] d = new byte[10];
			d[0] = (byte) 0xfe;
		    d[1] = (byte) 0xfe;
		    d[2] = (byte) 0xfe;
		    d[3] = (byte) 0x68;
		    d[4] = (byte) 0x02;
		    d[5] = (byte) 0x00;
		    d[6] = (byte) 0x05;
			d[7] = (byte) 0xB9;
		    int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[9] = (byte) 0x16;
			return d;
		}
		
	}
	
	/**
	 * 解析读取流量计当前设备状态及数据  返回解析
	 * @param output
	 * @return
	 */
	public JSONObject getCurrentStateEquipmentInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = 0; 
				float volt = (float)(output[9] & 0xff);
				json.put("物联网设备电池电压", (volt) / 10.0f);
				tmp = (int)output[10] & 0xff; 
				if (tmp == 0) {
					json.put("物联网设备电池状态", "0");
				}else {
					json.put("物联网设备电池状态", "1");
				}
				tmp = (int)output[8] & 0xff; 
				if (tmp == 0) {//汇中水表状态数据解析
					tmp = 0;
//					for (int i = 4; i > 0; i--)
//					{
//						tmp = tmp * 256;
//						tmp = tmp + ((int)(output[i+10]) & 0xff);
//					}
//					json.put("汇中仪表读数", tmp);
					//注：如果读出当前读数为 00 06 56 01 四字节，转换成十进制读数为，6560.1（缩小十倍为实际读数）
					String value1 = String.valueOf(output[11] & 0xff);
					String value2 = String.valueOf(output[12] & 0xff);
					if (value2.length() == 1) {
						value2 = "0" + value2;
					}
					String value3 = String.valueOf(output[13] & 0xff);
					if (value3.length() == 1) {
						value3 = "0" + value3;
					}
					String value4 = String.valueOf(output[14] & 0xff);
					if (value4.length() == 1) {
						value4 = "0" + value4;
					}
					String value = (value1 + value2 + value3 + value4).replaceFirst("^0*", "");
					String string  = value.substring(0, value.length()-1);
                 	if (string.equals("")) {
                		string+="0";
					}
                 	String string1 = value.substring(value.length()-1, value.length());
                 	int CurrentCumulativeFlow = (Integer.parseInt(string+"."+string1))/10;
                 	json.put("汇中仪表读数", CurrentCumulativeFlow);
					
					tmp = (int)output[15] & 0xff;
					if (tmp == 1) {
						json.put("汇中仪表状态", "1");
					}else if (tmp == 2) {
						json.put("汇中仪表状态", "2");
					}else if (tmp == 3) {
						json.put("汇中仪表状态", "3");
					}else if (tmp == 4) {
						json.put("汇中仪表状态", "4");
					}else if (tmp == 5) {
						json.put("汇中仪表状态", "5");
					}else {
						json.put("汇中仪表状态", "6");
					}
					//增加参数信息
					json.put("gatherScene", "1");
					json.put("paramOrUnit", "2");
					json.put("productForm", "6");
				}else {//正反脉冲状态数据解析
					tmp = (int)output[11] & 0xff;
					if (tmp == 0) {
						json.put("数据正负标识", "0");
					}else {
						json.put("数据正负标识", "1");
					}
					tmp = 0;
					for (int i = 4; i > 0; i--)
					{
						tmp = tmp * 256;
						tmp = tmp + ((int)(output[i+11]) & 0xff);
					}
					json.put("正反脉冲读数", tmp*0.1);
					//增加参数信息
					json.put("gatherScene", "1");
					json.put("paramOrUnit", "2");
					json.put("productForm", "6");
				}
				
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
	
	
	
	/**
	 * 读取物联SN（包含汇中水表和正反脉冲）
	 * @return
	 */
	public byte[] getFlowMeterSnInfoBytes(){
		byte[] d = new byte[9];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00; 
	    d[6] = (byte) 0x06;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i]; 
	    }
	    d[length + 6] = cs;
	    d[8] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 解析读取读取物联SN（包含汇中水表和正反脉冲）数据解析
	 * @param output
	 * @return
	 */
	public JSONObject getFlowMeterSnInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = 0;
				for (int i = 16; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+7] & 0xff);
				}
				json.put("物联SN号", tmp);
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
	
	
	/**
	 * 读取基站转换板的版本信息
	 * @return
	 */
	public byte[] getBaseStationInfoBytes(){
		byte[] d = new byte[9];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x06;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[8] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 读取基站转换板下所有水表的信息
         FE FE FE 68 01 00 09 0A 16 
	 * @return
	 */
	public byte[] getBaseStationAllMeterInfoBytes(){
		byte[] d = new byte[9];
		d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x09;
	    d[7] = (byte) 0x0A;
	    d[8] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 基站转换器下所有表数据返回
	 * @param output
	 * @return
	 */
	public JSONObject getBaseStationAllMeterInfo(byte output[])
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
	
	
	
	/**
	 * 基站转换器版本信息返回
	 * @param output
	 * @return
	 */
	public JSONObject getBaseStationInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8];
				if (tmp == 0) {
					json.put("设备类型", "工业压力传感器");
				}else if (tmp == 1) 
						json.put("设备类型", "工业三磁敏水表");
				else if (tmp == 2) 
						json.put("设备类型", "工业超声波流量计");
				else if (tmp == 3) 
						json.put("设备类型", "家用水表");
				else  
						json.put("设备类型", "第三方01系统接入");
				json.put("硬件版本", output[9]);
				json.put("软件版本", output[10]);
				tmp = (int)output[11];
				if (tmp == 0) {
					json.put("设备电源类型", "电池型");
				}else {
					if(tmp == 1)
						json.put("设备电源类型", "外接电源型");
				}
				
				json.put("电池电压", ((float)output[12]) / 10);
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

	//获取盒子信息
	public byte[] getQueryInfoBytes() {
	    byte[] d = new byte[9];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x01;
//	    d[7] = (byte) 0x02;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[8] = (byte) 0x16;
	    return d;  
	}
	//获取盒子信息
	public JSONObject getInfo(byte output[])
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
				json.put("状态", "查询成功");
				json.put("硬件版本", output[8]);
				json.put("软件版本", output[9]);
				json.put("电池电压", ((float)output[10]) / 10);
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
	
	/**
	 * 获取压力传感器参数处设置命令
	 * @return
	 */
	public byte[] getPressureSensorInfoBytes() {
	    byte[] d = new byte[10];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x01;
	    d[7] = (byte) 0x00;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 解析工业压力传感器蓝牙设置的参数
	 * @param output
	 * @return
	 */
	public JSONObject getPressureSensorInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 0) {
					json.put("产品名称", "工业压力传感器");
				}
				tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+8] & 0xff);
				}
				json.put("ID", tmp);
				tmp = (int)output[14] & 0xff;
				if(tmp ==0 )
				    json.put("口径", "15mm");
				else if(tmp ==1 )
					    json.put("口径", "20mm");
				else 
					    json.put("口径", "25mm");
				tmp = 0;
				for (int i = 2; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+14]) & 0xff);
				}
				json.put("最低压力ADC_DR值", tmp);
				tmp = 0;
				for (int i = 2; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+16]) & 0xff);
				}
				json.put("最高压力ADC_DR值", tmp);
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
	
	
	/**
	 * 获取三磁敏设置参数命令
	 * @return
	 */
	public byte[] getMagnetoMeterInfoBytes() {
	    byte[] d = new byte[10];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x01;
	    d[7] = (byte) 0x01;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 解析三磁敏蓝牙设置的参数数据
	 * @param output
	 * @return
	 */
	public JSONObject getMagnetoMeterInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 1) {
					json.put("产品名称", "工业三磁敏大表");
				}
				tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+8] & 0xff);
				}
				json.put("ID", tmp);
				tmp = (int)output[14] & 0xff;
				json.put("口径", tmp+"mm");
				/*if(tmp ==0 )
				    json.put("口径", "15mm");
				else{
					if(tmp ==1 )
					    json.put("口径", "20mm");
					if(tmp ==2 )
					    json.put("口径", "25mm");
				}*/
				tmp = 0;
				for (int i = 4; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+14]) & 0xff);
				}
				json.put("脉冲底数", tmp);
				tmp = (int)output[19] & 0xff;
				if(tmp ==0 )
				    json.put("脉冲常数", 1000);
				else if(tmp ==1 )
					    json.put("脉冲常数", 100);
				else if(tmp ==2 )
					    json.put("脉冲常数", 10);
				else if(tmp ==3 )
					    json.put("脉冲常数", 1);
					else
					    json.put("脉冲常数", 0.1);
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
   
	/**
	 * 获取第三方01系统接入参数设置命令
	 * @return
	 */
	public byte[] get01MeterInfoBytes() {
	    byte[] d = new byte[10];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x01;
	    d[7] = (byte) 0x04;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 解析第三方01接入系统蓝牙设置的参数
	 * @param output
	 * @return
	 */
	public JSONObject get01MeterInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 4) {
					json.put("产品名称", "第三方01系统接入");
				}
				tmp = (int)output[9] & 0xff;
				json.put("开始表号", tmp);
				tmp = (int)output[10] & 0xff;
				json.put("结束表号", tmp);
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
	
	/**
	 * 读PC设置出厂信息:接入传感器类
	 * @return
	 */
	public byte[] getPCFactoryVersionSensorsSeries(){
		byte[] d = new byte[10];
		d[0] = (byte) 0xfe;
		d[1] = (byte) 0xfe;
		d[2] = (byte) 0xfe;
		d[3] = (byte) 0x68;
		d[4] = (byte) 0x02;
		d[5] = (byte) 0x00;
		d[6] = (byte) 0x02;
		d[7] = (byte) 0x00;
		int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
		return d;
	}
	
	/**
	 * 读PC设置出厂信息:接入传感器类(数据解析)
	 * @param output
	 * @return
	 */
	public JSONObject getPCFactoryVersionSensorsSeriesInfo(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 0) {
					json.put("产品名称", "工业压力传感器");
				}else if (tmp == 1)
						json.put("产品名称", "工业三磁敏大表");
				else if (tmp == 2)
						json.put("产品名称", "工业超声波流量计");
				else if (tmp == 3)
						json.put("产品名称", "家用一拖二");
				else
						json.put("产品名称", "01水表第三方系统接入");
				tmp = (int)output[9] & 0xff;
				if (tmp == 0) {
					json.put("传感信号", "0-5V");
				}else if (tmp == 1)
						json.put("传感信号", "三磁敏");
				else if (tmp == 2)
						json.put("传感信号", "超声波正反脉冲");
				else if (tmp == 3)
						json.put("传感信号", "双磁敏");
					else
						json.put("传感信号", "01集中器传感");
				tmp = (int)output[10] & 0xff;
				if (tmp == 0) {
					json.put("电源类型", "电池型");
				}else {
					if (tmp == 1)
						json.put("传感信号", "外接电源");
				}
				tmp = (int)output[11] & 0xff;
				if (tmp == 0) {
					json.put("使用类型", "基站工业用");
				}else {
					if (tmp == 1)
						json.put("使用类型", "基站民用");
				}
				json.put("硬件版本", output[12]);
				json.put("软件版本", output[13]);
				tmp = Integer.parseInt(Integer.toHexString((int)output[14] & 0xff));
				if (tmp == 0) {
					json.put("上传频率", "15分钟");
				}else if (tmp == 10)
						json.put("上传频率", "1小时");
				else if (tmp == 20)
						json.put("上传频率", "12小时");
				else if (tmp == 30)
						json.put("上传频率", "24小时");
					else 
						json.put("上传频率", "48小时");
				int cury = (int)output[15] & 0xff;
				int curm = (int)output[16] & 0xff;
				int curd = (int)output[17] & 0xff;
				json.put("设备出厂日期", String.format("%d-%d-%d", cury,curm,curd));
				
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
	
	/**
	 * 读取传感器类系统时间命令
	 * @return
	 */
	public byte[] SystemTime()
	{
		byte[] d = new byte[9];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x04;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[8] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 读取系统时间返回
	 * @param output
	 * @return
	 */
	public JSONObject SystemTimeReturn(byte output[])
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
				json.put("状态", "查询成功");
				int cury = (int)output[8] & 0xff;
				int curm = (int)output[9] & 0xff;
				int curd = (int)output[10] & 0xff;
				int week = (int)output[11] & 0xff;
				int curh = (int)output[12] & 0xff;
				int curi = (int)output[13] & 0xff;
				int curs = (int)output[14] & 0xff;
				json.put("系统当前时钟", String.format("%d-%d-%d %d %d:%d:%d", cury,curm,curd,week,curh,curi,curs));
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
	
	/**
	 * 读取设备sn号
	 * @return
	 */
	public byte[] EquipmentSn()
	{
		byte[] d = new byte[9];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x01;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x05;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[8] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 读取sn号返回
	 * @param output
	 * @return
	 */
	public JSONObject EquipmentSnReturn(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = 0;
				byte[] b=new byte[16];//字节数组

				for (int i = 16; i > 0; i--)
				{
					tmp =  (int)(output[i+7]);
					b[i-1] = (byte) tmp;
				}
		        String s=new String(b,"ascii");//第二个参数指定编码方式
				json.put("sn", s);
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
	
	/**
	 * 读取工业压力传感器设备上传信息
	 * @return
	 */
	public byte[] pressureSensorUplodInfo()
	{
		byte[] d = new byte[10];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x07;
	    d[7] = (byte) 0x00;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 读取工业压力传感器设备上传信息返回
	 * @param output
	 * @return
	 */
	public JSONObject pressureSensorUplodInfoReturn(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 0) {
					json.put("传感信号", "0-5V");
				}else {
				}
				tmp = (int)output[9] & 0xff;
				    if ((tmp & 0x01) == 0)
					    json.put("设备电源类型", "电池型");
					if ((tmp & 0x01) > 0)
						json.put("设备电源类型", "外接电源");
					if ((tmp & 0x02) == 0)
						json.put("设备电源状态", "正常");
					if ((tmp & 0x02) > 0)
						json.put("设备电源状态", "欠压");
					if ((tmp & 0x04) == 0)
						json.put("流向", "正常");
					if ((tmp & 0x08) == 0)
						json.put("拆卸状态", "正常");
					if ((tmp & 0x08) > 0)
						json.put("拆卸状态", "拆卸");
					if ((tmp & 0x10) == 0)
						json.put("磁场外干扰", "正常");
					else json.put("磁场外干扰", "强磁");
					if ((tmp & 0x20) == 0)
						json.put("传感器状态", "正常");
					if ((tmp & 0x20) > 0)
						json.put("传感器状态", "异常");
					if ((tmp & 0x40) == 0)
						json.put("设备类型", "基站工业用");
					if ((tmp & 0x40) > 0)
						json.put("设备类型", "基站民用");
					if ((tmp & 0x80) == 0)
						json.put("读表状态", "成功");
					if ((tmp & 0x80) > 0)
						json.put("读表状态", "失败");
				tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+9] & 0xff);
				}
				json.put("ID", tmp);
				tmp = 0;
				for (int i = 2; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+14]) & 0xff);
				}
				json.put("采样数据", tmp);
				tmp = (int)output[17] & 0xff;
				json.put("异常工作时间",(int)output[17] & 0xff);
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
	
	/**
	 * 读取三磁敏设备上传信息
	 * @return
	 */
	public byte[] magnetometerUplodInfo()
	{
		byte[] d = new byte[10];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x02;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x07;
	    d[7] = (byte) 0x01;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[9] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 读取三磁敏设备上传信息返回
	 * @param output
	 * @return
	 */
	public JSONObject magnetometerUplodInfoReturn(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
				if (tmp == 1) {
					json.put("产品名称", "三磁敏");
				}else {
				}
				tmp = (int)output[9] & 0xff;
				if (tmp == 1) {
					json.put("传感信号", "三磁敏");
				}else {
				}
				tmp = (int)output[10] & 0xff;
				if ((tmp & 0x01) == 0)
				    json.put("设备电源类型", "电池型");
				if ((tmp & 0x01) > 0)
					json.put("设备电源类型", "外接电源");
				if ((tmp & 0x02) == 0)
					json.put("设备电源状态", "正常");
				if ((tmp & 0x02) > 0)
					json.put("设备电源状态", "欠压");
				if ((tmp & 0x04) == 0)
					json.put("流向", "正常");
				if ((tmp & 0x04) > 0)
					json.put("流向", "倒流");
				if ((tmp & 0x08) == 0)
					json.put("拆卸状态", "正常");
				if ((tmp & 0x08) > 0)
					json.put("拆卸状态", "拆卸");
				if ((tmp & 0x10) == 0)
					json.put("磁场外干扰", "正常");
				else json.put("磁场外干扰", "强磁");
				if ((tmp & 0x20) == 0)
					json.put("传感器状态", "正常");
				if ((tmp & 0x20) > 0)
					json.put("传感器状态", "异常");
				if ((tmp & 0x40) == 0)
					json.put("设备类型", "基站工业用");
				if ((tmp & 0x40) > 0)
					json.put("设备类型", "基站民用");
				if ((tmp & 0x80) == 0)
					json.put("读表状态", "成功");
				if ((tmp & 0x80) > 0)
					json.put("读表状态", "失败");
				tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+10] & 0xff);
				}
				json.put("ID", tmp);
				tmp = 0;
				for (int i = 4; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+15]) & 0xff);
				}
//				json.put("脉冲数据1", tmp);
				json.put("采样数据", tmp);
				tmp = 0;
				for (int i = 4; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+19]) & 0xff);
				}
//				json.put("脉冲数据2", tmp);
				json.put("采样数据", json.getString("采样数据")+ "," + tmp);
				
				tmp = 0;
				for (int i = 4; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+23]) & 0xff);
				}
//				json.put("脉冲数据3", tmp);
				
				json.put("采样数据", json.getString("采样数据")+ "," + tmp);
				json.put("异常工作时间",(int)output[28] & 0xff);
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
	
	/**
	 * 读取第三方01系统设备上传信息
	 * @return
	 */
	public byte[] thridUplodInfo(int bh)
	{
		byte[] d = new byte[11];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x03;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x07;
	    d[7] = (byte) 0x04;
	    d[8] = (byte) bh;
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
 	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[10] = (byte) 0x16;
	    return d;  
	}
	
	/**
	 * 读取第三方01系统设备上传信息返回
	 * @param output
	 * @return
	 */
	public JSONObject thirdUplodInfoReturn(byte output[])
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
				json.put("状态", "查询成功");
				long tmp = (int)output[8] & 0xff;
					json.put("表号", tmp);
				tmp = 0;
				for (int i = 5; i > 0; i--)
				{
					tmp = (tmp *256) + (int)(output[i+8] & 0xff);
				}
				json.put("ID", tmp);
				tmp = (int)output[14] & 0xff;
				    if ((tmp & 0x01) == 0)
					    json.put("表磁场", "正常");
					if ((tmp & 0x01) > 0)
						json.put("表磁场", "强磁");
					if ((tmp & 0x02) == 0)
						json.put("d1", "正常");
					if ((tmp & 0x04) == 0)
						json.put("读表状态", "正常");
					if ((tmp & 0x04) > 0)
						json.put("读表状态", "失败");
					if ((tmp & 0x08) == 0)
						json.put("电源状态", "正常");
					if ((tmp & 0x08) > 0)
						json.put("电源状态", "欠压");
					if ((tmp & 0x10) == 0)
						json.put("设备电源类型", "电池型");
					if ((tmp & 0x10) > 0)
						json.put("设备电源类型", "外接电源型");
					if ((tmp & 0x20) == 0)
						json.put("设备电源状态", "正常");
					if ((tmp & 0x20) > 0)
						json.put("设备电源状态", "欠压");
					if ((tmp & 0x40) == 0)
						json.put("设备类型", "基站工业用");
					if ((tmp & 0x40) > 0)
						json.put("设备类型", "基站民用");
					if ((tmp & 0x80) == 0)
						json.put("d1", "正常");
				tmp = (int)output[15] & 0xff;
				if(tmp ==0 )
					json.put("脉冲常数", 1000);
				else{
					if(tmp ==1 )
						json.put("脉冲常数", 100);
					else if(tmp ==2 )
						json.put("脉冲常数", 10);
					else if(tmp ==3 )
						json.put("脉冲常数", 1);
					else
						json.put("脉冲常数", 0.1);
				}
				tmp = 0;
				for (int i = 3; i > 0; i--)
				{
					tmp = tmp * 256;// + (int)(output[i+27]) & 0xff;
					tmp = tmp + ((int)(output[i+15]) & 0xff);
				}
				json.put("脉冲实际值", tmp);
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
	
	
	/**
	 * 流量计（汇中水表和正反脉冲发送手机号到云端测试）
	 * @param ph1
	 * @param ph2
	 * @param ph3
	 * @param ph4
	 * @param ph5
	 * @param ph6
	 * @param ph7
	 * @param ph8
	 * @param ph9
	 * @param ph10
	 * @param ph11
	 * @return
	 */
	public byte[] MobilePhoneTest(String ph1,String ph2,String ph3,String ph4,String ph5,String ph6,String ph7,String ph8,String ph9,String ph10,String ph11)
	{
		byte[] d = new byte[20];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0c;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x07;
	    d[7] = (byte) Integer.parseInt(ph1);
	    d[8] = (byte) Integer.parseInt(ph2);
	    d[9] = (byte) Integer.parseInt(ph3);
	    d[10] = (byte) Integer.parseInt(ph4);
	    d[11] = (byte) Integer.parseInt(ph5);
	    d[12] = (byte) Integer.parseInt(ph6);
	    d[13] = (byte) Integer.parseInt(ph7);
	    d[14] = (byte) Integer.parseInt(ph8);
	    d[15] = (byte) Integer.parseInt(ph9);
	    d[16] = (byte) Integer.parseInt(ph10);
	    d[17] = (byte) Integer.parseInt(ph11);
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
	
	
	/**
	 * MD11A数据发送测试
	 * @param phone
	 * @return
	 */
	public byte[] MD11ATest(String ph1,String ph2,String ph3,String ph4,String ph5,String ph6,String ph7,String ph8,String ph9,String ph10,String ph11)
	{
		byte[] d = new byte[20];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0c;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x08;
	    d[7] = (byte) Integer.parseInt(ph1);
	    d[8] = (byte) Integer.parseInt(ph2);
	    d[9] = (byte) Integer.parseInt(ph3);
	    d[10] = (byte) Integer.parseInt(ph4);
	    d[11] = (byte) Integer.parseInt(ph5);
	    d[12] = (byte) Integer.parseInt(ph6);
	    d[13] = (byte) Integer.parseInt(ph7);
	    d[14] = (byte) Integer.parseInt(ph8);
	    d[15] = (byte) Integer.parseInt(ph9);
	    d[16] = (byte) Integer.parseInt(ph10);
	    d[17] = (byte) Integer.parseInt(ph11);
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
	//唤醒所有无线水表
	public byte[] wakeMeters()
	{
//		FE FE FE 68 0B 00 09 01 FF FF FF FF FF FF FF FF 01 0E 16  //唤醒
		byte[] d = new byte[19];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x0b;
	    d[5] = (byte) 0x00;
	    d[6] = (byte) 0x09;
	    d[7] = (byte) 0x01;
	    d[8] = (byte) 0xff;
	    d[9] = (byte) 0xff;
	    d[10] = (byte) 0xff;
	    d[11] = (byte) 0xff;
	    d[12] = (byte) 0xff;
	    d[13] = (byte) 0xff;
	    d[14] = (byte) 0xff;
	    d[15] = (byte) 0xff;
	    d[16] = (byte) 0x01;
	    d[17] = (byte) 0x0e;
	    d[18] = (byte) 0x16;
	    return d;  
	}
	//唤醒水表的返回
	public JSONObject wakeReturn(byte output[])
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

	//初始化
	public byte[] initMeter(String sn,int bh,int rate,int value,int type,int wh,int lyqwh,int id)
	{
		//倍率 FE FE FE 68 0C 00 09 01 09 28 00 00 00 00 00 00 02 02 4B 16  
		//底数 FE FE FE 68 0F 00 09  01  09 28 00 00 00 00 00 00  04  01 00 00 00  4F 16 
		//时间 FE FE FE 68 11 00 09 01 09 28 00 00 00 00 00 00 05  10 09 09 0E 09 0A  94 16
		
		byte[] d = new byte[27];  
	    d[0] = (byte) 0xfe;
	    d[1] = (byte) 0xfe;
	    d[2] = (byte) 0xfe;
	    d[3] = (byte) 0x68;
	    d[4] = (byte) 0x13;
	    d[5] = (byte) 0x00; 
	    d[6] = (byte) 0x09;
	    d[7] = (byte) (type + 1);
	    if (type+1 == 1)		//无线远传
	    {
	    	try							//8-15为sn号
		    {
//		    	int isn = Integer.parseInt(sn);
	    		long isn = Long.parseLong(sn);
		    	for (int i =0; i < 4; i++)
		    	{
		    		d[i + 8] = (byte) (isn & 0xff);
		    		isn = isn >> 8;
		    	}
		    	d[12] = (byte) wh;
		    	d[13] = (byte) lyqwh;
		    	d[14] = (byte) id;
		    	d[15] = 0x55;
		    }
		    catch (Exception e)
		    {
		    	
		    }
	    }
	    else if (type + 1 == 3) //总线采集
	    {
	    	try							//8-15为sn号
		    {
//		    	int isn = Integer.parseInt(sn);
	    		long isn = Long.parseLong(sn);
		    	for (int i =0; i < 6; i++)
		    	{
		    		d[i + 8] = (byte) (isn & 0xff);
		    		isn = isn >> 8;
		    	}
		    	d[14] = (byte) bh;
		    	d[15] = 0x55;
		    }
		    catch (Exception e)
		    {
		    	
		    }
	    }
	    
	    d[16] = (byte)0x02;
	    d[17] = (byte) rate;
	    d[18] = (byte) 0x04;
	    d[19] = (byte) (value & 0xff);
	    d[20] = (byte) ((value & 0xff00) / 0x0100);
	    d[21] = (byte) ((value & 0xff0000) / 0x010000);
	    d[22] = (byte) ((value & 0xff000000) / 0x01000000);
	    d[23] = (byte) 0x07;
	    d[24] = (byte) (0);
	    
	    int length = d[5] * 256 + d[4];
	    byte cs = 0;
	    for (int i = 4; i < length + 6; i++)
	    {
	    	cs += d[i];
	    }
	    d[length + 6] = cs;
	    d[26] = (byte) 0x16;
	    return d; 
	}
	
	//置数
		public byte[] setNumber(String sn,int bh,int rate,int value,int type,int statue){
			byte[] d = new byte[27];
			d[0] = (byte) 0xfe;
		    d[1] = (byte) 0xfe;
		    d[2] = (byte) 0xfe;
		    d[3] = (byte) 0x68;
		    d[4] = (byte) 0x13;
		    d[5] = (byte) 0x00;
		    d[6] = (byte) 0x09;
		    d[7] = (byte) (type + 1);
		    if (type+1 == 1)		//无线远传
		    {
		    	try							//8-15为sn号
			    {
//			    	int isn = Integer.parseInt(sn);
		    		long isn = Long.parseLong(sn);
			    	for (int i =0; i < 4; i++)
			    	{
			    		d[i + 8] = (byte) (isn & 0xff);
			    		isn = isn >> 8;
			    	}
			    	d[15] = (byte) 0xAA;
			    }
			    catch (Exception e)
			    {
			    	
			    }
		    }
		    else if (type + 1 == 3) //总线采集
		    {
		    	try							//8-15为sn号
			    {
//			    	int isn = Integer.parseInt(sn);
		    		long isn = Long.parseLong(sn);
			    	for (int i =0; i < 6; i++)
			    	{
			    		d[i + 8] = (byte) (isn & 0xff);
			    		isn = isn >> 8;
			    	}
			    	d[14] = (byte) bh;
			    	d[15] = (byte) 0xAA;
			    }
			    catch (Exception e)
			    {
			    	
			    }
		    }
		    
		    d[16] = (byte)0x02;
		    d[17] = (byte) rate;
		    d[18] = (byte) 0x04;
		    d[19] = (byte) (value & 0xff);
		    d[20] = (byte) ((value & 0xff00) / 0x0100);
		    d[21] = (byte) ((value & 0xff0000) / 0x010000);
		    d[22] = (byte) ((value & 0xff000000) / 0x01000000);
		    d[23] = (byte) 0x07;
		    d[24] = (byte) statue;
		    
		    int length = d[5] * 256 + d[4];
		    byte cs = 0;
		    for (int i = 4; i < length + 6; i++)
		    {
		    	cs += d[i];
		    }
		    d[length + 6] = cs;
		    d[26] = (byte) 0x16;
		    return d; 
		}
		
		
		//获取新集中器抄表指令
		public byte[] getConcentratorInfoBytes(){
			byte[] d = new byte[6];  
		    d[0] = (byte) 0xC0;
		    d[1] = (byte) 0xFD;
		    d[2] = (byte) 0x00;
		    d[3] = (byte) 0x02;
		    d[4] = (byte) 0xFB;
		    d[5] = (byte) 0xFA;
		    return d;
		}
				
		//解析集中器数据返回
		public JSONObject getInfoConcentrator(byte output[]){
			JSONObject json = new JSONObject();
			try {
				if (output == null)
				{
					json.put("状态", "未能读取到数据");
					return json;
				}else {
					long tmp = (int)output[0] & 0xff;
					json.put("表号", tmp);
					
					tmp = 0;
					for (int i = 6; i > 0; i--)
					{
						tmp = (tmp *256) + (int)(output[i+2] & 0xff);
					}
					json.put("ID号", tmp);
					
					tmp = 0;
					tmp = (int)output[9] & 0xff;
					if (tmp == 2) {
						json.put("类型", "冷水表");
					}
					
					tmp = 0;
					for (int i = 3; i > 0; i--)
					{
						tmp = (tmp *256) + (int)(output[i+9] & 0xff);
					}
					json.put("脉冲数", tmp);
					
					tmp = 0;
					tmp = (int)output[13] & 0xff;
					if (tmp == 0) {
						json.put("状态", "正常");
					}
					
					tmp = 0;
					tmp = (int)output[14] & 0xff;
					json.put("HUB号", tmp);
				}
			} catch (Exception e) {
			}
			return null;
		}
	
}
