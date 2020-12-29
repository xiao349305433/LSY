package met.hx.com.base.base.basebinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import met.hx.com.base.base.multitype.ItemViewBinder;


/**
 * Created by huxu on 2017/6/14.
 */

public abstract class BaseItemViewBinder<Content extends Object, SubViewHolder extends ChildContentHolder>
        extends ItemViewBinder<BaseBinderModel, BaseItemViewBinder.BaseHolder> {
    public Context context;

    public BaseItemViewBinder(Context context) {
        this.context = context;
    }

    protected abstract ChildContentHolder onCreateContentChildHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindContentChildHolder(
            @NonNull SubViewHolder holder, @NonNull Content content);

    protected void onChildViewAttachedToWindow(@NonNull SubViewHolder holder) {
    }

    protected void onChildViewDetachedFromWindow(@NonNull SubViewHolder holder) {
    }

    @NonNull
    @Override
    protected BaseItemViewBinder.BaseHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        LinearLayout root = new LinearLayout(inflater.getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(layoutParams);
        ChildContentHolder subViewHolder = onCreateContentChildHolder(inflater, parent);
        return new BaseHolder(root, subViewHolder);
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseHolder holder, @NonNull BaseBinderModel item) {
        onBindContentChildHolder((SubViewHolder) holder.subViewHolder, (Content) item.content);
    }

    @Override
    protected void onViewAttachedToWindow(BaseHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        onChildViewAttachedToWindow((SubViewHolder) viewHolder.subViewHolder);
    }

    @Override
    protected void onViewDetachedFromWindow(BaseHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        onChildViewDetachedFromWindow((SubViewHolder) viewHolder.subViewHolder);
    }


    /**
     * 获取当前位置的数据
     * @param baseHolder
     * @return
     */
    protected Content getPositionContent(RecyclerView.ViewHolder baseHolder) throws Exception {
        BaseBinderModel baseBinderModel = getContentByViewHolder(baseHolder);
        return (Content) baseBinderModel.content;
    }


    public static class BaseHolder extends RecyclerView.ViewHolder {
        public ChildContentHolder subViewHolder;

        public BaseHolder(LinearLayout itemView, ChildContentHolder subViewHolder) {
            super(itemView);
            subViewHolder.baseHolder = this;
            this.subViewHolder = subViewHolder;
            itemView.addView(subViewHolder.itemView);
        }
    }
}
