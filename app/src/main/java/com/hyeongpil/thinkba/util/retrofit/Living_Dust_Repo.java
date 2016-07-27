package com.hyeongpil.thinkba.util.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by hp on 2016. 7. 27..
 */
public class Living_Dust_Repo implements Serializable{
    @SerializedName("weather") weather weather;
    public class weather {
        @SerializedName("dust") ArrayList<dust> dust = new ArrayList<>();
        public class dust {
            @SerializedName("pm10") pm10 pm10;
            public class pm10 {
                @SerializedName("grade") String grade;
                @SerializedName("value") String value;

                public String getGrade() {return grade;}
                public String getValue() {return value;}
            }
            public Living_Dust_Repo.weather.dust.pm10 getPm10() {return pm10;}
        }
        public ArrayList<Living_Dust_Repo.weather.dust> getDust() {return dust;}
    }

    public Living_Dust_Repo.weather get_Dust_Weather() {return weather;}

    public interface LivingApiInterface {
        @Headers({"Accept: application/json","access_token: 705c1ee2-9ce5-4a61-97ad-fc3e55d0b4dc","appKey: d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4"})
        @GET("weather/dust")
        Call<Living_Dust_Repo> get_dust_retrofit(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
    }
}
