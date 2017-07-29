package com.mc.devwithchao.view.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mc on 2017/7/29.
 */

public class BezierTwoView extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private List<QuadView> rightQuad ;
    private List<QuadView> leftQuad ;
    private int radius;
    private Path path;

    public BezierTwoView(Context context) {
        super(context);
        initView();
    }

    public BezierTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BezierTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        radius = mHeight / 4 - 30;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point leftTop = new Point(0, 0);
        Point rightTop = new Point(mWidth, 0);
        Point rightBottom = new Point(mWidth, mHeight);
        Point leftBottom = new Point(0, mHeight);
        if (leftQuad==null||rightQuad==null){//onDraw会多次调用，这里判空，防止多次添加数据,导致bug
            leftQuad=new ArrayList<>();
            rightQuad=new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                QuadView right = new QuadView();
                right.setStartY(i * 20 + i * radius + 20);
                right.setStartX(mWidth);
                right.setCenterX(mWidth - radius / 2);
                right.setCenterY(i * 20 + i * radius + radius / 2 + 20);
                right.setEndY(i * 20 + i * radius + radius + 20);
                right.setEndX(mWidth);
                QuadView left = new QuadView();
                left.setEndX(0);
                left.setEndY(i * 20 + i * radius + 20);
                left.setCenterX(radius / 2);
                left.setCenterY(i * 20 + i * radius + 20 + radius / 2);
                left.setStartX(0);
                left.setStartY(i * 20 + i * radius + 20 + radius);
                rightQuad.add(right);
                leftQuad.add(left);
            }
        }
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(leftTop.x, leftTop.y);
        for (int i = 0; i < leftQuad.size(); i++) {
            path.lineTo(leftQuad.get(i).getStartX(), leftQuad.get(i).getStartY());
            path.quadTo(leftQuad.get(i).getCenterX(), leftQuad.get(i).getCenterY(), leftQuad.get(i).getEndX(), leftQuad.get(i).getEndY());
        }
        path.lineTo(leftBottom.x, leftBottom.y);
        path.lineTo(rightBottom.x, rightBottom.y);
        for (int i = rightQuad.size() - 1; i >= 0; i--) {
            path.lineTo(rightQuad.get(i).getStartX(), rightQuad.get(i).getStartY());
            path.quadTo(rightQuad.get(i).getCenterX(), rightQuad.get(i).getCenterY(), rightQuad.get(i).getEndX(), rightQuad.get(i).getEndY());
        }
        path.lineTo(rightTop.x, rightTop.y);
        path.close();

        canvas.drawPath(path, mPaint);
    }

    public class QuadView {
        private int startX;
        private int startY;
        private int centerX;
        private int centerY;
        private int endX;
        private int endY;

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getCenterX() {
            return centerX;
        }

        public void setCenterX(int centerX) {
            this.centerX = centerX;
        }

        public int getCenterY() {
            return centerY;
        }

        public void setCenterY(int centerY) {
            this.centerY = centerY;
        }

        public int getEndX() {
            return endX;
        }

        public void setEndX(int endX) {
            this.endX = endX;
        }

        public int getEndY() {
            return endY;
        }

        public void setEndY(int endY) {
            this.endY = endY;
        }
    }
}
