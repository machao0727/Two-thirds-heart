package com.mc.devwithchao.view.circledisperseview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mc.devwithchao.R;


/**
 * Created by Administrator on 2017/11/24.
 */

public class CircleDisperseView extends View implements CircleDisperseInterface{
    private int sizeWidth;
    private int sizeHeight;
    private int centerX;//圆心X
    private int centerY;//圆心Y
    private int maxRadius;//最大半径
    private int currentRadius;//当前半径
    private boolean isAnima=false;//正在动画
    private ValueAnimator open;
    private ValueAnimator close;
    private int animaType=-1;//动画类型  0 small 1 large
    private Paint paint;


    private boolean displayModeSmall=true;//开始是否显示小圆
    private int radius=50;//默认半径
    private int backgroudColor;
    private int duration=1000;//持续时间

    public CircleDisperseView(Context context) {
        super(context);
        initView(context,null,0);
    }

    public CircleDisperseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs,0);
    }

    public CircleDisperseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleDisperseView,defStyleAttr,0);
        backgroudColor=a.getResourceId(R.styleable.CircleDisperseView_cir_backgroundColor,Color.BLUE);
        duration=a.getInteger(R.styleable.CircleDisperseView_cir_duration,1000);
        displayModeSmall=a.getBoolean(R.styleable.CircleDisperseView_cir_displayModeSmall,true);
        radius=a.getDimensionPixelOffset(R.styleable.CircleDisperseView_cir_radius,50);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(backgroudColor));
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        centerX=sizeWidth/2;
        centerY=sizeHeight/2;
        maxRadius= (int) Math.sqrt(Math.pow(centerX,2)+Math.pow(centerY,2));
        if (displayModeSmall){
            currentRadius=radius;
            animaType=1;
        }else {
            currentRadius=maxRadius;
            animaType=0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX,centerY,currentRadius,paint);
    }

    @Override
    public void open() {
        if (isAnima||animaType==0){
            return;
        }
        animaType=0;
        isAnima=true;
        if (open==null){
            open = ValueAnimator.ofInt(radius,maxRadius);
            open.setDuration(duration);
            open.setInterpolator(new LinearInterpolator());
            open.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    currentRadius= (int) animation.getAnimatedValue();
                    if (currentRadius==maxRadius){
                        isAnima=false;
                    }
                    postInvalidate();
                }
            });
        }
        open.start();
    }




    @Override
    public void close() {
        if (isAnima||animaType==1){
            return;
        }
        animaType=1;
        isAnima=true;
        if (close==null){
            close = ValueAnimator.ofInt(maxRadius,radius);
            close.setDuration(duration);
            close.setInterpolator(new LinearInterpolator());
            close.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    currentRadius= (int) animation.getAnimatedValue();
                    if (currentRadius==radius){
                        isAnima=false;
                    }
                    postInvalidate();
                }
            });
        }
        close.start();
    }
}
