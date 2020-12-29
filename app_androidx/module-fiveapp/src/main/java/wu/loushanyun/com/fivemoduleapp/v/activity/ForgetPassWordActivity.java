package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import android.widget.EditText;

import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.RegexUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.p.runner.CheckNumberRunner;
import wu.loushanyun.com.module_initthree.init.InitCode;

public class ForgetPassWordActivity extends BaseNoPresenterActivity {
    private EditText editNumber;
    private RoundTextView textNext;

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.CheckNumberRunner,new CheckNumberRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if(code==InitCode.CheckNumberRunner){
            if(event.isSuccess()){
                String phoneNumber = (String) event.getParamAtIndex(0);
                Intent intent = new Intent(this,NoteActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                finish();
            }else {
                ToastManager.getInstance(this).show("手机号未注册，请重新输入");
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_forgetpass;
        ba.mTitleText="找回登录密码";
    }

    @Override
    protected void initView() {
        editNumber = (EditText) findViewById(R.id.edit_number);
        textNext = (RoundTextView) findViewById(R.id.text_next);
        textNext.setOnClickListener(v -> {
            String phoneNumber = editNumber.getText().toString();
            if (!XHStringUtil.isEmpty(phoneNumber, false)) {
                boolean isMobile = RegexUtils.isMobileSimple(phoneNumber);
                if (isMobile) {
                    pushEvent(InitCode.CheckNumberRunner,phoneNumber);
                } else {
                    ToastManager.getInstance(this).show("输入的不是手机号，请重新输入");
                }
            }

        });
    }
}