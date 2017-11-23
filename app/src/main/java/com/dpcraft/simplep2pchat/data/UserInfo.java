package com.dpcraft.simplep2pchat.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class UserInfo {
    private String name;
    private String address;
    private int port;

    public UserInfo(String username,String address,int port){
        this.name = username;
        this.address = address;
        this.port = port;

    };
    public UserInfo(){
        this.name = null;
        this.address = null;
        this.port = 0;
    }


    public static List<UserInfo> getUserListFromJSON(String JSONstring){
        Gson gson = new Gson();
        List<UserInfo> userInfoList = gson.fromJson(JSONstring,new TypeToken<List<UserInfo>>(){}.getType());
        return userInfoList;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
