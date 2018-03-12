package com.cx.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends Activity {
    private MapView mMapView = null;

    private BaiduMap bdMap;
    public LocationClient mLocationClient = null;
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

        bdMap.setMyLocationEnabled(true);//开启定位图层
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(mLocationClientListener);
        mLocationClient.start();
    }

    private BDLocationListener mLocationClientListener =new BDLocationListener() {


        @Override
        public void onReceiveLocation(BDLocation arg0) {
            // TODO Auto-generated method stub
            if(arg0==null)
                return;

            float  accuracy= arg0.getRadius();
            double  lat=  arg0.getLatitude();
            double  lon= arg0.getLongitude();

            MyLocationData.Builder build=new  MyLocationData.Builder();
            build.accuracy(accuracy);
            build.latitude(lat);
            build.longitude(lon);
            MyLocationData myLocationData=build.build();
            bdMap.setMyLocationData(myLocationData);//设置定位数据

            if (firstLoc) {
                firstLoc = false;

                MapStatus.Builder mapStatusBuilder = new MapStatus.Builder();
                LatLng ll = new LatLng(lat, lon);
                mapStatusBuilder.target(ll);
                //mapStatusBuilder.zoom(18.0f);

                MapStatus mapStatus = mapStatusBuilder.build();
                MapStatusUpdate mapUpdate = MapStatusUpdateFactory
                        .newMapStatus(mapStatus);
                bdMap.animateMapStatus(mapUpdate);

            }
        }
    };



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
