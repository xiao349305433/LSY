package com.lsy.equipment.list;

/**
 * 数据源类
 * @author zhang.q.s
 *
 */
public class ItemBean {
	public String Device_sn;
	public String FrequencyPoint;
	public String Power;
	public String Spectrum;
	public String SendFrequence;
	public String SignalRatio;
	public String SignalStrength;
	public String SpreadSpectrum;
	
	
	public ItemBean(String device_sn, String frequencyPoint, String power,
			String spectrum, String sendFrequence, String signalRatio,
			String signalStrength, String spreadSpectrum) {
		super();
		Device_sn = device_sn;
		FrequencyPoint = frequencyPoint;
		Power = power;
		Spectrum = spectrum;
		SendFrequence = sendFrequence;
		SignalRatio = signalRatio;
		SignalStrength = signalStrength;
		SpreadSpectrum = spreadSpectrum;
	}
	
	
	

}
