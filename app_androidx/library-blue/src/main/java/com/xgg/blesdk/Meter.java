package com.xgg.blesdk;

import org.json.JSONObject;

public abstract class Meter {
	protected int m_id;
	
	abstract public byte[] getQueryBytes(String sn);
	
	abstract public JSONObject getQueryObject(byte output[]);
}
