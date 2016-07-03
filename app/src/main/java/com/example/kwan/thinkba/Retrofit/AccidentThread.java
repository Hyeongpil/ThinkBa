package com.example.kwan.thinkba.Retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.kwan.thinkba.R;

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

    Context mContext;

    String apiKey;
    String temp_api ="uL50TuF8qdaxAAHQ5sXWvvPIO9VMNSToveE9ROkc8NdWfKj1ezFodcJM9BJbAuy%2B";
    int searchYearCd = 2015046; //14년
    int sido = 43; // 충청북도
    int gugun = 112; //청주시 서원구
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
        Log.e(TAG,"스레드 진입");


        Retrofit client = new Retrofit.Builder().baseUrl("http://taas.koroad.or.kr/data/").addConverterFactory(GsonConverterFactory.create()).build();
        Repo.ApiInterface service = client.create(Repo.ApiInterface.class);
        Call<Repo> call = service.getretrofit(apiKey,searchYearCd,sido,gugun,death);
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if(response.isSuccessful()){
                    repo = response.body();
                    Log.e(TAG,"response.raw :"+response.raw());

                    Log.e(TAG,"getSearchResult :"+repo.getFrequentzone());
                    Log.e(TAG,"getResultCode :"+repo.getResultCode());
//                    Log.e(TAG,"getspotname :"+repo.getFrequentzone().get(0).getSpotname());
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e(TAG,"taas 통신 실패 :"+t.getMessage());
            }
        });

//        Call<searchResult> call = service.getretrofit(apiKey,year,sido,gugun,death);
//        call.enqueue(new Callback<searchResult>() {
//            @Override
//            public void onResponse(Call<searchResult> call, Response<searchResult> response) {
//                Log.e(TAG,"성공");
//                if(response.isSuccessful()){
//                    searchResult = response.body();
//                    Log.e(TAG,"getSearchResult :"+searchResult.getSearchResult());
//                    Log.e(TAG,"getResultCode :"+searchResult.getResultCode());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<searchResult> call, Throwable t) {
//                Log.e(TAG,"실패");
//            }
//        });

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("THREAD", repo);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
