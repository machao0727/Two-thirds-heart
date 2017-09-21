package com.mc.devwithchao.view.viewpagerindicator;

import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by MC on 2017/9/21.
 */

public interface IndicatorInterface {

    /**
     * 设置table数据集合
     *
     * @param data 数据集合
     */
    void setData(List<String> data);

    /**
     * 设置默认选中
     *
     * @param position 默认选中
     */
    void setCurrentItem(int position);

    /**
     * 自定义child layout
     * PS：<TextView
     * android:id="@+id/tvTable"
     * android:layout_width="44dp"
     * android:layout_height="44dp"
     * android:gravity="center"
     * android:text="table"
     * android:textColor="@drawable/selector_gray_blue"
     * android:textSize="12sp"/>
     * table必须为textview且id为tvTable
     *
     * @param layout 自定义布局文件
     */
    void setChildLayout(int layout);

    /**
     * 设置viewpager
     *
     * @param viewPager 页卡
     */
    void setViewPager(ViewPager viewPager);
}
