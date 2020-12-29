package met.hx.com.base.base.basebinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


/**
 * Created by huxu on 2017/7/4.
 */

public abstract class CommonViewBinder<Content extends Object,SubHolder extends BaseCommonHolder> extends BaseItemViewBinder<Object,BaseCommonHolder>{


    public CommonViewBinder(Context context) {
        super(context);
    }


    protected abstract SubHolder onCreateBaseViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent, View root);

    protected abstract void onBindBaseViewHolder(
            @NonNull SubHolder holder, @NonNull Content content);

    @Override
    protected ChildContentHolder onCreateContentChildHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(onSetParentLayout(), parent, false);
        return onCreateBaseViewHolder(inflater,parent,root);
    }

    @Override
    protected void onBindContentChildHolder(@NonNull BaseCommonHolder holder, @NonNull Object object) {
        onBindBaseViewHolder((SubHolder)holder,(Content)object);
    }


    public abstract int onSetParentLayout();
    public abstract SubHolder onGetHolder(CommonHolder<SubHolder> commonHolder,View root);
}
