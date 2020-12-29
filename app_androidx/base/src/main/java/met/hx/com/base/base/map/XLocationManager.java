package met.hx.com.base.base.map;

import com.amap.api.location.AMapLocation;

/**
 * 地图管理类
 *
 * @author [熊超]
 * @date [2017/11/8]
 */
public final class XLocationManager {
    private volatile static XLocationManager instance;

    public static XLocationManager getInstance() {
        if (instance == null) {
            synchronized (XLocationManager.class) {
                if (instance == null) {
                    instance = new XLocationManager();
                }
            }
        }
        return instance;
    }

    private AMapLocation location;

    /**
     * 获取上一次定位的位置信息
     * @return
     */
    public AMapLocation getLocation() {
        return location;
    }
    /**
     * 设置位置信息
     * @return
     */
    public void setLocation(AMapLocation location) {
        this.location = location;
    }
}
