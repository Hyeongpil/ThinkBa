package com.example.kwan.thinkba.util;

/**
 * Created by hp on 2016-06-23.
 */
public class BasicValue {
    //싱글턴 패턴
    private static BasicValue ourInstance = new BasicValue();

    private boolean autoLogin;
    private boolean accident;
    private boolean accident_alarm;
    private boolean robber;

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
}
