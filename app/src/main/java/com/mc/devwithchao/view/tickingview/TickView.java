package com.mc.devwithchao.view.tickingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import com.mc.devwithchao.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/23.
 */

public class TickView extends View {
    private int sizeWidth;
    private int sizeHeight;
    private int radius = 50;//圆半径
    private int centerX;//圆心X
    private int centerY;//圆心Y
    private Paint mPaint;
    private int duration = 1000;
    private int startArc = 30;//将圆分12次画完，每次移动30度
    private RectF rectF;
    private int count = 0;
    private Timer timer;
    private Paint linePaint;
    private int backgroudColor;
    private int lineColor;
    private int lineWidth;

    private int lineStartX;//钩起始点
    private int lineStartY;//钩起始点Y
    private int lineCenterX;//钩转折点X
    private int lineCenterY;//钩转折点Y
    private int lineEndX;//钩终点X
    private int lineEndY;//钩终点Y
    private int offSetX;//转折过后X轴需要走的步长
    private boolean circleFinish = false;//画圆完成

    public TickView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TickView, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.TickView_duration, 1000);
        radius = a.getDimensionPixelOffset(R.styleable.TickView_radius, 50);
        backgroudColor = a.getResourceId(R.styleable.TickView_backgroundColor, Color.BLUE);
        lineColor = a.getResourceId(R.styleable.TickView_lineColor, Color.WHITE);
        lineWidth = a.getDimensionPixelOffset(R.styleable.TickView_lineWidth, 4);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(backgroudColor));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(lineColor));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int temp = sizeWidth < sizeHeight ? sizeWidth : sizeHeight;
        radius = temp / 2 < radius ? temp / 2 : radius;//取最小值
        lineStartX = centerX - radius + radius/5;
        lineStartY = centerY + radius/5;
        lineCenterX = centerX;
        lineCenterY = centerY + radius - radius/5;
        offSetX = (radius - radius/5) / 12;
        centerX = sizeWidth / 2;
        centerY = sizeHeight / 2;
        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas, count);
        if (circleFinish) {//圆画完接着画钩
            drawTicking(canvas, (count - 12));
        }
        if (count >= 24 && timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void drawTicking(Canvas canvas, int count) {
        count = count < 12 ? count : 12;
        Path path = new Path();
        path.moveTo(lineStartX, lineStartY);
        path.lineTo(lineCenterX, lineCenterY);
        //求正切先将角度60转换成弧度，再求正切
        path.lineTo(lineCenterX + offSetX * count, (float) (lineCenterY - Math.tan(Math.toRadians(60)) * offSetX * count));
        canvas.drawPath(path, linePaint);
    }

    public void startAnim() {
        if (timer == null) {
            count = 0;
            circleFinish = false;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    count++;
                    postInvalidate();
                }
            }, 0, duration / 24);
        }
    }

    /**
     * 画圆弧
     */
    private void drawArc(Canvas canvas, int count) {
        if (count > 12) circleFinish = true;
        count = count > 12 ? 12 : count;
        canvas.drawArc(rectF, -90, startArc * count, true, mPaint);
    }
}
