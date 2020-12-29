package com.test1moudle.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.test1moudle.R;
import com.wu.loushanyun.base.print.PrintTool;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.NoDoubleClickUtils;
import met.hx.com.librarybase.some_utils.ImageUtils;

@Route(path = C.PrintBitmapActivity)
public class PrintBitmapActivity extends BaseNoPresenterActivity {
    private TextView textPrint;
    private ImageView imageView1;
    private Button button1;
    private ImageView imageView2;
    private Button button2;
    private ImageView imageView2Chip;
    private Button button2Chip;
    private ImageView imageView3;
    private Button button3;
    private PrintTool printTool;

    private String strP3 = "\"181113151515\",\"2018\",\"11\",\"13\", \"贵州运通曙光技术服务有限公司\",\"无磁正反脉冲\"";
    private String strP2 = "\"02930017C613AEC2\",\"无磁正反脉冲\",\"贵州运通曙光技术服务有限公司\", \"181113151515\",\"2018\",\"11\",\"13\"";

    @Override
    protected void initLifeCycle() {
        printTool = new PrintTool(1, new PrintTool.PrintListener() {
            @Override
            public void onUsbPermission(Intent intent) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            System.out.println("permission ok for device " + device);
                        }
                    } else {
                        System.out.println("permission denied for device " + device);
                    }
                }
            }

            @Override
            public void onUsbDeviceDetached(Intent intent) {
            }

            @Override
            public void onAclDisconnected(Intent intent) {
            }

            @Override
            public void onConnectionState(Intent intent) {
                int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        textPrint.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        textPrint.setText("打印机已连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_FAILED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_fail));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onConnectionStateDisconnect(Intent intent) {
            }

            @Override
            public void onConnectionStateConnecting(Intent intent) {
            }

            @Override
            public void onConnectionStateConnected(Intent intent) {
            }

            @Override
            public void onConnectionStateFailed(Intent intent) {
            }

            @Override
            public void onQueryPrinterState(Intent intent) {
            }
        });
        registerLifeCycle(printTool);

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    public void onRightClick(View item) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        printTool.showPrintPopWindow(item);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_print_bitmap;
        ba.mTitleText = "打印机标签示例";
        ba.mTitleRightText = "打印";
    }

    @Override
    protected void initView() {
        textPrint = (TextView) findViewById(R.id.text_print);
        imageView1 = (ImageView) findViewById(R.id.image_view1);
        button1 = (Button) findViewById(R.id.button1);
        imageView2 = (ImageView) findViewById(R.id.image_view2);
        button2 = (Button) findViewById(R.id.button2);
        imageView2Chip = (ImageView) findViewById(R.id.image_view2_chip);
        button2Chip = (Button) findViewById(R.id.button2_chip);
        imageView3 = (ImageView) findViewById(R.id.image_view3);
        button3 = (Button) findViewById(R.id.button3);


        imageView1.setImageDrawable(ImageUtils.bitmap2Drawable(printTool.createPrint3("181113151515", "2018", "11", "13", "贵州运通曙光技术服务有限公司", "无磁正反脉冲")));
        imageView2.setImageDrawable(ImageUtils.bitmap2Drawable(printTool.createPrint2("02930017C613AEC2", "无磁正反脉冲", "贵州运通曙光技术服务有限公司", "181113151515", "2018", "11", "13")));
        imageView2Chip.setImageDrawable(ImageUtils.bitmap2Drawable(printTool.createChipPrint2("02930017C613AEC2")));
        imageView3.setImageDrawable(ImageUtils.bitmap2Drawable(printTool.createChipPrint1("02930017C613AEC2")));
        button1.setOnClickListener(this::onPrintClick);
        button2.setOnClickListener(this::onPrintClick);
        button3.setOnClickListener(this::onPrintClick);
        button2Chip.setOnClickListener(this::onPrintClick);
        button3.setVisibility(View.GONE);
    }

    private void onPrintClick(View view) {
        if (!printTool.isConnected()) {
            sendMessageToast("请先连接打印机");
            return;
        }
        switch (view.getId()) {
            case R.id.button1:
                printTool.print3("181113151515", "2018", "11", "13", "贵州运通曙光技术服务有限公司", "无磁正反脉冲");
                break;
            case R.id.button2:
                printTool.print2("02930017C613AEC2", "无磁正反脉冲", "贵州运通曙光技术服务有限公司", "181113151515", "2018", "11", "13");
                break;
            case R.id.button3:
//                printTool.printBitmap(createPrint3());
                break;
            case R.id.button2_chip:
                printTool.printBitmap(printTool.createChipPrint2("02930017C613AEC2"));
                break;
        }
    }


}
