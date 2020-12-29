package met.hx.com.base.base.nohttp;

import com.yanzhenjie.nohttp.BasicRequest;

/**
 * Created by huxu on 2017/9/11.
 */

public class NoHttpRequest {
    private BasicRequest request;
    private String Tag;
    private String TagRunner;

    @Override
    public String toString() {
        return "NoHttpRequest{" +
                "request=" + request +","+request.isCanceled()+
                ", Tag='" + Tag + '\'' +
                ", TagRunner='" + TagRunner + '\'' +
                '}';
    }

    public NoHttpRequest(BasicRequest request, String tag, String tagRunner) {
        this.request = request;
        Tag = tag;
        TagRunner = tagRunner;
    }

    public String getTagRunner() {
        return TagRunner;
    }

    public void setTagRunner(String tagRunner) {
        TagRunner = tagRunner;
    }


    public BasicRequest getRequest() {
        return request;
    }

    public void setRequest(BasicRequest request) {
        this.request = request;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
