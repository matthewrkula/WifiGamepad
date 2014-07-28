package com.mattkula.wifigamepad.customlayouting;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.SocketManager;

import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Stefan on 7/22/2014.
 */
public class GridElementButton extends Button {

    private Queue<ButtonType> buttonTypeQueue = new LinkedList<ButtonType>();
    private int SIZE_OF_SQUARE;
    private GestureDetector gestureDetector;
    private boolean isGamePad;

    public GridElementButton(Context context) {
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
        SIZE_OF_SQUARE = EditPadActivity.pxFromDp(56f);
        fillQueueWithDefaultData();
        setParamsOfButton();

        gestureDetector = new GestureDetector(getContext() ,new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                if(!isGamePad) {
                    resetQueue();
                    vibrate(25);
                }
            }
        });
    }

    public String getCurrentName(){
        if(this.getText().length() == 0) {
            return "none";
        }
        return this.getText().toString();
    }

    public ButtonType getCurrentButtonType(){
        return buttonTypeQueue.peek();
    }

    public void setCurrentItem(String itemName){
        this.setText(itemName);
        while(!itemName.equals(buttonTypeQueue.peek().toString().toLowerCase())){
            cycleOneObject();
        }
    }
    private void fillQueueWithDefaultData(){
        //Clearing the queue to make sure that nothing else is in the queue before adding more button types.
        buttonTypeQueue.clear();
        for(ButtonType buttonType : ButtonType.values()){
            buttonTypeQueue.add(buttonType);
        }
    }

    private void setParamsOfButton(){
        this.setWidth(SIZE_OF_SQUARE);
        this.setHeight(SIZE_OF_SQUARE);
        this.setBackgroundResource(R.drawable.grid_element);
        this.setTextColor(ColorStateList.valueOf(Color.WHITE));
    }

    private void cycleOneObject(){
        ButtonType buttonType = getButtonTypeQueue();
        buttonTypeQueue.remove();
        buttonTypeQueue.add(buttonType);
    }

    private void resetQueue(){
        fillQueueWithDefaultData();
        this.setText("");
    }

    private void vibrate(int miliseconds){
        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(miliseconds);
    }

    public void setIsGamepad(boolean isGamepad){
        this.isGamePad = isGamepad;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!isGamePad) {
                cycleOneObject();
                vibrate(25);
                this.setText(getButtonTypeQueue().toString().toLowerCase());
            } else {
                if(this.getText().length() != 0){
                    vibrate(25);
                    SocketManager.sendData(buttonTypeQueue.peek().getValue(), true);
                }
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            SocketManager.sendData(buttonTypeQueue.peek().getValue(),false);
        }
        return true;
    }

    private ButtonType getButtonTypeQueue() {
        return buttonTypeQueue.peek();
    }
}
