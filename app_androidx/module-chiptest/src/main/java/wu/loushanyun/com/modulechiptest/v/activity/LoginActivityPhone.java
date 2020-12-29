package wu.loushanyun.com.modulechiptest.v.activity;

import android.Manifest;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.url.UrlController;

import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.List;

import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.base.util.DefaultChecker;
import met.hx.com.base.util.Strategy;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.RegexUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.modulechiptest.BuildConfig;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.FactoryLoginData;
import wu.loushanyun.com.modulechiptest.m.LoginInfo;
import wu.loushanyun.com.modulechiptest.m.ProductRegister;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.p.adapter.LoginDialogViewBinder;
import wu.loushanyun.com.modulechiptest.p.runner.CheckNumberRunner;
import wu.loushanyun.com.modulechiptest.p.runner.LoginRunnerPhone;
import wu.loushanyun.com.modulechiptest.p.runner.SendNoteCodeRunner;

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
    private int seconds = 60;
    private Disposable disposable;
    private boolean canSend = true;
    private String phoneNumberTest;
    private String yanzhengma;
    private String phoneNumberOffice;
    private boolean isTest = true;

    private MDDialog mdDialog;
    private RecyclerView recycleLoginItem;
    private MultiTypeAdapter multiTypeAdapter;
    private LoginDialogViewBinder loginDialogViewBinder;

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.LoginCodePhone, new LoginRunnerPhone());
        registerEventRunner(ChipCode.CheckNumberRunner, new CheckNumberRunner());
        registerEventRunner(ChipCode.SendNoteCode, new SendNoteCodeRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.LoginCodePhone) {
            if (event.isSuccess()) {
                LoginInfo loginInfo= (LoginInfo) event.getReturnParamAtIndex(0);
                int err_code = loginInfo.getCode();
                if(err_code==0){
                    if(loginInfo.getData()!=null){
                        LoginParamManager.getInstance().setLoginInfo(new Gson().toJson(loginInfo));
                        sendMessageToast(loginInfo.getMsg());
                        switch (loginInfo.getData().getMloginHomeType()){
                            case 1:
                                ARouter.getInstance().build(K.ChipHomeActivity).navigation();
                                break;
                            case 2:
                                ARouter.getInstance().build(K.ProduceHomeActivity).navigation();
                                break;
                            case 3:
                                ARouter.getInstance().build(K.CheckActivity).navigation();
                                break;
                        }

                    }else {
                        ToastManager.getInstance(this).show("登录失败！请查看手机号是否已注册");
                    }
                }else {
                    ToastManager.getInstance(this).show("验证码输入错误！！！");
                }

            }
        } else if (code == ChipCode.CheckNumberRunner) {
            if (event.isSuccess()) {
                String phoneNumber = (String) event.getParamAtIndex(0);
                ResultJson resultJson= (ResultJson) event.getReturnParamAtIndex(0);
                if(resultJson.getCode()==0){
                    if (canSend) {
                        pushEvent(ChipCode.SendNoteCode, phoneNumber);
                    } else {
                        ToastUtils.showShort("60s内不能重复发送");
                    }
                }else {
                    ToastUtils.showShort("手机号未注册");
                }

            }
        } else if (code == ChipCode.SendNoteCode) {
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
        ba.mActivityLayoutId = R.layout.m_chip_activity_login2;
    }

    @Override
    protected void initView() {
        XActivityUtils.finishAllActivitiesExceptNewest();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.INTERNET
                , Manifest.permission.CALL_PHONE
                , Manifest.permission.CAMERA
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                ToastManager.getInstance(this).show("没有权限无法进行蓝牙配置,请到安全管家设置信任此app");
                finish();
            }
        });
        textBanben = (TextView) findViewById(R.id.text_banben);
        textTest = (TextView) findViewById(R.id.text_test);
        textOffice = (TextView) findViewById(R.id.text_office);
        editAccount = (EditText) findViewById(R.id.edit_account);
        editYanzhengma = (EditText) findViewById(R.id.edit_yanzhengma);
        textGetyanzhengma = (RoundTextView) findViewById(R.id.text_getyanzhengma);
        textLogin = (RoundTextView) findViewById(R.id.text_login);
        textFreeLogin = (RoundTextView) findViewById(R.id.text_free_login);
        textIp = (TextView) findViewById(R.id.text_ip);


        textBanben.setText("版本：" + AppUtils.getAppVersionName());
        View view=getLayoutInflater().inflate(R.layout.m_chip_dialog,null);
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
                    pushEvent(ChipCode.CheckNumberRunner, phoneNumber);
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

            pushEvent(ChipCode.LoginCodePhone, editAccount.getText().toString(), editYanzhengma.getText().toString());
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
        setCrashId();
        createBuilder().check();
    }

    public void setCrashId() {
        if (wu.loushanyun.com.libraryfive.BuildConfig.DEBUG) {
            CrashReport.setUserId(String.valueOf("模组测试") + ",net,Debug");
        } else {
            CrashReport.setUserId(String.valueOf("模组测试") + ",net,Release");
        }
    }
    private void freeLogin() {
        if (LoginParamManager.getInstance().compareTime()) {
            if (LoginParamManager.getInstance().getLoginInfo() != null) {
                switch (LoginParamManager.getInstance().getLoginInfo().getData().getMloginHomeType()){
                    case 1:
                        ARouter.getInstance().build(K.ChipHomeActivity).navigation();
                        break;
                    case 2:
                        ARouter.getInstance().build(K.ProduceHomeActivity).navigation();
                        break;
                    case 3:
                        ARouter.getInstance().build(K.CheckActivity).navigation();
                        break;
                }
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

    @NonNull
    private UpdateBuilder createBuilder() {
        UpdateBuilder builder = UpdateBuilder.create(createNewConfig());
        builder.setUpdateChecker(new DefaultChecker());
        builder.setUpdateStrategy(new Strategy());
        return builder;
    }

    private UpdateConfig createNewConfig() {
        return UpdateConfig.createConfig()
                .setUrl(URLUtils.MChipUpdate)
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String httpResponse) throws Exception {
                        XLog.i("更新返回的数据=" + httpResponse);
                        JSONObject object = new JSONObject(httpResponse);
                        Update update = new Update();
                        // 此apk包的下载地址
                        update.setUpdateUrl(object.optString("update_url"));
                        // 此apk包的版本号
                        update.setVersionCode(object.optInt("update_ver_code"));
                        // 此apk包的版本名称
                        update.setVersionName(object.optString("update_ver_name"));
                        // 此apk包的更新内容
                        update.setUpdateContent(object.optString("update_content"));
                        // 此apk包是否为强制更新
                        update.setForced(object.optBoolean("update_forced", false));
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(object.optBoolean("ignore_able", false));
                        return update;
                    }
                });
    }
}
