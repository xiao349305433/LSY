package com.wu.loushanyun.basemvp.init;

import met.hx.com.base.basemvp.init.BaseCode;

public class LSY2InitTypeCode extends BaseCode {
    public static final int TypeFromCreateLocation = ++CODE_INC;//从本地新装》附近设备新添加设备》进入
    public static final int TypeFromUpdateLocation = ++CODE_INC;//从同步娄山云》附近设备已存在本地的设备》进入
    public static final int TypeFromOnCloudUpdateLocation = ++CODE_INC;//从后期变更》附近设备新更新设备》进入
    public static final int TypeFromOnCloudInsideLocation = ++CODE_INC;//从后期变更》附近设备新添加设备》进入
    public static final int TypeFromReplace = ++CODE_INC;//从服务》检修》替换物联网端》进入
    public static final int TypeFromRead = ++CODE_INC;//从服务》检修》读取》进入
    public static final int IFiveSelectTokenRunner = ++CODE_INC;//从服务》检修》读取》进入
}
