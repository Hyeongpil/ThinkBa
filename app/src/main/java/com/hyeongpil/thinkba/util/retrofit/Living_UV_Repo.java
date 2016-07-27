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
public class Living_UV_Repo implements Serializable{
    @SerializedName("weather") weather weather;

    public class weather {
        @SerializedName("wIndex") wIndex wIndex;
        public class wIndex {
            @SerializedName("uvindex") ArrayList<uvindex> uvindex = new ArrayList<>();
            public class uvindex {
                @SerializedName("day00") day00 day00;
                public class day00{
                    @SerializedName("index") String index;

                    public String getIndex() {return index;}
                }
                public Living_UV_Repo.weather.wIndex.uvindex.day00 getDay00() {return day00;}
            }
            public ArrayList<Living_UV_Repo.weather.wIndex.uvindex> getUvindex() {return uvindex;}
        }
        public Living_UV_Repo.weather.wIndex getwIndex() {return wIndex;}
    }

    public Living_UV_Repo.weather get_UV_Weather() {return weather;}

    public interface LivingApiInterface {
        @Headers({"Accept: application/json","access_token: 705c1ee2-9ce5-4a61-97ad-fc3e55d0b4dc","appKey: d5c4630e-a1ac-3ddc-8417-03e1bf83e1b4"})
        @GET("weather/windex/uvindex")
        Call<Living_UV_Repo> get_uv_retrofit(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
    }
}
