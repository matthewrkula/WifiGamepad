package com.mattkula.wifigamepad.utilities;

import android.content.Context;
import android.view.KeyEvent;

import com.mattkula.wifigamepad.layouts.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stefan on 7/26/2014.
 */
public class FileUtil {

    public static final String dirName = "controllers";

    public static void saveController(Context c, Controller controller) {
        try {
            controller.save(c);
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
        if (!new File(c.getDir(dirName, 0), "Sample Controller").exists() || overwrite) {
            Controller controller = new Controller("Sample Controller", 7, 5);
            controller.addButton(2, 0, KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_LEFT));
            controller.addButton(1, 1, KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_DOWN));
            controller.addButton(3, 1, KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_UP));
            controller.addButton(2, 2, KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_RIGHT));
            controller.addButton(2, 4, KeybridgeUtil.getServerKeycode('b'));
            controller.addButton(2, 6, KeybridgeUtil.getServerKeycode('a'));
            FileUtil.saveController(c, controller);
        }
    }

    public static Controller loadSampleController(Context c) {
        File f = new File(c.getDir(dirName, 0), "Sample Controller");
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
