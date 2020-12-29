package wu.loushanyun.com.moduletwoinit.m;

import com.sensoro.sensor.kit.entity.SensoroDevice;

public class ConverterDeviceData {
    private String serialnumber;
    private OnetoOneConverter converter;
    private  SensoroDevice device;
    private  boolean canClick;

    public ConverterDeviceData(){
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConverterDeviceData) {
            ConverterDeviceData converterDeviceData= (ConverterDeviceData) obj;
            return serialnumber.equals(converterDeviceData.serialnumber);
        }
        return false;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public OnetoOneConverter getConverter() {
        return converter;
    }

    public void setConverter(OnetoOneConverter converter) {
        this.converter = converter;
    }

    public SensoroDevice getDevice() {
        return device;
    }

    public void setDevice(SensoroDevice device) {
        this.device = device;
    }
}
