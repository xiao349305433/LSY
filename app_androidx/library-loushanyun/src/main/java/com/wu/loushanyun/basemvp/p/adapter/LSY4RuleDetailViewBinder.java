package com.wu.loushanyun.basemvp.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundRelativeLayout;
import com.wu.loushanyun.R;
import com.wu.loushanyun.basemvp.m.ModuleRuleDetail;

import met.hx.com.base.base.multitype.ItemViewBinder;

public class LSY4RuleDetailViewBinder extends ItemViewBinder<ModuleRuleDetail.DataBean, LSY4RuleDetailViewBinder.ViewHolder> {

    private ModuleRuleDetailListener moduleRuleDetailListener;

    public LSY4RuleDetailViewBinder(ModuleRuleDetailListener moduleRuleDetailListener) {
        this.moduleRuleDetailListener = moduleRuleDetailListener;
    }

    public interface ModuleRuleDetailListener {

        void onDelete(ModuleRuleDetail.DataBean item);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_item_rule_detail, parent, false);
        return new LSY4RuleDetailViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ModuleRuleDetail.DataBean item) {
        holder.textBitstart.setText(item.getStartBit() + "");
        holder.textBitend.setText(item.getEndBit() + "");
        holder.textMiaoshu.setText(item.getChineseName() + "");
        if (item.getByteMode() == 1) {
            holder.textMoshi.setText("大端模式");
        } else {
            holder.textMoshi.setText("小端模式");
        }
        holder.textMinling.setText(item.getCommand());
        holder.textMaxbyte.setText(item.getMaxByte()+"");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private RoundRelativeLayout relativeDetails;
        private LinearLayout linearStartbit;
        private TextView textBitstart;
        private LinearLayout linearBitend;
        private TextView textBitend;
        private LinearLayout linearTiji;
        private TextView textMiaoshu;
        private LinearLayout linearMoshi;
        private TextView textMoshi;
        private LinearLayout linearMinling;
        private TextView textMinling;
        private LinearLayout linearMaxbyte;
        private TextView textMaxbyte;

        public ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            relativeDetails = (RoundRelativeLayout) view.findViewById(R.id.relative_details);
            linearStartbit = (LinearLayout) view.findViewById(R.id.linear_startbit);
            textBitstart = (TextView) view.findViewById(R.id.text_bitstart);
            linearBitend = (LinearLayout) view.findViewById(R.id.linear_bitend);
            textBitend = (TextView) view.findViewById(R.id.text_bitend);
            linearTiji = (LinearLayout) view.findViewById(R.id.linear_tiji);
            textMiaoshu = (TextView) view.findViewById(R.id.text_miaoshu);
            linearMoshi = (LinearLayout) view.findViewById(R.id.linear_moshi);
            textMoshi = (TextView) view.findViewById(R.id.text_moshi);
            linearMinling = (LinearLayout) view.findViewById(R.id.linear_minling);
            textMinling = (TextView) view.findViewById(R.id.text_minling);
            linearMaxbyte = (LinearLayout) view.findViewById(R.id.linear_maxbyte);
            textMaxbyte = (TextView) view.findViewById(R.id.text_maxbyte);
        }
    }
}

