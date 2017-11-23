package com.dpcraft.simplep2pchat.app;

/**
 * Created by dpcraft on 23/11/2017.
 */

import android.app.Application;

import com.dpcraft.simplep2pchat.data.Register;

/**
 * Created by dpcraft on 23/11/2017.
 */

public class MyApplication extends Application {

    private static MyApplication mMyApplication;

    private Register mRegister;

    @Override
    public void onCreate(){
        super.onCreate();
        mMyApplication = this;
        mRegister = new Register();
    }
    public String getRegisterName() {
        return mRegister.getName();
    }

    public void setRegisterName(String userName){
        mRegister.setName(userName);
    }
    public int getPort(){
        return mRegister.getPort();
    }
    public void setPort(int port){
        mRegister.setPort(port);
    }
    public Register getRegister() {
        return mRegister;
    }

    public void setRegister(Register register) {
        mRegister = register;
    }

    public boolean refreshToken(String newToken){

        return mRegister.refreshToken(newToken);

    }

    public static MyApplication getInstance(){
        return mMyApplication;
    }

}