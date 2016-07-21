package com.hyeongpil.thinkba.util.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyeongpil.thinkba.util.model.TmapPointArr;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hp on 2016-06-28.
 */
public class AccidentThread extends Thread {
    final static String TAG = "AccidentThread";
    Handler handler;
    Repo repo;
    TmapPointArr tmapPointArr;
    ArrayList<TMapPoint> arr_TmapPoint;

    Context mContext;

    String apiKey;
    String temp_api ="uL50TuF8qdaxAAHQ5sXWvvPIO9VMNSToveE9ROkc8NdWfKj1ezFodcJM9BJbAuy%2B";
    int searchYearCd = 2015046; //14년
    String sido = ""; // 공백 시 전체 선택
    String gugun = "";
    String death = "N";

    public AccidentThread(Handler handler, Context mContext){
        super();
        this.handler = handler;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        super.run();

        try {
            apiKey = java.net.URLDecoder.decode(temp_api, "UTF-8");
        }catch (Exception e){Log.e(TAG,"인코딩 오류 :"+e.getMessage());}
        Log.d(TAG,"스레드 진입");

        Retrofit client = new Retrofit.Builder().baseUrl("http://taas.koroad.or.kr/data/").addConverterFactory(GsonConverterFactory.create()).build();
        Repo.ApiInterface service = client.create(Repo.ApiInterface.class);
        Call<Repo> call = service.getretrofit(apiKey,searchYearCd,sido,gugun,death);
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if(response.isSuccessful()){
                    arr_TmapPoint = new ArrayList<TMapPoint>();
                    //사고다발지역 결과를 받아 repo로 Gson 양식으로 받음
                    repo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    List<Repo.searchResult.frequentzone> frequentzoneList = repo.getSearchResult().getFrequentzone();

                    //받은 결과를 TMapPoint ArrayList로 저장
                    for(int i=0; i < frequentzoneList.size(); i++ ){
                        try {
                            arr_TmapPoint.add(new TMapPoint(Double.parseDouble(frequentzoneList.get(i).getY_crd()),
                                    Double.parseDouble(frequentzoneList.get(i).getX_crd())));
                        }catch (NumberFormatException e){Log.d(TAG,"좌표변환 오류 :"+e.getMessage());}
                    }

                    tmapPointArr = new TmapPointArr(arr_TmapPoint);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("THREAD", tmapPointArr);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e(TAG,"taas 통신 실패 :"+t.getMessage());
            }
        });
    }
}
