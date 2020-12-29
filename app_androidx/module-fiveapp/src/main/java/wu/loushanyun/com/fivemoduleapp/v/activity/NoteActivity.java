package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.flyco.roundview.RoundTextView;
import com.jkb.vcedittext.VerificationCodeEditText;

import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.p.runner.CheckNoteCodeRunner;
import wu.loushanyun.com.fivemoduleapp.p.runner.SendNoteCodeRunner;
import wu.loushanyun.com.module_initthree.init.InitCode;

public class NoteActivity extends BaseNoPresenterActivity {
    @Autowired
    public String phoneNumber;
    private TextView mPhoneNumber;
    private VerificationCodeEditText mEditCode;
    private RoundTextView mTextNext;
    private TextView mTextSeconds;
    private TextView mTextSend;

    private int seconds = 60;
    private boolean canSend = true;
    private Disposable disposable;

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.SendNoteCode, new SendNoteCodeRunner());
        registerEventRunner(InitCode.CheckNoteCodeRunner, new CheckNoteCodeRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.SendNoteCode) {
            if (event.isSuccess()) {
                compositeDisposable.add(disposable = FlowableUtil.intervalRxTimer(1000, new RxTimerListener() {
                    @Override
                    public void onNext(@NonNull Long number) {
                        seconds--;
                        if (seconds == 0) {
                            seconds = 60;
                            canSend = true;
                            mTextSend.setTextColor(getResources().getColor(R.color.base_Q1));
                            mTextSeconds.setText(seconds + "s");
                            disposable.dispose();
                        } else {
                            canSend = false;
                            mTextSend.setTextColor(getResources().getColor(R.color.m_five_gray));
                            mTextSeconds.setText(seconds + "s");
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
        } else if (code == InitCode.CheckNoteCodeRunner) {
            if (event.isSuccess()) {
                Intent intent = new Intent(this, NewPassWordActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                KeyboardUtils.hideSoftInput(this);
                finish();
            } else {
                ToastManager.getInstance(this).show("您输入的验证码不正确，请重新输入");
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_note;
        ba.mTitleText = "找回登录密码";
    }

    @Override
    protected void initView() {
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mEditCode = (VerificationCodeEditText) findViewById(R.id.edit_code);
        mTextNext = (RoundTextView) findViewById(R.id.text_next);
        mTextSeconds = (TextView) findViewById(R.id.text_seconds);
        mTextSend = (TextView) findViewById(R.id.text_send);

        if (!XHStringUtil.isEmpty(phoneNumber, false)) {
            String number = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
            mPhoneNumber.setText(number);
            pushEvent(InitCode.SendNoteCode, phoneNumber);
        }
        mTextSend.setOnClickListener(v -> {
            if (canSend) {
                pushEvent(InitCode.SendNoteCode, phoneNumber);
            } else {
                ToastManager.getInstance(this).show("60s内不能重复发送");
            }
        });
        mTextNext.setOnClickListener(v -> {
            String codeInput = mEditCode.getText().toString();
            if (!XHStringUtil.isEmpty(codeInput, false)) {
                pushEvent(InitCode.CheckNoteCodeRunner, codeInput);
            }
        });

    }


}
