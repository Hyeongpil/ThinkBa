package com.hyeongpil.thinkba.util;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.hyeongpil.thinkba.R;

/**
 * Created by hp on 2016-06-23.
 */
public class BasicValue {
    final static String TAG = "BasicValue";
    //싱글턴 패턴
    private static BasicValue ourInstance = new BasicValue();

    GoogleApiClient mGoogleApiClient = GlobalApplication.getInstance().getmGoogleApiClient();

    private boolean autoLogin;
    private boolean accident;
    private boolean accident_alarm;
    private boolean robber;
    private String profile_img;
    private String profile_name;
    private String taas_key;
    private String skplanet_key;

    public static BasicValue getInstance(){return ourInstance;}
    private BasicValue(){}

    public boolean isAutoLogin() {return autoLogin;}
    public void setAutoLogin(boolean autoLogin) {this.autoLogin = autoLogin;}

    public boolean isAccident() {return accident;}
    public void setAccident(boolean accident) {this.accident = accident;}

    public boolean isAccident_alarm() {return accident_alarm;}
    public void setAccident_alarm(boolean accident_alarm) {this.accident_alarm = accident_alarm;}

    public boolean isRobber() {return robber;}
    public void setRobber(boolean robber) {this.robber = robber;}

    public String getProfile_img() {return profile_img;}
    public void setProfile_img(String profile_img) {this.profile_img = profile_img;}

    public String getProfile_name() {return profile_name;}
    public void setProfile_name(String profile_name) {this.profile_name = profile_name;}

    public String getTaas_key() {
        taas_key = GlobalApplication.getInstance().getString(R.string.taas_key);
        try {
            taas_key = java.net.URLDecoder.decode(taas_key, "UTF-8");
        }catch (Exception e){
            Log.e(TAG,"인코딩 오류 :"+e.getMessage());}
        return taas_key;
    }

    public String getSkplanet_key() {
        skplanet_key = GlobalApplication.getInstance().getString(R.string.skplanet_key);
        return skplanet_key;
    }

    //    public void achivementNoti(Context mContext, String title ,String text){
//        int id = 1;
//        NotificationManager notiManager = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity
//                (mContext,0,Games.Achievements.getAchievementsIntent(mGoogleApiClient),PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder mBuilder = new Notification.Builder(mContext);
//        mBuilder.setSmallIcon(R.drawable.archive_test);
//        mBuilder.setTicker("띵바 업적을 달성했습니다!");
//        mBuilder.setWhen(System.currentTimeMillis());
//        mBuilder.setContentTitle("띵바 업적 :"+title);
//        mBuilder.setContentText(text);
//        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setAutoCancel(true);
//
//        notiManager.notify(id,mBuilder.build());
//        id += 1;
//    }
}
