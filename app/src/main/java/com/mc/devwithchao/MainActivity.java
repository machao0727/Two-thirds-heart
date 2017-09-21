package com.mc.devwithchao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mc.devwithchao.view.BezierActivity;
import com.mc.devwithchao.view.ContentScrollViewActivity;
import com.mc.devwithchao.view.IndicatorActivity;
import com.mc.devwithchao.view.ScrollImageviewActivity;
import com.mc.devwithchao.view.TreeViewActivity;
import com.mc.devwithchao.view.newtreeview.ScreenUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenUtils.initScreen(this);
    }

    public void scrollimageview(View view) {
        startActivity(new Intent(this, ScrollImageviewActivity.class));
    }

    public void contentscrollview(View view) {
        startActivity(new Intent(this, ContentScrollViewActivity.class));
    }

    public void treeview(View view) {
        startActivity(new Intent(this, TreeViewActivity.class));
    }

    public void bezier(View view) {
        startActivity(new Intent(this, BezierActivity.class));
    }

    public void indicator(View view) {
        startActivity(new Intent(this, IndicatorActivity.class));
    }
}
