package com.dpcraft.simplep2pchat.test;

import android.util.Log;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.data.UserInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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


    public static List<ChatMessage> initChatMessageList(){
        List<ChatMessage> chatMessageList = new ArrayList<>();
        String message,sender,recver;
        for (int i = 0; i < 10; i++) {
            message = "Hello,this is the message: " + i;

            if(i % 2 == 0){
                sender = "user0";
                recver = "dpcraft";
            }else{
                sender = "dpcraft";
                recver = "user0";
            }
            chatMessageList.add(new ChatMessage(message,sender,recver));

        }
        return chatMessageList;
    }

}
