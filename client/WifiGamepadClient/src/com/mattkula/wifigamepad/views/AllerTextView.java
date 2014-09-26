package com.mattkula.wifigamepad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by matt on 9/26/14.
 */
public class AllerTextView extends TextView {

    private static Typeface regular;
    private static Typeface bold;
    private static Typeface light;

    public AllerTextView(Context context) {
        super(context);
        initFonts(context);
    }

    public AllerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFonts(context);
    }

    private void initFonts(Context context) {
        if (regular == null) {
            regular = Typeface.createFromAsset(context.getAssets(), "fonts/allerrg.ttf");
            bold = Typeface.createFromAsset(context.getAssets(), "fonts/allerbd.ttf");
            light = Typeface.createFromAsset(context.getAssets(), "fonts/allerlt.ttf");
        }
        setTypeface(regular);
    }

    public void setBold(boolean isBold) {
        if (isBold) {
            this.setTypeface(bold);
        } else {
            this.setTypeface(regular);
        }
    }

    public void setLight(boolean isLight) {
        if (isLight) {
            this.setTypeface(light);
        } else {
            this.setTypeface(regular);
        }
    }
}
