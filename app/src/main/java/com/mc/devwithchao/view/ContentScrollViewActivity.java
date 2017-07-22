package com.mc.devwithchao.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mc.devwithchao.R;
import com.mc.devwithchao.view.contentscrollview.MCScrollLayout;

public class ContentScrollViewActivity extends AppCompatActivity {
    private MCScrollLayout scrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_scroll_view);
        scrollLayout = (MCScrollLayout) findViewById(R.id.scrolllayout);
        scrollLayout.setToOpen();
        scrollLayout.setOnMCScrolllistener(new MCScrollLayout.OnScrolllistener() {
            @Override
            public void parentScroll(int ScrollY) {
                Log.w("parentscroll------>", ScrollY + "");
            }

            @Override
            public void childScroll(int ScrollY) {
                Log.w("childscroll------>", ScrollY + "");
            }
        });
    }
}
