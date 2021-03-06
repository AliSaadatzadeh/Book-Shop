package com.skynic.ketabkhoneh.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;
import java.util.zip.Inflater;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.model.Category;
import com.skynic.ketabkhoneh.view.CategoryView;

public class CategoriesFragment extends Fragment {

    private View mView;
    private int defaultCategoryId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_categories, null);
        defaultCategoryId = 0;
        initUi();

        return mView;
    }



    private void initUi() {

        ViewGroup lnrContainer = mView.findViewById(R.id.lnrContainer);
        lnrContainer.removeAllViews();

        TextView txtCurrentCategory = mView.findViewById(R.id.txtCurrentCategory);


        Map categories = Configuration.getCategories();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String[] value = (String[]) categories.get(key);
            if(value[1].equals(String.valueOf(defaultCategoryId))) {
                CategoryView categoryView = new CategoryView(getActivity(), CategoryView.ViewSize.LARGE, value[0], key);
                if(!Configuration.hasCategoryChild(key)) {
                    categoryView.setOnClickListener(view -> {
                        txtCurrentCategory.setText("در دسته بندی \"" + ((String[]) categories.get(key))[0] + "\"");
                        defaultCategoryId = key;
                        initUi();
                    });
                }
                lnrContainer.addView(categoryView);
            }
        }

        View btnBack = mView.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> {
            defaultCategoryId = 0;
            txtCurrentCategory.setText("در دسته بندی \"" + "خانه" + "\"");
            initUi();
        });


        btnBack.setVisibility(defaultCategoryId == 0 ? View.INVISIBLE : View.VISIBLE);

    }
}
