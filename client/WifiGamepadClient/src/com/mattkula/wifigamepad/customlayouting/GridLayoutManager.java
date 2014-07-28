package com.mattkula.wifigamepad.customlayouting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 7/25/2014.
 */
public class GridLayoutManager {

    public static void loadLayoutGamepad(GridLayout gridLayout,String[] elements,Activity c){
        loadLayout(gridLayout,elements,c);
        for(int i = 0; i < gridLayout.getChildCount();i++){
            GridElementButton geb = (GridElementButton)gridLayout.getChildAt(i);
            geb.setHeight(EditPadActivity.pxFromDp(70f));
            geb.setWidth(EditPadActivity.pxFromDp(70f));
            geb.setIsGamepad(true);
            geb.setBackgroundResource(0);

            if(!geb.getCurrentName().equals("none")){
                geb.setBackgroundColor(geb.getCurrentButtonType().getColor(c));
            }
        }
    }

    public static Grid loadLayout(GridLayout gridLayout, String[] elements, Context c){
        gridLayout.removeAllViews();

        int size[] = Grid.autoAdjustGridLayout(c,gridLayout);
        Grid grid = new Grid(gridLayout,size[0],size[1]);
        grid.fillGrid(c);
        List<GridElementButton> gridElements = grid.getElements();

        for (int i = 0; i < elements.length; i++) {
            if(elements[i].equals("none")){
                continue;
            }
            gridElements.get(i).setText(elements[i]);
            gridElements.get(i).setCurrentItem(elements[i]);
        }
        return grid;
    }
}
