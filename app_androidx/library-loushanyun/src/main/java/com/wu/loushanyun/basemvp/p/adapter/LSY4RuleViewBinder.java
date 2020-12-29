package com.wu.loushanyun.basemvp.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.R;
import com.wu.loushanyun.basemvp.m.ModuleRule;

import met.hx.com.base.base.multitype.ItemViewBinder;

public class LSY4RuleViewBinder extends ItemViewBinder<ModuleRule.DataBean, LSY4RuleViewBinder.ViewHolder> {

    private OnLSY4RuleListener onLSY4RuleListener;
    private int themeColor;


    public LSY4RuleViewBinder(OnLSY4RuleListener onLSY4RuleListener, int themeColor) {
        this.onLSY4RuleListener = onLSY4RuleListener;
        this.themeColor = themeColor;
    }

    public interface OnLSY4RuleListener{
        void onPitch(int position,ModuleRule.DataBean dataBean);
        void onDetails(int position,ModuleRule.DataBean dataBean);
    }


    @NonNull
    @Override
    protected LSY4RuleViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_item_rule, parent, false);
        return new LSY4RuleViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull LSY4RuleViewBinder.ViewHolder holder, @NonNull ModuleRule.DataBean item) {
        holder.textBanben.setText(item.getProtocolVersion()+"");
        holder.textRata.setText("备注："+item.getRemark());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private RoundTextView textBanben;
        private TextView textRata;
        private RoundTextView textDetails;
        private RoundTextView textPitch;
        public ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            textBanben = (RoundTextView) view.findViewById(R.id.text_banben);
            textRata = (TextView) view.findViewById(R.id.text_rata);
            textDetails = (RoundTextView) view.findViewById(R.id.text_details);
            textPitch = (RoundTextView) view.findViewById(R.id.text_pitch);
            textBanben.getDelegate().setBackgroundColor(themeColor);
            textPitch.getDelegate().setBackgroundColor(themeColor);
            textDetails.getDelegate().setBackgroundColor(themeColor);
            textDetails.setOnClickListener(v -> {
                try {
                    onLSY4RuleListener.onDetails(getAdapterPosition(),getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            textPitch.setOnClickListener(v -> {
                try {
                    onLSY4RuleListener.onPitch(getAdapterPosition(),getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
