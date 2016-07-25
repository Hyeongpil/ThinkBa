package com.hyeongpil.thinkba.util.retrofit;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hp on 2016. 7. 25..
 */
public class WeatherRepo implements Serializable{


    public interface AccidentApiInterface {
        @GET("rest/frequentzone/bicycle")
        Call<WeatherRepo> get_Weather_retrofit(@Query("authKey") String authKey, @Query("searchYearCd") int searchYearCd, @Query("sido") String sido, @Query("gugun") String gugun, @Query("DEATH") String DEATH);
    }
}
