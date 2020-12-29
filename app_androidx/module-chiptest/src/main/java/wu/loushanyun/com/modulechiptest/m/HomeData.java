package wu.loushanyun.com.modulechiptest.m;

import com.wu.loushanyun.basemvp.m.HomeDataListener;

public class HomeData {
    public String itemString;
    public String jumpPathString;
    public HomeDataListener homeDataListener;

    public HomeData(String itemString, String jumpPathString) {
        this.itemString = itemString;
        this.jumpPathString = jumpPathString;
    }

    public HomeData(String itemString, HomeDataListener homeDataListener) {
        this.itemString = itemString;
        this.homeDataListener = homeDataListener;
    }

    public HomeDataListener getHomeDataListener() {
        return homeDataListener;
    }

    public void setHomeDataListener(HomeDataListener homeDataListener) {
        this.homeDataListener = homeDataListener;
    }

    public String getItemString() {
        return itemString;
    }

    public void setItemString(String itemString) {
        this.itemString = itemString;
    }

    public String getJumpPathString() {
        return jumpPathString;
    }

    public void setJumpPathString(String jumpPathString) {
        this.jumpPathString = jumpPathString;
    }
}