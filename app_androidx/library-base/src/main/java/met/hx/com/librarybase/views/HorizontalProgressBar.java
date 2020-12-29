package met.hx.com.librarybase.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import met.hx.com.librarybase.R;


/**
 * Created by huxu on 2016/9/8.
 */
public class HorizontalProgressBar extends View {

    private Paint mPaint;
    /**
     * 底部高度
     */
    private int bottomProgressBarHeight;
    /**
     * 进度高度
     */
    private int topProgressBarHeight;
    /**
     * 底色
     */
    private int bottomColor;
    /**
     * 进度色
     */
    private int topColor;
    /**
     * 圆圈颜色
     */
    private int circleColor;
    /**
     * 长度
     */
    private float mRealWidth;


    /**
     * 进度值百分比
     */
    private float percent = 0;

    /**
     * 上下间隔
     */
    private int paddingHeight = 0;
    /**
     * 左右间隔
     */
    private int paddingWidth;
    /**
     * 能否手动滑动
     */
    private boolean canTouchable = true;
    private int circleStrokeWidth;
    private int insideCircleColor;
    private int textColor;
    private int textSize;
    private Paint paintText;
    private boolean hasText;


    //    private Circle[] circles;
//    private Random random;
    private int width;
    private int height;

    public int getCircleStrokeWidth() {
        return circleStrokeWidth;
    }

    public void setCircleStrokeWidth(int circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    public int getInsideCircleColor() {
        return insideCircleColor;
    }

    public void setInsideCircleColor(int insideCircleColor) {
        this.insideCircleColor = insideCircleColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        paintText.setColor(topColor);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        paintText.setTextSize(textSize);
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public int getBottomProgressBarHeight() {
        return bottomProgressBarHeight;
    }

    public void setBottomProgressBarHeight(int bottomProgressBarHeight) {
        this.bottomProgressBarHeight = bottomProgressBarHeight;
    }

    public int getTopProgressBarHeight() {
        return topProgressBarHeight;
    }

    public void setTopProgressBarHeight(int topProgressBarHeight) {
        this.topProgressBarHeight = topProgressBarHeight;
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
    }

    public int getTopColor() {
        return topColor;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public float getmRealWidth() {
        return mRealWidth;
    }

    public void setmRealWidth(float mRealWidth) {
        this.mRealWidth = mRealWidth;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public int getPaddingHeight() {
        return paddingHeight;
    }

    public void setPaddingHeight(int paddingHeight) {
        this.paddingHeight = paddingHeight;
    }

    public int getPaddingWidth() {
        return paddingWidth;
    }

    public void setPaddingWidth(int paddingWidth) {
        this.paddingWidth = paddingWidth;
    }

    public boolean isCanTouchable() {
        return canTouchable;
    }

    public void setCanTouchable(boolean canTouchable) {
        this.canTouchable = canTouchable;
    }

    public HorizontalProgressBar(Context context) {
        super(context);
        init();
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        paintText = new Paint();
        bottomColor = Color.parseColor("#666666");
        topColor = getResources().getColor(R.color.base_utils_yellow_main);
        circleColor = getResources().getColor(R.color.base_utils_indianred);
        insideCircleColor = Color.WHITE;
        textColor = Color.BLACK;
        textSize = 10;
        circleStrokeWidth = 3;
        paintText.setColor(textColor);
        paintText.setTextSize(textSize);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setAntiAlias(true);
//        circles = new Circle[10];
//        for (int i = 0; i < circles.length; i++) {
//            circles[i] = new Circle(2, Color.WHITE);
//        }
//        random = new Random();
    }


    public void seekTo(float percent) {
        this.percent = percent;
        if (percent <= 1f) {
            invalidate();
        }else {
            this.percent=1f;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(bottomColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(bottomProgressBarHeight / 2);
        mPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        Paint.FontMetrics fm = paintText.getFontMetrics();
        /**测量文字的高度*/
        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);

        int y = getMeasuredHeight() / 2;
        canvas.drawLine(paddingWidth, y, mRealWidth + paddingWidth, y, mPaint);

        mPaint.setColor(topColor);
        mPaint.setMaskFilter(null);
        mPaint.setStrokeWidth(bottomProgressBarHeight / 2);
        float endX = (percent) * mRealWidth;
        canvas.drawLine(paddingWidth, y, endX + paddingWidth, y, mPaint);
        mPaint.setColor(circleColor);
        mPaint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        float xCircle = endX + paddingWidth;
        canvas.drawCircle(xCircle, y, bottomProgressBarHeight / 2, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(xCircle, y, bottomProgressBarHeight / 2 - circleStrokeWidth, mPaint);
        if(hasText){
            String text = (int) Math.floor(percent * 100) + "%";
            canvas.drawText(text, xCircle, y + textHeight / 3, paintText);
        }
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        topProgressBarHeight = height - 2 * paddingHeight;
        bottomProgressBarHeight = height - 2 * paddingHeight;
        mRealWidth = width - paddingWidth * 2;
        paddingWidth = height / 2;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (canTouchable) {
                    if (event.getX() > paddingWidth && event.getX() < mRealWidth + paddingWidth) {
                        percent = (event.getX() - paddingWidth) / mRealWidth;
                        invalidate();
                    }
                }
                break;

        }

        return true;
    }
}


/**
 * Paint类介绍
 * <p>
 * Paint即画笔，在绘图过程中起到了极其重要的作用，画笔主要保存了颜色，
 * 样式等绘制信息，指定了如何绘制文本和图形，画笔对象有很多设置方法，
 * 大体上可以分为两类，一类与图形绘制相关，一类与文本绘制相关。
 * <p>
 * 1.图形绘制
 * setARGB(int a,int r,int g,int b);
 * 设置绘制的颜色，a代表透明度，r，g，b代表颜色值。
 * <p>
 * setAlpha(int a);
 * 设置绘制图形的透明度。
 * <p>
 * setColor(int color);
 * 设置绘制的颜色，使用颜色值来表示，该颜色值包括透明度和RGB颜色。
 * <p>
 * setAntiAlias(boolean aa);
 * 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
 * <p>
 * setDither(boolean dither);
 * 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
 * <p>
 * setFilterBitmap(boolean filter);
 * 如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示
 * 速度，本设置项依赖于dither和xfermode的设置
 * <p>
 * setMaskFilter(MaskFilter maskfilter);
 * 设置MaskFilter，可以用不同的MaskFilter实现滤镜的效果，如滤化，立体等       *
 * setColorFilter(ColorFilter colorfilter);
 * 设置颜色过滤器，可以在绘制颜色时实现不用颜色的变换效果
 * <p>
 * setPathEffect(PathEffect effect);
 * 设置绘制路径的效果，如点画线等
 * <p>
 * setShader(Shader shader);
 * 设置图像效果，使用Shader可以绘制出各种渐变效果
 * <p>
 * setShadowLayer(float radius ,float dx,float dy,int color);
 * 在图形下面设置阴影层，产生阴影效果，radius为阴影的角度，dx和dy为阴影在x轴和y轴上的距离，color为阴影的颜色
 * <p>
 * setStyle(Paint.Style style);
 * 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
 * <p>
 * setStrokeCap(Paint.Cap cap);
 * 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
 * Cap.ROUND,或方形样式Cap.SQUARE
 * <p>
 * setSrokeJoin(Paint.Join join);
 * 设置绘制时各图形的结合方式，如平滑效果等
 * <p>
 * setStrokeWidth(float width);
 * 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度
 * <p>
 * setXfermode(Xfermode xfermode);
 * 设置图形重叠时的处理方式，如合并，取交集或并集，经常用来制作橡皮的擦除效果
 * <p>
 * 2.文本绘制
 * setFakeBoldText(boolean fakeBoldText);
 * 模拟实现粗体文字，设置在小字体上效果会非常差
 * <p>
 * setSubpixelText(boolean subpixelText);
 * 设置该项为true，将有助于文本在LCD屏幕上的显示效果
 * <p>
 * setTextAlign(Paint.Align align);
 * 设置绘制文字的对齐方向
 * <p>
 * setTextScaleX(float scaleX);
 * 设置绘制文字x轴的缩放比例，可以实现文字的拉伸的效果
 * <p>
 * setTextSize(float textSize);
 * 设置绘制文字的字号大小
 * <p>
 * setTextSkewX(float skewX);
 * 设置斜体文字，skewX为倾斜弧度
 * <p>
 * setTypeface(Typeface typeface);
 * 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
 * <p>
 * setUnderlineText(boolean underlineText);
 * 设置带有下划线的文字效果
 * <p>
 * setStrikeThruText(boolean strikeThruText);
 * 设置带有删除线的效果
 */