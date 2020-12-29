package wu.loushanyun.com.libraryfive.p.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;

import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.baseconfig.C;
import wu.loushanyun.com.libraryfive.R;

public class ImageBinder extends ItemViewBinder<String, ImageBinder.ViewHolder> {
    private Activity activity;

    public ImageBinder(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.l_five_base_location_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        GlideUtil.display(activity, holder.imageView,item,R.drawable.base_chat_img);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(v ->
                    ARouter.getInstance().build(C.FullViewPictureActivity)
                    .withStringArrayList("paths", (ArrayList<String>) getAdapter().getItems())
                    .withBoolean("hasDelete", false)
                    .withInt("num",getAdapterPosition())
                    .navigation(activity,getAdapterPosition()));

        }
    }
}
