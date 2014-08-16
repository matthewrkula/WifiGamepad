package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by matt on 7/27/14.
 */
public class ButtonGridLayout extends GridLayout {

    public ButtonGridLayout(Context context) {
        super(context);
    }

    public ButtonGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void saveToFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file, false);

        writer.write(Integer.toString(getRowCount()));
        writer.write(" ");
        writer.write(Integer.toString(getColumnCount()));
        writer.write("\n");

        for (int i=0; i < getColumnCount(); i++) {
            for (int j=0; j < getRowCount(); j++) {
                //TODO write button types to file
            }
        }
    }
}
