package met.hx.com.base.basemvp.eventbus;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huxu on 2017/11/17.
 * 发送界面加载框的事件
 */

public class DialogId {
    public static final int TYPE_SHOW=1;
    public static final int TYPE_DISMISS=2;

    @IntDef({DialogId.TYPE_SHOW, DialogId.TYPE_DISMISS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
    private int type;
    private boolean showTypeFirst =true;
    private boolean showTypeSecond =false;
    private int showTime =3000;
    private String text;
    private String activityTag;

    public DialogId( @Duration int type, String text,String activityTag) {
        this.type = type;
        this.text = text;
        this.activityTag=activityTag;
    }

    public DialogId(int type, boolean showTypeFirst, String text, String activityTag) {
        this.type = type;
        this.showTypeFirst = showTypeFirst;
        this.text = text;
        this.activityTag = activityTag;
    }


    public DialogId(int type, boolean showTypeFirst, boolean showTypeSecond, int showTime, String text, String activityTag) {
        this.type = type;
        this.showTypeFirst = showTypeFirst;
        this.showTypeSecond = showTypeSecond;
        this.showTime = showTime;
        this.text = text;
        this.activityTag = activityTag;
    }

    public DialogId(@Duration int type, String activityTag) {
        this.type = type;
        this.activityTag=activityTag;
    }

    @Override
    public String toString() {
        return "DialogId{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", activityTag='" + activityTag + '\'' +
                '}';
    }

    public boolean isShowTypeSecond() {
        return showTypeSecond;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public void setShowTypeSecond(boolean showTypeSecond) {
        this.showTypeSecond = showTypeSecond;
    }

    public boolean isShowTypeFirst() {
        return showTypeFirst;
    }

    public void setShowTypeFirst(boolean showTypeFirst) {
        this.showTypeFirst = showTypeFirst;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
