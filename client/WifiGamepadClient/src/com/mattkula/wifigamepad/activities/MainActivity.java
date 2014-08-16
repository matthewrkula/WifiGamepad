package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.customlayouting.EditPadActivity;
import com.mattkula.wifigamepad.customlayouting.FileUtil;
import com.mattkula.wifigamepad.layouts.Controller;

public class MainActivity extends Activity {

    EditText editIPAddress, editPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FileUtil.saveSampleControllerIfNeeded(this, true);

        editIPAddress = (EditText)findViewById(R.id.edit_ip_input);
        editIPAddress.setText("10.0.0.4");
        editPort = (EditText)findViewById(R.id.edit_port);
        editPort.setText("4848");
        Button btnSubmit = (Button)findViewById(R.id.button_submit);

        editIPAddress.requestFocus();

        final Controller controller = FileUtil.loadSampleController(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = editIPAddress.getText().toString();
                String port = editPort.getText().toString();
                if(ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") &&
                        port.matches("\\d+")){
                    Intent i = GamepadActivity.generateIntent(MainActivity.this, ipAddress, Integer.parseInt(port), controller);
                    startActivity(i);
                }
            }
        });
    }
}
