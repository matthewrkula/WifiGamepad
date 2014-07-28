package com.mattkula.wifigamepad.utilities;

import android.view.KeyEvent;

/**
 * Created by matt on 7/27/14.
 */
public class Keybridge {

    public static int getServerKeycode(int i) {
        if (i > 64 && i < 91) {
            return i;
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
        }

        throw new AssertionError("No keycode for key: " + i);
    }
}
