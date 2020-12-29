//package com.test1moudle.v.activity;
//
//import android.arch.lifecycle.Lifecycle;
//import android.arch.lifecycle.LifecycleObserver;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.BatteryManager;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.test1moudle.R;
//
//import io.flutter.facade.Flutter;
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.view.FlutterView;
//import jrt.cifco.com.base.base.BaseAttribute;
//import jrt.cifco.com.base.base.activity.BaseNoPresenterActivity;
//import jrt.cifco.com.base.baseconfig.C;
//import jrt.cifco.com.base.baseevent.Event;
//
//@Route(path = C.FlutterViewActivity)
//public class FlutterViewActivity extends BaseNoPresenterActivity {
//    private FrameLayout flutterFragment;
//    private FlutterView flutterView;
//    private static final String CHANNEL = "samples.flutter.io/battery";
//
//    @Override
//    protected void initEventListener() {
//
//    }
//
//    @Override
//    protected void onEventRunEnd(Event event, int code) {
//
//    }
//
//    @Override
//    protected void onInitAttribute(BaseAttribute ba) {
//        ba.mActivityLayoutId = R.layout.m_test_flutter_view;
//
//    }
//
//    @Override
//    protected void initView() {
//        flutterFragment = (FrameLayout) findViewById(R.id.flutterFragment);
//        flutterView = Flutter.createView(this, getLifecycle(), "route1");
//        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        flutterFragment.addView(flutterView, layout);
//
//        //    setMethodCallHandler在此通道上接收方法调用的回调
//        new MethodChannel(flutterView, CHANNEL).setMethodCallHandler(
//                new MethodChannel.MethodCallHandler() {
//                    @Override
//                    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
////                通过methodCall可以获取参数和方法名  执行对应的平台业务逻辑即可
//                        if (methodCall.method.equals("getBatteryLevel")) {
//                            int batteryLevel = getBatteryLevel();
//                            if (batteryLevel != -1) {
//                                result.success(batteryLevel);
//                            } else {
//                                result.error("UNAVAILABLE", "Battery level not available.", null);
//                            }
//                        } else {
//                            result.notImplemented();
//                        }
//                    }
//                }
//        );
//
//    }
//
//    /**
//     * 获取电池电量
//     *
//     * @return
//     */
//    private int getBatteryLevel() {
//
//        int batteryLevel = -1;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
//            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        } else {
//            Intent intent = new ContextWrapper(getApplicationContext()).
//                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
//                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//        }
//        return batteryLevel;
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (this.flutterView != null) {
//            this.flutterView.popRoute();
//        } else {
//            super.onBackPressed();
//        }
//    }
//}
