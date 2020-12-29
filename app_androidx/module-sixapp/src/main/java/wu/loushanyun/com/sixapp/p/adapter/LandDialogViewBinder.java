package wu.loushanyun.com.sixapp.p.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.p.runner.ArrearsVerificationRunner;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.views.dialog.MDDialog;

import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.LoginData;
import wu.loushanyun.com.sixapp.BuildConfig;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.m.LandInfo;

/**
 * 一号模组自定义协议网格视图的数据展示
 */
public class LandDialogViewBinder extends ItemViewBinder<LandInfo.DatasBean, LandDialogViewBinder.ViewHolder> {

    private MDDialog mdDialog;

    public MDDialog getMdDialog() {
        return mdDialog;
    }

    public void setMdDialog(MDDialog mdDialog) {
        this.mdDialog = mdDialog;
    }

    @Override
    protected void initEventRunner() {
        super.initEventRunner();
        registerEventRunner(LouShanYunCode.ArrearsVerificationRunner, new ArrearsVerificationRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        super.onEventRunEnd(event, code);
        if (code == LouShanYunCode.ArrearsVerificationRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    ARouter.getInstance().build(K.SixHomeActivity).navigation();
                    ToastUtils.showShort("登录成功！");
                    LoginParamManager.getInstance().setLoginInfo(new Gson().toJson(event.getParamAtIndex(1)));
                } else {
                    if(mdDialog!=null){
                        mdDialog.show();
                    }
                    ToastUtils.showLong(msg);
                }
            }
        }
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.m_six_item_login, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LandInfo.DatasBean item) {
        holder.roundTextLogin.setText(item.getBusinessName());
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
                    LandInfo.DatasBean loginPhoneData = getContentByViewHolder(this);
//                    if(BuildConfig.DEBUG){
                        ARouter.getInstance().build(K.SixHomeActivity).navigation();
                        ToastUtils.showShort("登录成功！");
                        LoginFiveParamManager.getInstance().setProductRegister(new Gson().toJson(loginPhoneData));
//                    }else {
//                        pushEvent(LouShanYunCode.ArrearsVerificationRunner, loginPhoneData.getId() + "", loginPhoneData);
//                    }
                    if(mdDialog!=null){
                        mdDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }
    }
}
