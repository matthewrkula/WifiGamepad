package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.customlayouting.FileUtil;
import com.mattkula.wifigamepad.layouts.Controller;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText editIPAddress, editPort;
    ListView controllerList;

    Controller selectedController;

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

        controllerList = (ListView)findViewById(R.id.list_controllers);
        controllerList.setAdapter(new ControllerListAdapter());
        controllerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controllerList.setSelection(i);
                selectedController = (Controller)controllerList.getItemAtPosition(i);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = editIPAddress.getText().toString();
                String port = editPort.getText().toString();
                if(ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") &&
                        port.matches("\\d+") && selectedController != null){
                    Intent i = GamepadActivity.generateIntent(MainActivity.this, ipAddress, Integer.parseInt(port), selectedController);
                    startActivity(i);
                }
            }
        });
    }

    private class ControllerListAdapter extends BaseAdapter {

        ArrayList<Controller> controllers;

        public ControllerListAdapter() {
            controllers = FileUtil.getSavedControllers(MainActivity.this);
        }

        @Override
        public int getCount() {
            return controllers.size();
        }

        @Override
        public Object getItem(int i) {
            return controllers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_single_choice, viewGroup, false);
            }

            Controller controller = (Controller)getItem(i);
            ((TextView)view.findViewById(android.R.id.text1)).setText(controller.getName());
            return view;
        }
    }
}
