package ir.skynic.bookshop.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DecimalFormat;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.fragments.ProductFragment;
import ir.skynic.bookshop.model.Book;

public class BookView extends FrameLayout {

    public enum ViewSize {SMALL, LARGE}

    private TextView txtTile;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtCategory;
    private TextView txtStatus;
    private TextView txtOff;
    private ImageView imgProduct;


    public BookView(Context context, ViewSize viewSize, Book model) {
        super(context);

        if(viewSize == ViewSize.SMALL)
            inflate(context, R.layout.inflate_list_product_container, this);
        else if(viewSize == ViewSize.LARGE)
            inflate(context, R.layout.inflate_list_product_item, this);

        txtTile = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtCategory = findViewById(R.id.txtCategory);
        txtStatus = findViewById(R.id.txtStatus);
        txtOff = findViewById(R.id.txtOff);
        imgProduct = findViewById(R.id.imgProduct);

        txtTile.setText(model.getTitle());
        txtDescription.setText(model.getDescription());
        txtPrice.setText(new DecimalFormat("#,###").format(model.getPrice()));
        String category = (String) Configuration.getCategories().get(model.getCategoryId());
        txtCategory.setText("در دسته" + "\"" + String.valueOf(category) + "\"");

        if(model.getBookStatus() != 1)
            txtStatus.setVisibility(INVISIBLE);

        if(model.getOff() > 0) {
            txtOff.setText(model.getOff() + "%");
            txtOff.setVisibility(VISIBLE);
        }

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getThumbnailImageLink());
                Utils.runOnMainThread(() -> imgProduct.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();

        setOnClickListener(view -> {
            ProductFragment productFragment = new ProductFragment();
            productFragment.setModel(model);
            MainActivity.showFragment((Activity)context, productFragment);
        });
    }


}
