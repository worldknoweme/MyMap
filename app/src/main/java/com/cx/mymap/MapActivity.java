package com.cx.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends Activity {
    private MapView mMapView = null;

    private BaiduMap bdMap;
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    boolean firstLoc=true; //是否首次定位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        bdMap = mMapView.getMap();
        initLocation();
    }

    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(1000);//1000毫秒定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    //自定义的定位监听
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            bdMap.setMyLocationData(data);
            if (firstLoc) {
                //获取经纬度
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                //mBaiduMap.setMapStatus(status);//直接到中间
                bdMap.animateMapStatus(status);//动画的方式到中间
                firstLoc = false;
                showInfo("位置：" + location.getAddrStr());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        bdMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()){//如果定位client没有开启，开启定位
            mLocationClient.start();
        }
    }


    //显示消息
    private void showInfo(String str){
        Toast.makeText(MapActivity.this,str, Toast.LENGTH_SHORT).show();
    }
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        bdMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



}
