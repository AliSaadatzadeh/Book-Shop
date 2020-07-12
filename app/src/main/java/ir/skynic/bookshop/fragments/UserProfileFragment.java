package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;

public class UserProfileFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_profile, null);

        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.lnrFollower).setOnClickListener(view -> {
            MainActivity.showFragment(getActivity(), new FollowingFragment());
        });

        mView.findViewById(R.id.lnrFollowing).setOnClickListener(view -> {
            MainActivity.showFragment(getActivity(), new FollowingFragment());
        });

        return mView;
    }

}
