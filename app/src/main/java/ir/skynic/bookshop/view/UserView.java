package ir.skynic.bookshop.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ir.skynic.bookshop.R;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.fragments.UserProfileFragment;
import ir.skynic.bookshop.model.User;

public class UserView extends FrameLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtName;
    private ImageView imgUser;


    public UserView(Context context, ViewSize viewSize, User model) {
        super(context);

        if(viewSize == ViewSize.SMALL)
            inflate(context, R.layout.inflate_list_seller_container, this);
        else if(viewSize == ViewSize.LARGE)
            inflate(context, R.layout.inflate_list_product_item, this);

        txtName = findViewById(R.id.txtName);
        imgUser = findViewById(R.id.imgUser);

        txtName.setText(model.getName());

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getImageLink());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();

        setOnClickListener(view -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            //productFragment.setModel(model);
            MainActivity.showFragment((Activity)context, userProfileFragment);
        });
    }


}
