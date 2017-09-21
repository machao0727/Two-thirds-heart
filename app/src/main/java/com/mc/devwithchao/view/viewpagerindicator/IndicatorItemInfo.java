package com.mc.devwithchao.view.viewpagerindicator;

import android.view.View;

/**
 * Created by MC on 2017/9/19.
 */

public class IndicatorItemInfo {
    private int childWidth;
    private int childHeight;
    private int childStartX;
    private int childEndX;
    private View child;

    public View getChild() {
        return child;
    }

    public void setChild(View child) {
        this.child = child;
    }

    public int getChildWidth() {
        return childWidth;
    }

    public void setChildWidth(int childWidth) {
        this.childWidth = childWidth;
    }

    public int getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(int childHeight) {
        this.childHeight = childHeight;
    }

    public int getChildStartX() {
        return childStartX;
    }

    public void setChildStartX(int childStartX) {
        this.childStartX = childStartX;
    }

    public int getChildEndX() {
        return childEndX;
    }

    public void setChildEndX(int childEndX) {
        this.childEndX = childEndX;
    }
}
