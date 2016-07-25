package com.hyeongpil.thinkba.util.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyeongpil.thinkba.util.BasicValue;
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
    AccidentRepo accidentRepo;
    TmapPointArr tmapPointArr;
    ArrayList<TMapPoint> arr_TmapPoint;

    Context mContext;

    String apiKey;
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
        apiKey = BasicValue.getInstance().getTaas_key();

        Retrofit client = new Retrofit.Builder().baseUrl("http://taas.koroad.or.kr/data/").addConverterFactory(GsonConverterFactory.create()).build();
        AccidentRepo.AccidentApiInterface service = client.create(AccidentRepo.AccidentApiInterface.class);
        Call<AccidentRepo> call = service.get_Accident_retrofit(apiKey,searchYearCd,sido,gugun,death);
        call.enqueue(new Callback<AccidentRepo>() {
            @Override
            public void onResponse(Call<AccidentRepo> call, Response<AccidentRepo> response) {
                if(response.isSuccessful()){
                    arr_TmapPoint = new ArrayList<TMapPoint>();
                    //사고다발지역 결과를 받아 repo로 Gson 양식으로 받음
                    accidentRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    List<AccidentRepo.searchResult.frequentzone> frequentzoneList = accidentRepo.getSearchResult().getFrequentzone();

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
            public void onFailure(Call<AccidentRepo> call, Throwable t) {
                Log.e(TAG,"taas 통신 실패 :"+t.getMessage());
            }
        });
    }
}
