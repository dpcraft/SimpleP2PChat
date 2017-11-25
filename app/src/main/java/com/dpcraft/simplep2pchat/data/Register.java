package com.dpcraft.simplep2pchat.data;

import com.google.gson.Gson;

/**
 * Created by dpcraft on 23/11/2017.
 */

public class Register {

    private String name;
    private int port;
    private String token;

    public Register(String name, int port, String token){
        this.name = name;
        this.port = port;
        this.token = token;
    }

    public Register(String name, int port){
        this.name = name;
        this.port = port;
        this.token = "initialization";
    }

    public Register(){
        this.name = null;
        this.port = 0;
        this.token = "initialization";
    }

    public String toJSON(){
        Gson gson = new Gson();
        System.out.println(gson.toJson(this));
        return gson.toJson(this);
    }

    public boolean refreshToken(String newToken){

        try {
            setToken(newToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
