package com.dpcraft.simplep2pchat.network;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.dpcraft.simplep2pchat.ChatMessage;

import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import static android.content.ContentValues.TAG;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class NetworkUtils {

    public static String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        return ipAddressString;
    }

    public void sendMsgToUser(final ChatMessage chatMessage, final String host,final int port)
    {
        Thread sendthread  = new Thread(){
            @Override
            public void run() {
                try
                {
                    Socket socket;
                    Log.e(TAG, "run: "+ port );
                    InetAddress address = InetAddress.getByName(host);
                    Log.e(TAG, "run: addr"+address );
                    socket = new Socket(address, port);
                    //log.d for writing msg
                    Log.e(TAG, "SendMsgUtil: socket created");
                    //Send the message to the server
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    Log.e(TAG, "SendMsgUtil: msg sent");
                    objectOutputStream.writeObject(chatMessage);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        };
        sendthread.start();
    }

}
