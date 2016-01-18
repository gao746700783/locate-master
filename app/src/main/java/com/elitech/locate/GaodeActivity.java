package com.elitech.locate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GaodeActivity extends AppCompatActivity implements AMapLocationListener {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    public AMapLocationListener mLocationListener = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;


    private TextView mLocationStatus;
    private Button startLocation;
    private Button stopLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        initLocationOption();


        mLocationStatus = (TextView) findViewById(R.id.status);
        startLocation = (Button) findViewById(R.id.startLocation);
        stopLocation = (Button) findViewById(R.id.stopLocation);
        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动定位
                mLocationClient.startLocation();
            }
        });
        stopLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.stopLocation();
            }
        });
    }

    private void initLocationOption() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(3000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {

            StringBuffer sb = new StringBuffer();
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码


                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + amapLocation.getLocationType() + "\n");
                sb.append("经    度    : " + amapLocation.getLongitude() + "\n");
                sb.append("纬    度    : " + amapLocation.getLatitude() + "\n");
                sb.append("精    度    : " + amapLocation.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + amapLocation.getProvider() + "\n");

                if (amapLocation.getProvider().equalsIgnoreCase(
                        android.location.LocationManager.GPS_PROVIDER)) {
                    // 以下信息只有提供者是GPS时才会有
                    sb.append("速    度    : " + amapLocation.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + amapLocation.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : "
                            + amapLocation.getSatellites() + "\n");
                } else {
                    // 提供者是GPS时是没有以下信息的
                    sb.append("国    家    : " + amapLocation.getCountry() + "\n");
                    sb.append("省            : " + amapLocation.getProvince() + "\n");
                    sb.append("市            : " + amapLocation.getCity() + "\n");
                    sb.append("城市编码 : " + amapLocation.getCityCode() + "\n");
                    sb.append("区            : " + amapLocation.getDistrict() + "\n");
                    sb.append("区域 码   : " + amapLocation.getAdCode() + "\n");
                    sb.append("地    址    : " + amapLocation.getAddress() + "\n");
                    sb.append("兴趣点    : " + amapLocation.getPoiName() + "\n");
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + amapLocation.getErrorCode() + "\n");
                sb.append("错误信息:" + amapLocation.getErrorInfo() + "\n");
                sb.append("错误描述:" + amapLocation.getLocationDetail() + "\n");
            }

            mLocationStatus.append(sb.toString());
            mLocationStatus.append("\n---\n");

            mLocationClient.stopLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }
}
