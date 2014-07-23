package com.mattkula.wifigamepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    EditText editIPAddress, editPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editIPAddress = (EditText)findViewById(R.id.edit_ip_input);
        editPort = (EditText)findViewById(R.id.edit_port);
        Button btnSubmit = (Button)findViewById(R.id.button_submit);

        editIPAddress.requestFocus();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = editIPAddress.getText().toString();
                String port = editPort.getText().toString();
                if(ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") &&
                        port.matches("\\d+")){
    Intent i = GamepadActivity.generateIntent(MainActivity.this, ipAddress, port);
                    startActivity(i);
                }
            }
        });
    }
}
