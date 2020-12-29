package met.hx.com.base.base.matisse;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;

import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.lang.ref.WeakReference;
import java.util.Set;

import met.hx.com.base.R;

/**
 * Created by huxu on 2017/11/23.
 * 图片选择
 */

public class MatisseUtil {

    /**
     * 只是一个通用的例子，如有特殊情况，请自己写，api在后面
     *
     * @param activity
     * @param requestCode 在 onActivityResult获取路径
     *                    LogUtils.i("第一数据", Matisse.obtainResult(data).toString());
     *                    LogUtils.i("第二数据", Matisse.obtainPathResult(data).toString());
     */
    public static void choosePicture(Activity activity, Set<MimeType> mimeTypes, int maxCount, int requestCode,String FILE_PROVIDER) {
        WeakReference<Activity> mActivityRef = new WeakReference<Activity>(activity);
        RxPermissions rxPermissions = new RxPermissions(mActivityRef.get());
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                // 用户已经同意该权限
                Matisse.from(mActivityRef.get())
                        .choose(mimeTypes, false)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(new CaptureStrategy(true, FILE_PROVIDER))
                        .maxSelectable(maxCount)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(DensityUtil.dp2px(120))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.BaseMyMatisse)
                        .forResult(requestCode);
            }
        });
    }

    public static void choosePicture(Fragment fragment, Set<MimeType> mimeTypes, int maxCount, int requestCode, String FILE_PROVIDER) {
        WeakReference<Fragment> mActivityRef = new WeakReference<Fragment>(fragment);
        RxPermissions rxPermissions = new RxPermissions(mActivityRef.get().getActivity());
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                // 用户已经同意该权限
                Matisse.from(mActivityRef.get())
                        .choose(mimeTypes, false)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(new CaptureStrategy(true, FILE_PROVIDER))
                        .maxSelectable(maxCount)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(DensityUtil.dp2px(120))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.BaseMyMatisse)
                        .forResult(requestCode);
            }
        });
    }


    /**
     * 方法名	描述	参数
     showSingleMediaType()	仅仅显示一种媒体类型	Boolean
     from()	传递当前的Activity或者fragment	MainActivity.this
     choose()	显示图片的类型	MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF) 或者MimeType.ofAll()
     countable()	是否有序选择图片	Boolean
     maxSelectable()	最大的选择数量	int类型 多少都可以
     addFilter()	添加过滤器	new GifSizeFilter(320, 320, 5 Filter.K Filter.K)，自己重写过滤器
     gridExpectedSize()	每个图片方格的大小	120dp
     restrictOrientation()	设置图像选择和预览活动所需的方向	ActivityInfo.SCREEN_ORIENTATION_PORTRAIT或者
     thumnailScale()	缩放比例	0.1-1之间，一般0.85f
     imageEngine()	使用图片的加载方式	new GlideEngine()或者new PicassoEngine()
     theme()	主题的设置	R.style.Matisse_Zhihu 或者 R.style.Matisse_Dracula
     forResult()	请求码	REQUEST_CODE_CHOOSE
     包含的类

     类名	描述
     ImageEngine	图片加载接口，方便后面根据Glide和Picasso分别实现
     GlideEngine	Glide实现ImageEngine
     PicassoEngine	Picasso实现ImageEngine
     Filter	过滤条件抽象类，我们可以通过集成Filter实现对应的过滤条件来对图片进行筛选，可以添加多个Filter
     Album	相册Entity
     CaptureStrategy	拍照相关，媒体处理authority
     IncapableCause	信息处理，toast和dialog
     Item	选择媒体界面的Item
     SelectionSpec	选择参数类
     AlbumLoader	相册CursorLoader
     AlbumMediaLoader	图片和视频CursorLoader
     AlbumMediaCollection	AlbumMediaLoader回调
     SelectedItemCollection	被选中项集合
     internal/ui包	界面显示的Adapter,自定义视图，Fragment和Activity
     internal/utils包	工具类
     MatisseActivity	关键类，执行选择媒体操作的时候展示出来的Activity
     Matisse	开源库的入口和出口，用来传递Activity和Fragment，创建SelectionSpecBuilder和返回结果
     MimeType	媒体类型
     SelectionSpecBuilder	Build构造类，用来传递参数
     */

}
