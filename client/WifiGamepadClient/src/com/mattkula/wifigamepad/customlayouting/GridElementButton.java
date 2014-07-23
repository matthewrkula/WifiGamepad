package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mattkula.wifigamepad.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Stefan on 7/22/2014.
 */
public class GridElementButton extends Button{
    private Queue<ButtonType> currentButtonType = new LinkedList<ButtonType>();
    private int SIZE_OF_SQUARE;
    public GridElementButton(Context context){
        super(context);
        init();
    }
    public GridElementButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridElementButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init(){
        SIZE_OF_SQUARE = convertDpToPixel(70f);
        for(ButtonType buttonType : ButtonType.values()){
            currentButtonType.add(buttonType);
        }
        this.setWidth(SIZE_OF_SQUARE);
        this.setHeight(SIZE_OF_SQUARE);
        this.setBackgroundResource(R.drawable.grid_element);
        this.setTextColor(ColorStateList.valueOf(Color.WHITE));
        //this.setText(currentButtonType.peek().toString());
    }
    private void cycleOneObject(){
        ButtonType buttonType = currentButtonType.peek();
        currentButtonType.remove();
        currentButtonType.add(buttonType);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            cycleOneObject();

            this.setText(currentButtonType.peek().toString().toLowerCase());
        }
        return true;
    }

    private int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

}
