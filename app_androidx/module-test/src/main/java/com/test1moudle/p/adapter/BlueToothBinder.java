package com.test1moudle.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.test1moudle.R;

import met.hx.com.base.base.multitype.ItemViewBinder;

public class BlueToothBinder extends ItemViewBinder<BleDevice, BlueToothBinder.ViewHolder> {
    private OnClickTextListener onClickTextListener;

    public BlueToothBinder(OnClickTextListener onClickTextListener) {
        this.onClickTextListener = onClickTextListener;
    }

    @NonNull
    @Override
    protected BlueToothBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_test_item_bluetooth, parent, false);
        return new BlueToothBinder.ViewHolder(root);
    }

    public interface OnClickTextListener {
        void onClickText(BleDevice item);

        void onClickButton(BleDevice item);
    }

    @Override
    protected void onBindViewHolder(@NonNull BlueToothBinder.ViewHolder holder, @NonNull BleDevice item) {
        holder.textRssi.setText(item.getRssi() + "");
        holder.textName.setText(item.getName() + "");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMain11;
        private TextView textName;
        private TextView textRssi;
        private Button buttonWrite;

        ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            textName = (TextView) view.findViewById(R.id.text_name);
            textRssi = (TextView) view.findViewById(R.id.text_rssi);
            buttonWrite = (Button) view.findViewById(R.id.button_write);

            cardMain11.setOnClickListener(v -> {
                try {
                    onClickTextListener.onClickText(getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            buttonWrite.setOnClickListener(v -> {
                try {
                    onClickTextListener.onClickButton(getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
