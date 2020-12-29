package com.wu.loushanyun.basemvp.p;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.wu.loushanyun.basemvp.m.ProvinceCitysBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ProvinceUtil {
    public static List<ProvinceCitysBean.ProvinceBean> getAllProvince(Context context) {
        String cityJson = getJson(context, "china_city_data.json");
        //解析省份
        ProvinceCitysBean provinceCitysBean = new Gson().fromJson(cityJson, ProvinceCitysBean.class);
        return provinceCitysBean.getProvince();
    }

    public static List<ProvinceCitysBean.ProvinceBean.CityListBeanX> getAllCity(Context context, String provinceName) {
        String cityJson = getJson(context, "china_city_data.json");
        //解析省份
        ProvinceCitysBean provinceCitysBean = new Gson().fromJson(cityJson, ProvinceCitysBean.class);
        for (int i = 0; i < provinceCitysBean.getProvince().size(); i++) {
            if (provinceName.equals(provinceCitysBean.getProvince().get(i).getName())) {
                return provinceCitysBean.getProvince().get(i).getCityList();
            }
        }
        return null;
    }

    public static List<ProvinceCitysBean.ProvinceBean.CityListBeanX.CityListBean> getAllDistrict(Context context, String provinceName, String cityName) {
        String cityJson = getJson(context, "china_city_data.json");
        //解析省份
        ProvinceCitysBean provinceCitysBean = new Gson().fromJson(cityJson, ProvinceCitysBean.class);
        for (int i = 0; i < provinceCitysBean.getProvince().size(); i++) {
            if (provinceName.equals(provinceCitysBean.getProvince().get(i).getName())) {
                for (int j = 0; j < provinceCitysBean.getProvince().get(i).getCityList().size(); j++) {
                    if (cityName.equals(provinceCitysBean.getProvince().get(i).getCityList().get(j).getName())) {
                        return provinceCitysBean.getProvince().get(i).getCityList().get(j).getCityList();
                    }
                }

            }
        }
        return null;
    }


    //读取方法
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
