package com.wu.loushanyun.basemvp.v.activity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.SensoroDeviceSession;
import com.sensoro.sensor.kit.callback.ConnectionCallback;
import com.sensoro.sensor.kit.callback.WriteCallback;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;

import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseinterface.RxTimerListener;

/**
 * s芯片sdk封装基类
 */
public abstract class BaseBlueToothActivity extends BaseNoPresenterActivity  {
    //470芯片变量
    protected SensoroDevice sensoroDevice;
    protected SensoroDeviceSession sensoroDeviceSession;
    protected String loadingTag = this.getClass().getName();
    protected static final int TIME_OUT=10000;//操作超时
    protected static final int WRITE_TIME_DELAY=500;//发送命令延迟
    protected static final int CONNECT_TIME_DELAY=1500;//连接蓝牙延迟
    protected static final int BLUE_SEND_TIME_OUT =36000;//强制发送等待时间
    protected static final int READ_BIAO_TIME_OUT =20000;//读表等待时间


    protected ConnectionCallback connectionCallback= new ConnectionCallback() {
        @Override
        public void onConnectFailed(int i) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onConnectFailed" + i);
                onChildConnectFailed(i);
                sendMessageToast("蓝牙连接失败");
            }
        }

        @Override
        public void onConnectSuccess() {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onConnectSuccess");
                onChildConnectSuccess();
            }
        }

        @Override
        public void onNotify(byte[] bytes) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onNotify" + DataParser.byteToString(bytes));
                onChildNotify(bytes);
            }
        }
    };
    protected WriteCallback writeCallback=new WriteCallback(){

        @Override
        public void onWriteSuccess() {
            if (isTopTaskAcitivity()) {
                XLog.i("蓝牙：onWriteSuccess");
                onChildWriteSuccess();
            }
        }

        @Override
        public void onWriteFailure(int i) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onWriteFailure");
                onChildWriteFailure(i);
                sendMessageToast("蓝牙数据传送失败，请退出该界面重进或断开重新连接蓝牙");
            }
        }
    };



    @CallSuper
    @Override
    protected void initView() {
        getIntentData();
        sensoroDeviceSession = new SensoroDeviceSession(this, sensoroDevice);
    }





    /**
     * 连接
     */
    protected void connectBlueTooth() {
        connectBlueTooth(TIME_OUT);
    }

    protected void connectBlueTooth(SensoroDevice sensoroDevice) {
        connectBlueTooth(TIME_OUT,sensoroDevice);
    }

    protected void connectBlueTooth(int milliseconds) {
        SensoroBlueManager.stopSearch();
        LoadingDialogUtil.showByEvent(true, milliseconds, "SN:"+sensoroDevice.getSerialNumber()+"蓝牙连接中", loadingTag);
        compositeDisposable.add(FlowableUtil.createRxTimer(CONNECT_TIME_DELAY, new RxTimerListener() {
            @Override
            public void onNext(@NonNull Long number) {
                sensoroDeviceSession.startSession("eCkRN[r&E}ta.1kn", connectionCallback);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    protected void connectBlueTooth(int milliseconds, SensoroDevice sensoroDevice) {
        if(sensoroDevice==null){
            sendMessageToast("请选择蓝牙设备");
            return;
        }
        SensoroBlueManager.stopSearch();
        sensoroDeviceSession.disconnect();
        sensoroDeviceSession.onSessonPause();
        sensoroDeviceSession.onSessionResume();
        sensoroDeviceSession = new SensoroDeviceSession(this, sensoroDevice);
        LoadingDialogUtil.showByEvent(true, milliseconds, "SN:"+sensoroDevice.getSerialNumber()+"蓝牙连接中", loadingTag);
        compositeDisposable.add(FlowableUtil.createRxTimer(CONNECT_TIME_DELAY, new RxTimerListener() {
            @Override
            public void onNext(@NonNull Long number) {
                sensoroDeviceSession.startSession("eCkRN[r&E}ta.1kn", connectionCallback);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    @Override
    protected void onLoadingDismissTimeOut() {
        sendMessageToast("操作超时");
    }

    /**
     * 写入
     *
     * @param data 参数
     */
    protected void write(byte[] data) {
        if (sensoroDeviceSession != null) {
            write(data, "正在写入", true, TIME_OUT);
        }

    }

    /**
     *
     * @param data  参数
     * @param loadingText 提示文字
     */
    protected void write(byte[] data, String loadingText) {
        if (sensoroDeviceSession != null) {
            write(data, loadingText, true, TIME_OUT);
        }
    }

    /**
     * 写入
     *
     * @param data 参数
     * @param loadingText 提示文字
     * @param showTypeFirst 是否消失蓝牙超时的提示（只在出厂配置app中使用）
     */
    protected void write(byte[] data, String loadingText, boolean showTypeFirst) {
        if (sensoroDeviceSession != null) {
            LoadingDialogUtil.showByEvent(showTypeFirst, loadingText, loadingTag);
            write(data, loadingText, showTypeFirst, WRITE_TIME_DELAY);
        }
    }

    /**
     *
     * @param data 参数
     * @param loadingText 提示文字
     * @param showTypeFirst 是否消失蓝牙超时的提示（只在出厂配置app中使用）
     * @param milliseconds 延迟多少后执行写入操作
     */
    protected void write(byte[] data, String loadingText, boolean showTypeFirst, long milliseconds) {
        if (sensoroDeviceSession != null) {
            LoadingDialogUtil.showByEvent(showTypeFirst, loadingText, loadingTag);
            compositeDisposable.add(FlowableUtil.createRxTimer(milliseconds, new RxTimerListener() {
                @Override
                public void onNext(@NonNull Long number) {
                    XLog.i("蓝牙：write" + DataParser.byteToString(data));
                    sensoroDeviceSession.write(data, writeCallback);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            }));
        }
    }

    /**
     *
     * @param data 参数
     * @param loadingText  提示文字
     * @param showTypeSecond 是否加入延迟消失
     * @param showTime 延迟多少秒后加载框消失
     */
    protected void write(byte[] data, String loadingText, boolean showTypeSecond, int showTime) {
        if (sensoroDeviceSession != null) {
            write(data, loadingText, showTypeSecond, showTime, WRITE_TIME_DELAY);
        }
    }

    /**
     * @param data 参数
     * @param loadingText  提示文字
     * @param showTypeSecond 是否加入延迟消失
     * @param showTime 延迟多少秒后加载框消失
     * @param milliseconds   延迟多少后执行写入操作
     */
    protected void write(byte[] data, String loadingText, boolean showTypeSecond, int showTime, long milliseconds) {
        if (sensoroDeviceSession != null) {
            LoadingDialogUtil.showByEvent(showTypeSecond, showTime, loadingText, loadingTag);
            compositeDisposable.add(FlowableUtil.createRxTimer(milliseconds, new RxTimerListener() {
                @Override
                public void onNext(@NonNull Long number) {
                    XLog.i("蓝牙：write" + DataParser.byteToString(data));
                    sensoroDeviceSession.write(data, writeCallback);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            }));
        }
    }


    /**
     * 获取前面的页面传来的数据
     */
    protected void getIntentData() {
        sensoroDevice = this.getIntent().getParcelableExtra("sensoroDevice");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.onSessionResume();
        }

    }

    /**
     * 加入生命周期方法onSessonPause！！！
     */
    @Override
    public void onPause() {
        super.onPause();
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.onSessonPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.disconnect();
        }
    }

    protected abstract void onChildConnectFailed(int i);

    protected abstract void onChildConnectSuccess();

    protected abstract void onChildNotify(byte[] bytes);

    protected abstract void onChildWriteSuccess();

    protected abstract void onChildWriteFailure(int i);
}
