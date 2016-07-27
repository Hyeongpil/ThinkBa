package com.hyeongpil.thinkba.util.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyeongpil.thinkba.util.model.Weather;

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
    Context mContext;
    WeatherRepo weatherRepo;
    Handler handler;

    int version = 1;
    String lat;
    String lon;

    public WeatherThread(Handler handler, Context mContext, double lat, double lon) {
        this.mContext = mContext;
        this.lat = String.valueOf(lat);
        this.lon = String .valueOf(lon);
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.AccidentApiInterface service = client.create(WeatherRepo.AccidentApiInterface.class);
        // TODO: 2016. 7. 27. 좌표로 바꿔야함
        Call<WeatherRepo> call = service.get_Weather_retrofit(version,String.valueOf(36.6244636),String.valueOf(127.4617878));
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                if(response.isSuccessful()){
                    weatherRepo = response.body();
                    // TODO: 2016. 7. 25. 미세먼지와 자외선도 받아서 메인에 추가
                    Log.d(TAG,"response.raw :"+response.raw());
                    Log.e(TAG,"sky :"+weatherRepo.getWeather().getHourly().get(0).getSky().getName());
                    Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
                    Weather.getInstance().setCloud(weatherRepo.getWeather().getHourly().get(0).getSky().getName());
                    Weather.getInstance().setWind_direction(weatherRepo.getWeather().getHourly().get(0).getWind().getWdir());
                    Weather.getInstance().setWind_speed(weatherRepo.getWeather().getHourly().get(0).getWind().getWspd());
                    Weather.getInstance().setIcon(weatherRepo.getWeather().getHourly().get(0).getSky().getCode());

                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("weather","weather");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e(TAG,"날씨정보 실패 :" + t.getMessage() );
            }
        });
    }
}
