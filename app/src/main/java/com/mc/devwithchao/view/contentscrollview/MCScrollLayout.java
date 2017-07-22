package com.mc.devwithchao.view.contentscrollview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.mc.devwithchao.R;

/**
 * Created by MC on 2017/6/29.
 * 外部控制view
 */

public class MCScrollLayout extends FrameLayout {
    private boolean isTop;
    private int maxoffsetY = 400;
    private int minoffsetY = 0;
    private LayoutParams lp;
    private int duartion = 100;

    public MCScrollLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public MCScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }


    public MCScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MCContentScrollView, defStyleAttr, 0);
        minoffsetY = a.getDimensionPixelSize(R.styleable.MCContentScrollView_minOffSetY, 0);
        maxoffsetY = a.getDimensionPixelOffset(R.styleable.MCContentScrollView_maxOffSetY, 400);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        a.recycle();
    }

    public void setToClosed() {
        isTop = true;
        lp.topMargin = minoffsetY;
        this.setLayoutParams(lp);
    }

    public void setToOpen() {
        isTop = false;
        lp.topMargin = maxoffsetY;
        this.setLayoutParams(lp);
    }

    private MCContentScrollView Child;
    private float interStartY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Child = (MCContentScrollView) getChildAt(0);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://这里直接在down事件进行拦截，后续就不会传给move,up
                //把getTop和startY放在这里来获取，是因为ACTION_DOWN未拦截，ACTION_MOVE拦截，
                // 那么，onTouchEvent事件只能收到ACTION_MOVE和ACTION_UP事件，为了保证每次
                // 都能获取这两个值，所以放这里获取
                offsetTop = getTop();
                startY = ev.getRawY();
                if (isTop) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                //每次事件分开算，每次的down,move,up都是互相独立分开的，
                // 所以规则也是每个事件独立计算，可以在down事件进行拦截，也可以在move事件进行拦截，up事件就没有必要了
                //并且onInterceptTouchEvent里面的每种事件只能调用一次
                if (isTop) {
                    if (Child.getScrollY() == 0) {
                        if (ev.getRawY() - startY <= 0) {//这里要<=0，防止ev.getRawY() - startY=0时，向上滑动导致事件拦截
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    private int offsetTop;
    private float startY;
    private float distanceY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://这里需要return true,才能消耗事件
                return true;
            case MotionEvent.ACTION_MOVE:
                distanceY = offsetTop + event.getRawY() - startY;
                lp.topMargin = (int) distanceY;
                this.setLayoutParams(lp);
                if (distanceY <= minoffsetY) {
                    distanceY = minoffsetY;
                    lp.topMargin = minoffsetY;
                    this.setLayoutParams(lp);
                }
                listener.parentScroll((int) distanceY);
                break;
            case MotionEvent.ACTION_UP:
                if (distanceY == minoffsetY) {
                    isTop = true;
                } else {
                    isTop = false;
                }
                if (distanceY < minoffsetY) {//回弹至minoffsetY
                    isTop = true;
                    ScrollerTo((int) distanceY, minoffsetY);
                } else if (distanceY > maxoffsetY) {//回弹至maxoffsetY
                    ScrollerTo((int) distanceY, maxoffsetY);
                } else if (distanceY - minoffsetY < (maxoffsetY - minoffsetY) / 2) {//回弹至minoffsetY
                    isTop = true;
                    ScrollerTo((int) distanceY, minoffsetY);
                } else if (distanceY - minoffsetY >= (maxoffsetY - minoffsetY) / 2) {//回弹至maxoffsetY
                    ScrollerTo((int) distanceY, maxoffsetY);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void ScrollerTo(int fromY, int toY) {
        final ValueAnimator animator = ValueAnimator.ofInt(fromY, toY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.topMargin = (int) animation.getAnimatedValue();
                listener.parentScroll((int) animation.getAnimatedValue());
                setLayoutParams(lp);
            }
        });
        animator.setDuration(duartion);
        animator.start();
    }

    public void setChildScroll(int childTop) {
        listener.childScroll(childTop);
    }

    private OnScrolllistener listener;

    public void setOnMCScrolllistener(OnScrolllistener listener) {
        this.listener = listener;
    }


    public interface OnScrolllistener {
        void parentScroll(int ScrollY);

        void childScroll(int ScrollY);
    }
}
