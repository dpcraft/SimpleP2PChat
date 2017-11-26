package com.dpcraft.simplep2pchat.UI;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.R;
import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.app.MyApplication;
import com.dpcraft.simplep2pchat.data.UserInfo;
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;
import com.dpcraft.simplep2pchat.network.NetworkUtils;
import com.dpcraft.simplep2pchat.network.ServerPacketHandler;
import com.dpcraft.simplep2pchat.network.ServerUtils;
import com.dpcraft.simplep2pchat.network.UDP;
import com.dpcraft.simplep2pchat.test.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class ChatActivity extends AppCompatActivity {


    static int RESULT_LOAD_IMG;
    private ChatMessageUpdateReceiver mChatMessageUpdateReceiver;
    static final String TAG = "ChatActivity";
    static ChatArrayAdapter chatArrayAdapter;
    static ListView listView;
    static EditText chatText;
    static Button buttonSend;
    private Toolbar mToolbar;
    static String me = MyApplication.getInstance().getRegisterName();
    private static String receiver = null;
    private static Context context = null;
    static MyDatabaseHelper db;
    public static NotificationManager notificationManager;
    private IntentFilter intentFilter;
    private List<ChatMessage> oldmsgs;

    public ChatActivity() {
        context = this;
        db = new MyDatabaseHelper(context);
    }

    public MyDatabaseHelper getDb() {
        return db;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        receiver = intent.getStringExtra("username");
        mToolbar = findViewById(R.id.tb_chat);
        mToolbar.setTitle(receiver);
        setSupportActionBar(mToolbar);

        buttonSend = findViewById(R.id.buttonSend);
        listView = findViewById(R.id.listView1);
//        initMessage();
        registReceiver();

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = findViewById(R.id.chatText);
        //Show old messages
            oldmsgs = getDb().getConversation(receiver);
            for (ChatMessage msg : oldmsgs) {
                chatArrayAdapter.add(msg);
            }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    //create a new msg;
                        ChatMessage chatMessage = new ChatMessage();
                        UserInfo userInfo = db.getUserInfoByName(receiver);

                        chatMessage.setMessage(chatText.getText().toString());
                        chatMessage.setReceiver(receiver);
                        Log.e(TAG, "onClick: new tag " + chatMessage.getMessage());
                        chatMessage.setSender(me);
                        sendRecvChatMessage(chatMessage);
                    ServerUtils.sendMSG(userInfo,chatMessage);



                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        listView.setDivider(null);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

    }

    public static void sendRecvChatMessage(ChatMessage chatMessage) throws ExecutionException, InterruptedException, GeneralSecurityException {

        chatArrayAdapter.add(chatMessage);
        db.addMsg(chatMessage);
        //changed for groupchat
        chatText.setText("");
    }

    public static void actionStart(Context context,String username){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("username",username);
        context.startActivity(intent);

    }


    private void registReceiver(){
        intentFilter = new IntentFilter();
        intentFilter.addAction(Config.ACTION_USER_INFO_UPDATE);
        mChatMessageUpdateReceiver = new ChatActivity.ChatMessageUpdateReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mChatMessageUpdateReceiver, intentFilter);
    }

    class ChatMessageUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Log.i(TAG, "onReceive: " + "update chatmessage broadcast");
            chatArrayAdapter.refreshMsg(db.getConversation(receiver));
            chatArrayAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mChatMessageUpdateReceiver);
    }

    private void initMessage(){
        List<ChatMessage> chatMessageList = Test.initChatMessageList();
        for(ChatMessage chatMessage : chatMessageList){
            db.addMsg(chatMessage);

        }
    }



}