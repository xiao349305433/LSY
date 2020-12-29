package met.hx.com.base.base.basebinder;

import android.view.View;

import androidx.annotation.NonNull;


/**
 * Created by huxu on 2017/6/15.
 */

public class ChildContentHolder {
    public  View itemView;

    public BaseItemViewBinder.BaseHolder baseHolder;

    public ChildContentHolder(View itemView) {
        this.itemView = itemView;
    }

    @NonNull
    public  BaseItemViewBinder.BaseHolder getParent() {
        return baseHolder;
    }


    public final int getAdapterPosition() {
        return getParent().getAdapterPosition();
    }


    public final int getLayoutPosition() {
        return getParent().getLayoutPosition();
    }


    public final int getOldPosition() {
        return getParent().getOldPosition();
    }


    public final boolean isRecyclable() {
        return getParent().isRecyclable();
    }


    public final void setIsRecyclable(boolean recyclable) {
        getParent().setIsRecyclable(recyclable);
    }
}
