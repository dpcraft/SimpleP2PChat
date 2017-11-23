package com.dpcraft.simplep2pchat.UI;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.R;
import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.app.MyApplication;
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;
import com.dpcraft.simplep2pchat.test.Test;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class ChatActivity extends AppCompatActivity {


    static int RESULT_LOAD_IMG;
    static final String TAG = "ChatActivity";
    static boolean status;
    static ChatArrayAdapter chatArrayAdapter;
    static ListView listView;
    static EditText chatText;
    static Button buttonSend;
    private Toolbar mToolbar;
    static String me = MyApplication.getInstance().getUserName();
    private static String receiver = null;
    private static Context context = null;
    static MyDatabaseHelper db;
    String username;
    static String password = "password";
    private String groupflag = null;
    private ArrayList<String> grouplist = null;
    public static NotificationManager notificationManager;

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
        initMessage();

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = findViewById(R.id.chatText);
        //Show old messages
            List<ChatMessage> oldmsgs = getDb().getConversation(receiver);
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

                        chatMessage.setMessage(chatText.getText().toString());
                        chatMessage.setReceiver(receiver);
                        Log.e(TAG, "onClick: new tag " + chatMessage.getMessage());
                        chatMessage.setSender(me);
                        sendRecvChatMessage(chatMessage);

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

    private void initMessage(){
        List<ChatMessage> chatMessageList = Test.initChatMessageList();
        for(ChatMessage chatMessage : chatMessageList){
            db.addMsg(chatMessage);

        }
    }


}