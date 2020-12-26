package com.skynic.ketabkhoneh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.Utils;
import com.skynic.ketabkhoneh.model.Comment;

public class CommentView extends FrameLayout{

    private Comment model;

    private TextView userName;
    private TextView comment;
    private ImageView imgUser;

    public CommentView(Context context, Comment model) {
        super(context);
        inflate(context, R.layout.inflate_comment_item, this);
        this.model = model;

        initUi();
        setInformation();
    }

    private void setInformation() {
        userName.setText("@" + model.getUserName());
        comment.setText(model.getComment());
        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getUserImage());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ingnore) {}
        }).start();
    }

    private void initUi() {
        userName = findViewById(R.id.txtUserName);
        comment = findViewById(R.id.txtComment);
        imgUser = findViewById(R.id.imgUser);
    }

}
