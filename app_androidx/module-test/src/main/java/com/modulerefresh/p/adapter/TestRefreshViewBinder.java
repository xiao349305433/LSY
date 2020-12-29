package com.modulerefresh.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.modulerefresh.init.RefreshEventCode;
import com.test1moudle.R;
import com.test1moudle.p.runner.GeneralFromLoginRunner;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;

/**
 * Created by huxu on 2017/11/15.
 */

public class TestRefreshViewBinder extends ItemViewBinder<Integer, TestRefreshViewBinder.TestRefreshHolder> {

    @Override
    protected void initEventRunner() {
        registerEventRunner(RefreshEventCode.HTTP_Test, new GeneralFromLoginRunner());
    }

    @NonNull
    @Override
    protected TestRefreshHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_test_refresh_item, parent, false);
        return new TestRefreshHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull TestRefreshHolder holder, @NonNull Integer item) {
        holder.roundTextView.setText(item + "");
        LogUtils.i("复用了么");
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        super.onEventRunEnd(event, code);
        LogUtils.i("请求返回");
    }

    public class TestRefreshHolder extends RecyclerView.ViewHolder {

        private RoundTextView roundTextView;

        public TestRefreshHolder(View itemView) {
            super(itemView);
            roundTextView = itemView.findViewById(R.id.textview);
            roundTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushEvent(RefreshEventCode.HTTP_Test, "15527919058", "111111");
                    ToastManager.getInstance(getActivity()).showToast("第" + getAdapterPosition() + "个");
                }
            });
        }
    }
}
