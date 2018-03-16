package com.cx.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.cx.mymap.model.History;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PlanActivity extends Activity {

    @BindView(R.id.start)
    EditText start;
    @BindView(R.id.end)
    EditText end;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.planMapView)
    MapView planMapView;

    private BaiduMap bdMap;
    private RoutePlanSearch rps;
    String type = "步行";
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    boolean firstLoc = true; //是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        List<String> dataset = new LinkedList<>(Arrays.asList("步行", "公交", "驾车"));
        niceSpinner.attachDataSource(dataset);
        bdMap = planMapView.getMap();
        initLocation();
    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        //获取起点和终点
        String startString = start.getText().toString();
        String endString = end.getText().toString();

        niceSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                type =  editable.toString();
            }
        });
        rps = RoutePlanSearch.newInstance();
        //设置监听
        rps.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    showInfo("抱歉，未找到结果");
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<WalkingRouteLine> routeLines = result.getRouteLines();
                    WalkingRouteLine wrl = routeLines.get(0);
                    WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(bdMap);
                    walkingRouteOverlay.setData(wrl);
                    walkingRouteOverlay.addToMap();
                    walkingRouteOverlay.zoomToSpan();
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(bdMap);
                TransitRouteLine transitRouteLine = transitRouteResult.getRouteLines().get(0);
                transitRouteOverlay.setData(transitRouteLine);
                transitRouteOverlay.addToMap();
                transitRouteOverlay.zoomToSpan();
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                    DrivingRouteLine drivingRouteLine = drivingRouteResult.getRouteLines().get(0);
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(bdMap);
                drivingRouteOverlay.setData(drivingRouteLine);
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        //开始规划
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都", startString);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都", endString);
        if(type.equals("步行")){
            rps.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }else if(type.equals("驾车")){
            rps.drivingSearch((new DrivingRoutePlanOption().from(stNode).to(enNode)));
        }else{
            rps.transitSearch((new TransitRoutePlanOption()).from(stNode).to(enNode));
        }

        //数据库入库
        History history = new History();
        history.setUsername(MyMapApplication.username);
        history.setType(type);
        history.setDes("起点："+startString+"->终点："+endString);
        history.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    showInfo("保存为历史数据");
                }else{
                    showInfo("保存历史数据失败，请联系管理员");
                }
            }
        });


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

    //显示消息
    private void showInfo(String str) {
        Toast.makeText(PlanActivity.this, str, Toast.LENGTH_SHORT).show();
    }


    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        bdMap.setMyLocationEnabled(false);
        planMapView.onDestroy();
        rps.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        bdMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {//如果定位client没有开启，开启定位
            mLocationClient.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        planMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        planMapView.onPause();
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
}
