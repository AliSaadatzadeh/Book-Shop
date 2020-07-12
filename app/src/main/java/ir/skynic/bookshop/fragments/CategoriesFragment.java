package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;
import java.util.zip.Inflater;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.view.CategoryView;

public class CategoriesFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_categories, null);

        initUi();

        return mView;
    }

    private void initUi() {

        ViewGroup lnrContainer = mView.findViewById(R.id.lnrContainer);
        lnrContainer.removeAllViews();


        Map categories = Configuration.getCategories();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String value = (String) categories.get(key);

            CategoryView categoryView = new CategoryView(getActivity(), CategoryView.ViewSize.LARGE, value);
            categoryView.setOnClickListener(view -> {

                SearchFragment searchFragment = new SearchFragment();
                searchFragment.clearFilters();
                searchFragment.setCategory(key);

                MainActivity.showFragment(CategoriesFragment.this.getActivity(), searchFragment);
            });

            lnrContainer.addView(categoryView);
        }
    }
}
