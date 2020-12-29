package wu.loushanyun.com.fivemoduleapp.v.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;

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
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.LoginPhoneData;
import wu.loushanyun.com.fivemoduleapp.m.ResultJson;
import wu.loushanyun.com.fivemoduleapp.p.adapter.LoginDialogViewBinder;
import wu.loushanyun.com.fivemoduleapp.p.runner.CheckNumberRunner;
import wu.loushanyun.com.fivemoduleapp.p.runner.LoginRunnerPhone;
import wu.loushanyun.com.fivemoduleapp.p.runner.SendNoteCodeRunner;
import wu.loushanyun.com.libraryfive.m.LoginData;
import wu.loushanyun.com.module_initthree.init.InitCode;

@Route(path = K.LoginActivityFivePhone)
public class LoginActivityPhone extends BaseNoPresenterActivity {
    private RoundTextView textManager;
    private RoundTextView textProduct;
    private LinearLayout linearLogin;
    private EditText editAccount;
    private EditText editYanzhengma;
    private RoundTextView textGetyanzhengma;
    private ImageView imageButton;
    private ImageView textWeb;
    private TextView textBanben;

    //底部显示图片的标识
    private boolean imgFlag = true;
    private String accountTest;
    private String accountOffice;
    private String yanzhengma;
    private int seconds = 60;
    private Disposable disposable;
    private boolean canSend = true;

    private MDDialog mdDialog;
    private RecyclerView recycleLoginItem;
    private LoginDialogViewBinder loginDialogViewBinder;
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        registerLifeCycle(loginDialogViewBinder = new LoginDialogViewBinder());
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.LoginRunnerPhone, new LoginRunnerPhone());
        registerEventRunner(InitCode.SendNoteCode, new SendNoteCodeRunner());
        registerEventRunner(InitCode.CheckNumberRunner, new CheckNumberRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.LoginRunnerPhone) {
            if (event.isSuccess()) {
                LoginPhoneData loginPhoneData = (LoginPhoneData) event.getReturnParamAtIndex(0);
                int err_code = loginPhoneData.getCode();
                if (err_code == -1) {
                    ToastManager.getInstance(this).show("验证码输入错误！！！");
                } else if (err_code == 0) {
                    List<LoginData> productRegisters = loginPhoneData.getData();
                    multiTypeAdapter.setItems(productRegisters);
                    multiTypeAdapter.notifyDataSetChanged();
                    mdDialog.show();
                } else {
                    sendMessageToast(loginPhoneData.getMsg());
                }
            }
        } else if (code == InitCode.CheckNumberRunner) {
            if (event.isSuccess()) {
                String phoneNumber = (String) event.getParamAtIndex(0);
                ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
                if (resultJson.getCode() == 0) {
                    if (canSend) {
                        pushEvent(InitCode.SendNoteCode, phoneNumber);
                    } else {
                        ToastManager.getInstance(this).show("60s内不能重复发送");
                    }
                } else {
                    ToastUtils.showLong(resultJson.getMsg());
                }
            }
        } else if (code == InitCode.SendNoteCode) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mdDialog.isShowing()) {
            mdDialog.dismiss();
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_login_activity2;
    }

    @Override
    protected void initView() {
        textManager = (RoundTextView) findViewById(R.id.text_manager);
        textProduct = (RoundTextView) findViewById(R.id.text_product);
        linearLogin = (LinearLayout) findViewById(R.id.linear_login);
        editAccount = (EditText) findViewById(R.id.edit_account);
        editYanzhengma = (EditText) findViewById(R.id.edit_yanzhengma);
        textGetyanzhengma = (RoundTextView) findViewById(R.id.text_getyanzhengma);
        imageButton = (ImageView) findViewById(R.id.image_button);
        textWeb = (ImageView) findViewById(R.id.text_web);
        textBanben = (TextView) findViewById(R.id.text_banben);

        textBanben.setText("版本号：" + AppUtils.getAppVersionName());
        View view1 = getLayoutInflater().inflate(R.layout.m_five_dialog, null);
        mdDialog = new MDDialog.Builder(this).setContentView(view1).setShowTitle(false).setShowButtons(false).create();
        loginDialogViewBinder.setMdDialog(mdDialog);
        recycleLoginItem = (RecyclerView) view1.findViewById(R.id.recycle_login_item);
        multiTypeAdapter = new MultiTypeAdapter();

        multiTypeAdapter.register(LoginData.class, loginDialogViewBinder);
        recycleLoginItem.setAdapter(multiTypeAdapter);

        editAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (imgFlag) {
                    accountTest = charSequence.toString();
                } else {
                    accountOffice = charSequence.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textGetyanzhengma.setOnClickListener(v -> {
            String phoneNumber = editAccount.getText().toString();
            if (!XHStringUtil.isEmpty(phoneNumber, false)) {
                boolean isMobile = RegexUtils.isMobileSimple(phoneNumber);
                if (isMobile) {
                    pushEvent(InitCode.CheckNumberRunner, phoneNumber);
                } else {
                    ToastManager.getInstance(this).show("输入的不是手机号，请重新输入");
                }
            } else {
                sendMessageToast("请输入手机号");
            }

        });
        imageButton = (ImageView) findViewById(R.id.image_button);
        textWeb = (ImageView) findViewById(R.id.text_web);
        imageButton.setOnClickListener(view -> {
            if (!imgFlag) {
                ToastManager.getInstance(this).show("暂不支持");
                return;
            }
            String uid = editAccount.getText().toString();
            String pwd = editYanzhengma.getText().toString();
            if (XHStringUtil.isEmpty(uid, false)) {
                ToastManager.getInstance(this).show("请输入账号");
                return;
            }
            if (XHStringUtil.isEmpty(pwd, false)) {
                ToastManager.getInstance(this).show("请输入密码");
                return;
            }
            pushEvent(InitCode.LoginRunnerPhone, uid, pwd);
            if (imgFlag) {
                saveAccountTest();
            } else {
                saveAccountOffice();
            }
        });

        textWeb.setOnClickListener(view -> {
            if (imgFlag) {
                ARouter.getInstance().build(C.WEB).withString("url", URLUtils.getIP() + URLUtils.IPManager)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withInt("onProgressColor", R.color.l_five_Q)
                        .withBoolean("canBack", true)
                        .navigation();
            } else {
                ARouter.getInstance().build(C.WEB).withString("url", URLUtils.getIP() + URLUtils.IPProduct)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withInt("onProgressColor", R.color.l_five_Q)
                        .withBoolean("canBack", true)
                        .navigation();
            }

        });
        textManager.setOnClickListener(view -> {
            textManager.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
            textProduct.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            textWeb.setImageResource(R.drawable.dsj);
            imgFlag = true;
            editAccount.setText(accountTest);
            editYanzhengma.setText(yanzhengma);
            linearLogin.setVisibility(View.VISIBLE);
        });
        textProduct.setOnClickListener(view -> {
            textProduct.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
            textManager.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            textWeb.setImageResource(R.drawable.wlw);
            imgFlag = false;
            editAccount.setText(accountOffice);
            editYanzhengma.setText(yanzhengma);
            linearLogin.setVisibility(View.GONE);
        });
        readAccountTest();
        readAccountOffice();
        editAccount.setText(accountTest);
    }


    private void saveAccountTest() {
        AbSharedUtil.putString(this, "accountTest_phone", accountTest);
    }

    private void readAccountTest() {
        accountTest = AbSharedUtil.getString(this, "accountTest_phone");
    }

    private void saveAccountOffice() {
        AbSharedUtil.putString(this, "accountOffice_phone", accountOffice);
    }

    private void readAccountOffice() {
        accountOffice = AbSharedUtil.getString(this, "accountOffice_phone");
    }
}
