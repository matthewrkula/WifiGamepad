package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.layouts.Controller;
import com.mattkula.wifigamepad.utilities.FileUtil;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText editIPAddress, editPort;
    ListView controllerList;

    Controller selectedController;

    ControllerListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileUtil.saveSampleControllerIfNeeded(this, false);

        editIPAddress = (EditText)findViewById(R.id.edit_ip_input);
        editIPAddress.setText("192.168.1.67");
        editPort = (EditText)findViewById(R.id.edit_port);
        editPort.setText("4848");
        Button btnSubmit = (Button)findViewById(R.id.button_submit);

        editIPAddress.requestFocus();

        controllerList = (ListView)findViewById(R.id.list_controllers);
        adapter = new ControllerListAdapter(this);
        controllerList.setAdapter(adapter);
        controllerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controllerList.setSelection(i);
                selectedController = (Controller)controllerList.getItemAtPosition(i);
            }
        });

        controllerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Controller deletedController = (Controller)controllerList.getItemAtPosition(i);
                FileUtil.deleteSavedController(MainActivity.this, deletedController.getName());
                adapter.notifyDataSetChanged();
                selectedController = null;
                return true;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = editIPAddress.getText().toString();
                String port = editPort.getText().toString();
                if(ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") &&
                        port.matches("\\d+")) {
                    if (selectedController == null) {
                        Toast.makeText(MainActivity.this, "Please select or make a controller.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent i = GamepadActivity.generateIntent(MainActivity.this, ipAddress, Integer.parseInt(port), selectedController);
                    startActivity(i);
                }
            }
        });
    }

    private void startEditPadActivity(String filename) {
        String ipAddress = editIPAddress.getText().toString();
        String port = editPort.getText().toString();
        Intent i = EditPadActivity.generateIntent(MainActivity.this, ipAddress, port, filename);
        startActivity(i);
    }

    private void showNewNameDialog() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Enter New Name")
                .setMessage("Please enter the name of your new controller.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputText = input.getText().toString();
                        if (!inputText.isEmpty()) {
                            startEditPadActivity(inputText);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_controller:
                showNewNameDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class ControllerListAdapter extends BaseAdapter {

        ArrayList<Controller> controllers;

        public ControllerListAdapter(Context c) {
            controllers = FileUtil.getSavedControllers(c);
        }

        @Override
        public void notifyDataSetChanged() {
            controllers = FileUtil.getSavedControllers(getApplicationContext());
            super.notifyDataSetChanged();
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
