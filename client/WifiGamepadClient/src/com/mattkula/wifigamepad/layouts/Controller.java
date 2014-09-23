package com.mattkula.wifigamepad.layouts;

import android.content.Context;

import com.mattkula.wifigamepad.utilities.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by matt on 8/15/14.
 */
public class Controller implements Serializable {

    public static final int DEFAULT_ROWS = 7;
    public static final int DEFAULT_COLS = 5;

    String name;
    int rowCount;
    int columnCount;
    ArrayList<ControllerButton> buttons;

    public Controller(String name, int rowCount, int columnCount) {
        this.name = name;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.buttons = new ArrayList<ControllerButton>();
    }

    public int addButton(int row, int column, int keyCode) {
        removeButton(buttonAt(row, column));
        buttons.add(new ControllerButton(row, column, keyCode));
        return keyCode;
    }

    public ControllerButton buttonAt(int row, int column) {
        for (ControllerButton button : buttons) {
            if (button.equals(new ControllerButton(row, column, -1))) {
                return button;
            }
        }
        return null;
    }

    public void removeButton(ControllerButton button) {
        if (buttons.contains(button)) {
            buttons.remove(button);
        }
    }

    public void clearButtons() {
        buttons.clear();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save(Context c) throws IOException {
        File file = new File(c.getDir(FileUtil.dirName, 0), getName());
        FileWriter writer = new FileWriter(file, false);

        writer.write(Integer.toString(getRowCount()) + " ");
        writer.write(Integer.toString(getColumnCount()) + " ");

        for (ControllerButton button : buttons) {
            writer.write(button.getRow() + " ");
            writer.write(button.getColumn() + " ");
            writer.write(button.getKeyCode() + " ");
        }

        writer.flush();
        writer.close();
    }

    public static Controller loadFromFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        String name = file.getName();
        int boardRows = scanner.nextInt();
        int boardColumns = scanner.nextInt();
        Controller board = new Controller(name, boardRows, boardColumns);

        int row, column, keycode;
        while (scanner.hasNextInt()) {
            row = scanner.nextInt();
            column = scanner.nextInt();
            keycode = scanner.nextInt();
            board.addButton(row, column, keycode);
        }
        return board;
    }
}
