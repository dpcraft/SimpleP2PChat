package com.dpcraft.simplep2pchat.app;

/**
 * Created by dpcraft on 23/11/2017.
 */

import android.app.Application;

/**
 * Created by dpcraft on 23/11/2017.
 */

public class MyApplication extends Application {

    private static MyApplication mMyApplication;
    private String userName;
    private String key;
    @Override
    public void onCreate(){
        super.onCreate();
        mMyApplication = this;
        userName = "dpcraft";
        key = null;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static MyApplication getInstance(){
        return mMyApplication;
    }

}