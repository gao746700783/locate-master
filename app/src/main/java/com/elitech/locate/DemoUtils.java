package com.elitech.locate;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocationManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * 一些工具方法.
 */
public class DemoUtils {

    /**
     * 返回坐标系名称
     */
    public static String toString(int coordinateType) {
        if (coordinateType == TencentLocationManager.COORDINATE_TYPE_GCJ02) {
            return "国测局坐标(火星坐标)";
        } else if (coordinateType == TencentLocationManager.COORDINATE_TYPE_WGS84) {
            return "WGS84坐标(GPS坐标, 地球坐标)";
        } else {
            return "非法坐标";
        }
    }

    /**
     * 返回 manifest 中的 key
     */
    public static String getKey(Context context) {
        String key = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null) {
                key = metaData.getString("TencentMapSDK");
            }
        } catch (NameNotFoundException e) {
            Log.e("TencentLocation",
                    "Location Manager: no key found in manifest file");
            key = "";
        }
        return key;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
