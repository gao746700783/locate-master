package com.elitech.locate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.util.List;

public class TencentActivity extends AppCompatActivity implements DialogInterface.OnClickListener,
        TencentLocationListener {


    private static final String[] NAMES = new String[] { "GEO", "NAME",
            "ADMIN AREA", "POI"};

    private static final int[] LEVELS = new int[] {
            TencentLocationRequest.REQUEST_LEVEL_GEO,
            TencentLocationRequest.REQUEST_LEVEL_NAME,
            TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA,
            TencentLocationRequest.REQUEST_LEVEL_POI};
    private static final int DEFAULT = 2;

    private int mIndex = DEFAULT;
    private int mLevel = LEVELS[DEFAULT];
    private TencentLocationManager mLocationManager;
    private TextView mLocationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tencent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocationStatus = (TextView) findViewById(R.id.status);

        Button settings = ((Button) findViewById(R.id.settings));
        settings.setText("Level");
        settings.setVisibility(View.VISIBLE);

        mLocationManager = TencentLocationManager.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出 activity 前一定要停止定位!
        stopLocation(null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mIndex = which;
        mLevel = LEVELS[which];
        dialog.dismiss();
    }

    // ====== view listener

    // 响应点击"停止"
    public void stopLocation(View view) {
        mLocationManager.removeUpdates(this);

        updateLocationStatus("停止定位");
    }

    // 响应点击"开始"
    public void startLocation(View view) {
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create()
                .setInterval(5000) // 设置定位周期
                .setRequestLevel(mLevel); // 设置定位level

        // 开始定位
        mLocationManager.requestLocationUpdates(request, this);

        updateLocationStatus("开始定位: " + request + ", 坐标系="
                + DemoUtils.toString(mLocationManager.getCoordinateType()));
    }

    public void clearStatus(View view) {
        mLocationStatus.setText(null);
    }

    public void settings(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setSingleChoiceItems(
                NAMES, mIndex, this);
        builder.show();
    }

    // ====== view listener

    // ====== location callback

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        String msg = null;
        if (error == TencentLocation.ERROR_OK) {
            // 定位成功
            msg = toString(location, mLevel);
        } else {
            // 定位失败
            msg = "定位失败: " + reason;
        }
        updateLocationStatus(msg);
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
    }

    // ====== location callback

    private void updateLocationStatus(String message) {
        mLocationStatus.append(message);
        mLocationStatus.append("\n---\n");
    }

    // ===== util method
    private static String toString(TencentLocation location, int level) {
        StringBuilder sb = new StringBuilder();

        sb.append("latitude=").append(location.getLatitude()).append(",");
        sb.append("longitude=").append(location.getLongitude()).append(",");
        sb.append("altitude=").append(location.getAltitude()).append(",");
        sb.append("accuracy=").append(location.getAccuracy()).append(",");

        switch (level) {
            case TencentLocationRequest.REQUEST_LEVEL_GEO:
                break;

            case TencentLocationRequest.REQUEST_LEVEL_NAME:
                sb.append("name=").append(location.getName()).append(",");
                sb.append("address=").append(location.getAddress()).append(",");
                break;

            case TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA:
            case TencentLocationRequest.REQUEST_LEVEL_POI:
            case 7:
                sb.append("nation=").append(location.getNation()).append(",");
                sb.append("province=").append(location.getProvince()).append(",");
                sb.append("city=").append(location.getCity()).append(",");
                sb.append("district=").append(location.getDistrict()).append(",");
                sb.append("town=").append(location.getTown()).append(",");
                sb.append("village=").append(location.getVillage()).append(",");
                sb.append("street=").append(location.getStreet()).append(",");
                sb.append("streetNo=").append(location.getStreetNo()).append(",");

                if (level == TencentLocationRequest.REQUEST_LEVEL_POI) {
                    List<TencentPoi> poiList = location.getPoiList();
                    int size = poiList.size();
                    for (int i = 0, limit = 3; i < limit && i < size; i++) {
                        sb.append("\n");
                        sb.append("poi[" + i + "]=")
                                .append(toString(poiList.get(i))).append(",");
                    }
                }

                break;

            default:
                break;
        }

        return sb.toString();
    }

    private static String toString(TencentPoi poi) {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(poi.getName()).append(",");
        sb.append("address=").append(poi.getAddress()).append(",");
        sb.append("catalog=").append(poi.getCatalog()).append(",");
        sb.append("distance=").append(poi.getDistance()).append(",");
        sb.append("latitude=").append(poi.getLatitude()).append(",");
        sb.append("longitude=").append(poi.getLongitude()).append(",");
        return sb.toString();
    }
}
