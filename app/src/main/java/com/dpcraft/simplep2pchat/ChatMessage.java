package com.dpcraft.simplep2pchat;


import com.dpcraft.simplep2pchat.app.MyApplication;
import com.google.gson.Gson;

import java.io.Serializable;

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
		return getSender().equals(MyApplication.getInstance().getRegisterName());
	}


	public String toJSON(){
		Gson gson = new Gson();
		System.out.println(gson.toJson(this));
		return gson.toJson(this);
	}
	public static ChatMessage fromJSON(String JSONString){
		return new Gson().fromJson(JSONString,ChatMessage.class);
	}


}