package com.mattkula.wifigamepad.customlayouting;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Stefan on 7/22/2014.
 */
public enum ButtonType {
    //TODO: Add more button types.
    LEFT(1),RIGHT(2),UP(3),DOWN(4),

    //All these are characters which will be converted to their ascii integers.
    A(5),B(6),W(7),S(8),D(9),

    START(10),SELECT(11);

    //each button has a value
    private final int value;
    private ButtonType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getColor(Activity a) {
        switch (this){
            case LEFT:
                return a.getResources().getColor(android.R.color.holo_orange_light);
            case RIGHT:
                return a.getResources().getColor(android.R.color.holo_blue_bright);
            case UP:
                return a.getResources().getColor(android.R.color.holo_green_light);
            case DOWN:
                return a.getResources().getColor(android.R.color.holo_purple);
            case A:
                return a.getResources().getColor(android.R.color.holo_red_light);
            case B:
                return a.getResources().getColor(android.R.color.holo_green_dark);
            case W:
                return a.getResources().getColor(android.R.color.holo_orange_dark);
            case S:
                return a.getResources().getColor(android.R.color.holo_blue_dark);
            case D:
                return a.getResources().getColor(android.R.color.holo_red_dark);
            case SELECT:
                return a.getResources().getColor(android.R.color.holo_blue_dark);
            default:
                return a.getResources().getColor(android.R.color.holo_green_dark);
        }
    }
}
