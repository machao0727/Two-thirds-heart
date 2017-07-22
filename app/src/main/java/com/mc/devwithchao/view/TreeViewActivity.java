package com.mc.devwithchao.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.mc.devwithchao.R;
import com.mc.devwithchao.view.newtreeview.NewTreeView;
import com.mc.devwithchao.view.newtreeview.NodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreeViewActivity extends AppCompatActivity {
    private NewTreeView newtreeview;
    private NodeView rootView;
    private boolean isfirst = true;
    boolean readhas = false;
    private List<List<NodeView>> node = new ArrayList<>();
    private String json = "{\"tel\": \"15893948\",\"name\": \"用户A\",\"child\": " +
            "[{\"tel\": \"158939483\",\"name\": \"用户B1\",\"child\": " +
            "[{\"tel\": \"1589394831\",\"name\": \"用户C1\",\"child\": []}," +
            "{\"tel\": \"1589394832\",\"name\": \"用户C2\",\"child\": []}]}," +
            "{\"tel\": \"158939483\", \"name\": \"用户B2\", \"child\": []}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_view);
        newtreeview = (NewTreeView) findViewById(R.id.newtreeview);
        initView();
    }

    private void initView() {
        JSONObject jsStr;
        try {
            jsStr = new JSONObject(json);
            String tel = jsStr.getString("tel");
            String name = jsStr.getString("name") + "\n" + tel;
            rootView = new NodeView(this);
            rootView.setText(name);
            newtreeview.addrootNode(rootView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        analysisJson(json);
        for (int i = 0; i < node.size(); i++) {
            if (node.get(i).size() == 1) {
                newtreeview.addNodeView(node.get(i).get(0));
            } else if (node.get(i).size() == 2) {
                newtreeview.addNodeView(node.get(i).get(0), node.get(i).get(1));
            } else {
                newtreeview.addNodeView(node.get(i).get(0), node.get(i).get(1), node.get(i).get(2));
            }
        }
        newtreeview.layoutView();
    }

    /**
     * 解析json
     */
    private void analysisJson(final String json) {
        try {
            List<String> jsonString = new ArrayList<>();
            List<NodeView> list = new ArrayList<>();
            JSONObject jsStr = new JSONObject(json);
            String tel = jsStr.getString("tel");
            String name = jsStr.getString("name") + "\n" + tel;
            String child = jsStr.getString("child");
            if (isfirst) {
                list.add(rootView);
                isfirst = false;
            } else {
                checkisHas(list, name);
                if (!readhas) {
                    final NodeView nodeA = new NodeView(this);
                    nodeA.setText(name);
                    list.add(nodeA);
                }
            }
            readhas = false;
            if (!TextUtils.isEmpty(child)) {
                JSONArray ja = new JSONArray(child);
                for (int i = 0; i < ja.length(); i++) {
                    Object object = ja.get(i);
                    if (object != null) {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        String tel1 = jsonObject.getString("tel");
                        String name1 = jsonObject.getString("name") + "\n" + tel1;
                        jsonString.add(jsonObject.toString());
                        checkisHas(list, name1);
                        if (!readhas) {
                            final NodeView nodeB = new NodeView(this);
                            nodeB.setText(name1);
                            list.add(nodeB);
                        }
                        readhas = false;
                    }
                }
            }
            node.add(list);
            for (int i = 0; i < jsonString.size(); i++) {
                analysisJson(jsonString.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查集合中是否已经有该元素
     */
    private void checkisHas(List<NodeView> list, String name) {
        if (node.size() > 0) {
            for (int i = 0; i < node.size(); i++) {
                for (NodeView info : node.get(i)) {
                    if (info.getText().equals(name)) {//已经有这个节点
                        readhas = true;
                        list.add(info);
                    }
                }
            }
        }
    }
}
