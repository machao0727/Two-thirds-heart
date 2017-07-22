package com.mc.devwithchao.view.scrollimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import com.mc.devwithchao.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MC on 2017/4/26.
 * 无限滑动的iamgeview
 */

public class ScrollViewImageView extends View {
    private static Canvas canvas;
    private Paint paint;
    private Bitmap backBitmap;
    private int backBit;
    private int backBitWidth;
    private int backBitHeight;
    private Timer timer;
    private int speed = 300;//间隔时间
    private Context context;
    private float offset = 0.5f;//偏移量

    private Matrix matrix = new Matrix();

    public ScrollViewImageView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ScrollViewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ScrollViewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewImageView, defStyleAttr, 0);
        backBit = a.getResourceId(R.styleable.ScrollViewImageView_backgroundImg, 0);
        int Speed = a.getInteger(R.styleable.ScrollViewImageView_scrollSpeed, 0);
        if (Speed != 0) speed = Speed;
        initBackground();
    }

    private void initBackground() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), backBit);
        backBitmap = createRepeater(bitmap);
        backBitWidth = bitmap.getWidth();
        backBitHeight=bitmap.getHeight();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        setMeasuredDimension(sizeWidth, backBitHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backBitmap, matrix, paint);
        startAnimate();
    }

    /**
     * 开始左移动画
     */
    private void startAnimate() {
        if (timer==null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    matrix.setTranslate(-offset,0);
                    offset++;
                    if (offset >= backBitWidth) {
                        offset=0.5f;
                        matrix.setTranslate(offset-backBitWidth, 0);
                    }
                    postInvalidate();
                }
            }, 0, speed);
        }
    }

    public static Bitmap createRepeater(Bitmap src) {
        //先设置足够大的bitmap画布，宽为scr*2放两个src
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth()*2, src.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        for (int idx = 0; idx < 2; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }
}
