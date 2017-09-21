package com.mc.devwithchao.view.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.mc.devwithchao.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MC on 2017/9/19
 * viewpagerIndicator
 */

public class ViewpagerIndicator extends ViewGroup implements IndicatorInterface {
    private Context mContext;
    private int startX = 0;
    private Map<Integer, IndicatorItemInfo> childMap = new HashMap<>();
    private int sizeHeight;
    private int sizeWidth;
    private int currentItem = 0;
    private Paint mPaint;
    private int lineStartX;
    private int lineEndX;
    private int layoutID;
    private int lineSize = 4;
    private int rightBorder;
    private Scroller scroller;
    private boolean isClick = false;
    private ViewPager viewPager;
    private int TotalWidth = 0;//滑动的总长度

    public ViewpagerIndicator(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * init
     *
     * @param context
     * @param attr
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attr, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.ViewpagerIndicator, defStyleAttr, 0);
        mContext = context;
        scroller = new Scroller(mContext);
        mPaint = new Paint();
        mPaint.setColor(a.getColor(R.styleable.ViewpagerIndicator_MC_dividerColor, Color.BLUE));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(a.getDimensionPixelOffset(R.styleable.ViewpagerIndicator_MC_dividerWidth, lineSize));
    }


    private void setViewpagerLisenter() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setScrollOffset(positionOffset, position);
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                setSelectOffSet();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                setScrollStates(state);
            }
        });
    }

    /**
     * 设置选中以及滑动位置
     */
    private void setSelectOffSet() {
        if (childMap.size()==0)return;
        if (childMap.get(currentItem - 1) != null) {
            childMap.get(currentItem - 1).getChild().setSelected(false);
        }
        if (childMap.get(currentItem + 1) != null) {
            childMap.get(currentItem + 1).getChild().setSelected(false);
        }
        if (childMap.get(currentItem)!=null){
            childMap.get(currentItem).getChild().setSelected(true);
        }
        setCurrentOffset();
    }

    private void addChild(String txt) {
        View child = View.inflate(mContext, layoutID, null);
        TextView tvTable = (TextView) child.findViewById(R.id.tvTable);
        tvTable.setText(txt);
        addView(child);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);
        int childCount = getChildCount();
        startX = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            IndicatorItemInfo itemInfo = new IndicatorItemInfo();
            int childWidth = child.getMeasuredWidth();
            itemInfo.setChildWidth(child.getMeasuredWidth());
            itemInfo.setChildHeight(childWidth);
            itemInfo.setChildStartX(startX);
            itemInfo.setChildEndX(startX + childWidth);
            itemInfo.setChild(child);
            TotalWidth += childWidth;
            childMap.put(i, itemInfo);
            if (currentItem == i) {
                lineStartX = startX;
                lineEndX = startX + childWidth;
                setSelected(child);
            }
            startX += childWidth;
            setClickScroll(itemInfo, i);
        }

        setCurrentOffset();
        rightBorder = startX;
    }

    private void setSelected(View view) {
        view.setSelected(true);
    }

    private void setScrollStates(int states) {
        if (states == ViewPager.SCROLL_STATE_IDLE) {//停止滑动过后再微调
            isClick = false;
            lineStartX = childMap.get(currentItem).getChildStartX();
            lineEndX = childMap.get(currentItem).getChildEndX();
            invalidate();
        }
    }

    /**
     * 设置滑动
     */
    private void setScrollOffset(float positionOffset, int position) {
        if (!isClick) {//不是点击才进行滑动逻辑
            IndicatorItemInfo current = childMap.get(currentItem);
            IndicatorItemInfo next;
            if (currentItem > position) {//往右滑动,lineEndX不变，lineStartX开始变化
                next = childMap.get(currentItem - 1);
                if (positionOffset <= 0.5) {//滑动到终点，这是lineStartX应该在目标起点
                    lineStartX = next.getChildStartX();
                    lineEndX = (int) (current.getChildStartX() + current.getChildWidth() * positionOffset * 2);
                } else {//lineStartX变化，lineEndX不变
                    lineStartX = (int) (current.getChildStartX() - current.getChildWidth() * (1 - positionOffset) * 2);
                    lineEndX = current.getChildEndX();
                }
            } else if (currentItem == position) {//往左滑动，linestartX不变，lineEndX开始变化
                if (childMap.get(currentItem + 1) != null) {
                    next = childMap.get(currentItem + 1);
                    if (positionOffset <= 0.5) {//linestartX不变，lineEndX变大
                        lineEndX = (int) (current.getChildEndX() + next.getChildWidth() * positionOffset * 2);
                        lineStartX = current.getChildStartX();
                    } else {
                        lineEndX = next.getChildEndX();
                        lineStartX = (int) (current.getChildStartX() + current.getChildWidth() * (positionOffset - 0.5) * 2);
                    }
                }
            }
            invalidate();
        }
    }

    /**
     * 设置选中位置
     */
    private void setCurrentOffset() {
        if (TotalWidth > sizeWidth) {
            int currentX = childMap.get(currentItem).getChildStartX();
            if (currentX < sizeWidth / 2) {
                scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0);
            } else if (currentX + sizeWidth / 2 > rightBorder) {
                scroller.startScroll(getScrollX(), 0, rightBorder - sizeWidth - getScrollX(), 0);
            } else {
                int offsetScrollX = currentX - sizeWidth / 2;
                //参数三是需要滑动的距离而不是滑动最终坐标
                scroller.startScroll(getScrollX(), 0, offsetScrollX - getScrollX(), 0);
            }
        }
    }

    /**
     * 点击过后的滑动距离
     */
    private void setClickScroll(final IndicatorItemInfo itemInfo, final int position) {
        itemInfo.getChild().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                childMap.get(currentItem).getChild().setSelected(false);
                currentItem = position;
                childMap.get(currentItem).getChild().setSelected(true);
                setCurrentOffset();
                lineStartX = itemInfo.getChildStartX();
                lineEndX = itemInfo.getChildEndX();
                invalidate();
                viewPager.setCurrentItem(currentItem);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (Integer i : childMap.keySet()) {
            IndicatorItemInfo itemInfo = childMap.get(i);
            itemInfo.getChild().layout(itemInfo.getChildStartX(), 0, itemInfo.getChildEndX(), sizeHeight);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(lineStartX, sizeHeight - lineSize, lineEndX, sizeHeight - lineSize, mPaint);
    }

    private float downX;
    private float offsetX;//m每次滑动的偏移量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (TotalWidth > sizeWidth) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    offsetX = downX - event.getX();
                    if (getScrollX() + offsetX >= rightBorder - sizeWidth) {//左滑，超出右边界
                        scrollTo(rightBorder - sizeWidth, 0);
                    } else if (getScrollX() + offsetX <= 0) {
                        //右滑，超出左边界
                        scrollTo(0, 0);
                    } else {
                        scroll(downX - event.getX());
                        downX = event.getX();
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
            }
        }
        return true;
    }

    private void scroll(float offSetX) {
        scrollBy((int) offSetX, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float diff = Math.abs(ev.getX() - downX);
                if (diff > 10) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }


    @Override
    public void setData(List<String> data) {
        for (String item : data) {
            addChild(item);
        }
    }

    @Override
    public void setCurrentItem(int position) {
        currentItem = position;
        if (viewPager != null) {
            viewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    public void setChildLayout(int layout) {
        layoutID = layout;
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        setViewpagerLisenter();
    }
}
