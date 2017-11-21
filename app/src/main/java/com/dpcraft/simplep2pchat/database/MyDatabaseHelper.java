package com.dpcraft.simplep2pchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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

    private static final String KEY_LEFT = "isleft";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_SENDFLAG="sendflag";

    public static final String CREATE_USERINFO = "create table " + TABLE_USER + " ("
            + "id integer primary key autoincrement, "
            + "username varchar(30), "
            + "address varchar(15), "
            + "port integer)";
    public static final String CREATE_MESSAGE = "CREATE TABLE " + TABLE_MSG + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_LEFT + " INTEGER,"
            + KEY_CONTENT + " TEXT," + KEY_SENDER + " TEXT," + KEY_RECEIVER + " TEXT," + KEY_SENDFLAG + " INTEGER)";

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
            System.out.println(user.getUsername() + " " + user.getAddress() + ":" + user.getPort());
        }



        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USER, null, null);
            ContentValues contentValues = new ContentValues();
            for (UserInfo userInfo : userInfoList) {
                contentValues.put("username", userInfo.getUsername());
                contentValues.put("address", userInfo.getAddress());
                contentValues.put("port", Integer.parseInt(userInfo.getPort()));
                db.insert("user_info", null, contentValues);
                contentValues.clear();
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<UserInfo> getUserInfoList(){

        List<UserInfo> userInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{

                userInfoList.add(new UserInfo(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        Integer.toString(cursor.getInt((cursor.getColumnIndex("port"))))));

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
        values.put(KEY_TYPE,msg.getType());
        values.put(KEY_LEFT,msg.isLeft()==true?1:0);
        values.put(KEY_CONTENT, msg.getMessage());
        values.put(KEY_SENDER,msg.getSender());
        values.put(KEY_RECEIVER,msg.getReceiver());
        values.put(KEY_SENDFLAG,msg.getSendFlag()==true?1:0);

        // Inserting Row
        db.insert(TABLE_MSG, null, values);
        db.close(); // Closing database connection
    }

    public List<ChatMessage> getConversation(String person) {  //person is contact whose chat screen you will open
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM msgs WHERE receiver = ? OR sender = ?", new String[]{person, person});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();
                msg.setId(Integer.parseInt(cursor.getString(0)));
                msg.setType(cursor.getString(1));
                msg.setLeft(cursor.getInt(2) == 1);
                msg.setMessage(cursor.getString(3));
                msg.setSender(cursor.getString(4));
                msg.setReceiver(cursor.getString(5));
                msg.setSendFlag(cursor.getInt(6) == 1);
                // Adding contact to list
                msgList.add(msg);
            } while (cursor.moveToNext());
        }
        // return msg list
        return msgList;
    }




}
