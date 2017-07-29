package com.mc.devwithchao.view.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mc.devwithchao.view.newtreeview.ScreenUtils;

/**
 * Created by mc on 2017/7/29.
 * 贝塞尔曲线
 */

public class BezierView extends View {
    private Paint mPaint;
    private int startX,startY;
    private int endX,endY;
    private int centerX,centerY;

    public BezierView(Context context) {
        super(context);
        initView();
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        startX=100;
        endX=ScreenUtils.getScreenW()-100;
        startY=endY=ScreenUtils.getScreenH()/2;
        centerX=(endY-startX)/2+startX;
        centerY=startY-200;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            centerX= (int) event.getX();
            centerY= (int) event.getY();
            invalidate();//重新绘制
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //话三个点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(8);
        canvas.drawPoint(startX,startY,mPaint);
        canvas.drawPoint(endX,endY,mPaint);
        canvas.drawPoint(centerX,centerY,mPaint);
        //画辅助线
        mPaint.setStrokeWidth(4);
        canvas.drawLine(startX,startY,centerX,centerY,mPaint);
        canvas.drawLine(centerX,centerY,endX,endY,mPaint);

        //画贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        Path path=new Path();
        path.moveTo(startX,startY);
        path.quadTo(centerX,centerY,endX,endY);
        canvas.drawPath(path,mPaint);
    }
}
