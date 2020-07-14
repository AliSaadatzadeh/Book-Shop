package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.UserView;

public class FollowingFragment extends Fragment {

    private View mView;
    private String username;
    private boolean isFollowingMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_following, null);

        initUi();
        getInformation(isFollowingMode);


        return mView;
    }

    private void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        TabLayout tabLayout = mView.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(isFollowingMode ? 1 : 0);
        tab.select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getInformation(false);
                } else if (tab.getPosition() == 1) {
                    getInformation(true);
                } else {
                    getInformation(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getInformation(boolean usersIsFollowing) {

        ViewGroup userContainer = mView.findViewById(R.id.lnrUserContainer);
        userContainer.removeAllViews();

        String requestMode = usersIsFollowing ? "get-following" : "get-follower";

        String userRequest[] = {requestMode, Configuration.getUsername(getActivity()), username};
        ApiClient.getModel(userRequest, "user", User.class, o -> {
            if(o != null) {

                List<User> userList = (List) o[1];

                for (User user : userList) {
                    UserView userView = new UserView(getActivity(), UserView.ViewSize.LARGE, user);
                    userContainer.addView(userView);
                }

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFollowingMode(boolean followingMode) {
        isFollowingMode = followingMode;
    }
}
