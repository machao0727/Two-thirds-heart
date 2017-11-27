package com.mc.devwithchao.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mc.devwithchao.R;
import com.mc.devwithchao.view.hastitlerecycleview.CityEntry;
import com.mc.devwithchao.view.hastitlerecycleview.CityView.Contact;
import com.mc.devwithchao.view.hastitlerecycleview.CityView.ContactAdapter;
import com.mc.devwithchao.view.hastitlerecycleview.CityView.LetterView;
import com.mc.devwithchao.view.hastitlerecycleview.TitleView;


import java.util.List;

public class TitleRecyclerActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private LetterView indexBar;
    private LinearLayoutManager layoutManager;
    private ContactAdapter adapter;
    private List<String> city;
    private int TotalDy = 0;
    private TitleView titleview;
    private List<Contact> resultList;
    private TextView tvSideBarHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_recycler);
        city = CityEntry.getCity();
        mRv = (RecyclerView) findViewById(R.id.rv);
        indexBar = (LetterView) findViewById(R.id.indexBar);
        titleview = (TitleView) findViewById(R.id.titleview);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContactAdapter(this, CityEntry.getCity());
        mRv.setLayoutManager(layoutManager);
//        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRv.setAdapter(adapter);
        titleview.setTitleData(adapter.getcharacterList());
        resultList = adapter.getResultList();
        titleview.setRecyclerView(mRv, resultList, layoutManager);
        tvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        initEvent();
    }


    private String name = "";

    private void initEvent() {
        indexBar.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0);
                titleview.setTitle(character);
                tvSideBarHint.setText(character);
            }

            @Override
            public void clickArrow(boolean click) {
                if (click) {
                    tvSideBarHint.setVisibility(View.VISIBLE);
                } else {
                    tvSideBarHint.setVisibility(View.GONE);
                }
            }
        });
    }
}
