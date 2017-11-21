package com.dpcraft.simplep2pchat;


import java.io.Serializable;
import java.util.ArrayList;

public class ChatMessage implements Serializable {
	public boolean left;
    public int id;
	public String message;
	public String type;
	public String sender;
	public String receiver;
	public boolean sendFlag=false;
	public boolean incogMode=false;
	public String groupname=null;
	public ArrayList<String> groupmembers=null;
	public boolean groupmode=false;

    public ChatMessage(){}
    public ChatMessage(Boolean left, String type, String message, String sender, String receiver, Boolean sendFlag)
    {
        this.left=left;
        this.type=type;
        this.message=message;
        this.sender=sender;
        this.receiver=receiver;
        this.sendFlag=sendFlag;
    }

    public void setId(int id){this.id=id;}
	public void setLeft(boolean doleft){
		this.left = doleft;
	}

	public void setMessage(String msg){
		this.message = msg;
	}

	public void setType(String type){
		this.type = type;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	public void setReceiver(String receiver){this.receiver = receiver;}

	public void setSendFlag(boolean issendflag){
		this.sendFlag = issendflag;
	}
    public int getId(){return id;}
	public String getMessage() {
		return message;
	}

	public boolean isLeft() {
		return left;
	}

	public String getType() {
		return type;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver(){ return receiver;}

	public boolean getSendFlag(){
		return sendFlag;
	}

	public void setIncogFlag(boolean incogflag){
		this.incogMode = incogflag;
	}

	public boolean getIncogFlag(){
		return incogMode;
	}

	public void setGroupname(String groupname){
		this.groupname = groupname;
	}

	public String getGroupname(){return groupname;}

	public void setGroupmembers(ArrayList<String> groupmembers){
		this.groupmembers = groupmembers;
	}

	public ArrayList<String> getGroupmembers(){return groupmembers;}

	public void setGroupmode(boolean flag){
		this.groupmode = flag;
	}

	public boolean getGroupmode() {
		return groupmode;
	}
}