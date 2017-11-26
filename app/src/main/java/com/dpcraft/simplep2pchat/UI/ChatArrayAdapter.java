package com.dpcraft.simplep2pchat.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpcraft.simplep2pchat.ChatMessage;
import com.dpcraft.simplep2pchat.R;
import com.dpcraft.simplep2pchat.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpcraft on 19/11/2017.
 */



public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private LinearLayout singleMessageContainer;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public void refreshMsg(List<ChatMessage> list) {

        chatMessageList.clear();
        for(ChatMessage chatMessage : list) {
            chatMessageList.add(chatMessage);
            super.add(chatMessage);
        }
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    @Override
    public int getCount() {
        return this.chatMessageList.size();
    }

    @Override
    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }

        singleMessageContainer = row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj = getItem(position);
        chatText = row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.getMessage());
        chatText.setBackgroundResource(chatMessageObj.isSentFromMe() ? R.drawable.bubble_a : R.drawable.bubble_b);
        singleMessageContainer.setGravity(chatMessageObj.isSentFromMe() ? Gravity.RIGHT :Gravity.LEFT);
        return row;
    }



}
