package ir.skynic.bookshop.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;


public class HomeFragment extends Fragment {

    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, null);
        mView.findViewById(R.id.btnProfile).setOnClickListener(view -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            MainActivity.showFragment(getActivity(), userProfileFragment);
        });

        return mView;
    }
}
