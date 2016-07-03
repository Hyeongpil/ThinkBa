package com.example.kwan.thinkba.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hp on 2016-06-28.
 */
// TODO: 2016-07-04  frequentzone 데이터 가져와야함
    // http://taas.koroad.or.kr/data/rest/frequentzone/bicycle?authKey=uL50TuF8qdaxAAHQ5sXWvvPIO9VMNSToveE9ROkc8NdWfKj1ezFodcJM9BJbAuy%2B&searchYearCd=2015046&sido=43&gugun=112&DEATH=N
public class Repo implements Serializable{
    @SerializedName("frequentzone")
    ArrayList<frequentzone> frequentzone = new ArrayList<frequentzone>();

    @SerializedName("resultCode")
    String resultCode;

    public String getResultCode() {
        return resultCode;
    }

    public ArrayList<frequentzone> getFrequentzone() {
        return frequentzone;
    }

    public class frequentzone {
        @SerializedName("spotname")
        String spotname;
//        String x_crd;
//        String y_crd;

        public String getSpotname() {
            return spotname;
        }
//
//        public String getX_crd() {
//            return x_crd;
//        }
//
//        public String getY_crd() {
//            return y_crd;
//        }
    }

    public interface ApiInterface{
        @GET("rest/frequentzone/bicycle")
        Call<Repo> getretrofit(@Query("authKey") String authKey, @Query("searchYearCd") int searchYearCd, @Query("sido") int sido, @Query("gugun") int gugun, @Query("DEATH") String DEATH);
    }
}
