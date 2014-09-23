package com.mattkula.wifigamepad.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mattkula.wifigamepad.R;
import com.mattkula.wifigamepad.layouts.Controller;
import com.mattkula.wifigamepad.layouts.ControllerButton;
import com.mattkula.wifigamepad.utilities.FileUtil;
import com.mattkula.wifigamepad.utilities.KeybridgeUtil;

import java.util.HashMap;

public class EditPadActivity extends Activity {

    public static final String EXTRA_IP = "ipaddress";
    public static final String EXTRA_PORT = "port";
    public static final String EXTRA_NAME = "name";

    private Controller controller;

    private GridLayout gridLayout;
    private ImageButton saveButton;
    private Button btnMoreCols, btnLessCols, btnMoreRows, btnLessRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_pad);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString(EXTRA_NAME);

        controller = new Controller(name, Controller.DEFAULT_ROWS, Controller.DEFAULT_COLS);

        gridLayout = (GridLayout)findViewById(R.id.rootGridLayout);
        gridLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {  // Need this so grid layout can be measured.
            @Override
            public boolean onPreDraw() {
                gridLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                populateGridview();
                return true;
            }
        });
        saveButton = (ImageButton)findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.saveController(getApplicationContext(), controller);
                finish();
            }
        });

        btnMoreCols = (Button)findViewById(R.id.btn_more_cols);
        btnMoreCols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setColumnCount(controller.getColumnCount() + 1);
                populateGridview();
            }
        });
        btnLessCols = (Button)findViewById(R.id.btn_less_cols);
        btnLessCols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller.getColumnCount() > 1) {
                    controller.setColumnCount(controller.getColumnCount() - 1);
                    populateGridview();
                }
            }
        });
        btnMoreRows = (Button)findViewById(R.id.btn_more_rows);
        btnMoreRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setRowCount(controller.getRowCount() + 1);
                populateGridview();
            }
        });
        btnLessRows = (Button)findViewById(R.id.btn_less_rows);
        btnLessRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller.getRowCount() > 1) {
                    controller.setRowCount(controller.getRowCount() - 1);
                    populateGridview();
                }
            }
        });
    }

    public void populateGridview() {
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(controller.getColumnCount());
        gridLayout.setRowCount(controller.getRowCount());

        for (int row = 0; row < controller.getRowCount(); row++) {
            for (int col = 0; col < controller.getColumnCount(); col++) {
                ControllerButton button = controller.buttonAt(row, col);
                TextView v = new TextView(this);
                v.setTextSize(20);
                v.setLayoutParams(
                        new ViewGroup.LayoutParams(gridLayout.getWidth() / controller.getColumnCount(),
                                gridLayout.getHeight() / controller.getRowCount()));
                v.setGravity(Gravity.CENTER);
                v.setOnClickListener(new ButtonClickListener(row, col));
                v.setBackgroundResource(R.drawable.bordered_background);

                if (button != null) {
                    v.setText(KeybridgeUtil.getCharForKeycode(button.getKeyCode()));
                }
                gridLayout.addView(v);
            }
        }
    }

    private void showPickerDialog(final int row, final int column) {
        String[] choices = {"Letter Or Number", "Arrow or Other Key"};
        AlertDialog dialog = new AlertDialog.Builder(this).setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    showCharacterDialog(row, column);
                } else if (i == 1) {
                    showSpecialKeyDialog(row, column);
                }
                dialogInterface.dismiss();
            }
        }).create();
        dialog.show();
    }

    private void showCharacterDialog(final int row, final int column) {
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        new AlertDialog.Builder(this)
                .setTitle("Enter Button")
                .setMessage("Enter the key that this button should 'press'.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputText = input.getText().toString();
                        if (inputText.length() == 1) {
                            int charcode = KeybridgeUtil.getServerKeycode(inputText.charAt(0));
                            controller.addButton(row, column, charcode);
                            populateGridview();
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void showSpecialKeyDialog(final int row, final int column) {
        final String[] choices = {"Right Arrow", "Left Arrow", "Up Arrow", "Down Arrow"};

        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("Right Arrow", KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_RIGHT));
        hashMap.put("Left Arrow", KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_LEFT));
        hashMap.put("Up Arrow", KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_UP));
        hashMap.put("Down Arrow", KeybridgeUtil.getServerKeycode(KeyEvent.KEYCODE_DPAD_DOWN));

        AlertDialog dialog = new AlertDialog.Builder(this).setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Integer charCode = hashMap.get(choices[i]);
                controller.addButton(row, column, charCode);
                populateGridview();
                dialogInterface.dismiss();
            }
        }).create();
        dialog.show();
    }

    private class ButtonClickListener implements View.OnClickListener {
        int column;
        int row;

        public ButtonClickListener(int row, int col) {
            this.column = col;
            this.row = row;
        }

        @Override
        public void onClick(View view) {
            showPickerDialog(row, column);
        }
    }

    public static Intent generateIntent(Context c, String ipAddress, String port, String name){
        Intent i = new Intent(c, EditPadActivity.class);
        i.putExtra(EXTRA_IP, ipAddress);
        i.putExtra(EXTRA_PORT, Integer.parseInt(port));
        i.putExtra(EXTRA_NAME, name);
        return i;
    }
}
