package com.mc.devwithchao.view.bookview;

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


/**
 * Created by Administrator on 2017/11/25.
 */

public class BookView extends View implements BookViewInterface {

    private int sizeWidth;
    private int sizeHeight;
    private Paint mPaint;
    private RectF rectF;
    private int lineColor;
    private int lineSize;

    /**************外层圆*****************/
    private int radius = 50;//圆半径
    private int centerX;//圆心X
    private int centerY;//圆心Y
    /**************外层圆*****************/


    /*********外层矩形4个顶点**********/
    /*********设置短边为半径长度，长边为直径-50**********/
    private Point BR_RT = new Point();//右上
    private Point BR_RB = new Point();//右下
    private Point BR_TC = new Point();//顶部圆角控制点
    private Point BR_BC = new Point();//底部圆角控制点
    private float B_Width;//宽
    private float B_Height;//高
    private float length;//总长度
    private int corner = 10;//圆角直径
    /*********外层矩形4个顶点**********/


    /*********内层矩形4个顶点**********/
    /*********设置横变为半径长度-20，短边为20**********/
    private int offsetSize = 10;//内外矩形间距
    private Point SR_RT = new Point();//右上
    private Point SR_RB = new Point();//右下
    private Point SR_LT = new Point();//左上
    private Point SR_LB = new Point();//左下
    private float S_Width;
    private float S_Height = 10;
    private float SMlength;//总长度
    /*********外层矩形4个顶点**********/

    /*********底部两条线**********/
    private Point bottomLineLeft = new Point();
    private Point bottomLineRight = new Point();
    private float bottomLineLength;
    private Point topLineLeft = new Point();
    private Point topLineRight = new Point();
    private float topLineLength;


    private float progress = 0f;
    private Path path;
    private Path smPath;

    public BookView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public BookView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public BookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.BookView,defStyleAttr,0);
        radius=a.getDimensionPixelOffset(R.styleable.BookView_bv_radius,50);
        lineColor=a.getResourceId(R.styleable.BookView_bv_LineColor,Color.BLACK);
        lineSize=a.getDimensionPixelSize(R.styleable.BookView_bv_LineSize,2);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(lineColor));
        mPaint.setStrokeWidth(lineSize);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int temp = sizeWidth < sizeHeight ? sizeWidth : sizeHeight;
        radius = temp / 2 < radius ? temp / 2 : radius;//取最小值
        centerX = sizeWidth / 2;
        centerY = sizeHeight / 2;
        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        BR_TC.setX(centerX - radius / 2);
        BR_TC.setY(centerY - radius + 25);
        BR_BC.setX(centerX - radius / 2);
        BR_BC.setY(centerY + radius - 25);
        BR_RT.setX(centerX + radius / 2);
        BR_RT.setY(centerY - radius + 25);
        BR_RB.setX(centerX + radius / 2);
        BR_RB.setY(centerY + radius - 25);
        B_Width = radius;
        B_Height = 2 * radius - 50;
        length = 2 * B_Width + 2 * B_Height;

        SR_LT.setX(centerX - radius / 2 + offsetSize);
        SR_LT.setY(centerY - radius + 25 + offsetSize);
        SR_RT.setX(centerX + radius / 2 - offsetSize);
        SR_RT.setY(centerY - radius + 25 + offsetSize);
        SR_LB.setX(centerX - radius / 2 + offsetSize);
        SR_LB.setY((int) (centerY - radius + 25 + offsetSize + S_Height));
        SR_RB.setX(centerX + radius / 2 - offsetSize);
        SR_RB.setY((int) (centerY - radius + 25 + offsetSize + S_Height));
        S_Width = radius - 20;
        SMlength = 2 * S_Width + 2 * S_Height;

        topLineLeft.setX(centerX - radius / 2);
        topLineLeft.setY(centerY + radius - 25 - corner);
        topLineRight.setX(centerX + radius / 2);
        topLineRight.setY(centerY + radius - 25 - corner);
        bottomLineLeft.setX(centerX - radius / 2 + corner / 2);
        bottomLineLeft.setY(centerY + radius - 25 - corner / 2);
        bottomLineRight.setX(centerX + radius / 2);
        bottomLineRight.setY(centerY + radius - 25 - corner / 2);
        bottomLineLength=B_Width-corner/2;
        topLineLength=B_Width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBigRectF(canvas, progress);
        drawSmallRectF(canvas, progress);
        if (progress > 0.75) drawTopLine(canvas, (progress - 0.75f) / 0.25f);//将剩余的0.25当成1
        if (progress > 0.8) drawBottomLine(canvas, (progress - 0.8f) / 0.2f);//将剩余的0.2当成1
        drawCircle(canvas, progress);
    }

    /**
     * 画底部第二条边
     *
     * @param canvas
     * @param p
     */
    private void drawBottomLine(Canvas canvas, float p) {
        canvas.drawLine(bottomLineLeft.getX(),bottomLineLeft.getY(),bottomLineLeft.getX()+bottomLineLength*p,bottomLineLeft.getY(),mPaint);
    }

    /**
     * 画底部第一条边
     *
     * @param canvas
     * @param p
     */
    private void drawTopLine(Canvas canvas, float p) {
        canvas.drawLine(topLineLeft.getX(),topLineLeft.getY(),topLineLeft.getX()+topLineLength*p,topLineLeft.getY(),mPaint);
    }

    private void drawSmallRectF(Canvas canvas, float p) {
        if (smPath == null) smPath = new Path();
        smPath.reset();
        if (progress == 0) return;
        smPath.moveTo(SR_LT.getX(), SR_LT.getY());
        if (p < S_Width / SMlength) {
            smPath.lineTo(SR_LT.getX() + SMlength * p, SR_LT.getY());
        } else if (p < (S_Width + S_Height) / SMlength) {
            smPath.lineTo(SR_RT.getX(), SR_RT.getY());
            smPath.lineTo(SR_RT.getX(), SR_RT.getY() + SMlength * (p - S_Width / SMlength));
        } else if (p < (2 * S_Width + S_Height) / SMlength) {
            smPath.lineTo(SR_RT.getX(), SR_RT.getY());
            smPath.lineTo(SR_RB.getX(), SR_RB.getY());
            smPath.lineTo(SR_RB.getX() - SMlength * (p - (S_Width + S_Height) / SMlength), SR_RB.getY());
        } else {
            smPath.lineTo(SR_RT.getX(), SR_RT.getY());
            smPath.lineTo(SR_RB.getX(), SR_RB.getY());
            smPath.lineTo(SR_LB.getX(), SR_LB.getY());
            smPath.lineTo(SR_LB.getX(), SR_LB.getY() - SMlength * (p - (2 * S_Width + S_Height) / SMlength));
        }
        canvas.drawPath(smPath, mPaint);
    }

    /**
     * 画外层矩形
     * 将进度分为4等份，没等分画不同的变
     *
     * @param canvas 画布
     * @param v      进度  0-1
     */
    private void drawBigRectF(Canvas canvas, float v) {
        if (path == null) path = new Path();
        path.reset();
        if (v == 0) return;
        path.moveTo(BR_TC.getX() + corner, BR_TC.getY());
        if (v < B_Width / length) {// 1等份前，只画了第一条边
            path.lineTo(BR_TC.getX() + length * v, BR_TC.getY());
        } else if (v < (B_Width + B_Height) / length) { // 2等份前，画两条边
            path.lineTo(BR_RT.getX(), BR_RT.getY());
            path.lineTo(BR_RT.getX(), BR_RT.getY() + length * (v - B_Width / length));
        } else if (v < (B_Width * 2 + B_Height) / length) {//3等份前，画三条边
            path.lineTo(BR_RT.getX(), BR_RT.getY());
            path.lineTo(BR_RB.getX(), BR_RB.getY());
            path.lineTo(BR_RB.getX() - length * (v - (B_Width + B_Height) / length), BR_RB.getY());
        } else if (v < 1f) {//4等份，画四条边
            path.lineTo(BR_RT.getX(), BR_RT.getY());
            path.lineTo(BR_RB.getX(), BR_RB.getY());
            path.lineTo(BR_BC.getX() + corner, BR_BC.getY());
            path.quadTo(BR_BC.getX(), BR_BC.getY(), BR_BC.getX(), BR_BC.getY() - corner);
            path.lineTo(BR_BC.getX(), BR_BC.getY() - length * (v - (B_Width * 2 + B_Height) / length));
        } else {//完整矩形
            path.lineTo(BR_RT.getX(), BR_RT.getY());
            path.lineTo(BR_RB.getX(), BR_RB.getY());
            path.lineTo(BR_BC.getX() + corner, BR_BC.getY());
            path.quadTo(BR_BC.getX(), BR_BC.getY(), BR_BC.getX(), BR_BC.getY() - corner);
            path.lineTo(BR_TC.getX(), BR_TC.getY() + corner);
            path.quadTo(BR_TC.getX(), BR_TC.getY(), BR_TC.getX() + corner, BR_TC.getY());
            path.close();
        }
        canvas.drawPath(path, mPaint);
    }


    /**
     * 画外层圆
     *
     * @param canvas 画布
     * @param offset 角度偏移量  0-1
     */
    private void drawCircle(Canvas canvas, float offset) {
        canvas.drawArc(rectF, 90, -360 * offset, false, mPaint);
    }

    @Override
    public void updataProgress(float p) {
        progress = p;
        invalidate();
    }

    public class Point {
        private int X;
        private int Y;

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }

        public int getY() {
            return Y;
        }

        public void setY(int y) {
            Y = y;
        }
    }
}
