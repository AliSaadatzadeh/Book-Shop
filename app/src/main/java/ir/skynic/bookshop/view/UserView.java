package ir.skynic.bookshop.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.fragments.UserProfileFragment;
import ir.skynic.bookshop.model.User;

public class UserView extends FrameLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtName;
    private TextView txtCity;
    private ImageView imgUser;

    public UserView(Context context, ViewSize viewSize, User model) {
        super(context);

        if(viewSize == ViewSize.SMALL) {
            inflate(context, R.layout.inflate_list_seller_container, this);
        } else if(viewSize == ViewSize.LARGE) {
            inflate(context, R.layout.inflate_following_item, this);
            txtCity = findViewById(R.id.txtCity);
            txtCity.setText((String) Configuration.getCities().get(model.getCityId()));
        }

        imgUser = findViewById(R.id.imgUser);

        txtName = findViewById(R.id.txtName);
        txtName.setText(model.getName());

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(viewSize == ViewSize.SMALL ? model.getThumbNailImageLink() : model.getImageLink());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();

        setOnClickListener(view -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            userProfileFragment.setModel(model);
            MainActivity.showFragment((Activity)context, userProfileFragment);
        });
    }


}
