package com.dpcraft.simplep2pchat;


import android.util.Log;

import com.dpcraft.simplep2pchat.app.MyApplication;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChatMessage implements Serializable {

	private String message;
	private String sender;
	private String receiver;

    public ChatMessage(){}
    public ChatMessage(String message, String sender, String receiver)
    {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

	public void setMessage(String msg){
		this.message = msg;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public boolean isSentFromMe(){
		return getSender().equals(MyApplication.getInstance().getUserName());
	}

}