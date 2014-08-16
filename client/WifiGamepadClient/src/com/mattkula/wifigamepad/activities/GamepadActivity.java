package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Space;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.SocketManager;

import com.mattkula.wifigamepad.layouts.Controller;
import com.mattkula.wifigamepad.layouts.ControllerButton;

/**
 * Created by matt on 6/21/14.
 */
public class GamepadActivity extends Activity {

    public static final String EXTRA_IP = "ipaddress";
    public static final String EXTRA_PORT = "port";
    public static final String EXTRA_CONTROLLER = "controller";

    private GridLayout gridLayout;
    private Controller controller;

    String ipAddress;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepad);

        gridLayout = (GridLayout)findViewById(R.id.gamePadGridLayout);

        Bundle b = this.getIntent().getExtras();
        port = b.getInt(EXTRA_PORT, 4848);
        ipAddress = b.getString(EXTRA_IP);
        controller = (Controller)b.getSerializable(EXTRA_CONTROLLER);
        populateGridview();

//        Grid.autoAdjustGridLayout(this,(GridLayout)findViewById(R.id.gamePadGridLayout));
//        GridLayoutManager.loadLayoutGamepad(((GridLayout)findViewById(R.id.gamePadGridLayout)),elements,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocketManager.getInstance().connectToSocket(ipAddress, port);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SocketManager.getInstance().disconnectFromSocket();
    }

    public void populateGridview() {
        gridLayout.setColumnCount(controller.getColumnCount());
        gridLayout.setRowCount(controller.getRowCount());

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        for (int y = 0; y < controller.getRowCount(); y++) {
            for (int x = 0; x < controller.getColumnCount(); x++) {
                ControllerButton button = controller.buttonAt(x, y);
                View v = new View(this);
                v.setLayoutParams(
                        new ViewGroup.LayoutParams(width / controller.getColumnCount(),
                                height / controller.getRowCount()));
                if (button != null) {
                    v.setOnTouchListener(new ButtonTouchListener(button.getKeyCode()));
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                }
                gridLayout.addView(v);
            }
        }
    }

    public static Intent generateIntent(Context c, String ipAddress, int port, Controller controller){
        Intent i = new Intent(c, GamepadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);
        i.putExtra(EXTRA_PORT, port);
        i.putExtra(EXTRA_CONTROLLER, controller);
        return i;
    }

    private class ButtonTouchListener implements View.OnTouchListener {

        int keycode;
        public ButtonTouchListener(int keycode) {this.keycode = keycode;}

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    SocketManager.getInstance().sendData(keycode, true);
                    return true;
                case MotionEvent.ACTION_UP:
                    SocketManager.getInstance().sendData(keycode, false);
                    return true;
            }
            return false;
        }
    }
}
