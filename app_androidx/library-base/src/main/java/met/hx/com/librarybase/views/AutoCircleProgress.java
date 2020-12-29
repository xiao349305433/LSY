package met.hx.com.librarybase.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HuXu on 20/11/2015.
 */
public class AutoCircleProgress extends View {

    //如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/

    private  boolean canTouchable=false;

    //flags
    private boolean isInitialized = false;
    private float viewWidth;
    private float circleRadiusHour;
    private float circleRadiusDragButton;
    private float currentDegreeHour;
    private float centerXHour;
    private float centerYHour;
    private float strokeWidth;
    private float[] dragButtonHourPosition;
    private float[] defaultDragButtonHourPosition;
    //paint
    private Paint paintCircleBackground;
    private Paint paintDragButton;
    private Paint paintHour;
    private Paint paintNumber;

    private Paint paintGlowEffect;

    private float process;
    private String percent;
    private float progress=0;
    private float max;
    private float space=1;
    //color
    private  int colorDefault = 0xFFD6D6D6 ;
    private  int colorRound = 0xFF9AD13C ;

    private float startArc=60;

    private int intStyle=0;

    //initialize every thing
    //初始化
    private void initialize(Canvas canvas) {
        viewWidth = canvas.getWidth();
        //保证高分辨率屏幕有比较好的显示效果
        if (viewWidth > 720){
            strokeWidth = 30;
            circleRadiusDragButton = 50;
        }else {
            strokeWidth = 15;
            circleRadiusDragButton = 25;
        }
        circleRadiusHour = viewWidth / 2-viewWidth/8;
        currentDegreeHour = 0;
        centerXHour = viewWidth / 2;
        centerYHour = viewWidth / 2;
        defaultDragButtonHourPosition = new float[]{centerXHour, centerYHour - circleRadiusHour};
        dragButtonHourPosition = defaultDragButtonHourPosition;
        paintCircleBackground = new Paint();
        paintDragButton = new Paint();
        paintNumber = new Paint();
        paintHour = new Paint();
        paintGlowEffect = new Paint();
        paintCircleBackground.setColor(colorDefault);
        paintCircleBackground.setStrokeWidth(strokeWidth);
        paintCircleBackground.setStyle(Paint.Style.STROKE);
        paintCircleBackground.setAntiAlias(true);
        paintDragButton.setStrokeWidth(5);
        paintDragButton.setStyle(Paint.Style.FILL);
        paintDragButton.setAntiAlias(true);
        paintHour.setColor(colorRound);
        paintHour.setStrokeWidth(strokeWidth);
        if(intStyle==0){
            paintHour.setStyle(Paint.Style.STROKE);
        }else paintHour.setStyle(Paint.Style.FILL);

        paintHour.setAntiAlias(true);
        paintNumber.setStrokeWidth(2);
        paintNumber.setStyle(Paint.Style.FILL);
        paintNumber.setAntiAlias(true);

        //用于绘制圆弧尽头的辉光效果,辉光区域就是dragButton的区域
        paintGlowEffect.setMaskFilter(new BlurMaskFilter(2*strokeWidth/3, BlurMaskFilter.Blur.NORMAL));
        paintGlowEffect.setStrokeWidth(strokeWidth);
        paintGlowEffect.setAntiAlias(true);
        paintGlowEffect.setStyle(Paint.Style.FILL);

    }

    public boolean isCanTouchable() {
        return canTouchable;
    }

    public void setCanTouchable(boolean canTouchable) {
        this.canTouchable = canTouchable;
    }

    public int getColorDefault() {
        return colorDefault;
    }

    public void setColorDefault(int colorDefault) {
        this.colorDefault = colorDefault;
    }

    public int getColorRound() {
        return colorRound;
    }
    public Paint getPaintHour() {
        return paintHour;
    }
    public int getIntStyle() {
        return intStyle;
    }
    public void setIntStyle(int intStyle) {
        this.intStyle = intStyle;
    }
    public void setPaintHour(Paint paintHour) {
        this.paintHour = paintHour;
    }
    public void setColorRound(int colorRound) {
        this.colorRound = colorRound;
    }

    public AutoCircleProgress(Context context) {
        this(context, null);
    }

    //在.xml中使用此控件时调用此构造函数
    public AutoCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getProcess() {
        return process;
    }

    public float getSpace() {
        return space;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public void setProcess(float process) {
        this.process = process;
    }

    public float getStartArc() {
        return startArc;
    }

    public void setStartArc(float startArc) {
        this.startArc = startArc;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //初始化尺寸，只会调用一次
        if (!isInitialized) {
            initialize(canvas);
            isInitialized = true;
        }
        updateDragButtonPosition(0);


        //画背景的圆圈
        canvas.drawCircle(centerXHour, centerYHour, circleRadiusHour, paintCircleBackground);

        //画弧形
        RectF rectFHour = new RectF(centerXHour - circleRadiusHour, centerYHour - circleRadiusHour
                , centerXHour + circleRadiusHour, centerYHour + circleRadiusHour);

        canvas.drawArc(rectFHour, startArc, currentDegreeHour, false, paintHour);


        //画辉光效果
        paintDragButton.setColor(colorRound);
        canvas.drawCircle(dragButtonHourPosition[0],dragButtonHourPosition[1],strokeWidth/2,paintDragButton);
        paintGlowEffect.setColor(colorRound);
        canvas.drawCircle(dragButtonHourPosition[0],dragButtonHourPosition[1],strokeWidth,paintGlowEffect);

        //画百分比
        Rect rect = new Rect();
        percent= String.valueOf(Math.round(currentDegreeHour * 100 / 360))+"%";
        paintNumber.setTextSize(70);
        paintNumber.setColor(colorRound);
        /**测量字符串的高度和宽度*/
        paintNumber.getTextBounds(percent,0,percent.length(),rect);
        canvas.drawText(percent,centerXHour-rect.width()/2,centerYHour+rect.height()/2,paintNumber);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (canTouchable){
                                currentDegreeHour = getDegree(event.getX(), event.getY(), centerXHour, centerYHour)-startArc;
                                updateDragButtonPosition(0);
                                invalidate();
                }
                break;

        }

        return true;
    }

    //update degree,depend on the path user dragging on the screen
    //根据用户在屏幕划过的轨迹更新角度
    private float getDegree(float eventX, float eventY, float centerX, float centerY) {
        //    http://stackoverflow.com/questions/7926816/calculate-angle-of-touched-point-and-rotate-it-in-android
        //    Math has defeated me once again.So sad...
        double tx;
        double ty;
        double t_length;
        double a ;
        float degree = 90+startArc;

        /**第一半圆*/
        if(centerY<eventY&&centerX<eventX){
            tx = eventX - centerX;
            ty = eventY-centerY;
            t_length = Math.sqrt(tx * tx + ty * ty);
            a = Math.asin(ty / t_length);
            float b=(float) Math.toDegrees(a);
            if(b<=startArc&&b>=0){
                degree =360+ b;
            }else degree = b;
        }
        /**第二半圆*/
        if(centerY<eventY&&centerX>eventX){
            tx = centerX-eventX;
            ty = eventY-centerY;
            t_length = Math.sqrt(tx * tx + ty * ty);
            a = Math.acos(ty/t_length);
            float b=(float) Math.toDegrees(a);
            if(b>=0&&b<=startArc-90){
                degree =90 + b+360;
            }else
            degree =90 + b;
        }
        /**第三半圆*/
        if(centerY>eventY&&centerX>eventX){
            tx = centerX-eventX;
            ty = centerY-eventY;
            t_length = Math.sqrt(tx * tx + ty * ty);
            a = Math.asin(ty/t_length);
            float b=(float) Math.toDegrees(a);
            if(b>=0&&b<=startArc-180){
                degree =180 + b+360;
            }else
                degree =180 + b;
        }
        /**第四半圆*/
        if(centerY>=eventY&&centerX<=eventX){
             tx = eventX - centerX;
             ty = centerY - eventY;
             t_length = Math.sqrt(tx * tx + ty * ty);
            a = Math.acos(ty/t_length);
            float b=(float) Math.toDegrees(a);
            if(b>=0&&b<=startArc-270){
                degree =270 + b+360;
            }else
                degree =270 + b;
        }

        return degree;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getPercent() {
        return percent;
    }

    @Override
    public void setFadingEdgeLength(int length) {
        super.setFadingEdgeLength(length);
    }

    @Override
    public boolean callOnClick() {
        return super.callOnClick();
    }


    //update drag button position(glow effect area)
    //更新拖动按钮中心点（辉光效果区域）
    private void updateDragButtonPosition(int flag){

        switch (flag){
            case 0:
                dragButtonHourPosition[0] = (float) (centerXHour + circleRadiusHour*(Math.sin(Math.toRadians(currentDegreeHour+90+startArc))));
                dragButtonHourPosition[1] = (float)(centerYHour - circleRadiusHour*(Math.cos(Math.toRadians(currentDegreeHour+90+startArc))));
                break;
        }

    }


    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    invalidate();
                    break;
            }

        }
    };
    public void start(){
       TimerTask task=new TimerTask() {
           @Override
           public void run() {
               progress+=space;
               if(progress<=process){
                   currentDegreeHour= Math.round(progress/max*360);
                   updateDragButtonPosition(0);
                   Message message = new Message();
                   message.what = 1;
                   mHandler.sendMessage(message);
               }

           }
       };
       Timer timer=new Timer();
       timer.schedule(task,1000,100);
   }

}
