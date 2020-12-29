package met.hx.com.base.base.matisse;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseconfig.PathConfig;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.views.MarqueeTextView;

@Route(path = C.FullViewPictureActivity)
public class FullViewPictureActivity extends BaseNoPresenterActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    @Autowired
    public int num;
    private ArrayList<String> paths;
    private boolean hasDelete = true;

    private ViewPager pager;
    private PhotoPagerAdapter adapter;
    private MarqueeTextView mTextViewTitle;
    private LinearLayout linearAll;
    private int colorBackground;
    private boolean hasClickLongMenu;
    private PopupWindow bottomPop;


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomPop.dismiss();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.base_activity_photo_pager;
        //顶部title的颜色
        int color = getIntent().getIntExtra("titleColor", 0);
        hasClickLongMenu = getIntent().getBooleanExtra("hasClickLongMenu", false);
        //背景的颜色
        colorBackground = getIntent().getIntExtra("bgColor", 0);
        ba.mTitleBackgroundColorId = color;
        //是否显示右边的删除
        hasDelete = getIntent().getBooleanExtra("hasDelete", true);
        if (hasDelete) {
            ba.mTitleRightText = "删除";
        }
    }

    @Override
    protected void initView() {
        paths = getIntent().getStringArrayListExtra("paths");
        mTextViewTitle = (MarqueeTextView) findViewById(R.id.base_text_middle);
        if (mTextViewTitle != null) {
            if (num + 1 > paths.size()) {
                mTextViewTitle.setText(paths.size() + "/" + paths.size());
                LogUtils.e("传入的num比实际的大" + num + ",,,," + paths.size());
                num = paths.size() - 1;
            } else {
                mTextViewTitle.setText((num + 1) + "/" + paths.size());
            }
        }
        pager = (ViewPager) findViewById(R.id.view_pager);
        linearAll = (LinearLayout) findViewById(R.id.linear_all);
        if (colorBackground != 0) {
            linearAll.setBackgroundResource(colorBackground);
        }
        adapter = new PhotoPagerAdapter(paths, Glide.with(this));
        pager.setAdapter(adapter);
        pager.setCurrentItem(num);
        pager.setOnPageChangeListener(this);
        bottomPop = getPopupWindow();

    }

    private PopupWindow getPopupWindow() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.base_fullphoto_pop, null);
        PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new ColorDrawable(0));
        LinearLayout mDialog = (LinearLayout) view.findViewById(R.id.dialog);
        TextView mTextSave = (TextView) view.findViewById(R.id.text_save);
        TextView mTextCancle = (TextView) view.findViewById(R.id.text_cancle);
        mTextSave.setOnClickListener(this);
        mTextCancle.setOnClickListener(this);
        return pop;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage("确定要删除吗？").setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String url = paths.get(pager.getCurrentItem());
                Intent data = new Intent();
                data.putExtra("delete", true);
                data.putExtra("path", url);
                data.putExtra("num", num);
                setResult(RESULT_OK, data);
                finish();
            }
        }).setPositiveButton("取消", null).create().show();
    }


    @Override
    public void onRightClick(View item) {
        if (hasDelete) {
            super.onRightClick(item);
            showDialog();
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        if (mTextViewTitle != null) {
            mTextViewTitle.setText((arg0 + 1) + "/" + paths.size());
        }
        num = arg0;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_save) {
            ToastManager.getInstance(FullViewPictureActivity.this).show("正在保存....");
            File file=new File(PathConfig.getImagePath()+"/"+System.currentTimeMillis()+"."+"jpeg");
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter -> {
                        Bitmap bitmap = GlideUtil.getBitmapFromCache(this, paths.get(num));
                        boolean save = ImageUtils.save(bitmap, file, Bitmap.CompressFormat.JPEG);

                        emitter.onNext(save);
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            Uri uri = Uri.fromFile(file);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            ToastManager.getInstance(FullViewPictureActivity.this).show("保存成功");
                            bottomPop.dismiss();
                        } else {
                            ToastManager.getInstance(FullViewPictureActivity.this).show("保存失败");
                            bottomPop.dismiss();
                        }
                    }));
        } else if (v.getId() == R.id.text_cancle) {
            bottomPop.dismiss();
        }
    }

    class PhotoPagerAdapter extends PagerAdapter {
        private ArrayList<String> paths;
        private RequestManager mGlide;

        public PhotoPagerAdapter(ArrayList<String> paths, RequestManager mGlide) {
            this.paths = paths;
            this.mGlide = mGlide;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            if (hasClickLongMenu) {
                photoView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        bottomPop.showAtLocation(linearAll, Gravity.BOTTOM, 0, 0);
                        return false;
                    }
                });
            }
            GlideUtil.display(mGlide, photoView, paths.get(position), R.drawable.base_chat_img);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
