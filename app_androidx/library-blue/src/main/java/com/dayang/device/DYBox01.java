package com.dayang.device;

import com.xgg.blesdk.Box2001;

import org.json.JSONObject;


public class DYBox01 extends Box2001 {
    public DYBox01() {
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
    protected int CheckInput() {
        if (m_bytes == null)
            return -1;
        try {
            if (m_bytes[0] != -2)        //协议头不对
                return -1;
            if (m_bytes.length < 2)
                return 0;
            if (m_bytes[1] != -2)        //协议头不对
                return -1;
            if (m_bytes.length < 3)
                return 0;
            if (m_bytes[2] != -2)        //协议头不对
                return -1;
            if (m_bytes.length < 4)
                return 0;
            if (m_bytes[3] != 0x68)        //协议头不对
                return -1;
            if (m_bytes.length <= 5)
                return 0;
            int length = m_bytes[5] * 256 + m_bytes[4];
            if (length + 8 > m_bytes.length)    //现在收到的字节数目不够
                return 0;
    		/*if (m_bytes[6] != (byte)(m_curcmd | 0x80) )		//答非所问
    			return -3;*/
            if (m_bytes[length + 7] != 0x16)        //协议尾不对
                return -1;
            //字节足够多了，开始校验
            byte cs = 0;
            for (int i = 4; i < m_bytes.length - 2; i++) {
                cs += m_bytes[i];
            }
            if (cs == m_bytes[m_bytes.length - 2])        //校验正确
            {
                return 1;
            } else
                return -2;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public int clearBytes() {
        if (m_bytes == null)
            return 0;
        try {
            int rt = CheckInput();
            if (rt < 0)        //校验不正确
            {
                m_bytes = null;
                return 0;
            } else if (rt == 0) {
                return 0;
            } else        //校验正确
            {
                //移除掉第一帧
                //看看如果还有字节，返回1，否则返回0
                int length = m_bytes[5] * 256 + m_bytes[4];
                if (m_bytes.length <= length + 8) {
                    m_bytes = null;
                    return 0;
                } else {
                    int newlength = m_bytes.length - length - 8;
                    byte newbyte[] = new byte[newlength];
                    for (int i = 0; i < newlength; i++)
                        newbyte[i] = m_bytes[i + length + 8];
                    m_bytes = newbyte;
                    return 1;
                }
            }
        } catch (Exception e) {
            m_bytes = null;
            return 0;
        }
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
        d[7] = (byte) 0x02;
        d[8] = (byte) 0x16;
        return d;
    }

    //获取盒子信息
    public JSONObject getInfo(byte output[]) {
        JSONObject json = new JSONObject();
        try {
            if (output == null) {
                json.put("状态", "未能读取到数据");
                return json;
            }
            if (output[7] == 0)        //查询成功
            {
                json.put("状态", "查询成功");
                json.put("硬件版本", output[8]);
                json.put("软件版本", output[9]);
                json.put("电池电压", ((float) output[10]) / 10);
            } else {
                json.put("状态", "未能读取到数据");
                return json;
            }
        } catch (Exception e) {

        }
        return json;
    }

    //唤醒所有无线水表
    public byte[] wakeMeters() {
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
    public JSONObject wakeReturn(byte output[]) {
        JSONObject json = new JSONObject();
        try {
            if (output == null) {
                json.put("状态", "未能读取到数据");
                return json;
            }
            if (output[7] == 0)        //查询成功
            {
                json.put("状态", "唤醒成功");
            } else {
                json.put("状态", "未能读取到数据");
                return json;
            }
        } catch (Exception e) {

        }
        return json;
    }

    /**
     * @param sn
     * @param bh
     * @param value
     * @param type
     * @return
     */
    public byte[] initMeter1(String sn, int bh, int value, int type) {
        //倍率 FE FE FE 68 0C 00 09 01 09 28 00 00 00 00 00 00 02 02 4B 16
        //底数 FE FE FE 68 0F 00 09  01  09 28 00 00 00 00 00 00  04  01 00 00 00  4F 16
        //时间 FE FE FE 68 11 00 09 01 09 28 00 00 00 00 00 00 05  10 09 09 0E 09 0A  94 16

        byte[] d = new byte[23];
        d[0] = (byte) 0xfe;
        d[1] = (byte) 0xfe;
        d[2] = (byte) 0xfe;
        d[3] = (byte) 0x68;
        d[4] = (byte) 0x0f;
        d[5] = (byte) 0x00;
        d[6] = (byte) 0x09;
        d[7] = (byte) (type + 1);
        if (type + 1 == 3) //总线采集
        {
            try
            {
                long isn = Long.parseLong(sn);
                for (int i = 0; i < 6; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[14] = (byte) bh;
                d[15] = 0x55;
            } catch (Exception e) {

            }
        }

        d[16] = (byte) 0x04;
        d[17] = (byte) (value & 0xff);
        d[18] = (byte) ((value & 0xff00) / 0x0100);
        d[19] = (byte) ((value & 0xff0000) / 0x010000);
        d[20] = (byte) ((value & 0xff000000) / 0x01000000);
        byte cs = 0;
        for (int i = 4; i < 21; i++) {
            cs += d[i];
        }

        d[21] = (byte) (cs & 0xff);

        d[22] = (byte) 0x16;
        return d;
    }

    //初始化
    public byte[] initMeter(String sn, int bh, int rate, int value, int type, int wh, int lyqwh, int id) {
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
        if (type + 1 == 1)        //无线远传
        {
            try                            //8-15为sn号
            {
//		    	int isn = Integer.parseInt(sn);
                long isn = Long.parseLong(sn);
                for (int i = 0; i < 4; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[12] = (byte) wh;
                d[13] = (byte) lyqwh;
                d[14] = (byte) id;
                d[15] = 0x55;
            } catch (Exception e) {

            }
        } else if (type + 1 == 3) //总线采集
        {
            try                            //8-15为sn号
            {
//		    	int isn = Integer.parseInt(sn);
                long isn = Long.parseLong(sn);
                for (int i = 0; i < 6; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[14] = (byte) bh;
                d[15] = 0x55;
            } catch (Exception e) {

            }
        }

        d[16] = (byte) 0x02;
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
        for (int i = 4; i < length + 6; i++) {
            cs += d[i];
        }
        d[length + 6] = cs;
        d[26] = (byte) 0x16;
        return d;
    }


    //置数
    public byte[] setNumber(String sn, int bh, int rate, int value, int type, int statue) {
        byte[] d = new byte[27];
        d[0] = (byte) 0xfe;
        d[1] = (byte) 0xfe;
        d[2] = (byte) 0xfe;
        d[3] = (byte) 0x68;
        d[4] = (byte) 0x13;
        d[5] = (byte) 0x00;
        d[6] = (byte) 0x09;
        d[7] = (byte) (type + 1);
        if (type + 1 == 1)        //无线远传
        {
            try                            //8-15为sn号
            {
//		    	int isn = Integer.parseInt(sn);
                long isn = Long.parseLong(sn);
                for (int i = 0; i < 4; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[15] = (byte) 0xAA;
            } catch (Exception e) {

            }
        } else if (type + 1 == 3) //总线采集
        {
            try                            //8-15为sn号
            {
                int isn = Integer.parseInt(sn);
                for (int i = 0; i < 6; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[14] = (byte) bh;
                d[15] = (byte) 0xAA;
            } catch (Exception e) {

            }
        }

        d[16] = (byte) 0x02;
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
        for (int i = 4; i < length + 6; i++) {
            cs += d[i];
        }
        d[length + 6] = cs;
        d[26] = (byte) 0x16;
        return d;
    }


}
