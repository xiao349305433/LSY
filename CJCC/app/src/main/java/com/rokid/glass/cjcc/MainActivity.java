package com.rokid.glass.cjcc;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.ui.dialog.GlassDialog;
import com.rokid.glass.ui.dialog.GlassDialogListener;
import com.rokid.glass.videorecorder.manager.FloatWindowManager;
import com.rokid.glass.videorecorder.utils.Logger;

public class MainActivity extends InstructionActivity {

    private GlassDialog mGlassDialog;
    private InstructConfig mInstructConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkRecord(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkRecord(false);
    }

    @Override
    public InstructConfig configInstruct() {
        if(mInstructConfig != null){
            return mInstructConfig;
        }
        mInstructConfig = new InstructConfig();
        mInstructConfig.setActionKey(getClass().getName() + InstructConfig.ACTION_SUFFIX);
        mInstructConfig.addInstructEntity(new InstructEntity()
                .addEntityKey(new EntityKey(Constant.OfflineVoice.GLOBAL_START_RECORD, null))
                .addEntityKey(new EntityKey(EntityKey.Language.en, Constant.OfflineVoice_EN.GLOBAL_START_RECORD))
                .setCallback(new IInstructReceiver() {
                    @Override
                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                        checkRecord(true);
                    }
                }));
        mInstructConfig.addInstructEntity(new InstructEntity()
                .addEntityKey(new EntityKey(Constant.OfflineVoice.GLOBAL_STOP_RECORD, null))
                .addEntityKey(new EntityKey(EntityKey.Language.en, Constant.OfflineVoice_EN.GLOBAL_STOP_RECORD))
                .setCallback(new IInstructReceiver() {
                    @Override
                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                        checkRecord(false);
                    }
                }));
        return mInstructConfig;
    }

    @Override
    public boolean doReceiveCommand(String command) {
        return false;
    }
    private void checkRecord(boolean isSartRecord) {
        if (isSartRecord) {
            if (!FloatWindowManager.getInstance().isRecording())
                FloatWindowManager.getInstance().checkRecord();

        } else {
            if (FloatWindowManager.getInstance().isRecording())
                FloatWindowManager.getInstance().checkRecord();
        }
    }

    protected void exitAppDialog(){
        Logger.d("exitAppDialog ----->is called");
        if(mGlassDialog != null && mGlassDialog.isShowing()){
            Logger.d("exitAppDialog -----> dismiss exit dialog");
            mGlassDialog.dismiss();
            return;
        }
        mGlassDialog = new GlassDialog.CommonDialogBuilder(this)
                .setConfirmText(getResources().getString(R.string.s_yes))
                .setCancelText(getResources().getString(R.string.s_no))
                .setTitle(getResources().getString(R.string.s_check_exit_app))
                .setCancelable(true)
                .setConfirmListener(new GlassDialogListener() {
                    @Override
                    public void onClick(View view) {
                        mGlassDialog.dismiss();
                        MainActivity.this.finish();
                    }
                })

                .create();
        Logger.d("exitAppDialog -----> show exit dialog");
        mGlassDialog.show();
    }
}
