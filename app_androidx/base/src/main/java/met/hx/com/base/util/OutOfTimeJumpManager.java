package met.hx.com.base.util;

import met.hx.com.base.base.application.AppContext;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

public class OutOfTimeJumpManager {

    private volatile static OutOfTimeJumpManager instance;

    public static OutOfTimeJumpManager getInstance() {
        if (instance == null) {
            synchronized (OutOfTimeJumpManager.class) {
                if (instance == null) {
                    instance = new OutOfTimeJumpManager();
                }
            }
        }
        return instance;
    }

    public String getJumpString() {
        return AbSharedUtil.getString(AppContext.getInstance().getApplication(), "JumpString");
    }

    public void setJumpString(String jumpString) {
        AbSharedUtil.putString(AppContext.getInstance().getApplication(),"JumpString",jumpString);
    }

    public void setShowDialog(boolean showDialog) {
        AbSharedUtil.putBoolean(AppContext.getInstance().getApplication(),"showDialog",showDialog);
    }

    public boolean getShowDialog() {
       return AbSharedUtil.getBoolean(AppContext.getInstance().getApplication(),"showDialog",true);
    }
}
