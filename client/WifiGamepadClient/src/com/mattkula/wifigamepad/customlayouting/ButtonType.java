package com.mattkula.wifigamepad.customlayouting;

/**
 * Created by Stefan on 7/22/2014.
 */
public enum ButtonType {
    //TODO: Add more button types.
    LEFT(4),RIGHT(6),UP(8),DOWN(2),


    //All these are characters which will be converted to their ascii integers.
    A('A'),B('B'),W('W'),S('S'),D('D'),


    START(1),SELECT(0);

    private final int value;
    private ButtonType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
