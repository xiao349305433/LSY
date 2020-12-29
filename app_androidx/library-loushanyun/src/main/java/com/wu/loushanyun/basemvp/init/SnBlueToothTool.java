package com.wu.loushanyun.basemvp.init;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.SensoroDeviceSession;
import com.sensoro.sensor.kit.callback.ConnectionCallback;
import com.sensoro.sensor.kit.callback.WriteCallback;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.util.DataParser;

import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.base.baseinterface.SimpleLifeCycle;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;

public class SnBlueToothTool extends SimpleLifeCycle  {
    public SensoroDeviceSession sensoroDeviceSession;
    public String loadingTag;
    public static final int TIME_OUT = 20000;//操作超时
    public int writeTimeDelay = 500;//发送命令延迟
    public static final int CONNECT_TIME_DELAY = 1500;//连接蓝牙延迟
    public static final int BLUE_SEND_TIME_OUT = 36000;//强制发送等待时间
    public static final int READ_BIAO_TIME_OUT =20000;//读表等待时间


    private SnBlueToothListener snBlueToothListener;
    protected boolean isConnected;


    public int getWriteTimeDelay() {
        return writeTimeDelay;
    }

    public void setWriteTimeDelay(int writeTimeDelay) {
        this.writeTimeDelay = writeTimeDelay;
    }

    protected ConnectionCallback connectionCallback = new ConnectionCallback() {
        @Override
        public void onConnectFailed(int i) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onConnectFailed" + i);
                snBlueToothListener.onChildConnectFailed(i);
                ToastUtils.showLong("蓝牙连接失败");
            }
        }

        @Override
        public void onConnectSuccess() {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onConnectSuccess");
                isConnected=true;
                snBlueToothListener.onChildConnectSuccess();
            }
        }

        @Override
        public void onNotify(byte[] bytes) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onNotify" + DataParser.byteToString(bytes));
                if(((byte) (bytes[2] ^ ((byte) 0x80))==0x50)){
                    XLog.i("蓝牙ASCII：onNotify=" + ByteConvertUtils.getASCIIbyByte(bytes, 3, 2));
                }
                snBlueToothListener.onChildNotify(bytes);
            }
        }
    };
    protected WriteCallback writeCallback = new WriteCallback() {

        @Override
        public void onWriteSuccess() {
            if (isTopTaskAcitivity()) {
                XLog.i("蓝牙：onWriteSuccess");
                snBlueToothListener.onChildWriteSuccess();
            }
        }

        @Override
        public void onWriteFailure(int i) {
            if (isTopTaskAcitivity()) {
                LoadingDialogUtil.dismissByEvent(loadingTag);
                XLog.i("蓝牙：onWriteFailure");
                snBlueToothListener. onChildWriteFailure(i);
                ToastUtils.showLong("蓝牙数据传送失败，请退出该界面重进或断开重新连接蓝牙");
            }
        }
    };

    public SensoroDeviceSession getSensoroDeviceSession() {
        return sensoroDeviceSession;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public SnBlueToothTool(SnBlueToothListener snBlueToothListener) {
        this.snBlueToothListener = snBlueToothListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        loadingTag = activity.getClass().getName();
    }

    public void connectBlueTooth(SensoroDevice sensoroDevice) {
        connectBlueTooth(TIME_OUT, sensoroDevice);
    }


    public void connectBlueTooth(int milliseconds, SensoroDevice sensoroDevice) {
        if (sensoroDevice == null) {
            ToastUtils.showLong("请选择蓝牙设备");
            return;
        }
        SensoroBlueManager.stopSearch();
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.disconnect();
            isConnected = false;
            sensoroDeviceSession.onSessonPause();
            sensoroDeviceSession.onSessionResume();
        }
        sensoroDeviceSession = new SensoroDeviceSession(getActivity(), sensoroDevice);
        LoadingDialogUtil.showByEvent(true, milliseconds, "SN:" + sensoroDevice.getSerialNumber() + "蓝牙连接中", loadingTag);
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

    public void disconnect(){
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.disconnect();
        }
    }
    public void disconnectBlueTooth() {
        if (sensoroDeviceSession != null) {
            sensoroDeviceSession.disconnect();
            isConnected = false;
        }
    }


    /**
     * 写入
     *
     * @param data 参数
     */
    public void write(byte[] data) {
        if (sensoroDeviceSession != null) {
            write(data, "正在写入", true, TIME_OUT);
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }

    }

    /**
     * @param data        参数
     * @param loadingText 提示文字
     */
    public void write(byte[] data, String loadingText) {
        if (sensoroDeviceSession != null) {
            write(data, loadingText, true, TIME_OUT);
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }
    }

    /**
     * 写入
     *
     * @param data          参数
     * @param loadingText   提示文字
     * @param showTypeFirst 是否消失蓝牙超时的提示（只在出厂配置app中使用）
     */
    public void write(byte[] data, String loadingText, boolean showTypeFirst) {
        if (sensoroDeviceSession != null) {
            LoadingDialogUtil.showByEvent(showTypeFirst, loadingText, loadingTag);
            write(data, loadingText, showTypeFirst, writeTimeDelay);
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }
    }

    /**
     * @param data          参数
     * @param loadingText   提示文字
     * @param showTypeFirst 是否消失蓝牙超时的提示（只在出厂配置app中使用）
     * @param milliseconds  延迟多少后执行写入操作
     */
    public void write(byte[] data, String loadingText, boolean showTypeFirst, long milliseconds) {
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
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }
    }

    /**
     * @param data           参数
     * @param loadingText    提示文字
     * @param showTypeSecond 是否加入延迟消失
     * @param showTime       延迟多少秒后加载框消失
     */
    public void write(byte[] data, String loadingText, boolean showTypeSecond, int showTime) {
        if (sensoroDeviceSession != null) {
            write(data, loadingText, showTypeSecond, showTime, writeTimeDelay);
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }
    }

    /**
     * @param data           参数
     * @param loadingText    提示文字
     * @param showTypeSecond 是否加入延迟消失
     * @param showTime       延迟多少秒后加载框消失
     * @param milliseconds   延迟多少后执行写入操作
     */
    public void write(byte[] data, String loadingText, boolean showTypeSecond, int showTime, long milliseconds) {
        if (sensoroDeviceSession != null) {
            LoadingDialogUtil.showByEvent(showTypeSecond, showTime, loadingText, loadingTag);
            compositeDisposable.add(FlowableUtil.createRxTimer(milliseconds, new RxTimerListener() {
                @Override
                public void onNext(@NonNull Long number) {
                    XLog.i("蓝牙：write" + DataParser.byteToString(data));
                    if(data[2]==0x50){
                        XLog.i("蓝牙ASCII：write=" + ByteConvertUtils.getASCIIbyByte(data, 3, 2));
                    }
                    sensoroDeviceSession.write(data, writeCallback);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            }));
        } else {
            ToastUtils.showLong("未连接蓝牙");
        }
    }


    /**
     * 判断当前显示的activity是否在栈顶
     *
     * @return
     */
    public boolean isTopTaskAcitivity() {
        if (loadingTag.equals(XActivityUtils.getTopActivity().getClass().getName())) {
            return true;
        } else {
            return false;
        }
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
            isConnected = false;
        }
    }

}
