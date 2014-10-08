package com.mattkula.wifigamepad;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Stefan on 7/27/2014.
 */
public class SocketManager {
    public enum ConnectionState {
        DISCONNECTED,
        CONNECTED
    }

    private static DataOutputStream outputStream;
    private static SocketManager socketManager;
    private static Socket socket;

    private ConnectionState state;
    private SocketManagerConnectionListener listener;

    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
            socketManager.state = ConnectionState.DISCONNECTED;
        }
        return socketManager;
    }

    public boolean isConnected() {
        return this.state == ConnectionState.CONNECTED;
    }

    public void connectToSocket(final String ipAddress, final int port){
        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    state = ConnectionState.CONNECTED;
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
        this.state = ConnectionState.DISCONNECTED;
        try {
            if (listener != null) {
                listener.onSocketDisconnected();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            sendData(-1, true);
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

    public void setListener(SocketManagerConnectionListener listener) {
        this.listener = listener;
    }

    public interface SocketManagerConnectionListener {
        public void onSocketDisconnected();
    }
}
