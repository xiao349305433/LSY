package com.wu.loushanyun.basemvp.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wu.loushanyun.R;

import met.hx.com.base.base.multitype.ItemViewBinder;

public class CityTextViewBinder extends ItemViewBinder<String, CityTextViewBinder.ViewHolder> {

    private OnClickTextListener onClickTextListener;
    public interface OnClickTextListener{
       void OnClickText(String s, int position);
    }

    public CityTextViewBinder(OnClickTextListener onClickTextListener) {
        this.onClickTextListener = onClickTextListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_item_text, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String path) {
        holder.defaultItemCityNameTv.setText(path);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView defaultItemCityNameTv;

        public ViewHolder(View view) {
            super(view);
            defaultItemCityNameTv = (TextView) view.findViewById(R.id.default_item_city_name_tv);
            defaultItemCityNameTv.setOnClickListener(v -> {
                try {
                    onClickTextListener.OnClickText(getContentByViewHolder(this),getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
