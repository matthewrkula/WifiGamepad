package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.SocketManager;
import com.mattkula.wifigamepad.layouts.Controller;
import com.mattkula.wifigamepad.layouts.ControllerButton;
import com.mattkula.wifigamepad.utilities.ColorUtil;

/**
 * Created by matt on 6/21/14.
 */
public class GamepadActivity extends Activity implements SocketManager.SocketManagerConnectionListener {

    public static final String EXTRA_IP = "ipaddress";
    public static final String EXTRA_PORT = "port";
    public static final String EXTRA_CONTROLLER = "controller";

    private GridLayout gridLayout;
    private Controller controller;
    private SocketManager socketManager;

    String ipAddress;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepad);

        socketManager = SocketManager.getInstance();

        gridLayout = (GridLayout)findViewById(R.id.gamePadGridLayout);

        Bundle extras = this.getIntent().getExtras();
        port = extras.getInt(EXTRA_PORT, 4848);
        ipAddress = extras.getString(EXTRA_IP);
        controller = (Controller)extras.getSerializable(EXTRA_CONTROLLER);
        populateGridview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        socketManager.connectToSocket(ipAddress, port);
        socketManager.setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SocketManager.getInstance().isConnected()) {
            SocketManager.getInstance().disconnectFromSocket();
        }
        socketManager.setListener(null);
    }

    @Override
    public void onSocketDisconnected() {
        this.finish();
    }

    public void populateGridview() {
        gridLayout.setColumnCount(controller.getColumnCount());
        gridLayout.setRowCount(controller.getRowCount());

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        for (int row = 0; row < controller.getRowCount(); row++) {
            for (int col = 0; col < controller.getColumnCount(); col++) {
                ControllerButton button = controller.buttonAt(row, col);
                View v = new View(this);
                v.setLayoutParams(
                        new ViewGroup.LayoutParams(width / controller.getColumnCount(),
                                height / controller.getRowCount()));
                if (button != null) {
                    v.setOnTouchListener(new ButtonTouchListener(button.getKeyCode()));
                    v.setBackgroundColor(ColorUtil.colorForKeycode(this, button.getKeyCode()));
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
        Vibrator vibrator;

        public ButtonTouchListener(int keycode) {
            this.keycode = keycode;
            this.vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    SocketManager.getInstance().sendData(keycode, true);
                    vibrator.vibrate(10);
                    return true;
                case MotionEvent.ACTION_UP:
                    SocketManager.getInstance().sendData(keycode, false);
                    vibrator.vibrate(10);
                    return true;
            }
            return false;
        }
    }
}
