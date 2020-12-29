package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.ResponseAnalysis;
import wu.loushanyun.com.fivemoduleapp.p.runner.LoginRunner;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.module_initthree.init.InitCode;

@Route(path = K.LoginActivityFive)
public class LoginActivity extends BaseNoPresenterActivity {
    private RoundTextView textManager;
    private RoundTextView textProduct;
    private EditText editAccount;
    private EditText editPassword;
    private CheckBox checkPassword;
    private CheckBox checkSavePassword;
    private TextView textForgetPassword;
    private ImageView imageButton;
    private ImageView textWeb;

    //底部显示图片的标识
    private boolean imgFlag=true;
    private boolean savePassWordTest;
    private boolean savePassWordOffice;
    private String accountTest;
    private String passwordTest;
    private String accountOffice;
    private String passwordOffice;
    private TextView textBanben;


    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.LoginCode,new LoginRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.LoginCode){
            if (event.isSuccess()) {
                ResponseAnalysis aLoginAnalysis = (ResponseAnalysis) event.getReturnParamAtIndex(0);
                int err_code = aLoginAnalysis.getCode();
                if (err_code == -1) {
                    ToastManager.getInstance(this).show("用户名或密码错误！！！");
                } else if (err_code == 0) {
                    ToastManager.getInstance(this).show("登录成功！");
                    LoginFiveParamManager.getInstance().setProductRegister(aLoginAnalysis.getData());
                    ARouter.getInstance().build(K.HomeActivity).navigation();
                }
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_login_activity;
    }

    @Override
    protected void initView() {
        textManager = (RoundTextView) findViewById(R.id.text_manager);
        textProduct = (RoundTextView) findViewById(R.id.text_product);
        editAccount = (EditText) findViewById(R.id.edit_account);
        textBanben = (TextView) findViewById(R.id.text_banben);
        textBanben.setText("版本号："+AppUtils.getAppVersionName());
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
        editPassword = (EditText) findViewById(R.id.edit_password);
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (imgFlag) {
                    passwordTest = charSequence.toString();
                } else {
                    passwordOffice = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkPassword = (CheckBox) findViewById(R.id.check_password);
        checkPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //如果选中，显示密码
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkSavePassword = (CheckBox) findViewById(R.id.check_save_password);
        checkSavePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (imgFlag) {
                    savePassWordTest = b;
                    saveCheckSavePasswordTest();
                    if (savePassWordTest) {
                        savePasswordTest();
                        saveAccountTest();
                    }
                } else {
                    savePassWordOffice = b;
                    saveCheckSavePasswordOffice();
                    if (savePassWordOffice) {
                        saveAccountOffice();
                        savePasswordOffice();
                    }
                }
            }
        });
        textForgetPassword = (TextView) findViewById(R.id.text_forget_password);
        textForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassWordActivity.class));
            }
        });
        imageButton = (ImageView) findViewById(R.id.image_button);
        textWeb = (ImageView) findViewById(R.id.text_web);
        imageButton.setOnClickListener(view -> {
            if(!imgFlag){
                ToastManager.getInstance(this).show("暂不支持");
                return;
            }
           String uid = editAccount.getText().toString();
            String pwd = editPassword.getText().toString();
            if (XHStringUtil.isEmpty(uid, false)) {
                ToastManager.getInstance(this).show("请输入账号");
                return;
            }
            if (XHStringUtil.isEmpty(pwd, false)) {
                ToastManager.getInstance(this).show("请输入密码");
                return;
            }
            pushEvent(InitCode.LoginCode, uid, pwd);
            if (imgFlag) {
                saveCheckSavePasswordTest();
                if (savePassWordTest) {
                    savePasswordTest();
                    saveAccountTest();
                }
            } else {
                saveCheckSavePasswordOffice();
                if (savePassWordOffice) {
                    saveAccountOffice();
                    savePasswordOffice();
                }
            }
        });

        textWeb.setOnClickListener(view -> {
            if (imgFlag) {
                ARouter.getInstance().build(C.WEB).withString("url", URLUtils.HttpHead+URLUtils.IPManager)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withInt("onProgressColor",R.color.l_five_Q)
                        .withBoolean("canBack",true)
                        .navigation();
            }else {
                ARouter.getInstance().build(C.WEB).withString("url", URLUtils.HttpHead+URLUtils.IPProduct)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withInt("onProgressColor",R.color.l_five_Q)
                        .withBoolean("canBack",true)
                        .navigation();
            }

        });
        textManager.setOnClickListener(view -> {
            textManager.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
            textProduct.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            textWeb.setImageResource(R.drawable.dsj);
            imgFlag = true;
            editAccount.setText(accountTest);
            editPassword.setText(passwordTest);
            checkSavePassword.setChecked(savePassWordTest);
        });
        textProduct.setOnClickListener(view -> {
            textProduct.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
            textManager.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            textWeb.setImageResource(R.drawable.wlw);
            imgFlag = false;
            editAccount.setText(accountOffice);
            editPassword.setText(passwordOffice);
            checkSavePassword.setChecked(savePassWordOffice);
        });
        readCheckSavePasswordTest();
        readCheckSavePasswordOffice();
        if (savePassWordTest) {
            readAccountTest();
            readPasswordTest();
        }
        if (savePassWordOffice) {
            readAccountOffice();
            readPasswordOffice();
        }
        if (imgFlag) {
            editAccount.setText(accountTest);
            editPassword.setText(passwordTest);
        } else {
            editAccount.setText(accountOffice);
            editPassword.setText(passwordOffice);
        }
        if (imgFlag) {
            checkSavePassword.setChecked(savePassWordTest);
        } else {
            checkSavePassword.setChecked(savePassWordOffice);
        }
    }

    private void saveCheckSavePasswordTest() {
        AbSharedUtil.putBoolean(this, "check_save_password_test", savePassWordTest);
    }

    private void saveCheckSavePasswordOffice() {
        AbSharedUtil.putBoolean(this, "check_save_password_office", savePassWordOffice);
    }

    private void readCheckSavePasswordTest() {
        savePassWordTest = AbSharedUtil.getBoolean(this, "check_save_password_test", false);
    }

    private void readCheckSavePasswordOffice() {
        savePassWordOffice = AbSharedUtil.getBoolean(this, "check_save_password_office", false);
    }

    private void savePasswordTest() {
        AbSharedUtil.putString(this, "passwordTest", passwordTest);
    }

    private void readPasswordTest() {
        passwordTest = AbSharedUtil.getString(this, "passwordTest");
    }

    private void savePasswordOffice() {
        AbSharedUtil.putString(this, "passwordOffice", passwordOffice);
    }

    private void readPasswordOffice() {
        passwordOffice = AbSharedUtil.getString(this, "passwordOffice");
    }

    private void saveAccountTest() {
        AbSharedUtil.putString(this, "accountTest", accountTest);
    }

    private void readAccountTest() {
        accountTest = AbSharedUtil.getString(this, "accountTest");
    }

    private void saveAccountOffice() {
        AbSharedUtil.putString(this, "accountOffice", accountOffice);
    }

    private void readAccountOffice() {
        accountOffice = AbSharedUtil.getString(this, "accountOffice");
    }
}
