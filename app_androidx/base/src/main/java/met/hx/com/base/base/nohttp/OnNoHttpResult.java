package met.hx.com.base.base.nohttp;

import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2017/8/24.
 */

public interface OnNoHttpResult<T> {

    void onSuccess(T result);

    void onFailure(Response<T> response, Exception error);
}
