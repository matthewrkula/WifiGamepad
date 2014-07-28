package com.mattkula.wifigamepad.customlayouting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mattkula.wifigamepad.GamepadActivity;
import com.mattkula.wifigamepad.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditPadActivity extends Activity {

    private Grid grid;
    public static final String EXTRA_IP = "ipaddress";
    public static final String EXTRA_PORT = "port";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_pad);


        int[] sizeOfGrid = Grid.autoAdjustGridLayout(this,((GridLayout)findViewById(R.id.rootGridLayout)));
        int cols = sizeOfGrid[0];
        int rows = sizeOfGrid[1];
        this.grid = new Grid(((GridLayout)findViewById(R.id.rootGridLayout)), rows,cols);
        grid.fillGrid(this);


        //The following is just to clear the file of any saved layouts you might have.
//               try {
//            clearFile("layouts");
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }

    }



    public void clickedSave(View v){


        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setSingleLine();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(input);
        builder.setMessage("Name your custom layout.")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            FileUtil.saveLayout(input.getText().toString(),getApplicationContext(),grid);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
        builder.show();


    }

    public void clickedLoad(View v){
        String fileName = "layouts";
        List<String> lines = null;
        //TODO: Make this error gracefully fix.
        try{
            lines =  FileUtil.getContentsOf(fileName,this);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        final String[] layouts = new String[lines.size()];
        final int FIRST_WORD = 0;
        String[] layoutNames = new String[layouts.length];
        for(int i = 0 ; i < layouts.length;i++){
            layouts[i] = lines.get(i);

            //The name for every layout is the first word. Each word is separated by spaces.
            layoutNames[i] = layouts[i].split(" ")[FIRST_WORD];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Color Mode");


        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, layoutNames);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int SIZE_OF_LAYOUT = layouts[position].split(" ").length;
                final int OFFSET = -1;

                //Basically that for loop just copies all elements othher than the first word to array representation.
                //The offset is 1 because every layout has a name.
                String[] arrayRepresentation = new String[SIZE_OF_LAYOUT + OFFSET];
                String[] splittedString = layouts[position].split(" ");
                for(int i = 0; i < splittedString.length;i++){
                    if(i == FIRST_WORD){
                        //Skip the first word because that's the name of the layout and has nothing to do with the grid itself.
                        continue;
                    }


                    arrayRepresentation[i + OFFSET] = splittedString[i];
                }
                grid = GridLayoutManager.loadLayout((GridLayout)findViewById(R.id.rootGridLayout),arrayRepresentation,getApplicationContext());
            }


        });
        Dialog d = builder.show();

    }
    public void clickedStart(View v){
        //TODO: I should probably send it as a parcelable but it's probably too much work anyways.

        String ipAddress = getIntent().getStringExtra(EXTRA_IP);
        int port = getIntent().getIntExtra(EXTRA_PORT, 4848);
        Intent i = GamepadActivity.generateIntent(this,ipAddress,port,grid);
        startActivity(i);

    }




    //util methods

    public static int dpFromPx(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }
    public static int pxFromDp(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public static Intent generateIntent(Context c, String ipAddress, String port){
        Intent i = new Intent(c, EditPadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);

        i.putExtra(EXTRA_PORT, Integer.parseInt(port));
        return i;
    }
}
