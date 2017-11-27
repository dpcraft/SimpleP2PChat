package com.dpcraft.simplep2pchat.network;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.R;
import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.app.MyApplication;
import com.dpcraft.simplep2pchat.data.ResponseFromServer;
import com.dpcraft.simplep2pchat.data.UserInfo;
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;
import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dpcraft on 25/11/2017.
 */

public class ServerPacketHandler implements PacketHandler {

    String info;
    InetAddress clientAddress;
    int clientPort;
    private List<UserInfo> userInfoList;
    private ResponseFromServer mResponseFromServer;
    private MyApplication mMyApplication;
    MyDatabaseHelper databaseHelper ;


    public ServerPacketHandler() {
        info = null;
        clientAddress = null;
        clientPort = 0;
        mMyApplication = MyApplication.getInstance();
        databaseHelper = new MyDatabaseHelper(mMyApplication);
    }

    private void sendUserInfoUpdateBroadcast(){
        Intent intent = new Intent(Config.ACTION_USER_INFO_UPDATE);
        LocalBroadcastManager.getInstance(mMyApplication).sendBroadcast(intent);
    }

    private void sendChatMessageUpdateBroadcast(){
        Intent intent = new Intent(Config.ACTION_CHAT_MSG_UPDATE);
        LocalBroadcastManager.getInstance(mMyApplication).sendBroadcast(intent);
    }
    private void sendNotification(ChatMessage chatMessage){
        NotificationManager manager = (NotificationManager) mMyApplication.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(mMyApplication)
                .setContentTitle("SimpleP2P Chat 收到一条新消息")
                .setContentText(chatMessage.getSender() + " : " + chatMessage.getMessage())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mMyApplication.getResources(),R.drawable.ic_launcher))
                .build();
        manager.notify(1,notification);
    }

    @Override
    public boolean handle(DatagramPacket datagramPacket) {

        System.out.println(datagramPacket);
        clientAddress = datagramPacket.getAddress();
        clientPort = datagramPacket.getPort();
        info = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println(clientAddress.getHostAddress() + ":" + clientPort);
        System.out.println("receive: " + info);
            Log.i(TAG, "handle: " + "handle执行"+ clientAddress.getHostAddress());
        if (clientAddress.getHostAddress().equals(Config.ServerIP)) {
            if (ResponseFromServer.getCode(info) == Config.REGISTER_SUCCESS) {
                mResponseFromServer = new Gson().fromJson(info,ResponseFromServer.class);
                userInfoList = mResponseFromServer.getUserInfoList();
                databaseHelper.getWritableDatabase();
                databaseHelper.refreshUserInfo(userInfoList);
                mMyApplication.refreshToken(mResponseFromServer.getToken());

                sendUserInfoUpdateBroadcast();
                return true;
            }else{
                return false;
            }

        }else{
            Log.i(TAG, "收到陈律的消息： "+ info);
            //handle chat message
            databaseHelper.addMsg(ChatMessage.fromJSON(info));

            sendChatMessageUpdateBroadcast();
            sendNotification(ChatMessage.fromJSON(info));
            return true;
        }


    }
}
