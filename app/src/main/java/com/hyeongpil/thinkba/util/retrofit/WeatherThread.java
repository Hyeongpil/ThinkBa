package com.hyeongpil.thinkba.util.retrofit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hp on 2016. 7. 25..
 */
public class WeatherThread extends Thread {
    final static String TAG = "WeatherThread";
    Handler handler;
    Context mContext;
    WeatherRepo weatherRepo;

    int version = 1;
    String lat;
    String lon;

    public WeatherThread(Handler handler, Context mContext, double lat, double lon) {
        this.handler = handler;
        this.mContext = mContext;
        this.lat = String.valueOf(lat);
        this.lon = String .valueOf(lon);
    }

    @Override
    public void run() {
        super.run();
        Log.e(TAG,"진입");
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.AccidentApiInterface service = client.create(WeatherRepo.AccidentApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(version,lat,lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                Log.e(TAG,"response.header :"+response.headers());
                Log.e(TAG,"response.raw :"+response.raw());
                if(response.isSuccessful()){
                    weatherRepo = response.body();
                    Log.e(TAG,"weatherRepo.getresult() :"+weatherRepo.getResult().getMessage());
                    Log.e(TAG,"weatherRepo.getCurrent() :"+weatherRepo.getGweather().getCurrent());
                    Log.e(TAG,"sky :"+weatherRepo.getGweather().getCurrent().get(0).getSky().getName());
                }
            }

            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e(TAG,"날씨정보 실패 :" + t.getMessage() );
            }
        });
    }
}
