package com.example.kwan.thinkba.model;

import com.skp.Tmap.TMapPoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hp on 2016-07-04.
 */
public class TmapPointArr implements Serializable{
    private ArrayList<TMapPoint> tMapPointArr;

    public TmapPointArr(ArrayList<TMapPoint> tMapPointArr){
        this.tMapPointArr = tMapPointArr;
    }

    public ArrayList<TMapPoint> gettMapPointArr() {
        return tMapPointArr;
    }
}
