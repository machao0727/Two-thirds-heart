package com.mc.devwithchao.view.contentscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by MC on 2017/6/30.
 * 内部滑动view
 */

public class MCContentScrollView extends ScrollView {
    private GestureDetector gestureDetector;
    private Context context;
    private MCScrollLayout parent;

    public MCContentScrollView(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    public MCContentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }


    public MCContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView();
    }

    private void initView() {
        parent = (MCScrollLayout) getParent();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //滑动状态改变
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (parent==null){
            parent = (MCScrollLayout) getParent();
        }
        if (t<0)t=0;
        parent.setChildScroll(t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
