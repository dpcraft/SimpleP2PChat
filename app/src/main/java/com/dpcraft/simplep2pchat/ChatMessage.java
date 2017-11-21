package com.dpcraft.simplep2pchat;


import java.io.Serializable;
import java.util.ArrayList;

public class ChatMessage implements Serializable {

	public String message;
	public String sender;

    public ChatMessage(){}
    public ChatMessage(String message, String sender)
    {
        this.message=message;
        this.sender=sender;
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

}