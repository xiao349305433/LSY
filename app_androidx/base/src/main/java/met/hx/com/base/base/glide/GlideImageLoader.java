package met.hx.com.base.base.glide;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * banner适配
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        LogUtils.i("图片地址"+(String) path);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
