package wu.loushanyun.com.sixapp.v.activity;

import android.Manifest;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
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
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.m.LoginData;
import wu.loushanyun.com.sixapp.BuildConfig;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.m.LandInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;
import wu.loushanyun.com.sixapp.p.adapter.LandDialogViewBinder;
import wu.loushanyun.com.sixapp.p.runner.CheckPhoneRunner;
import wu.loushanyun.com.sixapp.p.runner.GetCodeRunner;
import wu.loushanyun.com.sixapp.p.runner.LoginLandRunner;

@Route(path = K.SixLoginActivity)
public class SixLoginActivity extends BaseNoPresenterActivity {

    private TextView loginBanben;
    private EditText mEditYanzhengma;
    private EditText mEditAccount;
    private RoundTextView mTextGetyanzhengma;
    private ImageView mLoginImgNext;
    private boolean canSend = true;
    private Disposable disposable;
    private int seconds = 60;
    private boolean isTest = true;
    private String phoneNumberOffice;
    private String phoneNumberTest;
    private String yanzhengma;

    private MDDialog mdDialog;
    private RecyclerView recycleLoginItem;
    private LandDialogViewBinder loginDialogViewBinder;
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_six_activity_login;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        registerLifeCycle(loginDialogViewBinder = new LandDialogViewBinder());
    }

    @Override
    protected void initView() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.INTERNET
                , Manifest.permission.CAMERA
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                ToastManager.getInstance(this).show("没有权限无法进行蓝牙配置,请到安全管家设置信任此app");
                finish();
            }
        });

        mEditYanzhengma = (EditText) findViewById(R.id.edit_yanzhengma);
        mEditAccount = (EditText) findViewById(R.id.edit_account);
        mTextGetyanzhengma = (RoundTextView) findViewById(R.id.text_getyanzhengma);
        mLoginImgNext = (ImageView) findViewById(R.id.login_img_next);
        loginBanben= (TextView) findViewById(R.id.login_banben);
        loginBanben.setText("版本：" + AppUtils.getAppVersionName());
        View view1 = getLayoutInflater().inflate(R.layout.m_six_dialog, null);
        mdDialog = new MDDialog.Builder(this).setContentView(view1).setShowTitle(false).setShowButtons(false).create();
        loginDialogViewBinder.setMdDialog(mdDialog);
        recycleLoginItem = (RecyclerView) view1.findViewById(R.id.recycle_login_item);
        multiTypeAdapter = new MultiTypeAdapter();

        multiTypeAdapter.register(LandInfo.DatasBean.class, loginDialogViewBinder);
        recycleLoginItem.setAdapter(multiTypeAdapter);

        readAccountTest();
        readAccountOffice();
        if (BuildConfig.DEBUG) {
            setTestType();
        } else {
            setReleaseType();
        }

        createBuilder().check();


        mTextGetyanzhengma.setOnClickListener(v -> {
            String phoneNumber = mEditAccount.getText().toString();
            if (!XHStringUtil.isEmpty(phoneNumber, false)) {
                boolean isMobile = RegexUtils.isMobileSimple(phoneNumber);
                if (isMobile) {
                    pushEvent(SixCode.MSixCheckPhone, phoneNumber);
                } else {
                    ToastManager.getInstance(this).show("输入的不是手机号，请重新输入");
                }
            } else {
                sendMessageToast("请输入手机号");
            }

        });


        mLoginImgNext.setOnClickListener(v -> {

            if (XHStringUtil.isEmpty(mEditAccount.getText().toString(), false)) {
                ToastManager.getInstance(this).show("请输入手机号");
                return;
            }
            if (XHStringUtil.isEmpty(mEditYanzhengma.getText().toString(), false)) {
                ToastManager.getInstance(this).show("请输入验证码");
                return;
            }

            pushEvent(SixCode.MSixLand, mEditAccount.getText().toString(), mEditYanzhengma.getText().toString());
            if (isTest) {
                saveAccountTest();
            } else {
                saveAccountOffice();
            }
        });

        mEditAccount.addTextChangedListener(new TextWatcher() {
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

        mEditYanzhengma.addTextChangedListener(new TextWatcher() {
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


    private void setReleaseType() {
        isTest = false;
        mEditAccount.setText(phoneNumberOffice);
        mEditYanzhengma.setText(yanzhengma);
        UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostOFFICIAL);

    }

    private void setTestType() {
        isTest = true;
        mEditAccount.setText(phoneNumberTest);
        XLog.i("phoneNumberTest  :"+phoneNumberTest);
        mEditYanzhengma.setText(yanzhengma);
        UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostTEST);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mdDialog.isShowing()) {
            mdDialog.dismiss();
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixCheckPhone, new CheckPhoneRunner());
        registerEventRunner(SixCode.MSixGetVerificationCode, new GetCodeRunner());
        registerEventRunner(SixCode.MSixLand, new LoginLandRunner());
    }



    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == SixCode.MSixCheckPhone) {
            if (event.isSuccess()) {
                String phoneNumber = (String) event.getParamAtIndex(0);
                ResultJson resultJson= (ResultJson) event.getReturnParamAtIndex(0);
                if(resultJson.getCode()==0){
                    if (canSend) {
                        pushEvent(SixCode.MSixGetVerificationCode, phoneNumber);
                    } else {
                        ToastUtils.showShort("60s内不能重复发送");
                    }
                }else {
                    ToastUtils.showShort("手机号未注册");
                }
            }
        }else if(code==SixCode.MSixGetVerificationCode){
            if (event.isSuccess()) {
                compositeDisposable.add(disposable = FlowableUtil.intervalRxTimer(1000, new RxTimerListener() {
                    @Override
                    public void onNext(@NonNull Long number) {
                        seconds--;
                        if (seconds == 0) {
                            seconds = 60;
                            canSend = true;
                            mTextGetyanzhengma.setText("发送验证码");
                            disposable.dispose();
                        } else {
                            canSend = false;
                            mTextGetyanzhengma.setText(seconds + "s");
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
        }else if(code==SixCode.MSixLand){
            if (event.isSuccess()) {
                LandInfo loginInfo= (LandInfo) event.getReturnParamAtIndex(0);
                int err_code = loginInfo.getCode();

                if (err_code == -1) {
                    ToastManager.getInstance(this).show("验证码输入错误！！！");
                } else if (err_code == 0) {
                    List<LandInfo.DatasBean> productRegisters = loginInfo.getDatas();
                    multiTypeAdapter.setItems(productRegisters);
                    multiTypeAdapter.notifyDataSetChanged();
                    mdDialog.show();
                } else {
                    sendMessageToast(loginInfo.getMessage());
                }


//                if(err_code==0){
//                    if(loginInfo.getDatas()!=null){
//                        LoginParamManager.getInstance().setLoginInfo(new Gson().toJson(loginInfo));
//                        sendMessageToast(loginInfo.getMessage());
//                        ARouter.getInstance().build(K.SixHomeActivity).navigation();
//                    }else {
//                        ToastManager.getInstance(this).show("登录失败！请查看手机号是否已注册");
//                    }
//                }else {
//                    ToastManager.getInstance(this).show("验证码输入错误！！！");
//                }

            }

        }
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
                .setUrl(URLUtils.MSixUpdate)
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String httpResponse) throws Exception {
                        XLog.i("SIX更新返回的数据=" + httpResponse);
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
