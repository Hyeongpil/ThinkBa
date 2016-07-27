package com.hyeongpil.thinkba.util.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by hp on 2016. 7. 25..
 */
public class WeatherRepo implements Serializable{

    @SerializedName("result")
    Result result;
    @SerializedName("gweather")
    Gweather gweather;


    public class Result {
        @SerializedName("message") String message;

        public String getMessage() {
            return message;
        }
    }

    public class Gweather{

        public List<Gweather.current> current = new ArrayList<>();
        public List<Gweather.current> getCurrent() {return current;}

        public class current{
            @SerializedName("sky") Sky sky;

            public class Sky{
                @SerializedName("name") String name;
                @SerializedName("icon") String icon;

                public String getName() {return name;}
                public String getIcon() {return icon;}
            }

            public class precipitation{ // 강수 정보
                @SerializedName("value") String precipitation; // 강우
                @SerializedName("type") String type; //0 :없음 1:비 2: 비/눈 3: 눈

                public String getPrecipitation() {return precipitation;}
                public String getType() {return type;}
            }
            public class temperature{
                @SerializedName("tc") String tc; // 현재 기온

                public String getTc() {return tc;}
            }
            public class wind{ // 바람
                @SerializedName("speed") Speed speed;
                @SerializedName("direction") Direction direction;
                public class Speed{
                    @SerializedName("name") String name; // 바람 이름

                    public String getName() {return name;}
                }
                public class Direction{
                    @SerializedName("name") String name; // 풍향
                    @SerializedName("value") String value; // 풍속

                    public String getName() {return name;}
                    public String getValue() {return value;}
                }
                public Speed getSpeed() {return speed;}
                public Direction getDirection() {return direction;}
            }

            public Sky getSky() {return sky;}
        }


    }


    public Result getResult() {
        return result;
    }

    public Gweather getGweather() {
        return gweather;
    }

    //한국 날씨 weather/current/hourly
    //세계 날씨 gweather/current
    public interface AccidentApiInterface {
        @Headers({"Accept: application/json","access_token: 705c1ee2-9ce5-4a61-97ad-fc3e55d0b4dc","appKey: d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4"})
        @GET("gweather/current")
        Call<WeatherRepo> get_Weather_retrofit(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
    }
}
