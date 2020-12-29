package wu.loushanyun.com.modulechiptest.p.util;

import com.wu.loushanyun.basemvp.m.SaveDataMeter;

import java.util.ArrayList;

import wu.loushanyun.com.modulechiptest.m.FactoryProductionInfo;

public class SnPrintUtil {
    /**
     * 是否同一个2号模组的对象,返回-1证明不存在
     *
     * @param arrayList
     * @param factoryProductionInfo
     * @return
     */
    public static int containsSecond(ArrayList<FactoryProductionInfo> arrayList, FactoryProductionInfo factoryProductionInfo) {
        int containsIndex = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getSnr()==factoryProductionInfo.getSnr()) {
                containsIndex = i;
            }
        }
        return containsIndex;
    }
}
