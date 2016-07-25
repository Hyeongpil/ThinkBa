package com.hyeongpil.thinkba.util.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hp on 2016-06-28.
 */
public class AccidentRepo implements Serializable {

    @SerializedName("searchResult")
    private searchResult searchResult;

    @SerializedName("resultCode")
    String resultCode;

    public String getResultCode() {return resultCode;}
    public searchResult getSearchResult() {return searchResult;}

    public class searchResult {

        private List<frequentzone> frequentzone = new ArrayList<>();

        public List<frequentzone> getFrequentzone() {return frequentzone;}

        public class frequentzone {
            @SerializedName("spotname")
            String spotname;
            @SerializedName("x_crd")
            String x_crd;
            @SerializedName("y_crd")
            String y_crd;

            public String getSpotname() {return spotname;}

            public String getX_crd() {return x_crd;}

            public String getY_crd() {return y_crd;}
        }
    }

    public interface AccidentApiInterface {
        @GET("rest/frequentzone/bicycle")
        Call<AccidentRepo> get_Accident_retrofit(@Query("authKey") String authKey, @Query("searchYearCd") int searchYearCd, @Query("sido") String sido, @Query("gugun") String gugun, @Query("DEATH") String DEATH);
    }
}