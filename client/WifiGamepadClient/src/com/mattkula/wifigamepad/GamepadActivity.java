package com.mattkula.wifigamepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mattkula.wifigamepad.customlayouting.ButtonType;
import com.mattkula.wifigamepad.customlayouting.GridElementButton;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by matt on 6/21/14.
 */
public class GamepadActivity extends Activity {

    public static final String EXTRA_IP = "ipaddress";
    public static final String EXTRA_PORT = "port";


    private Socket socket;
    private DataOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepad);


        int[] sizeOfGrid = autoAdjustGridLayout();
        int cols = sizeOfGrid[0];
        int rows = sizeOfGrid[1];

        for(int i = 0; i < cols * rows;i++){
            GridLayout gridLayout = ((GridLayout)findViewById(R.id.rootGridLayout));
            gridLayout.addView(new GridElementButton(this));
        }

    }

    //Returns size of grid
    private int[] autoAdjustGridLayout(){
        int width,height;
        if (android.os.Build.VERSION.SDK_INT >= 13){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }
        else {
            Display display = getWindowManager().getDefaultDisplay();
             width = display.getWidth();
             height = display.getHeight();
        }
        width = dpFromPx(width);
        height = dpFromPx(height);

        int amountOfRows = height/70;
        int amountOfCols = width/70;

        GridLayout gridLayout = ((GridLayout)findViewById(R.id.rootGridLayout));

        gridLayout.setColumnCount(amountOfCols);
        gridLayout.setRowCount(amountOfRows);
        return new int[]{amountOfCols,amountOfRows};
    }
    private int dpFromPx(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    private void connectToSocket(){
        final String ipAddress = getIntent().getStringExtra(EXTRA_IP);
        final int port = getIntent().getIntExtra(EXTRA_PORT, 4848);

        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (ConnectException e){
                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
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

    @Override
    public void finish() {
        sendData(-1, true);
        super.finish();
    }

    public static Intent generateIntent(Context c, String ipAddress, String port){
        Intent i = new Intent(c, GamepadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);
        i.putExtra(EXTRA_PORT, Integer.parseInt(port));
        return i;
    }


        }

