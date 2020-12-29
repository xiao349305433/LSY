package wu.loushanyun.com.modulechiptest.p.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.SensoroDeviceInfo;

public class SecondUploadInfoViewBinder extends ItemViewBinder<SensoroDeviceInfo, SecondUploadInfoViewBinder.ViewHolder> {

    private OnPrintListener onPrintListener;

    public SecondUploadInfoViewBinder(OnPrintListener onPrintListener) {
        this.onPrintListener = onPrintListener;
    }

    public interface OnPrintListener {
        void onPrint(String sn);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_chip_item_second_upload, parent, false);
        return new ViewHolder(root);
    }

    //SpannableStringBuilder sb = new SpannableStringBuilder("字体多种颜色一&背景色");
//sb.setSpan(new ForegroundColorSpan(Color.RED),0,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//sb.setSpan(new ForegroundColorSpan(Color.YELLOW),2,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//sb.setSpan(new ForegroundColorSpan(Color.BLUE), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////背景色
//sb.setSpan(new BackgroundColorSpan(Color.GREEN), 7, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//textview.setText(sb);
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SensoroDeviceInfo sensoroDeviceInfo) {
        SensoroDeviceInfo.DataBeanX dataBeanX1 = sensoroDeviceInfo.getData().get(0);
        SensoroDeviceInfo.DataBeanX dataBeanX2 = sensoroDeviceInfo.getData().get(1);
        holder.textSn.setText("SN:" + dataBeanX1.getSn());
        SpannableStringBuilder spannableStringBuilder1 = getSpannable(dataBeanX1);
        SpannableStringBuilder spannableStringBuilder2 = getSpannable(dataBeanX2);
        holder.textTime1.setText(spannableStringBuilder1);
        holder.textTime2.setText(spannableStringBuilder2);

        if (!XHStringUtil.isEmpty(dataBeanX1.getData().getCustomer(), false)) {
            String resultStr = dataBeanX1.getData().getCustomer().substring(4, 6);
            if ("05".equals(resultStr)) {
                holder.textHege.setText("不合格");
                holder.textHege.setTextColor(Color.RED);
            } else {
                try {
//                    String[] stringTimes = TimeUtils.milliseconds2String(Long.valueOf(dataBeanX1.getCreatedTime())).split(" ");
//                    String[] currentTimes = TimeUtils.getCurTimeString().split(" ");
//                    XLog.i("stringTimes" + "," + currentTimes);
//                    if (stringTimes[0].equals(currentTimes[0])) {
//                        holder.textHege.setText("合格");
//                        holder.textHege.setTextColor(Color.BLACK);
//                    } else {
//                        holder.textHege.setText("不合格");
//                        holder.textHege.setTextColor(Color.RED);
//                    }
                    holder.textHege.setText("合格");
                    holder.textHege.setTextColor(Color.BLACK);
                } catch (Exception e) {
                    holder.textHege.setText("不合格");
                    holder.textHege.setTextColor(Color.RED);
                }
            }
        } else {
            holder.textHege.setText("不合格");
            holder.textHege.setTextColor(Color.RED);
        }

    }

    private SpannableStringBuilder getSpannable(SensoroDeviceInfo.DataBeanX dataBeanX) {
        String stringTime1 = "-";
        String stringLastTime1;
        if (!XHStringUtil.isEmpty(dataBeanX.getCreatedTime(), false)) {
            stringTime1 = TimeUtils.milliseconds2String(Long.valueOf(dataBeanX.getCreatedTime()));
        }
        if (!XHStringUtil.isEmpty(dataBeanX.getData().getCustomer(), false)) {
            String resultStr = dataBeanX.getData().getCustomer().substring(4, 6);
            if ("05".equals(resultStr)) {
                stringLastTime1 = "强制";
            } else {
                stringLastTime1 = "自动";
            }
        } else {
            stringLastTime1 = "无";
        }
        SpannableStringBuilder sb1 = new SpannableStringBuilder(stringTime1 + stringLastTime1);
        if ("强制".equals(stringLastTime1)) {
            sb1.setSpan(new ForegroundColorSpan(Color.BLUE), stringTime1.length(), (stringTime1 + stringLastTime1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if ("自动".equals(stringLastTime1)) {
            sb1.setSpan(new ForegroundColorSpan(Color.BLACK), stringTime1.length(), (stringTime1 + stringLastTime1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if ("无".equals(stringLastTime1)) {
            sb1.setSpan(new ForegroundColorSpan(Color.RED), stringTime1.length(), (stringTime1 + stringLastTime1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sb1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private RoundTextView textPrint;
        private TextView textSn;
        private TextView textTime1;
        private TextView textTime2;
        private TextView textHege;

        ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            textPrint = (RoundTextView) view.findViewById(R.id.text_print);
            textSn = (TextView) view.findViewById(R.id.text_sn);
            textTime1 = (TextView) view.findViewById(R.id.text_time1);
            textTime2 = (TextView) view.findViewById(R.id.text_time2);
            textHege = (TextView) view.findViewById(R.id.text_hege);
            textPrint.setOnClickListener(view1 -> {
                if(onPrintListener!=null){
                    try {
                        onPrintListener.onPrint(getContentByViewHolder(ViewHolder.this).getData().get(0).getSn());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort("数据异常");
                    }
                }
            });

        }
    }
}
