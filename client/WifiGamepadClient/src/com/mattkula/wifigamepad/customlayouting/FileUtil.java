package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 7/26/2014.
 */
public class FileUtil {

    public static void saveLayout(String layoutName, Context c, Grid grid) throws Exception {

        String filename = "layouts";
        FileOutputStream outputStream;

        String temp = layoutName + " ";
        for (GridElementButton g : grid.getElements()) {
            if(g.getText().toString().isEmpty()){
                temp += "none ";
            }
            else{
                temp += g.getText() + " ";
            }
        }

        outputStream = c.openFileOutput(filename, Context.MODE_APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream);

        osw.write(temp);
        osw.write("\n");
        osw.close();
        outputStream.close();
    }
    public static List<String> getContentsOf(String fileName, Context c) throws Exception {
        FileInputStream fis;
        String temp = "";
        List<String> lines = new ArrayList<String>();
        fis = c.openFileInput(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = "";
        while((line = br.readLine()) != null){
            lines.add(line);
        }

        return lines;
    }
    public  static void clearFile(String fileName,FileOutputStream fileOutputStream) throws Exception {
        FileOutputStream outputStream = fileOutputStream;
        OutputStreamWriter osw = new OutputStreamWriter(outputStream);
        osw.write("");
        osw.close();
        outputStream.close();
    }


}
