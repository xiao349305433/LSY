package com.test1moudle.v.views;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.elvishew.xlog.XLog;

/**
 * 自定义Behavior可以选择重写以下的几个方法有：
 * onInterceptTouchEvent()：是否拦截触摸事件
 * onTouchEvent()：处理触摸事件
 * layoutDependsOn()：确定使用Behavior的View要依赖的View的类型
 * onDependentViewChanged()：当被依赖的View状态改变时回调
 * onDependentViewRemoved()：当被依赖的View移除时回调
 * onMeasureChild()：测量使用Behavior的View尺寸
 * onLayoutChild()：确定使用Behavior的View位置
 * onStartNestedScroll()：嵌套滑动开始（ACTION_DOWN），确定Behavior是否要监听此次事件
 * onStopNestedScroll()：嵌套滑动结束（ACTION_UP或ACTION_CANCEL）
 * onNestedScroll()：嵌套滑动进行中，要监听的子 View的滑动事件已经被消费
 * onNestedPreScroll()：嵌套滑动进行中，要监听的子 View将要滑动，滑动事件即将被消费（但最终被谁消费，可以通过代码控制）
 * onNestedFling()：要监听的子 View在快速滑动中
 * onNestedPreFling()：要监听的子View即将快速滑动
 * 作者：SheHuan
 * 链接：https://www.jianshu.com/p/b987fad8fcb4
 * 来源：简书
 * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
 */
public class SampleTitleBehavior extends CoordinatorLayout.Behavior<View> {
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public SampleTitleBehavior() {
    }

    public SampleTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 使用该Behavior的View要监听哪个类型的View的状态变化。其中参数parant代表CoordinatorLayout，child代表使用该Behavior的View，dependency代表要监听的View。这里要监听RecyclerView。
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }
        XLog.i("deltaY="+deltaY+";dependency.getY()="+dependency.getY()+";child.getHeight()="+child.getHeight());
        float dy = dependency.getY() - child.getHeight();
        dy = dy < 0 ? 0 : dy;
        float y = -(dy / deltaY) * child.getHeight();
        child.setTranslationY(y);
        float alpha = 1 - (dy / deltaY);
        child.setAlpha(alpha);
        return true;
    }


}
