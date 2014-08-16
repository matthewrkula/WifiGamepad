package com.mattkula.wifigamepad;

import android.app.Activity;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Stefan on 7/27/2014.
 */
public class SocketManager {

    private static Socket socket;
    private static DataOutputStream outputStream;
    private static SocketManager socketManager;

    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    public void connectToSocket(final String ipAddress, final int port){
        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (ConnectException e){
                    disconnectFromSocket();
                } catch (Exception e){
                    disconnectFromSocket();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void disconnectFromSocket() {
        Log.v("NETWORKING", "Disconnecting from socket");
        try {
            sendData(-1, true);
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(final int data, final boolean down){
        if(socket != null && socket.isConnected() && outputStream != null){
            Log.v("NETWORKING", "Sending " + data + " - " + (down ? "down" : "up"));
            new Thread(){
                @Override
                public void run() {
                    try {
                        outputStream.writeInt(data);
                        outputStream.writeBoolean(down);
                        outputStream.flush();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            Log.e("NETWORKING", "Socket not connected.");
        }
    }
}
