package com.dpcraft.simplep2pchat;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.dpcraft.simplep2pchat.UI.ContactsActivity;
import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.app.MyApplication;
import com.dpcraft.simplep2pchat.data.ResponseFromServer;
import com.dpcraft.simplep2pchat.data.UserInfo;
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;
import com.dpcraft.simplep2pchat.network.NetworkUtils;
import com.dpcraft.simplep2pchat.network.ServerPacketHandler;
import com.dpcraft.simplep2pchat.network.ServerUtils;
import com.dpcraft.simplep2pchat.network.UDP;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class KeepAliveService extends Service {
    private final int REGISTER_SUCCESS = 200;
    private final int USERNAME_ALREADY_EXIST = 201;
    private ResponseFromServer mResponseFromServer;
    private List<UserInfo> userInfoList;
    private MyDatabaseHelper databaseHelper;
    private ScheduledExecutorService mScheduledExecutorService;
    private TimerTask mTimerTask;
    private final int SEND_PERIOD = 1;
    private final int SEND_DELAY = 1;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case REGISTER_SUCCESS:
                        mResponseFromServer = new Gson().fromJson(msg.obj.toString(),ResponseFromServer.class);
                        userInfoList = mResponseFromServer.getUserInfoList();
                        databaseHelper.refreshUserInfo(userInfoList);
                        mMyApplication.refreshToken(mResponseFromServer.getToken());
                        sendUserInfoUpdateBroadcast();
                        break;
                    case USERNAME_ALREADY_EXIST:
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private MyApplication mMyApplication;

    public KeepAliveService() {
    }
    @Override
    public void onCreate(){
        super.onCreate();
        mMyApplication = MyApplication.getInstance();
        databaseHelper = new MyDatabaseHelper(this,getResources().getString(R.string.db_name),null,1);
        databaseHelper.getWritableDatabase();
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
//                ServerUtils.register2(mMyApplication.getRegister(),handler);
                try {
                    Log.i(TAG, "定时任务启动 " + mMyApplication.getPort() + mMyApplication.getRegister().getName());

                UDP.getUDP(mMyApplication.getPort()).sendMsg(InetAddress.getByName(Config.ServerIP), Config.ServerPort, mMyApplication.getRegister().toJSON());
                }catch (UnknownHostException e){
                    e.printStackTrace();

                }
            }

        };
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){


        Log.i(TAG, "onStartCommand: 服务启动");
        mScheduledExecutorService.scheduleAtFixedRate(mTimerTask,SEND_DELAY,SEND_PERIOD, TimeUnit.SECONDS);
        new Thread(new Runnable() {
            @Override
            public void run() {

                UDP.getUDP(mMyApplication.getPort()).recvMsg(new ServerPacketHandler());
            }
        }).start();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sendUserInfoUpdateBroadcast(){
        Intent intent = new Intent(Config.ACTION_USER_INFO_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
