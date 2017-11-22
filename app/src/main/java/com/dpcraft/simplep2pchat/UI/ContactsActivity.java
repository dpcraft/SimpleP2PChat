package com.dpcraft.simplep2pchat.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dpcraft.simplep2pchat.ContactsAdapter;
import com.dpcraft.simplep2pchat.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        databaseHelper = new MyDatabaseHelper(this,getResources().getString(R.string.db_name),null,1);
        userInfoList = databaseHelper.getUserInfoList();
        Log.i(TAG, Integer.toString(userInfoList.size()));
        ContactsAdapter contactsAdapter = new ContactsAdapter(ContactsActivity.this,R.layout.contacts_item,userInfoList);
        ListView listView = findViewById(R.id.contacts_list);
        listView.setAdapter(contactsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserInfo userInfo = userInfoList.get(position);
                ChatActivity.actionStart(ContactsActivity.this,userInfo.getUsername());
            }
        });
    }



    public static void actionStart(Context context,String data1){
        Intent intent = new Intent(context,ContactsActivity.class);
        intent.putExtra("param1",data1);
        context.startActivity(intent);

    }

}
