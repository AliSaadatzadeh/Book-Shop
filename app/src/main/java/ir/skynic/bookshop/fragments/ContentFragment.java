package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.skynic.bookshop.R;

public class ContentFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_content, null);

        initUi();

        return mView;
    }

    private void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });
    }
}
