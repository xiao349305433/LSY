package com.loushanyun.modulefactory.v.activity;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.loushanyun.modulefactory.BuildConfig;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.FactoryLoginData;
import com.loushanyun.modulefactory.m.ProductRegister;
import com.loushanyun.modulefactory.p.adapter.LoginDialogViewBinder;
import com.loushanyun.modulefactory.p.runner.CheckNumberRunner;
import com.loushanyun.modulefactory.p.runner.LoginRunnerPhone;
import com.loushanyun.modulefactory.p.runner.SendNoteCodeRunner;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.url.UrlController;

import java.util.List;

import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.RegexUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;

@Route(path = K.LoginActivityPhone)
public class LoginActivityPhone extends BaseNoPresenterActivity implements View.OnClickListener {
    private TextView textBanben;
    private TextView textTest;
    private TextView textOffice;
    private EditText editAccount;
    private EditText editYanzhengma;
    private RoundTextView textGetyanzhengma;
    private RoundTextView textLogin;
    private RoundTextView textFreeLogin;
    private TextView textIp;
    private ImageView textWeb;
    private int seconds = 60;
    private Disposable disposable;
    private boolean canSend = true;
    private String phoneNumberTest;
    private String yanzhengma;
    private String phoneNumberOffice;
    private boolean isTest = true;

    private MDDialog mdDialog;
    private RecyclerView recycleLoginItem;
    private LoginDialogViewBinder loginDialogViewBinder;
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void initEventListener() {
        registerEventRunner(FactoryCode.LoginCodePhone, new LoginRunnerPhone());
        registerEventRunner(FactoryCode.CheckNumberRunner, new CheckNumberRunner());
        registerEventRunner(FactoryCode.SendNoteCode, new SendNoteCodeRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == FactoryCode.LoginCodePhone) {
            if (event.isSuccess()) {
                FactoryLoginData factoryLoginData = (FactoryLoginData) event.getReturnParamAtIndex(0);
                String err_code = factoryLoginData.getCode();
                if ("-1".equals(err_code)) {
                    ToastManager.getInstance(this).show("验证码输入错误！！！");
                } else if ("0".equals(err_code)) {
                    List<ProductRegister> productRegisters = factoryLoginData.getData();
                    multiTypeAdapter.setItems(productRegisters);
                    multiTypeAdapter.notifyDataSetChanged();
                    mdDialog.show();
//                    if (loginData != null) {
//                        LoginParamManager.getInstance().setProductRegister(aLoginAnalysis.getData());
//                        ARouter.getInstance().build(K.FEquipmentListActivity).navigation();
//                        ToastManager.getInstance(this).show("登录成功！");
//                    } else {
//                        ToastManager.getInstance(this).show("登录失败！请查看手机号是否已注册");
//                    }

                }
            }
        } else if (code == FactoryCode.CheckNumberRunner) {
            if (event.isSuccess()) {
                String phoneNumber = (String) event.getParamAtIndex(0);
                String result = (String) event.getReturnParamAtIndex(0);
                if ("0".equals(result)) {
                    ToastUtils.showShort("手机号未注册");
                } else {
                    if (canSend) {
                        pushEvent(FactoryCode.SendNoteCode, phoneNumber);
                    } else {
                        ToastUtils.showShort("60s内不能重复发送");
                    }
                }
            }
        } else if (code == FactoryCode.SendNoteCode) {
            if (event.isSuccess()) {
                compositeDisposable.add(disposable = FlowableUtil.intervalRxTimer(1000, new RxTimerListener() {
                    @Override
                    public void onNext(@NonNull Long number) {
                        seconds--;
                        if (seconds == 0) {
                            seconds = 60;
                            canSend = true;
                            textGetyanzhengma.setText("发送验证码");
                            disposable.dispose();
                        } else {
                            canSend = false;
                            textGetyanzhengma.setText(seconds + "s");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
            } else {
                ToastManager.getInstance(this).show("发送验证码失败，请重新发送");
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_login2;
    }

    @Override
    protected void initView() {
        XActivityUtils.finishAllActivitiesExceptNewest();
        textBanben = (TextView) findViewById(R.id.text_banben);
        textTest = (TextView) findViewById(R.id.text_test);
        textOffice = (TextView) findViewById(R.id.text_office);
        editAccount = (EditText) findViewById(R.id.edit_account);
        editYanzhengma = (EditText) findViewById(R.id.edit_yanzhengma);
        textGetyanzhengma = (RoundTextView) findViewById(R.id.text_getyanzhengma);
        textLogin = (RoundTextView) findViewById(R.id.text_login);
        textFreeLogin = (RoundTextView) findViewById(R.id.text_free_login);
        textIp = (TextView) findViewById(R.id.text_ip);
        textWeb = (ImageView) findViewById(R.id.text_web);


        textBanben.setText("版本：" + AppUtils.getAppVersionName());
        View view=getLayoutInflater().inflate(R.layout.m_factory_dialog,null);
        mdDialog = new MDDialog.Builder(this).setContentView(view).setShowTitle(false).setShowButtons(false).create();
        recycleLoginItem = (RecyclerView) view.findViewById(R.id.recycle_login_item);
        multiTypeAdapter=new MultiTypeAdapter();
        loginDialogViewBinder=new LoginDialogViewBinder();
        multiTypeAdapter.register(ProductRegister.class,loginDialogViewBinder);
        recycleLoginItem.setAdapter(multiTypeAdapter);

        textGetyanzhengma.setOnClickListener(v -> {
            String phoneNumber = editAccount.getText().toString();
            if (!XHStringUtil.isEmpty(phoneNumber, false)) {
                boolean isMobile = RegexUtils.isMobileSimple(phoneNumber);
                if (isMobile) {
                    pushEvent(FactoryCode.CheckNumberRunner, phoneNumber);
                } else {
                    ToastManager.getInstance(this).show("输入的不是手机号，请重新输入");
                }
            } else {
                sendMessageToast("请输入手机号");
            }

        });
        editAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isTest) {
                    phoneNumberTest = charSequence.toString();
                } else {
                    phoneNumberOffice = charSequence.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editYanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                yanzhengma = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textWeb.setOnClickListener(view1 -> {
            ARouter.getInstance().build(C.WEB).withString("url", URLUtils.getIP() + URLUtils.IPProduct)
                    .withBoolean("hasTitle", true)
                    .withBoolean("hasProgress", true)
                    .withInt("onProgressColor", R.color.base_Q1)
                    .withBoolean("canBack", true)
                    .navigation();
        });
        textTest.setOnClickListener(this);
        textOffice.setOnClickListener(this);
        textLogin.setOnClickListener(v -> {
            if (XHStringUtil.isEmpty(editAccount.getText().toString(), false)) {
                ToastManager.getInstance(this).show("请输入手机号");
                return;
            }
            if (XHStringUtil.isEmpty(editYanzhengma.getText().toString(), false)) {
                ToastManager.getInstance(this).show("请输入验证码");
                return;
            }
            pushEvent(FactoryCode.LoginCodePhone, editAccount.getText().toString(), editYanzhengma.getText().toString());
            if (isTest) {
                saveAccountTest();
            } else {
                saveAccountOffice();
            }
        });
        textFreeLogin.setOnClickListener(v -> {
            freeLogin();
        });
        readAccountTest();
        readAccountOffice();
        if (BuildConfig.DEBUG) {
            setTestType();
        } else {
            setReleaseType();
        }
    }

    private void freeLogin() {
        if (LoginParamManager.getInstance().compareTime()) {
            if (LoginParamManager.getInstance().getProductRegister() != null) {
                ARouter.getInstance().build(K.FEquipmentListActivity).navigation();
                sendMessageToast("免密登录成功，请注意账号与业务是否匹配！！不匹配请切换登录账号");
            } else {
                sendMessageToast("登录数据已过期，请重新登录");
            }
        } else {
            sendMessageToast("登录数据已过期，请重新登录");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mdDialog.isShowing()){
            mdDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_test:
                setTestType();
                break;
            case R.id.text_office:
                setReleaseType();
                break;
        }
    }

    private void setReleaseType() {
        isTest = false;
        textOffice.setTextColor(Color.parseColor("#3597ff"));
        textTest.setTextColor(Color.parseColor("#999999"));
        editAccount.setText(phoneNumberOffice);
        editYanzhengma.setText(yanzhengma);
        UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostOFFICIAL);
        textIp.setText(URLUtils.getHost());
    }

    private void setTestType() {
        isTest = true;
        textOffice.setTextColor(Color.parseColor("#999999"));
        textTest.setTextColor(Color.parseColor("#3597ff"));
        editAccount.setText(phoneNumberTest);
        editYanzhengma.setText(yanzhengma);
        UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostTEST);
        textIp.setText(URLUtils.getHost());
    }

    private void saveAccountTest() {
        AbSharedUtil.putString(this, "accountTest_phone", phoneNumberTest);
    }

    private void readAccountTest() {
        phoneNumberTest = AbSharedUtil.getString(this, "accountTest_phone");
    }

    private void saveAccountOffice() {
        AbSharedUtil.putString(this, "accountOffice_phone", phoneNumberOffice);
    }

    private void readAccountOffice() {
        phoneNumberOffice = AbSharedUtil.getString(this, "accountOffice_phone");
    }
}
