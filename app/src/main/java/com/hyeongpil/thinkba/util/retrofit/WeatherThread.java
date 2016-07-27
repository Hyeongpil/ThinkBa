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
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.AccidentApiInterface service = client.create(WeatherRepo.AccidentApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(version,lat,lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                if(response.isSuccessful()){
                    weatherRepo = response.body();
                    // TODO: 2016. 7. 25. 미세먼지와 자외선도 받아서 메인에 추가
                    Log.d(TAG,"response.raw :"+response.raw());
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
