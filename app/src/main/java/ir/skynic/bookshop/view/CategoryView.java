package ir.skynic.bookshop.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.skynic.bookshop.R;

public class CategoryView extends LinearLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtName;

    public CategoryView(Context context, ViewSize viewSize, String name) {
        super(context);

        if(viewSize == ViewSize.SMALL)
            inflate(context, R.layout.inflate_list_category_container, this);
        else if(viewSize == ViewSize.LARGE)
            inflate(context, R.layout.inflate_category_list_item, this);

        txtName = findViewById(R.id.txtName);

        txtName.setText(name);
    }
}
