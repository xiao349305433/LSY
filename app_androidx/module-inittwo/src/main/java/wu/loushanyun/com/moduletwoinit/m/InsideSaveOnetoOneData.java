package wu.loushanyun.com.moduletwoinit.m;

import java.io.Serializable;
import java.util.List;

public class InsideSaveOnetoOneData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String lat;//经度
    private String lon;//纬度
	private String provinces;//省份
	private String cities;//市
	private String counties;//县城
	private String businessLicense;//营业执照序号
	private String areaNumber;//区域号
	private String areaName;//区域名称
	private Integer loginId;//登录Id
	private List<String> image;
	
	private List<OnetoOneConverter> converter;
	
	

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}

	public String getCounties() {
		return counties;
	}

	public void setCounties(String counties) {
		this.counties = counties;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}


	public String getAreaNumber() {
		return areaNumber;
	}

	public void setAreaNumber(String areaNumber) {
		this.areaNumber = areaNumber;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public List<OnetoOneConverter> getConverter() {
		return converter;
	}

	public void setConverter(List<OnetoOneConverter> converter) {
		this.converter = converter;
	}
	
	
}
