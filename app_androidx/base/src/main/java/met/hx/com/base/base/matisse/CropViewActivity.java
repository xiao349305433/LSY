package met.hx.com.base.base.matisse;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.isseiaoki.simplecropview.CropImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.CompletableSource;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseconfig.PathConfig;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.FileUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

/**
 * Created by huxu on 2017/11/25.
 */
@Route(path = C.CropViewActivity)
public class CropViewActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    private CropImageView mCropView;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private Uri mSourceUri;
    /***
     * 图片的URi
     */
    @Autowired
    public String mStringUri;
    /**
     * 剪裁模式
     * 13种模式
     */
    @Autowired
    public int type;

    /**
     * 是否显示底部菜单
     */
    @Autowired
    public boolean showBottomMenu=true;

    /**
     * 例如
     * ArrayList<Integer> list = new ArrayList<>();
     * list.add(7);
     * list.add(5);  自定义模式
     */
    private ArrayList<Integer> custom;


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.base_activity_crop;
        ba.mTitleText = "剪裁图片";
        ba.mTitleRightText = "完成";
    }

    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        compositeDisposable.add(cropImage());
    }

    @Override
    protected void initView() {
        custom = getIntent().getIntegerArrayListExtra("custom");
        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        findViewById(R.id.tab_bar).setVisibility(showBottomMenu?View.VISIBLE:View.GONE);
        findViewById(R.id.buttonFitImage).setOnClickListener(this);
        findViewById(R.id.button1_1).setOnClickListener(this);
        findViewById(R.id.button3_4).setOnClickListener(this);
        findViewById(R.id.button4_3).setOnClickListener(this);
        findViewById(R.id.button9_16).setOnClickListener(this);
        findViewById(R.id.button16_9).setOnClickListener(this);
        findViewById(R.id.buttonFree).setOnClickListener(this);
        findViewById(R.id.buttonRotateLeft).setOnClickListener(this);
        findViewById(R.id.buttonRotateRight).setOnClickListener(this);
        findViewById(R.id.buttonCustom).setOnClickListener(this);
        findViewById(R.id.buttonCircle).setOnClickListener(this);
        findViewById(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(this);
        switch (type) {
            case 1:
                mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                break;
            case 2:
                mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                break;
            case 3:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                break;
            case 4:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            case 5:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                break;
            case 6:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                break;
            case 7:
                mCropView.setCustomRatio(7, 5);
                break;
            case 8:
                mCropView.setCropMode(CropImageView.CropMode.FREE);
                break;
            case 9:
                mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                break;
            case 10:
                mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
                break;
            case 11:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
            case 12:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
            default:
        }
        if (custom != null && custom.size() >= 2) {
            mCropView.setCustomRatio(custom.get(0), custom.get(1));
        }
        if (XHStringUtil.isEmpty(mStringUri, false)) {
            // default data
            mSourceUri = FileUtils.getUriFromDrawableResId(this, R.drawable.base_photo);
        } else {
            mSourceUri = Uri.parse(mStringUri);
        }
        compositeDisposable.add(loadImage(mSourceUri));
    }

    private Disposable loadImage(final Uri uri) {
        return new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean granted)
                            throws Exception {
                        return granted;
                    }
                })
                .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull Boolean aBoolean)
                            throws Exception {
                        return mCropView.load(uri)
                                .useThumbnail(true)
                                .executeAsCompletable();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    private Disposable cropImage() {

        return mCropView.crop(mSourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(@NonNull Bitmap bitmap)
                            throws Exception {
                        LogUtils.i("哈哈哈哈哈1111");
                        return mCropView.save(bitmap)
                                .compressFormat(mCompressFormat)
                                .executeAsSingle(createSaveUri());
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable)
                            throws Exception {
                        LoadingDialogUtil.showByEvent("剪裁中...", this.getClass().getName());
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        Intent intent = new Intent();
                        intent.putExtra("crop_uri", uri);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable)
                            throws Exception {
                    }
                });
    }

    public Uri createSaveUri() {
        return createNewUri(this, mCompressFormat);
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = PathConfig.getImagePath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonFitImage) {
            mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);

        } else if (i == R.id.button1_1) {
            mCropView.setCropMode(CropImageView.CropMode.SQUARE);

        } else if (i == R.id.button3_4) {
            mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);

        } else if (i == R.id.button4_3) {
            mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);

        } else if (i == R.id.button9_16) {
            mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);

        } else if (i == R.id.button16_9) {
            mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);

        } else if (i == R.id.buttonCustom) {
            mCropView.setCustomRatio(7, 5);

        } else if (i == R.id.buttonFree) {
            mCropView.setCropMode(CropImageView.CropMode.FREE);

        } else if (i == R.id.buttonCircle) {
            mCropView.setCropMode(CropImageView.CropMode.CIRCLE);

        } else if (i == R.id.buttonShowCircleButCropAsSquare) {
            mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);

        } else if (i == R.id.buttonRotateLeft) {
            mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);

        } else if (i == R.id.buttonRotateRight) {
            mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);

        }
    }
}
