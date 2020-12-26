package com.skynic.ketabkhoneh.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.fragments.CategoriesFragment;
import com.skynic.ketabkhoneh.fragments.SearchFragment;
import com.skynic.ketabkhoneh.model.Category;

public class CategoryView extends LinearLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtName;

    public CategoryView(Context context, ViewSize viewSize, String categoryName, int categoryId) {
        super(context);

        if(viewSize == ViewSize.SMALL)
            inflate(context, R.layout.inflate_list_category_container, this);
        else if(viewSize == ViewSize.LARGE)
            inflate(context, R.layout.inflate_category_list_item, this);

        setOnClickListener(view -> {

            SearchFragment searchFragment = new SearchFragment();
            searchFragment.clearFilters();
            searchFragment.setCategory(categoryId);

            MainActivity.showFragment((Activity) context, searchFragment);
        });

        txtName = findViewById(R.id.txtName);

        txtName.setText(categoryName);
    }
}
