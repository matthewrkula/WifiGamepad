package com.mattkula.wifigamepad.customlayouting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.GridLayout;

import com.mattkula.wifigamepad.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 7/24/2014.
 */

//Speed isn't really of any concern so Serializable will do.
public class Grid {
    private GridLayout gridLayout;
    private int rows,cols;
    private List<GridElementButton> elements = new ArrayList<GridElementButton>();
    public Grid(GridLayout gridLayout, int rows, int cols) {
        this.gridLayout = gridLayout;
        this.rows = rows;
        this.cols = cols;
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }

    public GridLayout getGamePadGridLayout(){
        for(GridElementButton g : elements){
            g.setBackgroundColor(Color.BLUE);
        }
        return gridLayout;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void fillGrid(Context c){
        if(elements.size() == 0)
            for (int i = 0; i < rows*cols; i++) {
                GridElementButton geb = new GridElementButton(c);
                gridLayout.addView(geb);
                elements.add(geb);
            }
    }

    public String[] getElementNames(){
        String[] temp = new String[elements.size()];
        for(int i = 0; i < elements.size();i++){
            temp[i] = elements.get(i).getCurrentName().toString().toLowerCase();
        }
        return temp;
    }

    public List<GridElementButton> getElements() {
        return elements;
    }

    //Returns size of grid
    public static int[] autoAdjustGridLayout(Context c,GridLayout g){

        final int INTENDED_SIZE_OF_BUTTON = 70;
        int width,height;
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
         width = metrics.widthPixels;
         height = metrics.heightPixels;

        width = EditPadActivity.dpFromPx(width);
        height = EditPadActivity.dpFromPx(height);

        int amountOfRows = height/INTENDED_SIZE_OF_BUTTON;
        int amountOfCols = width/INTENDED_SIZE_OF_BUTTON;

        GridLayout gridLayout = g;

        gridLayout.setColumnCount(amountOfCols);
        gridLayout.setRowCount(amountOfRows);
        return new int[]{amountOfCols,amountOfRows};
    }
}
