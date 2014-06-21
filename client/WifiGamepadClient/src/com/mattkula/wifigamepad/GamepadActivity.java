package com.mattkula.wifigamepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by matt on 6/21/14.
 */
public class GamepadActivity extends Activity {

    public static final String EXTRA_IP = "ipaddress";

    ImageView b1, b2, b3, b4, b5, b6;

    Socket socket;
    DataOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepad);

        b1 = (ImageView)findViewById(R.id.imageView);
        b2 = (ImageView)findViewById(R.id.imageView2);
        b3 = (ImageView)findViewById(R.id.imageView3);
        b4 = (ImageView)findViewById(R.id.imageView4);
        b5 = (ImageView)findViewById(R.id.imageView5);
        b6 = (ImageView)findViewById(R.id.imageView6);

        b1.setOnTouchListener(new ButtonTouchListener(1));
        b2.setOnTouchListener(new ButtonTouchListener(2));
        b3.setOnTouchListener(new ButtonTouchListener(3));
        b4.setOnTouchListener(new ButtonTouchListener(4));
        b5.setOnTouchListener(new ButtonTouchListener(5));
        b6.setOnTouchListener(new ButtonTouchListener(6));

        final String ipAddress = getIntent().getStringExtra(EXTRA_IP);

        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, 4848);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (Exception e){
                    e.printStackTrace();
                    b1.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GamepadActivity.this, "Error connecting", 500).show();
                        }
                    });
                }
            }
        }.start();
    }

    public static Intent generateIntent(Context c, String ipAddress){
        Intent i = new Intent(c, GamepadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);
        return i;
    }

    public void sendData(final int data, final boolean down){
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

    private class ButtonTouchListener implements View.OnTouchListener {

        int buttonNumber;

        public ButtonTouchListener(int buttonNumber){
            this.buttonNumber = buttonNumber;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    sendData(buttonNumber, true);
                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(25);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sendData(buttonNumber, false);
                    return true;
            }
            return false;
        }
    }
}
