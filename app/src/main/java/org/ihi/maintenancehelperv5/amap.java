package org.ihi.maintenancehelperv5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

import java.util.ArrayList;
import java.util.List;


public class amap extends Fragment implements CompoundButton.OnCheckedChangeListener, LocationSource, AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private ToggleButton btn_mapchange;
    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private View view;

    public static amap newInstance() {
        amap fragment = new amap();
        return fragment;
    }

    public amap() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.amap, container, false);
            initview(savedInstanceState, view);
            //initlistener();
        }
        return view;
    }

    private void initview(Bundle savedInstanceState, View view) {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);

        /*LatLng southwestLatLng = new LatLng(40.0, 105.0);
        LatLng northeastLatLng = new LatLng(50.0, 125.0);
        LatLngBounds latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        aMap.setMapStatusLimits(latLngBounds);

        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(45.5, 110.0), 15, 0, 0));
        aMap.moveCamera(mCameraUpdate);*/


        btn_mapchange = (ToggleButton) view.findViewById(R.id.btn_mapchange);

        view.findViewById(R.id.start_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoHang();
            }
        });
    }

    private void initlistener() {

        btn_mapchange.setOnCheckedChangeListener(this);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        /*mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            clientOption = new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
        }*/

    }

    private void DaoHang() {
        Poi start = new Poi("三元桥", new LatLng(39.96087, 116.45798), "");
        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi("北京站", new LatLng(39.904556, 116.427231), "B000A83M61");
        List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
        wayList.add(new Poi("团结湖", new LatLng(39.93413, 116.461676), ""));
        wayList.add(new Poi("呼家楼", new LatLng(39.923484, 116.461327), ""));
        wayList.add(new Poi("华润大厦", new LatLng(39.912914, 116.434247), ""));
        AmapNaviPage.getInstance().showRouteActivity(((Activity_Home) getActivity()).getApplicationContext(), new AmapNaviParams(start, wayList, end, AmapNaviType.DRIVER), new INaviInfoCallback() {
            @Override
            public void onInitNaviFailure() {
            }

            @Override
            public void onGetNavigationText(String s) {
            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
            }

            @Override
            public void onArriveDestination(boolean b) {
            }

            @Override
            public void onStartNavi(int i) {
            }

            @Override
            public void onArrivedWayPoint(int i) {
            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {
            }

            @Override
            public void onCalculateRouteFailure(int i) {
            }

            @Override
            public void onStopSpeaking() {
            }

            @Override
            public View getCustomNaviView() {
                return null;
            }

            @Override
            public View getCustomNaviBottomView() {
                return null;
            }

            @Override
            public void onReCalculateRoute(int i) {
            }

            @Override
            public void onStrategyChanged(int i) {
            }

            @Override
            public void onExitPage(int i) {
            }
        });
    }

    /**
     * 停止定位
     */
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
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        } else {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 必须重写以下方法
     */
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


}
