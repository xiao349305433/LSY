package com.wu.loushanyun.basemvp.p.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wu.loushanyun.R;
import com.wu.loushanyun.basemvp.m.PrintBean;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ImageUtils;


public class PrintBeanViewBinder extends ItemViewBinder<PrintBean, PrintBeanViewBinder.ViewHolder> {

    private Context context;
    private onPrinteListener printeListener;

    public PrintBeanViewBinder(Context context,onPrinteListener printeListener) {
        this.context = context;
        this.printeListener = printeListener;
    }

    public interface onPrinteListener {
       void onClickPrint(PrintBean printBean);
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_item_printer, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PrintBean printBean) {
        Drawable drawable = ImageUtils.getDrawableFromId(context, printBean.getLogoId(), R.drawable.l_loushanyun_wifi_black_24dp);
        holder.imageLogo.setImageDrawable(drawable);
        holder.textName.setText(printBean.getTextName());
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         private RelativeLayout relative;
         private ImageView imageLogo;
         private TextView textName;
         ViewHolder(View view) {
            super(view);
            relative = (RelativeLayout) view.findViewById(R.id.relative);
            imageLogo = (ImageView) view.findViewById(R.id.image_logo);
            textName = (TextView) view.findViewById(R.id.text_name);

             relative.setOnClickListener(view1 -> {
                try {
                    printeListener.onClickPrint(getContentByViewHolder(ViewHolder.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
