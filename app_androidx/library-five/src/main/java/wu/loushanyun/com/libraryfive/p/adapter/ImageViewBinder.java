package wu.loushanyun.com.libraryfive.p.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.baseconfig.C;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.R;

public class ImageViewBinder extends ItemViewBinder<String, ImageViewBinder.ViewHolder> {
    private int count = 10001;
    private Activity activity;
    private Drawable defaultDrawable;
    private ViewGroup.LayoutParams layoutParams;
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";

    public ImageViewBinder(Activity activity) {
        this.activity = activity;
        defaultDrawable = activity.getResources().getDrawable(R.drawable.l_five_tianjia);
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels >> 2;
        layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_five_base_location_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        if (XHStringUtil.isEmpty(item, false)) {
            holder.imageView.setImageDrawable(defaultDrawable);
        } else {
            GlideUtil.display(activity, holder.imageView, item, R.drawable.base_chat_img);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            final int id = count++;
            itemView.setLayoutParams(layoutParams);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (defaultDrawable.equals(imageView.getDrawable())) {
                        Log.i("yunanhao", "添加图片");
                        int x = 5 - getAdapter().getItemCount();
                        MatisseUtil.choosePicture(activity, MimeType.ofImage(), x <= 0 ? 1 : x, id, fileProvider);
                    } else {
                        Log.i("yunanhao", "查看图片");
                        ArrayList list = new ArrayList();
                        List<?> datalist = getAdapter().getItems();
                        String data;
                        for (int i = 0; i < datalist.size(); i++) {
                            data = String.valueOf(datalist.get(i));
                            if (!XHStringUtil.isEmpty(data, false)) {
                                list.add(data);
                            }
                        }
                        ARouter.getInstance().build(C.FullViewPictureActivity)
                                .withStringArrayList("paths", list)
                                .withBoolean("hasDelete", true)
                                .withInt("num", getAdapterPosition())
                                .navigation(activity, id);
                    }
                }
            });
            imageView = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
