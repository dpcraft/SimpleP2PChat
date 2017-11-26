package com.dpcraft.simplep2pchat.app;

import java.util.HashMap;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class Config {

    //public static final String ServerIP = "113.54.195.19";
    public static final String ServerIP = "192.168.1.231";
    public static final int ServerPort = 12345;

    public static final int REGISTER_SUCCESS = 200;
    public static final int USERNAME_ALREADY_EXIST = 201;
    public static HashMap<String,Boolean> IncogModeUsers;
    public static final String ACTION_USER_INFO_UPDATE = "com.dpcraft.simplep2pchat.USER_INFO_UPDATE";
    public static final String ACTION_CHAT_MSG_UPDATE = "com.dpcraft.simplep2pchat.CHAT_MSG_UPDATE";

    public static final String URL_REGISTER = "";

}
