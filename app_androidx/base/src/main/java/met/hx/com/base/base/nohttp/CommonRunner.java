package met.hx.com.base.base.nohttp;

import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


/**
 * Created by huxu on 2017/8/4.
 */

public abstract class CommonRunner extends NoHttpRunner {

    @Override
    protected <T> Response<T> addCommonParams(int type, Response<T> response, Request<T> request) {
        switch (type) {
            default:
        }
        return response;
    }


    @Override
    protected <T> void sentJsonReport(int type, Response<T> response) {
        switch (type) {
        }
    }


}
