package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.widget.EditText;

import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;

import wu.loushanyun.com.fivemoduleapp.p.runner.UpdateLoginPassWordRunner;
import  wu.loushanyun.com.module_initthree.init.InitCode;

public class NewPassWordActivity extends BaseNoPresenterActivity {

    public String phoneNumber;
    private EditText mEditPassword;
    private EditText mEditPasswordSure;
    private RoundTextView mTextNext;

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.UpdateLoginPassWordRunner, new UpdateLoginPassWordRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if(code==InitCode.UpdateLoginPassWordRunner){
            if(event.isSuccess()){
                finish();
                ToastManager.getInstance(this).show("密码重置成功，请重新登录");
            }
        }

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_new_password;
        ba.mTitleText = "找回登录密码";
    }

    @Override
    protected void initView() {
        phoneNumber=getIntent().getStringExtra("phoneNumber");
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mEditPasswordSure = (EditText) findViewById(R.id.edit_password_sure);
        mTextNext = (RoundTextView) findViewById(R.id.text_next);
        mTextNext.setOnClickListener(v -> {
            String passWord = mEditPassword.getText().toString();
            String passwordSure = mEditPasswordSure.getText().toString();
            if (XHStringUtil.isEmpty(passWord, false)) {
                ToastManager.getInstance(this).show("请输入新密码");
                return;
            }
            if (XHStringUtil.isEmpty(passwordSure, false)) {
                ToastManager.getInstance(this).show("请输入确认密码");
                return;
            }
            if(!passWord.equals(passwordSure)){
                ToastManager.getInstance(this).show("密码输入不一致，请重新输入");
                return;
            }
            pushEvent(InitCode.UpdateLoginPassWordRunner,phoneNumber,passWord);
        });
    }
}
