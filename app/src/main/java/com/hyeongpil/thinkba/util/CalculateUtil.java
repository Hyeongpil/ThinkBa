package com.hyeongpil.thinkba.util;

import android.location.Location;

/**
 * Created by hp on 2016-08-30.
 */
public class CalculateUtil {
    final static String TAG = "CalculateUtil";
    SharedManager sharedManager = new SharedManager();
    double beforeLat = 0;
    double beforeLon = 0;

    /**
     * 거리를 계산하여 표시한다.
     */
    public static String convertDistanceStr(Double distance){
        String distanceStr = "";
        if(distance > 1000) { // 1km 이상
            distanceStr = ""+distance / 1000;
            distanceStr = distanceStr.substring(0,distanceStr.indexOf(".")+2)+"km";
            return distanceStr;
        }
        else{
            distanceStr = ""+distance;
            distanceStr = distanceStr.substring(0,distanceStr.indexOf("."))+'m';
            return distanceStr;
        }
    }

    /**
     * 위도, 경도를 받아 거리를 계산하고 누적 거리를 저장한다.
     */
    public double calDistance(double lat, double lon){
        if(beforeLat == 0){
            beforeLat = lat;
            beforeLon = lon;
        }
        Location before = new Location("before");
        before.setLatitude(beforeLat);
        before.setLongitude(beforeLon);
        Location now = new Location("now");
        now.setLatitude(lat);
        now.setLongitude(lon);
        Double distance = new Double(before.distanceTo(now));

        beforeLat = lat;
        beforeLon = lon;

        sharedManager.putDistance(distance);
        return distance;
    }
}
