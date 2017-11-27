package com.mc.devwithchao.view.hastitlerecycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mc.devwithchao.R;
import com.mc.devwithchao.view.hastitlerecycleview.CityView.Contact;

import java.util.List;

/**
 * Created by MC on 2017/11/10.
 */

public class TitleView extends LinearLayout implements TitleViewInterface {
    private Context context;
    private int TextHeight;
    private int paddingleft;
    private int textColor;
    private int textSize;
    private int background;
    private TextView view;
    private List<String> title;
    private RecyclerView rv;


    public TitleView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        TextHeight = a.getDimensionPixelOffset(R.styleable.TitleView_LayoutHeight, 50);
        paddingleft = a.getDimensionPixelOffset(R.styleable.TitleView_paddingleft, 25);
        textColor = a.getResourceId(R.styleable.TitleView_MC_textColor, -1);
        textSize = a.getDimensionPixelSize(R.styleable.TitleView_MC_textSize, -1);
        background = a.getResourceId(R.styleable.TitleView_MC_background, -1);
        a.recycle();
        setOrientation(LinearLayout.VERTICAL);
        addChild();
    }

    private void addChild() {
        view = new TextView(context);
        if (textSize > 0) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (textColor > 0) {
            view.setTextColor(getResources().getColor(textColor));
        }
        if (background > 0) {
            view.setBackgroundColor(getResources().getColor(background));
        }
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, TextHeight);
        view.setPadding(paddingleft, 0, 0, 0);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setLayoutParams(lp);
        this.addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private void scroll(int dy) {
        scrollTo(0, dy);
    }


    private void reset(String text) {
        scrollTo(0, 0);
        view.setText(text);
    }


    /**
     * RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
     * RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
     *
     * @param text
     */
    @Override
    public void setTitle(String text) {
        if (title.contains(text) && rv.canScrollVertically(1)) {
            currentName=text;
            reset(text);
        }
    }

    @Override
    public void setTitleData(List<String> title) {
        this.title = title;
        currentName = title.get(0);
        view.setText(title.get(0));
    }

    private String currentName = "";
    private int TotalDy = 0;

    /**
     * @param rv            recyclerview
     * @param resultList    List<Contact> resultList 所有数据的集合，可以自定义，只要里面有标识数据能跟title的数据进行比对，这里的是mName字段和title数据比对
     * @param layoutManager 布局管理者
     */
    @Override
    public void setRecyclerView(final RecyclerView rv, final List<Contact> resultList, final LinearLayoutManager layoutManager) {
        this.rv = rv;
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    reset(title.get(0));
                    currentName = title.get(0);
                }
                if (!rv.canScrollVertically(1)){
                    reset(currentName);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (title.contains(resultList.get(layoutManager.findFirstVisibleItemPosition() + 1).getmName())) {
                    int current = title.indexOf(resultList.get(layoutManager.findFirstVisibleItemPosition() + 1).getmName());
                    if (rv.getChildAt(1).getTop() < TextHeight) {
                        currentName = resultList.get(layoutManager.findFirstVisibleItemPosition() + 1).getmName();
                        TotalDy += dy;
                        if (TotalDy > 0 && TotalDy < TextHeight) {
                            scroll(TotalDy);
                        }
                        if (dy < 0) {
                            currentName = title.get(current - 1);
                        }
                    }
                } else {
                    TotalDy = 0;
                    reset(currentName);
                }
            }
        });
    }
}
