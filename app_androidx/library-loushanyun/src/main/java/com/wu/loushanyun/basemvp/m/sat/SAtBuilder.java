package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.contract.SAtInstruct;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

public class SAtBuilder implements SAtInstruct {
    private String sAtParams;
    private double hardVersion;

    private SInstructDiscern sInstructDiscern;
    private SAtInstructChannel sAtInstructChannel;
    private SAtInstructFirmwareVersion sAtInstructFirmwareVersion;
    private SAtInstructForcedSend sAtInstructForcedSend;
    private SAtInstructRSSI sAtInstructRSSI;
    private SAtInstructSpreadingFactor sAtInstructSpreadingFactor;
    private SAtInstructSendingPower sAtInstructSendingPower;
    private SAtInstructRxDelay sAtInstructRxDelay;
    private SAtInstructToken sAtInstructToken;

    private void createSAtInstruct(double hardVersion) {
        sAtInstructChannel = new SAtInstructChannel(hardVersion);
        sAtInstructFirmwareVersion = new SAtInstructFirmwareVersion(hardVersion);
        sAtInstructForcedSend = new SAtInstructForcedSend(hardVersion);
        sAtInstructRSSI = new SAtInstructRSSI(hardVersion);
        sAtInstructSpreadingFactor = new SAtInstructSpreadingFactor(hardVersion);
        sAtInstructSendingPower = new SAtInstructSendingPower(hardVersion);
        sAtInstructToken = new SAtInstructToken(hardVersion);
        sAtInstructRxDelay = new SAtInstructRxDelay(hardVersion);
    }

    public SAtInstruct getSAtInstruct() {
        if (SAtInstructParams.sAtInstructChannel.equals(sAtParams)) {
            return sAtInstructChannel;
        } else if (SAtInstructParams.sAtInstructFirmwareVersion.equals(sAtParams)) {
            return sAtInstructFirmwareVersion;
        } else if (SAtInstructParams.sAtInstructForcedSend.equals(sAtParams)) {
            return sAtInstructForcedSend;
        } else if (SAtInstructParams.sAtInstructRSSI.equals(sAtParams)) {
            return sAtInstructRSSI;
        } else if (SAtInstructParams.sAtInstructSpreadingFactor.equals(sAtParams)) {
            return sAtInstructSpreadingFactor;
        } else if (SAtInstructParams.sInstructDiscern.equals(sAtParams)) {
            return sInstructDiscern;
        } else if (SAtInstructParams.sAtInstructSendingPower.equals(sAtParams)) {
            return sAtInstructSendingPower;
        } else if (SAtInstructParams.sAtInstructRxDelay.equals(sAtParams)) {
            return sAtInstructRxDelay;
        } else if (SAtInstructParams.sAtInstructToken.equals(sAtParams)) {
            return sAtInstructToken;
        } else {
            return null;
        }

    }

    /**
     * 返回0x50命令返回的数据类型
     *
     * @param result
     * @return
     */
    public String getSAtTypeString(byte[] result) {
        String resultAt = ByteConvertUtils.getASCIIbyByte(result, 3, 2);
        if (resultAt.contains("OK")) {
            if (resultAt.contains("+CHM:")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+V")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+SEND:2")) {
                return sAtParams + "+SEND2";
            } else if (resultAt.contains("+SEND:3")) {
                return sAtParams + "+SEND3";
            } else if (resultAt.contains("+RSSI")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+TXP")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+DR")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+TOKEN")) {
                return sAtParams + "-R";
            } else if (resultAt.contains("+RX1DL")) {
                return sAtParams + "-R";
            } else {
                return sAtParams + "-W";
            }
        } else {
            return "操作失败" + resultAt;
        }
    }

    public SAtBuilder() {
        sInstructDiscern = new SInstructDiscern(hardVersion);
    }


    public SAtBuilder setsAtParams(String sAtParams) {
        this.sAtParams = sAtParams;
        return this;
    }

    public void setHardVersion(double hardVersion) {
        this.hardVersion = hardVersion;
        createSAtInstruct(hardVersion);
    }


    @Override
    public byte[] getOneWriteBytes(String... params) throws Exception {
        return getSAtInstruct().getOneWriteBytes(params);
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) throws Exception {
        return getSAtInstruct().getTwoWriteBytes(params);
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) throws Exception {
        return getSAtInstruct().getThreeWriteBytes(params);
    }

    @Override
    public byte[] getFourWriteBytes(String... params) throws Exception {
        return getSAtInstruct().getFourWriteBytes(params);
    }

    @Override
    public byte[] getOneReadBytes(String... params) throws Exception {
        return getSAtInstruct().getOneReadBytes(params);
    }

    @Override
    public byte[] getTwoReadBytes(String... params) throws Exception {
        return getSAtInstruct().getTwoReadBytes(params);
    }

    @Override
    public byte[] getThreeReadBytes(String... params) throws Exception {
        return getSAtInstruct().getThreeReadBytes(params);
    }

    @Override
    public byte[] getFourReadBytes(String... params) throws Exception {
        return getSAtInstruct().getFourReadBytes(params);
    }

    @Override
    public HashMap<String, String> parseOneWNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseOneWNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseOneRNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseOneRNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseTwoWNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseTwoWNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseTwoRNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseTwoRNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseThreeWNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseThreeWNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseThreeRNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseThreeRNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseFourWNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseFourWNotifyBytes(bytes);
    }

    @Override
    public HashMap<String, String> parseFourRNotifyBytes(byte[] bytes) throws Exception {
        return getSAtInstruct().parseFourRNotifyBytes(bytes);
    }
}