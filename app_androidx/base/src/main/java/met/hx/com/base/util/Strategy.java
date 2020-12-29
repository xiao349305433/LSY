package met.hx.com.base.util;

import org.lzh.framework.updatepluginlib.base.UpdateStrategy;
import org.lzh.framework.updatepluginlib.model.Update;

public class Strategy implements UpdateStrategy {
    @Override
    public boolean isShowUpdateDialog(Update update) {
        // 在检查到有更新时，是否显示弹窗通知。
        return true;
    }

    @Override
    public boolean isAutoInstall() {    
        // 在下载完成后。是否自动进行安装
        return false;
    }

    @Override
    public boolean isShowDownloadDialog() {
        // 在下载过程中，是否显示下载进度通知
        return true;
    }
}