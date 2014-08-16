package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;
import android.view.KeyEvent;

import com.mattkula.wifigamepad.layouts.Controller;
import com.mattkula.wifigamepad.utilities.Keybridge;

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

    public static void saveSampleControllerIfNeeded(Context c, boolean overwrite) {
        if (!new File(c.getDir(dirName, 0), "SampleController").exists() || overwrite) {
            Controller controller = new Controller(7, 5);
            controller.addButton(2, 0, Keybridge.getServerKeycode(KeyEvent.KEYCODE_DPAD_LEFT));
            controller.addButton(1, 1, Keybridge.getServerKeycode(KeyEvent.KEYCODE_DPAD_DOWN));
            controller.addButton(3, 1, Keybridge.getServerKeycode(KeyEvent.KEYCODE_DPAD_UP));
            controller.addButton(2, 2, Keybridge.getServerKeycode(KeyEvent.KEYCODE_DPAD_RIGHT));
            controller.addButton(2, 4, Keybridge.getServerKeycode('b'));
            controller.addButton(2, 6, Keybridge.getServerKeycode('a'));
            FileUtil.saveController(c, "SampleController", controller);
        }
    }

    public static Controller loadSampleController(Context c) {
        File f = new File(c.getDir(dirName, 0), "SampleController");
        if (f.exists()) {
            try {
                return Controller.loadFromFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
