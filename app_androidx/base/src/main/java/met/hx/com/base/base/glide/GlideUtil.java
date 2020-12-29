package met.hx.com.base.base.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import met.hx.com.librarybase.image_util.GlideCircleTransform;
import met.hx.com.librarybase.image_util.GlideRoundTransform;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GlideUtil {


    /**
     * 为解决 You cannot start a load for a destroyed activity 这个异常
     *
     * @param mGlide
     * @param mImageView
     * @param url
     * @param mLoading
     */
    public static void display(RequestManager mGlide, ImageView mImageView, String url, int mLoading) {
        LogUtils.i("图片地址===" + url);
        /**
         *  WeakReference是解决：默认情况下，Glide会根据with()使用的Activity或Fragment的生命周期自动调整资源请求以及资源回收。
         但是如果有很占内存的Fragment或Activity不销毁而仅仅是隐藏视图，那么这些图片资源就没办法及时回收，即使是GC的时候。
         */
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            mGlide
                    // 加载URL
                    .load(url)
                    // 添加图片淡入加载的效果
                    .crossFade()
                    // 使得图片在有占位图的同时下载并且实时显示出图片
                    .dontAnimate()
                    // 缩放图片
                    .fitCenter()
                    // 加载中显示的图片
                    .placeholder(mLoading)
                    // 加载错误显示的图片
                    .error(mLoading)
                    // 缓存图片，当前设置为只缓存转换后的资源（最终的图像，即降低分辨率后的（或者是转换后的）
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
        }
    }


    /**
     * ImageView正常加载
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     */
    public static void display(Context mContext, ImageView mImageView, String url, int mLoading) {
        LogUtils.i("图片地址===" + url);
        /**
         *  WeakReference是解决：默认情况下，Glide会根据with()使用的Activity或Fragment的生命周期自动调整资源请求以及资源回收。
         但是如果有很占内存的Fragment或Activity不销毁而仅仅是隐藏视图，那么这些图片资源就没办法及时回收，即使是GC的时候。
         */
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(mContext)
                    // 加载URL
                    .load(url)
                    // 添加图片淡入加载的效果
                    .crossFade()
                    // 使得图片在有占位图的同时下载并且实时显示出图片
                    .dontAnimate()
                    // 缩放图片
                    .fitCenter()
                    // 加载中显示的图片
                    .placeholder(mLoading)
                    // 加载错误显示的图片
                    .error(mLoading)
                    // 缓存图片，当前设置为只缓存转换后的资源（最终的图像，即降低分辨率后的（或者是转换后的）
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
        }
    }


    /**
     * 自定义View加载静态图
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     */
    public static void display(Context mContext, final View mImageView, String url, int mLoading) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(mLoading)
                .error(mLoading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setBackgroundDrawable(new BitmapDrawable(resource));
                    }
                });
    }

    public static void display(Context mContext, ImageView mImageView, int resource) {
        Glide.with(mContext)
                .load(resource)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mImageView);
    }

    public static void display(Context mContext, ImageView mImageView, byte[] bytes) {
        Glide.with(mContext)
                .load(bytes)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mImageView);
    }



    public static void display(Context mContext, ImageView mImageView, String filePath) {
        Glide.with(mContext)
                .load(filePath)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mImageView);
    }

    public static void display(Context mContext, String url, int mLoading, SimpleTarget simpleTarget) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(mLoading)
                .error(mLoading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(simpleTarget);
    }

    public static void display(Context mContext, String url, int mLoading, RequestListener requestListener) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(mLoading)
                .error(mLoading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(requestListener);
    }

    /**
     * 加载Gif动态图
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     */
    public static void displayGif(Context mContext, ImageView mImageView, String url, int mLoading) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(mContext)
                    .load(url)
                    .asGif()
                    .fitCenter()
                    .placeholder(mLoading)
                    .error(mLoading)
                    .dontAnimate() //去掉显示动画
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(target);
        }
    }
    /**
     * 加载本地Gif动态图
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param int        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     */
    public static void displayGif(Context mContext, ImageView mImageView,int  url, int mLoading) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(mContext)
                    .load(url)
                    .asGif()
                    .fitCenter()
                    .placeholder(mLoading)
                    .error(mLoading)
                    .dontAnimate() //去掉显示动画
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(target);
        }
    }


    /**
     * 加载有圆角的图片
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     * @param dp         圆角的弧度
     */
    public static void displayRound(Context mContext, ImageView mImageView, String url, int mLoading, int dp) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(mContext)
                    .load(url)
                    .crossFade()
                    .fitCenter()
                    .placeholder(mLoading)
                    .error(mLoading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideRoundTransform(mContext, dp))
                    .into(target);
        }
    }

    /**
     * 加载有圆角的图片
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     * @param dp         圆角的弧度
     */
    public static void displayRound(Context mContext, final View mImageView, String url, int mLoading, int dp) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(mLoading)
                .error(mLoading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideRoundTransform(mContext, dp))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setBackgroundDrawable(new BitmapDrawable(resource));
                    }
                });
    }

    /**
     * 加载圆形头像
     *
     * @param mContext   上下文,用于绑定生命周期来移除被杀死界面还在加载中的请求
     * @param url        图片地址
     * @param mImageView
     * @param mLoading   占位图（加载中和图片加载错误时显示）
     */
    public static void displayCircle(Context mContext, ImageView mImageView, String url, int mLoading) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(mContext)
                    .load(url)
                    .crossFade()
                    .fitCenter()
                    .placeholder(mLoading)
                    .error(mLoading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(mContext))
                    .into(target);
        }
    }


    /**
     * 获取图片的缓存，并转换成Bitmap
     *
     * @param mContext
     * @param url
     * @param listener 回调处理Bitmap。
     */
    public static void getBitmapFromCache(final Context mContext, String url, final GetBitmapListener listener) {
        String finalUrl = url;
        new Thread() {
            @Override
            public void run() {
                super.run();
                File file = null;
                try {
                    // 此方法获取缓存前提是必须已经加载过图片并设置了缓存模式。
                    file = Glide.with(mContext).load(finalUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = null;
                if (file != null) {
                    String path = file.getPath();
                    bitmap = BitmapFactory.decodeFile(path);
                }
                listener.getBitmap(bitmap);
            }
        }.start();
    }

    public interface GetBitmapListener {
        void getBitmap(Bitmap bitmap);
    }


    /**
     * 获取图片的缓存，并转换成Bitmap
     *
     * @param mContext
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromCache(Context mContext, String url) {
        File file = null;
        try {
            // 此方法获取缓存前提是必须已经加载过图片并设置了缓存模式。
            file = Glide.with(mContext).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        if (file != null) {
            String path = file.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        return bitmap;
    }


    /**
     * 停止滑动时，调用此方法，可恢复图片加载
     *
     * @param mContext
     */
    public static void resumeRequests(Context mContext) {
        Glide.with(mContext).resumeRequests();
    }

    /**
     * 滑动时，调用此方法，可暂停图片加载
     *
     * @param mContext
     */
    public static void pauseRequests(Context mContext) {
        Glide.with(mContext).pauseRequests();
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 清除内存中的缓存 必须在UI线程中调用
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘中的缓存 必须在后台线程中调用，建议同时clearMemory()
     *
     * @param context
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

}






