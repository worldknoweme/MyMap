package com.cx.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cx.mymap.model.History;
import com.cx.mymap.model.Operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HistoryActivity extends Activity {

    @BindView(R.id.listView1)
    ListView listView1;
    List<Map<String, Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        //查询出所有历史数据
        BmobQuery<History> bmobQuery = new BmobQuery<History>();
        bmobQuery.addWhereEqualTo("username",MyMapApplication.username);
        bmobQuery.findObjects(new FindListener<History>() {
            @Override
            public void done(List<History> list, BmobException e) {
                 data= new ArrayList<Map<String, Object>>();
                //获取到了所有的查询历史数据
                for(History history:list){
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("type",history.getType());
                    item.put("des",history.getDes());
                    item.put("create",history.getCreateAt());
                    data.add(item);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(HistoryActivity.this,data
                ,R.layout.activity_main_item,new String[]{"type","des","create"},new int[]{R.id.historyType,R.id.historyDes,R.id.historyCreate});
                listView1.setAdapter(simpleAdapter);
            }
        });


    }
}
