package com.mattkula.wifigamepad.layouts;

import java.io.Serializable;

/**
 * Created by matt on 8/15/14.
 */
public class ControllerButton implements Serializable {

    int row, column, keyCode;

    public ControllerButton(int row, int column, int keyCode) {
        this.row = row;
        this.column = column;
        setPressedKey(keyCode);
    }

    public void setPressedKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public void reset() {
        setPressedKey(-1);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getKeyCode() {
        return keyCode;
    }

    @Override
    public boolean equals(Object o) {
        ControllerButton button = (ControllerButton)o;
        return button.getRow() == this.getRow() &&
                button.getColumn() == this.getColumn();
    }
}
