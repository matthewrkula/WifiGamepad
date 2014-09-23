package com.mattkula.wifigamepad.utilities;

import android.content.Context;
import android.util.Log;

/**
 * Created by matt on 8/16/14.
 */
public class ColorUtil {

    static int[] colors = {
        android.R.color.holo_orange_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_purple,
        android.R.color.holo_red_light,
        android.R.color.holo_green_dark,
        android.R.color.holo_orange_dark,
        android.R.color.holo_blue_dark,
        android.R.color.holo_red_dark,
        android.R.color.holo_blue_dark,
        android.R.color.holo_green_dark,
    };

    public static int colorForKeycode(Context c, int keycode) {
        Log.v("WTFMATT", "" + c.getResources().getColor(colors[keycode % colors.length]));
        return c.getResources().getColor(colors[keycode % colors.length]);
    }
}
