package wu.loushanyun.com.modulechiptest.p.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.SnPrintInfo;

public class SnPrintViewBinder extends ItemViewBinder<SnPrintInfo, SnPrintViewBinder.ViewHolder> {

    private OnSnPrintListener onSnPrintListener;
    private String deleteText;


    public SnPrintViewBinder(OnSnPrintListener onSnPrintListener, String deleteText) {
        this.onSnPrintListener = onSnPrintListener;
        this.deleteText = deleteText;
    }


    public interface OnSnPrintListener {
        void onUpData(int position);

        void onSend(int position);

        void onSetXinDao(int position);

        void onSetRxDelay(int position);
    }

    @NonNull
    @Override
    protected SnPrintViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_chip_item_sn_list, parent, false);
        return new SnPrintViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull SnPrintViewBinder.ViewHolder holder, @NonNull SnPrintInfo snPrintInfo) {
        holder.textNum.setText(snPrintInfo.getNum() + "");
        holder.textSn.setText("SN:" + snPrintInfo.getSensoroDevice().sn);
        XLog.i("LSYSN:" + snPrintInfo.getSensoroDevice().sn);
        if (XHStringUtil.isEmpty(snPrintInfo.getTishi(), false)) {
            holder.textTishi.setVisibility(View.GONE);
        } else {
            holder.textTishi.setVisibility(View.VISIBLE);
        }
        holder.textTishi.setText(snPrintInfo.getTishi());
        holder.textToken.setText("Token:" + snPrintInfo.getToken());
        XLog.i("LSYToken:" + snPrintInfo.getToken());
        holder.textRxDelay.setText("RxDelay:" + snPrintInfo.getRxDelay());
        String s = "";
        if (!XHStringUtil.isEmpty(snPrintInfo.getSendStatus(), false)) {
            s += snPrintInfo.getSendStatus();
        }
        if (!XHStringUtil.isEmpty(snPrintInfo.getRssi(), false)) {
            s += "；RSSI:" + snPrintInfo.getRssi();
        }
        if (!XHStringUtil.isEmpty(snPrintInfo.getSnr(), false)) {
            s += "；SNR:" + snPrintInfo.getSnr();
        }
        if (!XHStringUtil.isEmpty(s, false)) {
            holder.textRssi.setVisibility(View.VISIBLE);
            SpannableString ss = new SpannableString(s);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
            if ("合格".equals(snPrintInfo.getSendStatus())) {
                colorSpan = new ForegroundColorSpan(Color.BLACK);
            } else if ("不合格".equals(snPrintInfo.getSendStatus())) {
                colorSpan = new ForegroundColorSpan(Color.RED);
            }

//            else if ("强制发送失败".equals(snPrintInfo.getSendStatus())) {
//                colorSpan = new ForegroundColorSpan(Color.RED);
//            }
            ss.setSpan(colorSpan, 0, snPrintInfo.getSendStatus().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.textRssi.setText(ss);
        } else {
            holder.textRssi.setVisibility(View.GONE);
        }
        if(snPrintInfo.isSend()){
            holder.textIssend.setText("强制发送成功");
            holder.textIssend.setTextColor(getActivity().getResources().getColor(R.color.gray));
        }else {
            holder.textIssend.setText("强制发送失败");
            holder.textIssend.setTextColor(getActivity().getResources().getColor(R.color.red));
        }

        if(snPrintInfo.isUpData()){
            holder.textIsUpdata.setText("已上传");
            holder.textIsUpdata.setTextColor(getActivity().getResources().getColor(R.color.lawngreen));
        }else {
            holder.textIsUpdata.setText("未上传");
            holder.textIsUpdata.setTextColor(getActivity().getResources().getColor(R.color.red));
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private RoundTextView textNum;
        private TextView textSn;
        private TextView textToken;
        private TextView textRxDelay;
        private TextView textRssi;
        private TextView textTishi;
        private RoundTextView textSet;
        private RoundTextView textSend;
        private RoundTextView textDelete;
        private TextView textIssend;
        private TextView textIsUpdata;

        public ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            textNum = (RoundTextView) view.findViewById(R.id.text_num);
            textSn = (TextView) view.findViewById(R.id.text_sn);
            textToken = (TextView) view.findViewById(R.id.text_Token);
            textRxDelay = (TextView) view.findViewById(R.id.text_rxDelay);
            textRssi = (TextView) view.findViewById(R.id.text_rssi);
            textTishi = (TextView) view.findViewById(R.id.text_tishi);
            textSet = (RoundTextView) view.findViewById(R.id.text_set);
            textSend = (RoundTextView) view.findViewById(R.id.text_send);
            textDelete = (RoundTextView) view.findViewById(R.id.text_delete);
            textIssend=view.findViewById(R.id.text_issend);
            textIsUpdata=view.findViewById(R.id.text_isupdata);

            textDelete.setText(deleteText);
            textSend.setOnClickListener(v -> {
                try {
                    onSnPrintListener.onSend(getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            textSet.setOnClickListener(v -> {
                try {
                    onSnPrintListener.onSetXinDao(getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            textDelete.setOnClickListener(v -> {
                try {
                    onSnPrintListener.onUpData(getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            textNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSnPrintListener.onSetRxDelay(getAdapterPosition());
                }
            });

        }
    }
}
