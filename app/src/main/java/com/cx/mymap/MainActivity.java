package com.cx.mymap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cx.mymap.adapter.PictureAdapter;
import com.cx.mymap.model.Picture;
import com.cx.mymap.model.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 主菜单功能
 *
 *GPS定位当前位置
 *搜索目标位置并标记
 *道路拥挤情况查询
 *规划出行路线-->分别为公交、驾车、步行
 *导航
 */
public class MainActivity extends Activity {
    GridView gvInfo;// 创建GridView对象
    // 定义字符串数组，存储系统功能
    String[] titles = new String[] { "定位",  "退出" };
    // 定义int数组，存储功能对应的图标
    int[] images = new int[] { R.drawable.addoutaccount,
            R.drawable.exit };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gvInfo = (GridView) findViewById(R.id.gvInfo);// 获取布局文件中的gvInfo组件
        PictureAdapter adapter = new PictureAdapter(titles, images, this);// 创建pictureAdapter对象
        gvInfo.setAdapter(adapter);// 为GridView设置数据源
        gvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {// 为GridView设置项单击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;// 创建Intent对象
                switch (arg2) {
                    case 0:
                        intent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent);// 打开AddOutaccount
                        break;

                    case 1:
                        finish();// 关闭当前Activity
                }
            }
        });

    }
}
