package com.loushanyun.modulefactory.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.ProductRegister;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ToastUtils;

/**
 * 一号模组自定义协议网格视图的数据展示
 */
public class LoginDialogViewBinder extends ItemViewBinder<ProductRegister, LoginDialogViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.m_factory_item_login, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ProductRegister item) {
        holder.roundTextLogin.setText(item.getCompanyName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView relativeAll;
        private TextView roundTextLogin;

        public ViewHolder(View view) {
            super(view);
            relativeAll = (CardView) view.findViewById(R.id.relative_all);
            roundTextLogin = (TextView) view.findViewById(R.id.round_text_login);
            relativeAll.setOnClickListener(v -> {
                try {
                    ProductRegister productRegister=getContentByViewHolder(this);
                    LoginParamManager.getInstance().setProductRegister(new Gson().toJson(productRegister));
                    ARouter.getInstance().build(K.FEquipmentListActivity).navigation();
                    ToastUtils.showShort("登录成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }
    }
}
