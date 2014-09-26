package com.mattkula.wifigamepad.utilities;

import android.view.KeyEvent;

/**
 * Created by matt on 7/27/14.
 */
public class KeybridgeUtil {

    public static int getServerKeycode(int i) {
        if (i > 0x20 && i < 0x61) {
            return i;
        }

        if (i > 96 && i < 123) {
            return i - 32;
        }

        switch(i) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return 0x25;
            case KeyEvent.KEYCODE_DPAD_UP:
                return 0x26;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return 0x27;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return 0x28;
            case 0x20: //space
                return 0x20;
        }

        throw new AssertionError("No keycode for key: " + i);
    }

    public static String getCharForKeycode(int i) {
        switch(i) {
            case 0x20:
                return "Space";
            case 0x25:
                return "Left";
            case 0x26:
                return "Up";
            case 0x27:
                return "Right";
            case 0x28:
                return "Down";
        }

        return ((char)i) + "";
    }
}
