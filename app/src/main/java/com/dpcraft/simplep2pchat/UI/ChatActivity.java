package com.dpcraft.simplep2pchat.UI;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class ChatActivity extends AppCompatActivity{


    static int RESULT_LOAD_IMG;
    static final String TAG = "ChatActivity";
    static boolean status;
    static ChatArrayAdapter chatArrayAdapter;
    static ListView listView;
    static EditText chatText;
    static Button buttonSend;

    //static String me = SmsActivity.pref.getMobileNumber();
    static String me = "me";
    private static String receiver=null;
    private static Context context = null;
    static boolean side = false;
    static MyDatabaseHelper db;
    String username;
    static String password = "password";
    private String groupflag=null;
    private ArrayList<String> grouplist = null;
    public static NotificationManager notificationManager;

    public ChatActivity(){
        context = this;
        db = new MyDatabaseHelper(context);
    }

    public MyDatabaseHelper getDb(){
        return db;
    }

    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //setActionBar();
        Intent intent = getIntent();
        //see if group-flag is et or not
        //

        //receiver = new NetworkUtil().getParsedMobile((getIntent().getStringExtra("mobile")));

        buttonSend = findViewById(R.id.buttonSend);
        listView = findViewById(R.id.listView1);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chatText);
        //Show old messages
        if(groupflag.equals("false")) {
            List<ChatMessage> Oldmsgs = getDb().getConversation(receiver);
            for (ChatMessage msg : Oldmsgs) {
                chatArrayAdapter.add(msg);
            }
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    //create a new msg;
                    if(groupflag.equals("false")) {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSendFlag(true);
                        chatMessage.setType("txt");
                        chatMessage.setLeft(false);
                        chatMessage.setMessage(chatText.getText().toString());
                        Log.e(TAG, "onClick: new tag " + chatMessage.getMessage());
                        chatMessage.setSender(me);
                        chatMessage.setReceiver(receiver);
                        //check if icog is set for this receiver
                        if (Config.IncogModeUsers != null && Config.IncogModeUsers.get(receiver)) {
                            chatMessage.setIncogFlag(true);
                        }

                        sendRecvChatMessage(chatMessage);
                    }

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


    /*public static void sendRecvChatMessage(ChatMessage chatMessage) throws ExecutionException, InterruptedException, GeneralSecurityException {

        if(chatMessage.getSendFlag()==true){
            //if incog related req is sent
            if(chatMessage.getType().equals(Config.INCOG_INITIATE_REQUEST)
                    || chatMessage.getType().equals(Config.INCOG_REJECT_REQUEST)){}

            else if(chatMessage.getType().equals(Config.INCOG_ACCEPT_REQUEST)) {
                if(Config.IncogModeUsers==null)
                    Config.IncogModeUsers = new HashMap<String,Boolean>();

                Config.IncogModeUsers.put(chatMessage.getReceiver(),true);
            }
            else if(Config.IncogModeUsers!=null && Config.IncogModeUsers.get(chatMessage.getReceiver())){
                //&& Config.IncogModeUsers.get(chatMessage.getSender()))
                Log.e(TAG, "sendRecvChatMessage: Config.Incog!null" );
                Log.e(TAG, "sendRecvChatMessage: "+Config.IncogModeUsers.get(chatMessage.getSender()));
                chatArrayAdapter.add(chatMessage);
            }
            //if set_new group msg
            else if(chatMessage.getType().equals(Config.SET_NEW_GROUP)){}
            //if group-msg ; as of now not storing in db
            else if(chatMessage.getType().equals(Config.GROUP_MSG)){}
            //if normal msg-ing
            else{
                chatArrayAdapter.add(chatMessage);
                db.addMsg(chatMessage);
                //changed for groupchat
                chatText.setText("");
            }
            NetworkUtil networkUtil = new NetworkUtil();
            String sendingIP = networkUtil.getSendingIP(chatMessage.getReceiver());
            if (sendingIP!=null){
                networkUtil.sendMsgToUser(chatMessage,sendingIP);
                if(!chatMessage.getType().equals(Config.GROUP_MSG)) {
                    Log.e(TAG, "sendRecvChatMessage: " );
                    status = networkUtil.status;
                    Log.e(TAG, "sendRecvChatMessage: status "+status );
                    //logic for real time status changing
                    if (!status)
                        actionBar.setSubtitle("Offline");
                    else
                        actionBar.setSubtitle("Online");
                }
            }

        }
        else {
            //this if for incog start
            if (chatMessage.getType().equals(Config.INCOG_INITIATE_REQUEST)) {
                invokePrompt(getContext(), HomeScreen.contacthash.get(chatMessage.getSender()) + " wants to start incog with you");
            }
            else if(chatMessage.getType().equals(Config.INCOG_REJECT_REQUEST)){
                Toast.makeText(getContext(),HomeScreen.contacthash.get(chatMessage.getSender())+" rejected your incog request!",Toast.LENGTH_LONG).show();
            }
            else if(chatMessage.getType().equals(Config.INCOG_ACCEPT_REQUEST)) {
                if(Config.IncogModeUsers==null)
                    Config.IncogModeUsers = new HashMap<String,Boolean>();
                Config.IncogModeUsers.put(chatMessage.getSender(), true);
                Toast.makeText(getContext(),HomeScreen.contacthash.get(chatMessage.getSender())+" accepted your incog request!. Start conversing in private mode.",Toast.LENGTH_LONG).show();
            }
            else if(chatMessage.getType().equals(Config.FORCE_ABORT_INCOG)){
                Config.IncogModeUsers.put(chatMessage.getSender(), false);
                Toast.makeText(context,"You are now NOT in incognito mode. "+HomeScreen.contacthash.get(chatMessage.getSender())
                        +" left the incognito mode."
                        +" Messages will now be visible",Toast.LENGTH_LONG).show();
            }
            else if(Config.IncogModeUsers!=null && Config.IncogModeUsers.get(chatMessage.getSender())){
                chatMessage.setLeft(true);
                chatArrayAdapter.add(chatMessage);
            }
            //on grp-msg recieve
            else if(chatMessage.getType().equals(Config.GROUP_MSG)){
                chatMessage.setLeft(true);
                String sendermobile = chatMessage.getSender();
                String sendername = HomeScreen.contacthash.get(sendermobile);
                if(sendername!=null)
                    chatMessage.message = sendername+": "+chatMessage.getMessage();
                else
                    chatMessage.message = sendermobile+": "+chatMessage.getMessage();

                chatArrayAdapter.add(chatMessage);
            }
            else if(chatMessage.getType().equals(Config.SET_NEW_GROUP)){

                Log.e(TAG, "sendRecvChatMessage: set_new_group" );
                //get the group info from the msg
                String groupname = chatMessage.getGroupname();
                ArrayList<String> grouplist = chatMessage.getGroupmembers();
                //
                if(GroupChat.groupList == null)
                    GroupChat.groupList = new HashMap<String,ArrayList<String>>();

                GroupChat.groupList.put(groupname,grouplist);
                //
                Conversation conversation = new Conversation();
                conversation.setThumb(null);
                conversation.setName(groupname);
                conversation.setNumber(null);
                conversation.setGroupflag(true);
                //
                RecentFragment.conversations.add(conversation);
                RecentFragment.listView.setAdapter(RecentFragment.adapter);
            }
            else {
                Log.e(TAG, "sendRecvChatMessage: " + chatMessage.message);
                chatMessage.setLeft(true);
                Log.d("Insert: ", "Inserting Received Msg..");
                db.addMsg(chatMessage);
                chatArrayAdapter.add(chatMessage);
                Notification no=new Notification(context,notificationManager);
                no.startNotification(chatMessage.getSender(),chatMessage.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat_screen, menu);

        setActionBar();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.icon_attachment){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }

        if(id == R.id.icon_incognito_off){

            if(Config.IncogModeUsers==null)
                Config.IncogModeUsers = new HashMap<String,Boolean>();

            ChatMessage chatmessage = new ChatMessage();
            chatmessage.setSendFlag(true);
            chatmessage.setReceiver(receiver);
            chatmessage.setSender(me);
            chatmessage.setMessage(null);

            if(Config.IncogModeUsers.get(receiver) == null || Config.IncogModeUsers.get(receiver)==false) {
                chatmessage.setType(Config.INCOG_INITIATE_REQUEST);
                Toast.makeText(context,"Request sent. Wait for buddy to accept.",Toast.LENGTH_LONG).show();
            }else{
                chatmessage.setType(Config.FORCE_ABORT_INCOG);
                Config.IncogModeUsers.put(receiver, false);
                Toast.makeText(context,"You are now NOT in incognito mode. Messages will now be visible",Toast.LENGTH_LONG).show();
            }
            try {
                sendRecvChatMessage(chatmessage);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

        }

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setActionBar(){

        actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);


        if(getIntent().hasExtra("user_name")) {
            actionBar.setTitle(getIntent().getStringExtra("user_name"));
            actionBar.setLogo(R.mipmap.ic_launcher);
            //has to be set by cmds
            if(groupflag.equals("false"))
                actionBar.setSubtitle(getIntent().getStringExtra("status"));
        }
    }


    public ActionBar getActionB(){
        return actionBar;
    }


    public static void invokePrompt(final Context context, String msg){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        String positiveMsg = "Yes",negativeMsg="No";

        builder1.setPositiveButton(
                positiveMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //incog accept work
                        ChatMessage chatmessage = new ChatMessage();
                        chatmessage.setSendFlag(true);
                        chatmessage.setType(Config.INCOG_ACCEPT_REQUEST);
                        chatmessage.setSender(me);
                        chatmessage.setReceiver(receiver);
                        chatmessage.setIncogFlag(true);
                        //
                        try {
                            sendRecvChatMessage(chatmessage);
                            Toast.makeText(context,"Start chatting in incognito!!",Toast.LENGTH_LONG).show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                        //
                        dialog.cancel();
                    }
                });


        builder1.setNegativeButton(
                negativeMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //incog reject work
                        ChatMessage chatmessage = new ChatMessage();
                        chatmessage.setMessage(null);
                        chatmessage.setType(Config.INCOG_REJECT_REQUEST);
                        chatmessage.setSendFlag(true);
                        chatmessage.setSender(me);
                        chatmessage.setReceiver(receiver);

                        try {
                            sendRecvChatMessage(chatmessage);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                        //
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
*/
}
