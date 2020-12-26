package com.skynic.ketabkhoneh.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.Utils;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.fragments.UserProfileFragment;
import com.skynic.ketabkhoneh.model.User;

public class UserView extends FrameLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtName;
    private TextView txtCity;
    private ImageView imgUser;
    private CheckBox chkFollow;

    public UserView(Context context, ViewSize viewSize, User model) {
        super(context);

        if(viewSize == ViewSize.SMALL) {
            inflate(context, R.layout.inflate_list_seller_container, this);
        } else if(viewSize == ViewSize.LARGE) {
            inflate(context, R.layout.inflate_following_item, this);
            txtCity = findViewById(R.id.txtCity);
            txtCity.setText((String) Configuration.getCities().get(model.getCityId()));

            chkFollow = findViewById(R.id.chkFollow);

            if(model.getUserName().equals(Configuration.getUsername()))
                chkFollow.setVisibility(INVISIBLE);

            if (model.isFollowing() == 1) {
                chkFollow.setChecked(true);
                chkFollow.setText("دنبال شده");
            }
            chkFollow.setOnCheckedChangeListener((compoundButton, b) -> {

                String followMode = b ? "add-following" : "remove-following";

                String requests[] = {followMode, Configuration.getUsername(), model.getUserName()};
                ApiClient.executeCommand(requests, o -> {
                    if (o != null && ((int) o[0]) == 0) {

                    } else {
                        Toast.makeText(context, "خطایی رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        imgUser = findViewById(R.id.imgUser);

        txtName = findViewById(R.id.txtName);
        txtName.setText(model.getName());

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getThumbNailImageLink());
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
