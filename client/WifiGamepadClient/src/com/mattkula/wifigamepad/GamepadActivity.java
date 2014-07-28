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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mattkula.wifigamepad.customlayouting.ButtonType;
import com.mattkula.wifigamepad.customlayouting.Grid;
import com.mattkula.wifigamepad.customlayouting.GridElementButton;
import com.mattkula.wifigamepad.customlayouting.GridLayoutManager;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepad);

        String ipAddress = getIntent().getStringExtra(EXTRA_IP);
        int port = getIntent().getIntExtra(EXTRA_PORT, 4848);
       // SocketManager.connectToSocket(this, ipAddress, port);
        Bundle b=this.getIntent().getExtras();
        String[] elements=b.getStringArray("grid");
        Grid.autoAdjustGridLayout(this,(GridLayout)findViewById(R.id.gamePadGridLayout));
        GridLayoutManager.loadLayoutGamepad(((GridLayout)findViewById(R.id.gamePadGridLayout)),elements,this);

    }

    //Static Utility methods:

    public static Intent generateIntent(Context c, String ipAddress, int port,Grid grid){

        Intent i = new Intent(c, GamepadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);
        i.putExtra(EXTRA_PORT, port);

        Bundle b=new Bundle();
        b.putStringArray("grid", grid.getElementNames());
        i.putExtras(b);

        return i;
    }


}