package wu.loushanyun.com.modulechiptest.p.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.roundview.RoundTextView;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ViewHolder;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.FactoryProductionInfo;
import wu.loushanyun.com.modulechiptest.m.SnPrintInfo;
import wu.loushanyun.com.modulechiptest.m.ThirdProduceInfo;

public class ThirdProduceViewBinder extends ItemViewBinder<ThirdProduceInfo, ThirdProduceViewBinder.ViewHolder> {

    private ThirdProduceListener thirdProduceListener;

    public ThirdProduceViewBinder(ThirdProduceListener thirdProduceListener) {
        this.thirdProduceListener = thirdProduceListener;

    }


    public interface ThirdProduceListener {
        void onUpData(int position);
        void onSend(int position);

    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_chip_item_thirdproduce, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ThirdProduceInfo item) {
        holder.textNum.setText(item.getNum() + "");
        if (XHStringUtil.isEmpty( item.getUserId(), false)) {
            holder.textSn.setText("设备ID:" + "未设置");
        }else {
            holder.textSn.setText("设备ID:" + item.getUserId());
        }

        if(item.isUpData()){
            holder.textIsUpdata.setText("已上传");
            holder.textIsUpdata.setTextColor(getActivity().getResources().getColor(R.color.lawngreen));
        }else {
            holder.textIsUpdata.setText("未上传");
            holder.textIsUpdata.setTextColor(getActivity().getResources().getColor(R.color.red));

        }

        if (XHStringUtil.isEmpty(item.getUserId(), false)) {
            holder.textTishi.setVisibility(View.VISIBLE);
        } else {
            holder.textTishi.setVisibility(View.GONE);
        }

        holder.textTishi.setText(item.getTishi());

        if(item.isSave()){
            holder.textTishi.setVisibility(View.VISIBLE);
            holder.textIsUpdata.setVisibility(View.GONE);
        }else {
            holder.textTishi.setVisibility(View.GONE);
            holder.textIsUpdata.setVisibility(View.VISIBLE);
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private RoundTextView textNum;
        private TextView textSn;
        private TextView textTishi;
        private RoundTextView textSend;
        private RoundTextView textUpdata;
        private TextView textIsUpdata;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMain11 =itemView.findViewById(R.id.card_main_1_1);
            textNum = itemView.findViewById(R.id.text_num);
            textSn = itemView.findViewById(R.id.text_sn);
            textTishi = itemView.findViewById(R.id.text_tishi);
            textSend = itemView.findViewById(R.id.text_send);
             textUpdata= itemView.findViewById(R.id.text_updata);
            textIsUpdata=itemView.findViewById(R.id.text_isupdata);


            textUpdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        thirdProduceListener.onUpData(getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            textSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        thirdProduceListener.onSend(getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


}
