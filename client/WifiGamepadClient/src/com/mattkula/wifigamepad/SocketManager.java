package com.mattkula.wifigamepad;

import android.app.Activity;

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
    public static void connectToSocket(final Activity baseActivity, final String ipAddress, final int port){

        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (ConnectException e){

                    sendData(-1, true);
                    baseActivity.finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void sendData(final int data, final boolean down){
        if(socket != null && socket.isConnected() && outputStream != null){
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
        }
    }
}
