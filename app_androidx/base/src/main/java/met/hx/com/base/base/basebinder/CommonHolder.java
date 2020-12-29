package met.hx.com.base.base.basebinder;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by huxu on 2017/7/4.
 */

public class CommonHolder<contentHolder extends ChildContentHolder> {
    public contentHolder frameHolder;

    public final View itemView;


    public CommonHolder(@NonNull final View itemView) {
        this.itemView = itemView;
    }

    public View findViewById(int resId) {
        return itemView.findViewById(resId);
    }
    @NonNull
    public contentHolder getParent() {
        return frameHolder;
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
