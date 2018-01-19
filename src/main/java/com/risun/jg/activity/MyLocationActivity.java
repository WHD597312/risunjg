package com.risun.jg.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.risun.jg.R;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.ViewId;

public class MyLocationActivity extends AppCompatActivity{

    private MapView bmapView;
    private BaiduMap baiduMap;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    boolean isFirstLoc = true; // 是否首次定位
    private LocationClient locationClient;
    private MyLocationListener myLocationListener;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_my_location);
//        AnnotationUtils.findActivityById("com.risun.jg.MyLocationActivity",this);
        bmapView=(MapView) findViewById(R.id.bmapView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        baiduMap=bmapView.getMap();//获得百度地图控制器
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getApplicationContext());

        myLocationListener=new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();

        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        option.setAddrType("all");

        locationClient.setLocOption(option);

//        //构造定位数据
//        MyLocationData locationData=new MyLocationData.Builder().accuracy(loca)
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!locationClient.isStarted()){
//            locationClient.start();
//        }
    }



    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(bmapView==null || location==null){
                return;
            }
            mCurrentLat=location.getLatitude();
            mCurrentLon=location.getLongitude();
            mCurrentAccracy=location.getRadius();

            locData=new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locData);
            if(isFirstLoc){
                isFirstLoc=false;
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                MapStatus.Builder builder=new MapStatus.Builder();
                builder.target(latLng).zoom(18);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
}
