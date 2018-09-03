package org.ihi.maintenancehelperv5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Fragment_Navi_Home extends Fragment implements CompoundButton.OnCheckedChangeListener, AMapNaviListener, LocationSource, AMapLocationListener, AMap.OnMapLoadedListener {
    private View view;
    private ToggleButton btn_mapchange;

    /**
     * 导航对象(单例)
     */
    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapNavi mAMapNavi;
    private MapView mapView;
    private AMap mAMap;
    private NaviLatLng endLatlng = new NaviLatLng(39.90759, 116.392582);
    private NaviLatLng startLatlng = new NaviLatLng(39.993537, 116.472875);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    private RouteOverLay mRouteOverlay;
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /*
     * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
     * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
     */
    int strategyFlag = 0;
    //private Button mStartNaviButton;

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            locationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            mLocationOption.setOnceLocationLatest(true);//设置单次精确定位
            //mLocationOption.setInterval(1000);
            mLocationOption.setHttpTimeOut(20000);
            mLocationOption.setOnceLocationLatest(true);
            locationClient.setLocationOption(mLocationOption);
            locationClient.stopLocation();
            locationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                aMapLocation.getAoiName();//获取当前定位点的AOI信息
                aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                aMapLocation.getFloor();//获取当前室内定位的楼层
                aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);

                startLatlng = new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                endLatlng = new NaviLatLng(aMapLocation.getLatitude() - 0.5, aMapLocation.getLongitude());

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        } else {
            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult calcRouteResult) {
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult routeResult) {
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData notifyData) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_navi_home, container, false);

            /*mStartNaviButton = (Button) view.findViewById(R.id.calculate_route_start_navi);
            mStartNaviButton.setOnClickListener(this);*/
            //setUpMapIfNeeded();

            mapView = (MapView) view.findViewById(R.id.map_navi);
            mapView.onCreate(savedInstanceState);
            if (mAMap == null) {
                mAMap = mapView.getMap();
            }

            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.getUiSettings().setMyLocationButtonEnabled(true);
            mAMap.setLocationSource(this);
            mAMap.setMyLocationEnabled(true);

            mAMap.setOnMapLoadedListener(this);

            view.findViewById(R.id.calculate_route).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initNavi();
                    calculateDriveRoute();
                }
            });

            btn_mapchange = (ToggleButton) view.findViewById(R.id.btn_mapchange);
            btn_mapchange.setOnCheckedChangeListener(this);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
        }
    }

    /*private void setUpMapIfNeeded() {
        if (mAMap == null) {
            mAMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map_navi)).getMap();
            UiSettings uiSettings = mAMap.getUiSettings();
            if (uiSettings != null) {
                uiSettings.setRotateGesturesEnabled(false);
            }
            mAMap.setOnMapLoadedListener(this);
        }
    }*/

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute() {
        try {
            strategyFlag = mAMapNavi.strategyConvert(true, false, false, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (startList != null && endList != null) {
            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
        }

    }

    /**
     * 导航初始化
     */
    private void initNavi() {
        startList.add(startLatlng);
        endList.add(endLatlng);
        mAMapNavi = AMapNavi.getInstance(((Activity_Home) getActivity()).getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
    }

    private void cleanRouteOverlay() {
        if (mRouteOverlay != null) {
            mRouteOverlay.removeFromMap();
            mRouteOverlay.destroy();
        }
    }

    /**
     * 绘制路径规划结果
     *
     * @param path AMapNaviPath
     */
    private void drawRoutes(AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        mRouteOverlay = new RouteOverLay(mAMap, path, ((Activity_Home) getActivity()).getApplicationContext());
        mRouteOverlay.addToMap();
        mRouteOverlay.zoomToSpan();
    }

    /*@Override
    public void onClick(View view) {
        if (view.getId() == R.id.calculate_route_start_navi) {
            startNavi();
        }

    }*/

    /**
     * 开始导航
     */
    private void startNavi() {
        Intent gpsintent = new Intent(((Activity_Home) getActivity()).getApplicationContext(), RouteNaviActivity.class);
        gpsintent.putExtra("gps", false); // gps 为true为真实导航，为false为模拟导航
        startActivity(gpsintent);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        cleanRouteOverlay();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        if (path != null) {
            drawRoutes(path);
        }
        //mStartNaviButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onMapLoaded() {
        //calculateDriveRoute();
    }
}