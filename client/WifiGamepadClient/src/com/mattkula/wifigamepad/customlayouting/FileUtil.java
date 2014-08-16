package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;

import com.mattkula.wifigamepad.layouts.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan on 7/26/2014.
 */
public class FileUtil {

    private static final String dirName = "controllers";

    public static void saveController(Context c, String name, Controller controller) {
        File file = new File(c.getDir(dirName, 0), name);
        try {
            controller.saveToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSavedController(Context c, String name) {
        File file = new File(c.getDir(dirName, 0), name);
        file.delete();
    }

    public static ArrayList<Controller> getSavedControllers(Context c) {
        ArrayList<Controller> controllers = new ArrayList<Controller>();

        try {
            File file = c.getDir(dirName, 0);
            for (File controllerFile : file.listFiles()) {
                controllers.add(Controller.loadFromFile(controllerFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return controllers;
    }
}
