package com.dpcraft.simplep2pchat.network;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static android.content.ContentValues.TAG;

/**
 * Created by dpcraft on 25/11/2017.
 */

public class UDP {
    private volatile static UDP mUDP;



    private DatagramSocket socket;

    public static UDP getUDP(int port){
        if(mUDP == null){
            synchronized (UDP.class) {
                if (mUDP == null) {
                    mUDP = new UDP(port);
                }
            }
        }
        return mUDP;
    }

    public UDP(int port){

        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void recvMsg(ServerPacketHandler serverPacketHandler) {
        byte[] buff = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        while (true) {
            System.out.println("Server is listening...");
            System.out.println();
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(buff);

            System.out.println("receive: " + new String (packet.getData(),0,packet.getLength()));
            serverPacketHandler.handle(packet);
            System.out.println("Server has receive a package");
            System.out.println();
        }
    }

    public boolean sendMsg (InetAddress address, int port, String msg){

        DatagramPacket packet;
        byte[] buff = msg.getBytes();
        packet = new DatagramPacket(buff,buff.length,address,port);
        Log.i(TAG, "senMsg函数被调用 " + packet.getAddress().getHostAddress() + ":"+ packet.getPort());
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void close(){
        socket.close();
    }

}
