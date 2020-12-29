package met.hx.com.base.base.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import met.hx.com.base.R;
import met.hx.com.base.basemvp.eventbus.DialogId;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.CustomPopWindow;
import met.hx.com.librarybase.views.dialog.MDDialog;

/**
 * Created by huxu on 2017/9/19.
 */

public class LoadingDialogUtil {
    /**
     * LoadingDialogUtil.
     * 加载框
     *
     * @return
     */
    public static MDDialog createLodingDialog(Context context) {
        MDDialog mdDialog = new MDDialog.Builder(context)
                .setContentView(R.layout.base_loading_dialog)
                .setContentBackGround(R.color.base_trans)
                .setShowTitle(false)
                .setShowButtons(false)
                .create();
        return mdDialog;
    }

    public static CustomPopWindow createLoadingPop(Context context) {
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(R.layout.base_progress)//显示的布局，还可以通过设置一个View
                .setAnimationStyle(R.style.base_showPopupAnimation)
                .setFocusable(false)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) //设置显示的大小，不设置就默认包裹内容
                .create();//创建PopupWindow
        return popWindow;
    }

    public static void show(RelativeLayout loadingProgress, String text) {
        if (loadingProgress != null) {
            TextView textView = (TextView) loadingProgress.findViewById(R.id.tv);
            textView.setText(text);
            if (loadingProgress.getVisibility() == View.GONE) {
                AlphaAnimation appearAnimator = new AlphaAnimation(0, 1);
                appearAnimator.setDuration(500);
                loadingProgress.startAnimation(appearAnimator);
                loadingProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示（调用的时候）
     *
     * @param text
     */
    public static void showByEvent(String text, String activityTag) {
        if (!XHStringUtil.isEmpty(activityTag, false)) {
            EventBus.getDefault().post(new DialogId(DialogId.TYPE_SHOW, text, activityTag));
        }
    }

    /**
     * 是否显示蓝牙超时提示
     * @param showTypeFirst 为true时显示超时提示
     * @param text 提示文字
     * @param activityTag  界面标识
     */
    public static void showByEvent(boolean showTypeFirst, String text, String activityTag) {
        if (!XHStringUtil.isEmpty(activityTag, false)) {
            EventBus.getDefault().post(new DialogId(DialogId.TYPE_SHOW, showTypeFirst, text, activityTag));
        }
    }

    /**
     * 不显示蓝牙超时提示，添加加载框消失时间
     * @param showTypeSecond  为true时showtime生效
     * @param showTime  xx秒后加载框消失
     * @param text 提示文字
     * @param activityTag  界面标识
     */
    public static void showByEvent(boolean showTypeSecond, int showTime, String text, String activityTag) {
        if (!XHStringUtil.isEmpty(activityTag, false)) {
            EventBus.getDefault().post(new DialogId(DialogId.TYPE_SHOW,false, showTypeSecond, showTime, text, activityTag));
        }
    }

    /**
     * 隐藏（调用的时候）
     */
    public static void dismissByEvent(String activityTag) {
        if (!XHStringUtil.isEmpty(activityTag, false)) {
            EventBus.getDefault().post(new DialogId(DialogId.TYPE_DISMISS, activityTag));
        }
    }


    public static void dismiss(RelativeLayout loadingProgress) {
        if (loadingProgress != null) {
            if (loadingProgress.getVisibility() == View.VISIBLE) {
                AlphaAnimation appearAnimator = new AlphaAnimation(1, 0);
                appearAnimator.setDuration(500);
                loadingProgress.startAnimation(appearAnimator);
                loadingProgress.setVisibility(View.GONE);
            }
        }
    }

}
