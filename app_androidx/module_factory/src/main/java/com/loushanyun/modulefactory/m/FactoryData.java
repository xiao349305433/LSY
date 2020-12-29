package com.loushanyun.modulefactory.m;

import org.json.JSONException;
import org.json.JSONObject;

public class FactoryData {
    public JSONObject getFactoryData(String sn, String useScene, String combinationType, String workPattern, String sendingRate,
                                     String frequencyBand, String sendingPower, String productionTime, String networkInteraction,
                                     String hardwareVersion, String softVersion, String batteryState, String magneticInterferenceState,
                                     String disassemblyState, String backflowState, String sensorState, String valveControlState, String thirdPowerState,
                                     String circumscribedPowerState, int sensingSignal, int measuringMode, int productForm, String pulseWidth,
                                     String joinForm, String manufacturersIdentification, int powerType, String sf, String channel, float impulseInitial,
                                     float pulseConstant,String ordernumber,String exitlogo) {
        JSONObject json = new JSONObject();
        try {
            json.put("sn", sn);
            json.put("useScene", useScene);
            json.put("combinationType", combinationType);
            json.put("workPattern", workPattern);
            json.put("sendingRate", sendingRate);
            json.put("frequencyBand", frequencyBand);
            json.put("sendingPower", sendingPower);
            json.put("productionTime", productionTime);
            json.put("networkInteraction", networkInteraction);
            json.put("hardwareVersion", hardwareVersion);
            json.put("softVersion", softVersion);
            json.put("batteryState", batteryState);
            json.put("magneticInterferenceState", magneticInterferenceState);
            json.put("disassemblyState", disassemblyState);
            json.put("backflowState", backflowState);
            json.put("sensorState", sensorState);
            json.put("valveControlState", valveControlState);
            json.put("thirdPowerState", thirdPowerState);
            json.put("circumscribedPowerState", circumscribedPowerState);
            json.put("sensingSignal", sensingSignal);
            json.put("measuringMode", measuringMode);
            json.put("productForm", productForm);
            json.put("pulseWidth", pulseWidth);
            json.put("joinForm", joinForm);
            json.put("manufacturersIdentification", manufacturersIdentification);
            json.put("powerType", powerType);
            json.put("sf", sf);
            json.put("channel", channel);
            json.put("impulseInitial", impulseInitial);
            json.put("pulseConstant", pulseConstant);
            json.put("ordernumber", ordernumber);
            json.put("exitlogo", exitlogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public JSONObject getFactoryData(String sn, String useScene, int combinationType, int workPattern, int sendingRate,
                                     String frequencyBand, String sendingPower, String productionTime, String networkInteraction,
                                     String hardwareVersion, String softVersion, int batteryState, int magneticInterferenceState,
                                     int disassemblyState, int backflowState, int sensorState, int valveControlState, int thirdPowerState,
                                     int circumscribedPowerState, int sensingSignal, int measuringMode, int productForm, String pulseWidth,
                                     int joinForm, int manufacturersIdentification, int powerType, String sf, String channel, float impulseInitial, float pulseConstant) {
        JSONObject json = new JSONObject();
        try {
            json.put("sn", sn);
            json.put("useScene", useScene);
            json.put("combinationType", combinationType);
            json.put("workPattern", workPattern);
            json.put("sendingRate", sendingRate);
            json.put("frequencyBand", frequencyBand);
            json.put("sendingPower", sendingPower);
            json.put("productionTime", productionTime);
            json.put("networkInteraction", networkInteraction);
            json.put("hardwareVersion", hardwareVersion);
            json.put("softVersion", softVersion);
            json.put("batteryState", batteryState);
            json.put("magneticInterferenceState", magneticInterferenceState);
            json.put("disassemblyState", disassemblyState);
            json.put("backflowState", backflowState);
            json.put("sensorState", sensorState);
            json.put("valveControlState", valveControlState);
            json.put("thirdPowerState", thirdPowerState);
            json.put("circumscribedPowerState", circumscribedPowerState);
            json.put("sensingSignal", sensingSignal);
            json.put("measuringMode", measuringMode);
            json.put("productForm", productForm);
            json.put("pulseWidth", pulseWidth);
            json.put("joinForm", joinForm);
            json.put("manufacturersIdentification", manufacturersIdentification);
            json.put("powerType", powerType);
            json.put("sf", sf);
            json.put("channel", channel);
            json.put("impulseInitial", impulseInitial);
            json.put("pulseConstant", pulseConstant);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getFactoryData(String sn, String useScene, int combinationType, int workPattern, int sendingRate,
                                     String frequencyBand, String sendingPower, String productionTime, String networkInteraction,
                                     String hardwareVersion, String softVersion, String batteryState, String magneticInterferenceState,
                                     String disassemblyState, String backflowState, int sensorState, int valveControlState, int thirdPowerState,
                                     int circumscribedPowerState, int sensingSignal, int measuringMode, String productForm, String pulseWidth,
                                     int joinForm, int manufacturersIdentification, int powerType, String sf, String channel,
                                     String impulseInitial, String pulseConstant,String caliber,String tablenumber,String ordernumber,String exitlogo
    ) {
        JSONObject json = new JSONObject();
        try {
            json.put("sn", sn);
            json.put("useScene", useScene);
            json.put("combinationType", combinationType);
            json.put("workPattern", workPattern);
            json.put("sendingRate", sendingRate);
            json.put("frequencyBand", frequencyBand);
            json.put("sendingPower", sendingPower);
            json.put("productionTime", productionTime);
            json.put("networkInteraction", networkInteraction);
            json.put("hardwareVersion", hardwareVersion);
            json.put("softVersion", softVersion);
            json.put("batteryState", batteryState);
            json.put("magneticInterferenceState", magneticInterferenceState);
            json.put("disassemblyState", disassemblyState);
            json.put("backflowState", backflowState);
            json.put("sensorState", sensorState);
            json.put("valveControlState", valveControlState);
            json.put("thirdPowerState", thirdPowerState);
            json.put("circumscribedPowerState", circumscribedPowerState);
            json.put("sensingSignal", sensingSignal);
            json.put("measuringMode", measuringMode);
            json.put("productForm", productForm);
            json.put("pulseWidth", pulseWidth);
            json.put("joinForm", joinForm);
            json.put("manufacturersIdentification", manufacturersIdentification);
            json.put("powerType", powerType);
            json.put("sf", sf);
            json.put("channel", channel);
            json.put("impulseInitial", impulseInitial);
            json.put("pulseConstant", pulseConstant);
            json.put("caliber", caliber);
            json.put("tablenumber", tablenumber);
            json.put("ordernumber", ordernumber);
            json.put("exitlogo", exitlogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
