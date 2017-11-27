package com.mc.devwithchao.view.hastitlerecycleview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.mc.devwithchao.view.hastitlerecycleview.CityView.Contact;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public interface TitleViewInterface {

    void setTitle(String text);

    void setTitleData(List<String> title);

    void setRecyclerView(RecyclerView rv, List<Contact> resultList, LinearLayoutManager ly);
}
