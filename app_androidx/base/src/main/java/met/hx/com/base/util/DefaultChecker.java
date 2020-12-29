package met.hx.com.base.util;

import org.lzh.framework.updatepluginlib.base.UpdateChecker;
import org.lzh.framework.updatepluginlib.model.Update;

import met.hx.com.librarybase.some_utils.AppUtils;

public class DefaultChecker implements UpdateChecker {
    @Override
    public boolean check(Update update) {
        // 使用本地数据与update实体类数据进行比对。判断当前版本是否需要更新
        // 返回是否需要更新此版本。true为需要更新
        if (update.getVersionCode() > AppUtils.getAppVersionCode()) {
            return true;
        }

        if ((Double.valueOf(update.getVersionName()) > Double.valueOf(AppUtils.getAppVersionName())) && update.getVersionCode() == AppUtils.getAppVersionCode()) {
            return true;
        }
        return false;
    }
}