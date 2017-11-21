package com.dpcraft.simplep2pchat.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class UserInfo {
    private String username;
    private String address;
    private String port;

    public UserInfo(String username,String address,String port){
        this.username = username;
        this.address = address;
        this.port = port;

    };
    public UserInfo(){
        this.username = null;
        this.address = null;
        this.port = null;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static List<UserInfo> getUserListFromJSON(String JSONstring){
        Gson gson = new Gson();
        List<UserInfo> userInfoList = gson.fromJson(JSONstring,new TypeToken<List<UserInfo>>(){}.getType());
        return userInfoList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


}
