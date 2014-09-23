package com.mattkula.wifigamepad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PositionAwareView extends TextView {
    private int row = -1;
    private int column = -1;

    public PositionAwareView(Context context, int row, int column) {
        super(context);
        this.row = row;
        this.column = column;
    }

    public PositionAwareView(Context context) {
        super(context);
    }

    public PositionAwareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getRow() {
        if (row < 0) {
            throw new RuntimeException("Please set row on PositionAwareView");
        }
        return row;
    }

    public int getColumn() {
        if (column < 0) {
            throw new RuntimeException("Please set column on PositionAwareView");
        }
        return column;
    }
}
