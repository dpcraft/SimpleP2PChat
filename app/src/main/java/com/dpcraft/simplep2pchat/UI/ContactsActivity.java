package com.dpcraft.simplep2pchat.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dpcraft.simplep2pchat.ContactsAdapter;
import com.dpcraft.simplep2pchat.R;
import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.data.UserInfo;
import com.dpcraft.simplep2pchat.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dpcraft on 19/11/2017.
 */

public class ContactsActivity extends AppCompatActivity {
    final  private String TAG = "ContactsActivity===";
    private List<UserInfo> userInfoList = new ArrayList<>();
    private MyDatabaseHelper databaseHelper;
    private IntentFilter intentFilter;
    private UserInfoUpdateReceiver receiver;
    ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar contactsToolbar= findViewById(R.id.tb_contacts);
        contactsToolbar.setTitle(getResources().getString(R.string.actionbar_contacts));
        setSupportActionBar(contactsToolbar);

        databaseHelper = new MyDatabaseHelper(this,getResources().getString(R.string.db_name),null,1);
        userInfoList = databaseHelper.getUserInfoList();
        Log.i(TAG, Integer.toString(userInfoList.size()));
        contactsAdapter = new ContactsAdapter(ContactsActivity.this,R.layout.contacts_item,userInfoList);
        ListView listView = findViewById(R.id.contacts_list);
        listView.setAdapter(contactsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserInfo userInfo = userInfoList.get(position);
                ChatActivity.actionStart(ContactsActivity.this,userInfo.getName());
            }
        });
        registReceiver();
    }

    private void registReceiver(){
        intentFilter = new IntentFilter();
        intentFilter.addAction(Config.ACTION_USER_INFO_UPDATE);
        receiver = new UserInfoUpdateReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    public static void actionStart(Context context,String data1){
        Intent intent = new Intent(context,ContactsActivity.class);
        intent.putExtra("param1",data1);
        context.startActivity(intent);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    class UserInfoUpdateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            Log.i("update","get user info");
            userInfoList.clear();
            userInfoList.addAll(databaseHelper.getUserInfoList());
            contactsAdapter.notifyDataSetChanged();
        }
    }
}
