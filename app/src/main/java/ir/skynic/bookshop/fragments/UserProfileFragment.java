package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.UserView;

import static android.view.View.VISIBLE;

public class UserProfileFragment extends Fragment {

    private View mView;
    private User model;

    private TextView txtUsername;
    private TextView txtName;
    private TextView txtFollowing;
    private TextView txtFollower;
    private TextView txtBooksCount;
    private ImageView imgUser;
    private ProgressBar progressBar;

    private FollowingFragment followingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_profile, null);
        followingFragment = new FollowingFragment();
        followingFragment.setUsername(model.getUserName());

        initUi();
        getUser();

        return mView;
    }

    void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.lnrFollower).setOnClickListener(view -> {
            MainActivity.showFragment(getActivity(), followingFragment);
        });

        mView.findViewById(R.id.lnrFollowing).setOnClickListener(view -> {
            MainActivity.showFragment(getActivity(), followingFragment);
        });

        txtUsername = mView.findViewById(R.id.txtUsername);
        txtName = mView.findViewById(R.id.txtName);
        txtFollowing = mView.findViewById(R.id.txtFollowing);
        txtFollower = mView.findViewById(R.id.txtFollower);
        txtBooksCount = mView.findViewById(R.id.txtBooksCount);
        imgUser = mView.findViewById(R.id.imgUser);
        progressBar = mView.findViewById(R.id.progressBar);

        txtUsername.setText("@" + model.getUserName());
        txtName.setText(model.getName());
    }

    void getUser() {
        progressBar.setVisibility(View.VISIBLE);

        String userRequest[] = {"get-user", Configuration.getUsername(getActivity()), model.getUserName()};
        ApiClient.getModel(userRequest, "user", User.class, o -> {
            if(o != null) {
                model = (User)((List) o[1]).get(0);
                showInformation();

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        });
    }

    private void showInformation() {

        txtFollowing.setText(String.valueOf(model.getFollowingCount()));
        txtFollower.setText(String.valueOf(model.getFollowerCount()));
        txtBooksCount.setText(String.valueOf(model.getBookCount()));

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getImageLink());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();
    }

    public void setModel(User model) {
        this.model = model;
    }
}
