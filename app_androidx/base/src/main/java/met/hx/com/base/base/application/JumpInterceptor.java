package met.hx.com.base.base.application;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import met.hx.com.base.util.NoDoubleClickUtils;

/**
 * 拦截器（限制连续点击跳转）priority越小优先级越高
 */
@Interceptor(priority = 1)
public class JumpInterceptor implements IInterceptor {

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            callback.onContinue(postcard);
        } else {
            callback.onInterrupt(new RuntimeException("未超过一秒点击了跳转"));      // 觉得有问题，中断路由流程
        }

    }

    @Override
    public void init(Context context) {
        NoDoubleClickUtils.initLastClickTime();
    }
}
