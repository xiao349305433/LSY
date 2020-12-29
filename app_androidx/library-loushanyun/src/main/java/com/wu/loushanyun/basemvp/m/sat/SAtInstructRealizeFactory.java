package com.wu.loushanyun.basemvp.m.sat;


import java.util.ArrayList;
import java.util.HashMap;


public class SAtInstructRealizeFactory {


    private HashMap<String, String> hashMapXinDao;
    private ArrayList<String> arrayList;


    private SAtBuilder sAtBuilder;

    /**
     * 获取所有信道模式
     *
     * @return
     */
    public ArrayList<String> getAllMoShi() {
        return arrayList;
    }

    public int getMoShiIndex(String moshi) {
        return Integer.valueOf(hashMapXinDao.get(moshi));
    }

    public SAtInstructRealizeFactory(double hardVersion) {
        init();
        sAtBuilder.setHardVersion(hardVersion);
    }

    public SAtInstructRealizeFactory() {
        init();
    }

    private void init() {
        arrayList = new ArrayList<>();
        hashMapXinDao = new HashMap<>();
        sAtBuilder = new SAtBuilder();
        for (int i = 0; i <= 87; i++) {
            String moshi;
            if (i == 0) {
                moshi = "模式B";
            } else if (i == 80) {
                moshi = "模式A";
            } else {
                moshi = "模式" + String.valueOf(i);
            }
            arrayList.add(moshi);
            hashMapXinDao.put(moshi, i + "");
        }

    }


    /**
     * 识别模块之后设置版本号，否则无法使用命令
     *
     * @param hardVersion
     */
    public void initHardVersion(double hardVersion) {
        sAtBuilder.setHardVersion(hardVersion);
    }

    public SAtBuilder setsAtParams(String sAtParams) {
        return sAtBuilder.setsAtParams(sAtParams);
    }


    public String getSAtTypeString(byte[] result) {
        return sAtBuilder.getSAtTypeString(result);
    }
}
