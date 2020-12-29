package met.hx.com.base.base.multitype;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import met.hx.com.base.R;
import met.hx.com.base.base.basebinder.BaseItemViewBinder;
import met.hx.com.base.base.basebinder.ChildContentHolder;


/**
 * Created by huxu on 2017/6/14.
 * @author huxu
 */

public class NoViewBinder extends BaseItemViewBinder<Bundle, NoViewBinder.ViewHolder> {


    public NoViewBinder(Context context) {
        super(context);
    }

    @Override
    protected ChildContentHolder onCreateContentChildHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View contentView = inflater.inflate(R.layout.base_binder_no, parent, false);
        return new ViewHolder(contentView);
    }

    @Override
    protected void onBindContentChildHolder(@NonNull ViewHolder holder, @NonNull Bundle bundle) {

    }




    static class ViewHolder extends ChildContentHolder {

        ViewHolder(View view) {
            super(view);
        }
    }
}
