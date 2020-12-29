package met.hx.com.librarybase.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 条形图
 * Created by huxu on 2015/10/13.
 */
public class AutoHistogramView extends View {
    private float textRectSize;
    /**
     * 结果字体颜色
     */
    private int autoResultTextColor;
    /**
     * 金钱字体颜色
     */
    private int autoMoneyTextColor;
    /**
     * 第一个条形图颜色
     */
    private int autoBarChartColor1;
    /**
     * 第二个条形图颜色
     */
    private int autoBarChartColor2;
    /**
     * 第三个条形图颜色
     */
    private int autoBarChartColor3;
    private float autoStart;
    private float autoEnd;
    private float autoTextMargin;
    private int autoLineColor;
    private Paint paintLine = new Paint();
    private Paint paintText = new Paint();
    private Paint paintRect1 = new Paint();
    private Paint paintRect2 = new Paint();
    private Paint paintRect3 = new Paint();
    private Paint paintTextRect1 = new Paint();
    private Paint paintTextRect2 = new Paint();
    private Paint paintTextRect3 = new Paint();
    private int width;
    private int height;
    /**
     * 最大值
     */
    private float autoEvResultMax;
    /**
     * 进度
     */
    private float progressFirst = 100;
    private float progressSecond = 90;
    private float progressThird = 80;
    /**
     * 进度条的宽
     */
    private float autoRateWidth;
    /**
     * 进度百分比
     */
    private float percent1;
    private float percent2;
    private float percent3;


    public AutoHistogramView(Context context) {
        this(context, null);
    }

    public AutoHistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoHistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
//                R.styleable.AutoEvResultLine);
//
//        //获取自定义属性和默认值
//        autoLineColor = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoLineColor, Color.RED);
//        autoResultTextColor = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoResultTextColor, Color.RED);
//        autoMoneyTextColor = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoMoneyTextColor, Color.RED);
//        autoBarChartColor1 = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoBarChartColor1, Color.RED);
//        autoBarChartColor2 = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoBarChartColor2, Color.RED);
//        autoBarChartColor3 = mTypedArray.getColor(R.styleable.AutoEvResultLine_autoBarChartColor3, Color.RED);
//        autoStart = mTypedArray.getDimension(R.styleable.AutoEvResultLine_autoStart, 10);
//        autoEvResultMax = mTypedArray.getInteger(R.styleable.AutoEvResultLine_autoEvResultMax, 100);
//        autoEnd = mTypedArray.getDimension(R.styleable.AutoEvResultLine_autoEnd, 10);
//        textRectSize = mTypedArray.getDimension(R.styleable.AutoEvResultLine_textRectSize, 20);
//        autoTextMargin = mTypedArray.getDimension(R.styleable.AutoEvResultLine_autoTextMargin, 7);
//        mTypedArray.recycle();
    }

    /*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到中心坐标和坐标轴圆心所在的点。
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintLine.setColor(autoLineColor);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paintLine.setPathEffect(effects);
        paintLine.setAntiAlias(true);  //消除锯齿

        paintText.setColor(autoMoneyTextColor);
        paintText.setTextSize(textRectSize);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setAntiAlias(true);
        Paint.FontMetrics fm = paintText.getFontMetrics();

        /**测量文字宽度*/
        float textWidth = paintText.measureText("0");
        float textWidthEnd = paintText.measureText("万万万万");
        float textWidthTwo = paintText.measureText("00");
        /**测量文字的高度*/
        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
        int fenceX = (int) (width - autoStart - autoEnd - textWidthEnd) / 5;
        int fenceY = (int) ((height - autoTextMargin * 3) / 6);
        autoRateWidth = fenceY * 2 / 2.4f;
        percent1 = progressFirst / autoEvResultMax;
        percent2 = progressSecond / autoEvResultMax;
        percent3 = progressThird / autoEvResultMax;
        /**画虚线表格*/
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(0, fenceY * i, width - textWidthEnd, fenceY * i, paintLine);
            if (i < 6) {
                canvas.drawLine(autoStart + fenceX * i, 0, autoStart + fenceX * i, fenceY * 6, paintLine);
            }

        }

        paintTextRect1.setColor(autoBarChartColor1);
        paintTextRect1.setTextSize(textRectSize);
        paintTextRect1.setStyle(Paint.Style.FILL);
        paintTextRect1.setAntiAlias(true);

        paintTextRect2.setColor(autoBarChartColor2);
        paintTextRect2.setTextSize(textRectSize);
        paintTextRect2.setStyle(Paint.Style.FILL);
        paintTextRect2.setAntiAlias(true);

        paintTextRect3.setColor(autoBarChartColor3);
        paintTextRect3.setTextSize(textRectSize);
        paintTextRect3.setStyle(Paint.Style.FILL);
        paintTextRect3.setAntiAlias(true);


        canvas.drawText("0", autoStart - textWidth / 2, fenceY * 6 + autoTextMargin * 2, paintText);
        canvas.drawText("8", autoStart - textWidth / 2 + fenceX, fenceY * 6 + autoTextMargin * 2, paintText);
        canvas.drawText("16", autoStart - textWidthTwo / 2 + fenceX * 2, fenceY * 6 + autoTextMargin * 2, paintText);
        canvas.drawText("24", autoStart - textWidthTwo / 2 + fenceX * 3, fenceY * 6 + autoTextMargin * 2, paintText);
        canvas.drawText("32", autoStart - textWidthTwo / 2 + fenceX * 4, fenceY * 6 + autoTextMargin * 2, paintText);
        canvas.drawText("56 (万)", autoStart - textWidthTwo / 2 + fenceX * 5, fenceY * 6 + autoTextMargin * 2, paintText);
        /**画矩形*/
        paintRect1.setColor(autoBarChartColor1);
        paintRect1.setStrokeWidth(autoRateWidth);
        paintRect1.setStyle(Paint.Style.FILL);
        paintRect1.setAntiAlias(true);
        canvas.drawLine(autoStart, fenceY, autoStart + fenceX * 5 * percent1, fenceY, paintRect1);
        canvas.drawText(progressFirst / 10000f + "万", fenceX * 5 * percent1 + autoTextMargin, fenceY + textHeight / 4, paintTextRect1);

        paintRect2.setColor(autoBarChartColor2);
        paintRect2.setStrokeWidth(autoRateWidth);
        paintRect2.setStyle(Paint.Style.FILL);
        paintRect2.setAntiAlias(true);
        canvas.drawLine(autoStart, fenceY * 3, autoStart + fenceX * 5 * percent2, fenceY * 3, paintRect2);
        canvas.drawText(progressSecond / 10000f + "万", fenceX * 5 * percent2 + autoTextMargin, fenceY * 3 + textHeight / 4, paintTextRect2);

        paintRect3.setColor(autoBarChartColor3);
        paintRect3.setStrokeWidth(autoRateWidth);
        paintRect3.setStyle(Paint.Style.FILL);
        paintRect3.setAntiAlias(true);
        canvas.drawLine(autoStart, fenceY * 5, autoStart + fenceX * 5 * percent3, fenceY * 5, paintRect3);
        canvas.drawText(progressThird / 10000f + "万", fenceX * 5 * percent3 + autoTextMargin, fenceY * 5 + textHeight / 4, paintTextRect3);
    }

    public synchronized float getMax() {
        return autoEvResultMax;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(float max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.autoEvResultMax = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized float getProgressFirst() {
        return progressFirst;
    }

    public synchronized float getProgressSecond() {
        return progressSecond;
    }

    public synchronized float getProgressThird() {
        return progressThird;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progressSecond
     */
    public synchronized void setProgress(float progressFirst, float progressSecond, float progressThird) {
        if (progressFirst < 0 || progressSecond < 0 || progressThird < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progressFirst > autoEvResultMax || progressSecond > autoEvResultMax || progressThird > autoEvResultMax) {
            progressFirst = autoEvResultMax;
            progressSecond = autoEvResultMax;
            progressThird = autoEvResultMax;
        }
        if (progressThird <= autoEvResultMax || progressSecond <= autoEvResultMax || progressFirst <= autoEvResultMax) {
            this.progressFirst = progressFirst;
            this.progressSecond = progressSecond;
            this.progressThird = progressThird;
            postInvalidate();
        }

    }
}
