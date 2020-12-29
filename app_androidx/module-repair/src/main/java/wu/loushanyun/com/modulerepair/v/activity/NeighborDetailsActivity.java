package wu.loushanyun.com.modulerepair.v.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.NoDoubleClickUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulerepair.R;

@Route(path = K.NeighborDetailsActivity)
public class NeighborDetailsActivity extends BaseBlueToothActivity {
    private RoundTextView head;
    private RoundTextView systemStatusTools;
    private ImageView systemStatus;
    private TextView config;
    private TextView tvTitleType;
    private TextView tvTitle1;
    private TextView tvInfo1;
    private TextView tvTitle2;
    private TextView tvInfo2;
    private LinearLayout llBase;
    private TextView tvTitle4;
    private TextView tvInfo4;
    private TextView tvTitle3;
    private TextView tvInfo3;


    private boolean isSuccess, isActive;
    private HashMap<String, String> map;
    private ArrayList<String> keys;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!isSuccess) {
                    LoadingDialogUtil.dismissByEvent(loadingTag);
                    write(DataParser.CMD_METER_INFO, "正在读取");
                }
            } else if (msg.what == 1) {
                Log.i("yunanhao", map.toString());
                if ("1".equalsIgnoreCase(map.get(MapParams.系统状态))) {
                    isActive = true;
                    handler.sendEmptyMessage(3);
                } else {
                    isActive = false;
                    handler.sendEmptyMessage(4);
                }
                setConfig();
                setYuanChuanBiaoHaoInfo();
            } else if (msg.what == 2) {
                Log.i("yunanhao", map.toString());
                setConfig();
                setYuanChuanWuLianWangDuanInfo();
            } else if (msg.what == 3) {
                //已激活
                systemStatus.setVisibility(View.VISIBLE);
                systemStatusTools.setVisibility(View.GONE);
                systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
            } else if (msg.what == 4) {
                //未激活
                systemStatus.setVisibility(View.VISIBLE);
                systemStatusTools.setVisibility(View.VISIBLE);
                systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
            }
        }
    };

    private void setConfig() {
        StringBuilder sb = new StringBuilder();
        BigDecimal result;
        String value;
        if ((value = map.get(MapParams.倍率)) != null) {
            if (keys.indexOf(MapParams.倍率) == -1) {
                keys.add(MapParams.倍率);
            }
            value = LouShanYunUtils.getBLReadStringByCode(value);
        } else {
            value = "0";
        }
        try {
            result = new BigDecimal(1).divide(new BigDecimal(value));
        } catch (Exception e) {
            result = new BigDecimal(0);
        }
        sb.append("脉冲常数(个/m³)/倍率(m³/ev)：");
        sb.append(result.stripTrailingZeros().toPlainString());
        sb.append('/');
        sb.append(value);
        sb.append('\n');
        sb.append("当前脉冲(个)/当前读数(m³)：");
        if ((value = map.get(MapParams.当前脉冲读数)) != null) {
            if (keys.indexOf(MapParams.当前脉冲读数) == -1) {
                keys.add(MapParams.当前脉冲读数);
            }
            sb.append(value);
        } else if ((value = map.get(MapParams.安装脉冲底数)) != null) {
            if (keys.indexOf(MapParams.安装脉冲底数) == -1) {
                keys.add(MapParams.安装脉冲底数);
            }
            sb.append(value);
        } else {
            value = "0";
            sb.append(' ');
        }
        sb.append('/');
        try {
            sb.append(new BigDecimal(value).divide(result).stripTrailingZeros().toPlainString());
        } catch (Exception e) {
            sb.append('0');
        }
        sb.append('\n');
        sb.append("信道参数：");
        if ((value = map.get(MapParams.信道参数)) != null) {
            if (keys.indexOf(MapParams.信道参数) == -1) {
                keys.add(MapParams.信道参数);
            }
            sb.append(value.equals("0") ? "模式A" : "模式B");
        } else {
            sb.append(' ');
        }
        sb.append("     扩频因子：");
        if ((value = map.get(MapParams.扩频因子)) != null) {
            if (keys.indexOf(MapParams.扩频因子) == -1) {
                keys.add(MapParams.扩频因子);
            }
            sb.append(LouShanYunUtils.getKPYZReadStringByCode(value));
        } else {
            sb.append(' ');
        }
        config.setText(sb);
    }

    private void setYuanChuanBiaoHaoInfo() {
        tvTitleType.setText("产品形式：远传表号接入");
        String value;
        StringBuilder sb = new StringBuilder();
        sb.append("使用类型\n");
        sb.append("接入形式\n");
        sb.append("传感信号\n");
        sb.append("发送频率\n");
        sb.append("出厂时间");
        tvTitle1.setText(sb);
        sb.delete(0, sb.length());
        if ((value = map.get(MapParams.采集场景)) != null) {
            if (keys.indexOf(MapParams.采集场景) == -1) {
                keys.add(MapParams.采集场景);
            }
            sb.append(LouShanYunUtils.getCJCJReadStringByCode(value));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.信号类型)) != null) {
            if (keys.indexOf(MapParams.信号类型) == -1) {
                keys.add(MapParams.信号类型);
            }
            if ("0".equals(value)) {
                sb.append("模拟信号");
            } else if ("1".equals(value)) {
                sb.append("数字通讯");
            } else {

            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.传感信号)) != null) {
            if (keys.indexOf(MapParams.传感信号) == -1) {
                keys.add(MapParams.传感信号);
            }
            sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.发送频率)) != null) {
            if (keys.indexOf(MapParams.发送频率) == -1) {
                keys.add(MapParams.发送频率);
            }
            sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.出厂时间_年)) != null) {
            if (keys.indexOf(MapParams.出厂时间_年) == -1) {
                keys.add(MapParams.出厂时间_年);
            }
            sb.append("20");
            for (int i = value.length(); i < 2; i++) {
                sb.append(0);
            }
            sb.append(value);
            sb.append("-");
            if ((value = map.get(MapParams.出厂时间_月)) != null) {
                if (keys.indexOf(MapParams.出厂时间_月) == -1) {
                    keys.add(MapParams.出厂时间_月);
                }
                for (int i = value.length(); i < 2; i++) {
                    sb.append(0);
                }
                sb.append(value);
            } else {
                sb.append("00");
            }
            sb.append("-");
            if ((value = map.get(MapParams.出厂时间_日)) != null) {
                if (keys.indexOf(MapParams.出厂时间_日) == -1) {
                    keys.add(MapParams.出厂时间_日);
                }
                for (int i = value.length(); i < 2; i++) {
                    sb.append(0);
                }
                sb.append(value);
            } else {
                sb.append("00");
            }
            sb.append(" 00:00:00");
        }
        tvInfo1.setText(sb);
        sb.delete(0, sb.length());

        sb.append("通讯状态\n");
        sb.append("电池状态");
        tvTitle2.setText(sb);
        sb.delete(0, sb.length());
        sb.append("正常\n");
        if ((value = map.get(MapParams.电源类型)) != null) {
            if (keys.indexOf(MapParams.电源类型) == -1) {
                keys.add(MapParams.电源类型);
            }
            String type = LouShanYunUtils.getDianYuanLeiXin(value);
            if ("物联电池".equalsIgnoreCase(type)) {
                sb.append("0".equalsIgnoreCase(map.get(MapParams.底板状态_自备电池状态)) ? "正常" : "异常");
            } else {
                sb.append("0".equalsIgnoreCase(map.get(MapParams.底板状态_外接电源220V状态)) ? "正常" : "异常");
            }
        }
        tvInfo2.setText(sb);
        sb.delete(0, sb.length());
        sb.append("参数内容\n");
        sb.append("保留字节\n");
        sb.append("脉宽\n");
        sb.append("初始值\n");
        sb.append("最大值\n");
        sb.append("工作模式\n");
        sb.append("电源类型\n");
        sb.append("设备ID\n");
        sb.append("硬件版本\n");
        sb.append("软件版本");
        tvTitle3.setText(sb);
        sb.delete(0, sb.length());

        if ((value = map.get(MapParams.参数内容)) != null) {
            if (keys.indexOf(MapParams.参数内容) == -1) {
                keys.add(MapParams.参数内容);
            }
            sb.append(LouShanYunUtils.getCSNRReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.保留字节)) != null) {
            if (keys.indexOf(MapParams.保留字节) == -1) {
                keys.add(MapParams.保留字节);
            }
            sb.append(value);
        }
        sb.append('\n');
        if ((value = map.get(MapParams.脉宽)) != null) {
            if (keys.indexOf(MapParams.脉宽) == -1) {
                keys.add(MapParams.脉宽);
            }
            sb.append(LouShanYunUtils.getMKReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.压力值标定_初始值)) != null) {
            if (keys.indexOf(MapParams.压力值标定_初始值) == -1) {
                keys.add(MapParams.压力值标定_初始值);
            }
            sb.append(new BigDecimal(value).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());
            sb.append("Mpa");
        }
        sb.append('\n');
        if ((value = map.get(MapParams.压力值标定_最大值)) != null) {
            if (keys.indexOf(MapParams.压力值标定_最大值) == -1) {
                keys.add(MapParams.压力值标定_最大值);
            }
            sb.append(new BigDecimal(value).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString());
            sb.append("Mpa");
        }
        sb.append('\n');
        if ((value = map.get(MapParams.工作模式)) != null) {
            if (keys.indexOf(MapParams.工作模式) == -1) {
                keys.add(MapParams.工作模式);
            }
            if (value.equals("0")) {
                sb.append("无效参数");
            } else if (value.equals("1")) {
                sb.append("从机模式");
            } else if (value.equals("2")) {
                sb.append("主机模式");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.电源类型)) != null) {
            if (keys.indexOf(MapParams.电源类型) == -1) {
                keys.add(MapParams.电源类型);
            }
            sb.append(LouShanYunUtils.getDianYuanLeiXin(value));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.设备ID)) != null) {
            if (keys.indexOf(MapParams.设备ID) == -1) {
                keys.add(MapParams.设备ID);
            }
            sb.append(value);
        }
        sb.append('\n');
        if ((value = map.get(MapParams.硬件版本)) != null) {
            if (keys.indexOf(MapParams.硬件版本) == -1) {
                keys.add(MapParams.硬件版本);
            }
            sb.append(LouShanYunUtils.getHardWareVersion(Integer.parseInt(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.软件版本)) != null) {
            if (keys.indexOf(MapParams.软件版本) == -1) {
                keys.add(MapParams.软件版本);
            }
            sb.append(LouShanYunUtils.getSoftWareVersion(Integer.parseInt(value)));
        }
        tvInfo3.setText(sb);
        sb.delete(0, sb.length());

        llBase.setVisibility(View.VISIBLE);
        sb.append("外接电源220V\n");
        sb.append("第三方电池\n");
        sb.append("自备电池状态\n");
        sb.append("水表倒流状态\n");
        sb.append("设备强磁状态\n");
        sb.append("设备拆卸状态");
        tvTitle4.setText(sb);
        sb.delete(0, sb.length());

        if ((value = map.get(MapParams.底板状态_外接电源220V状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_外接电源220V状态) == -1) {
                keys.add(MapParams.底板状态_外接电源220V状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("异常");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.底板状态_第三方电池状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_第三方电池状态) == -1) {
                keys.add(MapParams.底板状态_第三方电池状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("欠压");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.底板状态_自备电池状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_自备电池状态) == -1) {
                keys.add(MapParams.底板状态_自备电池状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("欠压");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.底板状态_水表倒流状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_水表倒流状态) == -1) {
                keys.add(MapParams.底板状态_水表倒流状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("倒流");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.底板状态_设备强磁状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_设备强磁状态) == -1) {
                keys.add(MapParams.底板状态_设备强磁状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("强磁");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.底板状态_设备拆卸状态)) != null) {
            if (keys.indexOf(MapParams.底板状态_设备拆卸状态) == -1) {
                keys.add(MapParams.底板状态_设备拆卸状态);
            }
            if (value.equals("0")) {
                sb.append("正常");
            } else if (value.equals("1")) {
                sb.append("拆卸");
            } else if (value.equals("2")) {
                sb.append("空");
            }
        }
        tvInfo4.setText(sb);
        sb.delete(0, sb.length());
    }

    private void setYuanChuanWuLianWangDuanInfo() {
        tvTitleType.setText("产品形式：远传物联网端");
        String key, value;
        StringBuilder temp = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("物联SN\n");
        sb.append("接入形式\n");
        sb.append("传感信号\n");
        sb.append("发送频率\n");
        sb.append("出厂时间\n");
        sb.append("厂家标识");
        tvTitle1.setText(sb);
        sb.delete(0, sb.length());
        if ((value = map.get(MapParams.物联SN)) != null) {
            if (keys.indexOf(MapParams.物联SN) == -1) {
                keys.add(MapParams.物联SN);
            }
            sb.append(value.toUpperCase());
        } else {
            sb.append(sensoroDevice.getSerialNumber().toUpperCase());
        }
        sb.append('\n');
        if ((value = map.get(MapParams.信号类型)) != null) {
            if (keys.indexOf(MapParams.信号类型) == -1) {
                keys.add(MapParams.信号类型);
            }
            if ("0".equals(value)) {
                sb.append("模拟信号");
            } else if ("1".equals(value)) {
                sb.append("数字通讯");
            } else {

            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.传感信号)) != null) {
            if (keys.indexOf(MapParams.传感信号) == -1) {
                keys.add(MapParams.传感信号);
            }
            sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.发送频率)) != null) {
            if (keys.indexOf(MapParams.发送频率) == -1) {
                keys.add(MapParams.发送频率);
            }
            sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.出厂时间_年)) != null) {
            if (keys.indexOf(MapParams.出厂时间_年) == -1) {
                keys.add(MapParams.出厂时间_年);
            }
            sb.append("20");
            for (int i = value.length(); i < 2; i++) {
                sb.append(0);
            }
            sb.append(value);
            sb.append("-");
            if ((value = map.get(MapParams.出厂时间_月)) != null) {
                if (keys.indexOf(MapParams.出厂时间_月) == -1) {
                    keys.add(MapParams.出厂时间_月);
                }
                for (int i = value.length(); i < 2; i++) {
                    sb.append(0);
                }
                sb.append(value);
            } else {
                sb.append("00");
            }
            sb.append("-");
            if ((value = map.get(MapParams.出厂时间_日)) != null) {
                if (keys.indexOf(MapParams.出厂时间_日) == -1) {
                    keys.add(MapParams.出厂时间_日);
                }
                for (int i = value.length(); i < 2; i++) {
                    sb.append(0);
                }
                sb.append(value);
            } else {
                sb.append("00");
            }
            sb.append(" 00:00:00");
        }
        sb.append('\n');
        if ((value = map.get(MapParams.厂家标识)) != null) {
            if (keys.indexOf(MapParams.厂家标识) == -1) {
                keys.add(MapParams.厂家标识);
            }
            sb.append("未注册的厂家标识");
            pushEventNoProgress(0, value);
        }
        tvInfo1.setText(sb);
        sb.delete(0, sb.length());

        sb.append("流向\n");
        sb.append("传感器\n");
        sb.append("计量状态\n");
        sb.append("通讯状态\n");
        sb.append("电池状态");
        tvTitle2.setText(sb);
        sb.delete(0, sb.length());

        if ((value = map.get(MapParams.表流向状态)) != null) {
            if (keys.indexOf(MapParams.表流向状态) == -1) {
                keys.add(MapParams.表流向状态);
            }
            if ("0".equals(value)) {
                sb.append("正常");
            } else {
                sb.append("倒流");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.表拆卸状态)) != null) {
            if (keys.indexOf(MapParams.表拆卸状态) == -1) {
                keys.add(MapParams.表拆卸状态);
            }
            if ("0".equals(value)) {
                sb.append("正常");
            } else {
                sb.append("无");
            }
        }
        sb.append('\n');
        if ((value = map.get(MapParams.表强磁状态)) != null) {
            if (keys.indexOf(MapParams.表强磁状态) == -1) {
                keys.add(MapParams.表强磁状态);
            }
            if ("0".equals(value)) {
                sb.append("正常");
            }
        }
        sb.append('\n');
        sb.append("正常\n");
        if ((value = map.get(MapParams.表电池状态)) != null) {
            if (keys.indexOf(MapParams.表电池状态) == -1) {
                keys.add(MapParams.表电池状态);
            }
            sb.append(value.equals("0") ? "正常" : "异常");
        }
        tvInfo2.setText(sb);
        sb.delete(0, sb.length());
        sb.append("扩频因子\n");
        sb.append("信道参数\n");
        sb.append("设备ID\n");
        sb.append("当前脉冲读数\n");
        sb.append("倍率\n");
        sb.append("无线频率\n");
        sb.append("硬件版本\n");
        sb.append("软件版本");
        tvTitle3.setText(sb);
        sb.delete(0, sb.length());
        //去重
        if ((value = map.get(MapParams.扩频因子)) != null) {
            if (keys.indexOf(MapParams.扩频因子) == -1) {
                keys.add(MapParams.扩频因子);
                sb.append(LouShanYunUtils.getKPYZReadStringByCode(value));
                sb.append('\n');
            } else {
                temp.delete(0, temp.length());
                temp.append(tvTitle3.getText());
                key = MapParams.扩频因子;
                int index = temp.indexOf(key);
                if (index != -1) {
                    temp.delete(index, index + key.length() + 1);
                    tvTitle3.setText(temp);
                }
            }
        }
        if ((value = map.get(MapParams.信道参数)) != null) {
            if (keys.indexOf(MapParams.信道参数) == -1) {
                keys.add(MapParams.信道参数);
                sb.append(value.equals("0") ? "模式A" : "模式B");
                sb.append('\n');
            } else {
                temp.delete(0, temp.length());
                temp.append(tvTitle3.getText());
                key = MapParams.信道参数;
                int index = temp.indexOf(key);
                if (index != -1) {
                    temp.delete(index, index + key.length() + 1);
                    tvTitle3.setText(temp);
                }
            }
        }
        if ((value = map.get(MapParams.设备ID)) != null) {
            if (keys.indexOf(MapParams.设备ID) == -1) {
                keys.add(MapParams.设备ID);
            }
            sb.append(value);
        }
        sb.append('\n');
        if ((value = map.get(MapParams.当前脉冲读数)) != null) {
            if (keys.indexOf(MapParams.当前脉冲读数) == -1) {
                keys.add(MapParams.当前脉冲读数);
                sb.append(value);
                sb.append('\n');
            } else {
                temp.delete(0, temp.length());
                temp.append(tvTitle3.getText());
                key = MapParams.当前脉冲读数;
                int index = temp.indexOf(key);
                if (index != -1) {
                    temp.delete(index, index + key.length() + 1);
                    tvTitle3.setText(temp);
                }
            }
        }
        if ((value = map.get(MapParams.倍率)) != null) {
            if (keys.indexOf(MapParams.倍率) == -1) {
                keys.add(MapParams.倍率);
                sb.append(LouShanYunUtils.getBLReadStringByCode(value));
                sb.append('\n');
            } else {
                temp.delete(0, temp.length());
                temp.append(tvTitle3.getText());
                key = MapParams.倍率;
                int index = temp.indexOf(key);
                if (index != -1) {
                    temp.delete(index, index + key.length() + 1);
                    tvTitle3.setText(temp);
                }
            }
        }

        if ((value = map.get(MapParams.无线频率)) != null) {
            if (keys.indexOf(MapParams.无线频率) == -1) {
                keys.add(MapParams.无线频率);
            }
            sb.append(LouShanYunUtils.getWuXianPinLv(value));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.硬件版本)) != null) {
            if (keys.indexOf(MapParams.硬件版本) == -1) {
                keys.add(MapParams.硬件版本);
            }
            sb.append(LouShanYunUtils.getHardWareVersion(Integer.parseInt(value)));
        }
        sb.append('\n');
        if ((value = map.get(MapParams.软件版本)) != null) {
            if (keys.indexOf(MapParams.软件版本) == -1) {
                keys.add(MapParams.软件版本);
            }
            sb.append(LouShanYunUtils.getSoftWareVersion(Integer.parseInt(value)));
        }
        tvInfo3.setText(sb);
        sb.delete(0, sb.length());
    }

    @Override
    protected void initView() {
        super.initView();
        head = (RoundTextView) findViewById(R.id.head);
        systemStatusTools = (RoundTextView) findViewById(R.id.system_status_tools);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        config = (TextView) findViewById(R.id.config);
        tvTitleType = (TextView) findViewById(R.id.tv_titleType);
        tvTitle1 = (TextView) findViewById(R.id.tv_title1);
        tvInfo1 = (TextView) findViewById(R.id.tv_info1);
        tvTitle2 = (TextView) findViewById(R.id.tv_title2);
        tvInfo2 = (TextView) findViewById(R.id.tv_info2);
        llBase = (LinearLayout) findViewById(R.id.ll_base);
        tvTitle4 = (TextView) findViewById(R.id.tv_title4);
        tvInfo4 = (TextView) findViewById(R.id.tv_info4);
        tvTitle3 = (TextView) findViewById(R.id.tv_title3);
        tvInfo3 = (TextView) findViewById(R.id.tv_info3);

        llBase.setVisibility(View.GONE);//隐藏底板状态布局
        systemStatus.setVisibility(View.GONE);//默认隐藏系统激活状态
        systemStatusTools.setVisibility(View.GONE);//默认隐藏系统激活状态
        map = new HashMap<>();
        keys = new ArrayList<>();
        connectBlueTooth();
        systemStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                if (isActive) {
                    return;
                }
                if (isSuccess) {
                    write(DataParser.getSystemStatusSettingCMD(true), "激活中");
                } else {
                    write(new byte[]{0x68, 0x02, 0x24, 0x01, 0x27, 0x16}, "激活中");
                }
            }
        });
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(0, new GetChangJiaBiaoShiRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == 0) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                String message = tvInfo1.getText().toString();
                tvInfo1.setText(message.replaceAll("未注册的厂家标识", name));
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_neighbor_details;
        ba.mTitleText = "详情";

    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        write(DataParser.CMD_INFO_BASE, "正在读取");
        handler.sendEmptyMessageDelayed(0, 5000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("物联SN:");
                sb.append(sensoroDevice.getSerialNumber().toUpperCase());
                head.setText(sb);
            }
        });
    }

    @Override
    protected void onChildNotify(byte[] results) {
        Log.i("yunanhao  onNotify:", DataParser.byteToString(results));
        byte code = (byte) (results[2] ^ ((byte) 0x80));
        if (Byte.compare(code, (byte) 0x22) == 0) {
            map.putAll(DataParser.getMoudleNo2(results));
            write(DataParser.CMD_SYSTEM_STATUS, "正在读取");
            handler.sendEmptyMessage(2);
        } else if (Byte.compare(code, (byte) 0x30) == 0) {
            isSuccess = true;
            map.putAll(DataParser.getInformationBase(results));
            write(DataParser.CMD_INFO_ALL, "正在读取");
        } else if (Byte.compare(code, (byte) 0x11) == 0) {
            try {
                map.putAll(DataParser.getInformationAll(results));
            } catch (Exception e) {
                e.printStackTrace();
            }
            write(DataParser.CMD_SYSTEM_STATUS_NORMAL, "正在读取");
        } else if (Byte.compare(code, (byte) 0x26) == 0) {
            if (results[3] == 0) {
                if ("1".equals(String.valueOf(results[4] & 0xff))) {
                    isActive = true;
                    handler.sendEmptyMessage(3);
                } else {
                    isActive = false;
                    handler.sendEmptyMessage(4);
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        systemStatus.setVisibility(View.GONE);
                        systemStatusTools.setVisibility(View.GONE);

                    }
                });
            }
        } else if (Byte.compare(code, (byte) 0x31) == 0) {
            map.putAll(DataParser.getDiBanBiaoJiInfo(results));
            handler.sendEmptyMessage(1);
        } else if (Byte.compare(code, (byte) 0x23) == 0) {
            if (Byte.compare(results[3], (byte) 0) == 0) {
                isActive = !isActive;
                handler.sendEmptyMessage(3);
            }
        } else if (Byte.compare(code, (byte) 0x24) == 0) {
            if (Byte.compare(results[3], (byte) 0) == 0) {
                isActive = !isActive;
                handler.sendEmptyMessage(3);
            }
        }
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }
}
