package com.cx.mymap;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wang-jl on 2018/3/12.
 */

public class MyMapApplication extends Application{
    /**
     * 当前地址信息
     */
    public static BDLocation bdLocation;

    public static String username;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
