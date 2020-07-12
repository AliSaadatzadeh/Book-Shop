package ir.skynic.bookshop.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.skynic.bookshop.R;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.fragments.CategoriesFragment;
import ir.skynic.bookshop.fragments.SearchFragment;
import ir.skynic.bookshop.model.Category;

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
