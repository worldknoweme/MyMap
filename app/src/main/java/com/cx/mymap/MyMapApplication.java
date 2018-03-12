package com.cx.mymap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wang-jl on 2018/3/12.
 */

public class MyMapApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
