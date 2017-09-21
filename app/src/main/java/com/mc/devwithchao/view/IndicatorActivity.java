package com.mc.devwithchao.view;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.devwithchao.R;
import com.mc.devwithchao.view.viewpagerindicator.ViewpagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class IndicatorActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewpagerIndicator indicator;
    private List<String> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        indicator= (ViewpagerIndicator) findViewById(R.id.indicator);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            data.add(i+"");
        }
        viewPager.setAdapter(new mAdapter());
        indicator.setViewPager(viewPager);
        indicator.setChildLayout(R.layout.tablelayout);
        indicator.setData(data);
        indicator.setCurrentItem(0);
    }

    class mAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view=new TextView(IndicatorActivity.this);
            view.setText("这是第"+data.get(position)+"页");
            view.setTextColor(Color.BLUE);
            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(24);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
