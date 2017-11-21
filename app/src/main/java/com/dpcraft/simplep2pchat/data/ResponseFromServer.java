package com.dpcraft.simplep2pchat.data;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class ResponseFromServer {
    private int code;
    private String msg;
    private List<UserInfo> userInfoList;

    public static List<UserInfo> getUserInfoList(String JSONString){
        return new Gson().fromJson(JSONString,ResponseFromServer.class).getUserInfoList();
    }

    public static int getCode(String JSONString ){
        int code = 0;
        try{
            JSONObject jsonObject =new JSONObject(JSONString);
            String codeStr = jsonObject.getString("code");
            code = Integer.parseInt(codeStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return code;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }


}
