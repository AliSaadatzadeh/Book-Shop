package com.skynic.ketabkhoneh.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.skynic.ketabkhoneh.R;

public class PopupListView extends FrameLayout {

    public interface OnInputCompleted {
        void inputCompleted(String text);
    }

    private Context mContext;
    private AlertDialog mAlertDialog;

    public PopupListView(Context context, String title) {
        super(context);
        mContext = context;

        inflate(context, R.layout.dialog_select_city, this);
        ((TextView)findViewById(R.id.txtTitle)).setText(title);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(this);

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);
    }

    public void addItem(String itemName, Runnable onClickListener) {
        ViewGroup lnrList = findViewById(R.id.lnrList);
        View itemView = inflate(mContext, R.layout.inflate_selection_dialog_item, null);
        ((TextView)itemView.findViewById(R.id.txtItem)).setText(itemName);
        itemView.findViewById(R.id.txtItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
                onClickListener.run();
            }
        });
        lnrList.addView(itemView);
    }

    public void addInputText(String placeholder, OnInputCompleted onInputCompleted) {
        ViewGroup lnrList = findViewById(R.id.lnrList);
        View itemView = inflate(mContext, R.layout.inflate_input_dialog_item, null);
        EditText inputText = itemView.findViewById(R.id.edtInput);
        inputText.setHint(placeholder);
        itemView.findViewById(R.id.btnDone).setOnClickListener(view -> {
            hide();
            onInputCompleted.inputCompleted(inputText.getText().toString());
        });
        lnrList.addView(itemView);
    }

    public void addInputText(String text) {
        ViewGroup lnrList = findViewById(R.id.lnrList);
        View itemView = inflate(mContext, R.layout.inflate_input_dialog_item, null);
        EditText inputText = itemView.findViewById(R.id.edtInput);
        inputText.setText(text);
        inputText.setEnabled(false);
        itemView.findViewById(R.id.btnDone).setOnClickListener(view -> {
            hide();
        });
        lnrList.addView(itemView);
    }

    public void setCustomView(View view) {
        ViewGroup lnrList = findViewById(R.id.lnrList);
        lnrList.addView(view);
    }

    public void show() {
        mAlertDialog.show();
    }

    public void hide() {
        mAlertDialog.hide();
    }
}
