package com.hyeongpil.thinkba.util.model;

/**
 * Created by hp on 2016-05-20.
 */
public class POI_Data {
    //이름
    public String poiname;
    //주소
    public String address;
    //위치
    public String point;
    //거리
    public String distanceStr;
    //거리 더블
    public Double distance;

    public String getPoiname() {
        return poiname;
    }

    public String getAddress() {
        return address;
    }

    public String getPoint() {
        return point;
    }

    public String getDistanceStr() {
        return distanceStr;
    }

    public Double getDistance() {
        return distance;
    }
}
