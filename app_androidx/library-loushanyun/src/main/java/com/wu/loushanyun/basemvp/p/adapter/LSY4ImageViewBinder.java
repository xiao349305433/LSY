package com.wu.loushanyun.basemvp.p.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wu.loushanyun.R;

import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;

public class LSY4ImageViewBinder extends ItemViewBinder<String, LSY4ImageViewBinder.ViewHolder> {
    private Activity activity;
    private OnClickImageListener onClickImageListener;

    public LSY4ImageViewBinder(Activity activity,OnClickImageListener onClickImageListener) {
        this.activity = activity;
        this.onClickImageListener = onClickImageListener;
    }

    public interface OnClickImageListener{
       void OnClickImage(String path,int position);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_loushanyun_base_location_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String path) {
        if(!XHStringUtil.isEmpty(path,false)){
            GlideUtil.display(activity, holder.imageView,path,R.drawable.base_chat_img);
        }else {
            GlideUtil.display(activity, holder.imageView,R.drawable.l_loushanyun_tianjia);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            imageView.setOnClickListener(v -> {
                try {
                    onClickImageListener.OnClickImage(getContentByViewHolder(this),getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
