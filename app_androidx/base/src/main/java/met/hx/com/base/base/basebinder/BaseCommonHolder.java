package met.hx.com.base.base.basebinder;

import android.view.View;

/**
 * Created by huxu on 2017/7/4.
 */

public abstract  class BaseCommonHolder<Holder extends BaseCommonHolder> extends ChildContentHolder {

    public CommonHolder<Holder> subViewHolder;


    public BaseCommonHolder(View itemView, final CommonHolder<Holder> subViewHolder) {
        super(itemView);
        this.subViewHolder = subViewHolder;
        this.subViewHolder.frameHolder = (Holder) this;

    }


    public View findViewById(int resId) {
        return itemView.findViewById(resId);
    }
}
