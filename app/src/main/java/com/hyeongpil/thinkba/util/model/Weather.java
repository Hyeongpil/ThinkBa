package com.hyeongpil.thinkba.util.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.util.GlobalApplication;

/**
 * Created by chanyouvita on 2016. 7. 27..
 */
public class Weather {
    private static Weather ourInstance = new Weather();

    public static Weather getInstance() {
        return ourInstance;
    }

    private Weather() {}

    String temperature;
    String dust_grade;
    String dust_value;
    String uv;
    String cloud;
    String wind_direction;
    String wind_speed;
    String icon;

    public String getTemperature() {
        return temperature.substring(0,temperature.length()-1);
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDust_grade() {
        return dust_grade;
    }

    public void setDust_grade(String dust_grade) {
        this.dust_grade = dust_grade;
    }

    public String getUv() {
        try {
            uv = uv.substring(0, 2);
        }catch (Exception e){}
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public Drawable getWind_direction() {
        Bitmap wind_temp = rotate(Integer.parseInt(wind_direction.substring(0,wind_direction.indexOf("."))));
        Drawable wind_drawable = new BitmapDrawable(wind_temp);
        return wind_drawable;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_speed() {
        return wind_speed.substring(0,wind_speed.length()-1);
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getDust_value() {return dust_value.substring(0,dust_value.indexOf("."));}

    public void setDust_value(String dust_value) {this.dust_value = dust_value;}

    public void setIcon(String icon) {this.icon = icon;}

    public String getWeatherIcon(){
        switch (icon){
            case "SKY_O00":return "w38";
            case "SKY_O01":return "w01";
            case "SKY_O02":return "w02";
            case "SKY_O03":return "w03";
            case "SKY_O04":return "w12";
            case "SKY_O05":return "w13";
            case "SKY_O06":return "w14";
            case "SKY_O07":return "w18";
            case "SKY_O08":return "w21";
            case "SKY_O09":return "w32";
            case "SKY_O10":return "w04";
            case "SKY_O11":return "w29";
            case "SKY_O12":return "w26";
            case "SKY_O13":return "w27";
            case "SKY_O14":return "w28";
            default: return "w38";
        }
    }
    public static Bitmap rotate(int degrees) {
        BitmapDrawable wind_temp = (BitmapDrawable) GlobalApplication.getInstance().getResources().getDrawable(R.drawable.wind_dir);
        Bitmap b = wind_temp.getBitmap();
        if ( degrees != 0 && b != null ) {
            Matrix m = new Matrix();
            m.setRotate( degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2 );
            try {
                Bitmap b2 = Bitmap.createBitmap( b, 0, 0, b.getWidth(), b.getHeight(), m, true );
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                Log.e("Weather","rotate error");
            }
        }
        return b;
    }
}
