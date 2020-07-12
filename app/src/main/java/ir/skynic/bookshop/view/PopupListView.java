package ir.skynic.bookshop.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.skynic.bookshop.R;

public class PopupListView extends FrameLayout {

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

    public void show() {
        mAlertDialog.show();
    }

    public void hide() {
        mAlertDialog.hide();
    }
}
