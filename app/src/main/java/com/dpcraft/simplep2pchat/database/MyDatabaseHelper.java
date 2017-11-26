package com.dpcraft.simplep2pchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.lang.UScript;
import android.util.Log;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.data.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static  final String TAG = "DBHelper====";
    private Context mContext;
    private static final String DB_NAME = "P2PChat.db";
    private static final int DB_VERSION = 1;

    //table name
    private static final String TABLE_USER = "user_info";
    private static final String TABLE_MSG = "message";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ID = "id";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECVER = "recver";
    private static final String KEY_TIME = "recv_time";

    public static final String CREATE_USERINFO = "create table " + TABLE_USER + " ("
            + "id integer primary key autoincrement, "
            + "username varchar(30), "
            + "address varchar(15), "
            + "port integer)";
    public static final String CREATE_MESSAGE = "CREATE TABLE " + TABLE_MSG + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MESSAGE + " TEXT,"
            + KEY_SENDER + " TEXT,"
            + KEY_RECVER + " TEXT,"
            + KEY_TIME + " TIMESTAMP NOT NULL DEFAULT (DATETIME('now','localtime')))";


    public MyDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }
    public MyDatabaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERINFO);
        db.execSQL(CREATE_MESSAGE);

        Log.i(TAG,"sqlite onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){

    }

    /*
    *
    * refresh user_info table
    *
    * */

    public boolean refreshUserInfo(List<UserInfo> userInfoList) {
        for(UserInfo user: userInfoList ){
            System.out.println(TAG + "1:");
            System.out.println(user.getName() + " " + user.getAddress() + ":" + user.getPort());
        }



        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USER, null, null);
            ContentValues contentValues = new ContentValues();
            for (UserInfo userInfo : userInfoList) {
                contentValues.put("username", userInfo.getName());
                contentValues.put("address", userInfo.getAddress());
                contentValues.put("port", userInfo.getPort());
                db.insert("user_info", null, contentValues);
                contentValues.clear();
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public UserInfo getUserInfoByName(String username) {

        Cursor cursor = null;
        UserInfo userInfo = new UserInfo();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_USER + " WHERE username = '" + username + "'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    userInfo.setUsername(cursor.getString(1));
                    userInfo.setAddress(cursor.getString(2));
                    userInfo.setPort(cursor.getInt(3));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "getConversation: sqoliteException");
            e.printStackTrace();
        }
        return userInfo;
    }

    public List<UserInfo> getUserInfoList(){

        List<UserInfo> userInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{

                userInfoList.add(new UserInfo(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getInt((cursor.getColumnIndex("port")))));

            }while(cursor.moveToNext());
        }
        cursor.close();
        return userInfoList;
    }

    /*
    * message table method
    * */

    // Adding new msg
    public void addMsg(ChatMessage msg) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<ChatMessage> msgs;

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, msg.getMessage());
        values.put(KEY_SENDER,msg.getSender());
        values.put(KEY_RECVER,msg.getReceiver());
        // Inserting Row
        db.insert(TABLE_MSG, null, values);
        db.close(); // Closing database connection
    }

    public List<ChatMessage> getConversation(String person) {  //person is contact whose chat screen you will open
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        // Select All Query

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_MSG +" WHERE sender = '"+ person + "' OR recver = '" + person + "'";
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                do {
                    ChatMessage msg = new ChatMessage();
                    msg.setMessage(cursor.getString(1));
                    msg.setSender(cursor.getString(2));
                    msg.setReceiver(cursor.getString(3));
                    // Adding contact to list
                    msgList.add(msg);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "getConversation: sqoliteException");
            e.printStackTrace();
        }

        // looping through all rows and adding to list

        // return msg list
        return msgList;
    }

    public List<ChatMessage> getMSGListFromIndex(String person,int index) {  //person is contact whose chat screen you will open
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        // Select All Query

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_MSG +" WHERE sender = '"+ person + "' OR recver = '" + person + "' AND id > " + index ;
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                do {
                    ChatMessage msg = new ChatMessage();
                    msg.setMessage(cursor.getString(1));
                    msg.setSender(cursor.getString(2));
                    msg.setReceiver(cursor.getString(3));
                    // Adding contact to list
                    msgList.add(msg);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "getConversation: sqoliteException");
            e.printStackTrace();
        }

        // looping through all rows and adding to list

        // return msg list
        return msgList;
    }




}
