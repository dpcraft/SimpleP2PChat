package com.dpcraft.simplep2pchat.test;

import android.util.Log;

import com.dpcraft.simplep2pchat.data.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class Test {

    public static List<UserInfo> initUserInfoList(){
        final String TAG = "Test ===== ";
        List<UserInfo> userInfoList = new ArrayList<>();
        for(int i = 0; i < 10;i++){

            userInfoList.add(new UserInfo("user" + i, "192.168.1." + i, Integer.toString(1500 + i)));
        }

        return userInfoList;
    }

}
