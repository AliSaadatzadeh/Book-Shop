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

    public enum ViewSize {SMALL, LARGE, CART, INVOICE}

    private TextView txtTile;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtCategory;
    private TextView txtStatus;
    private TextView txtOff;
    private ImageView imgProduct;
    private TextView txtCity;
    private ImageView btnDelete;


    public BookView(Context context, ViewSize viewSize, Book model) {
        super(context);

        if(viewSize == ViewSize.SMALL) {
            inflate(context, R.layout.inflate_list_product_container, this);
            txtOff = findViewById(R.id.txtOff);

            if(model.getOff() > 0) {
                txtOff.setText(model.getOff() + "%");
                txtOff.setVisibility(VISIBLE);
            }
        } else if(viewSize == ViewSize.LARGE) {
            inflate(context, R.layout.inflate_list_product_item, this);
            txtDescription = findViewById(R.id.txtDescription);
            txtCategory = findViewById(R.id.txtCategory);
            txtStatus = findViewById(R.id.txtStatus);
            txtOff = findViewById(R.id.txtOff);

            if(model.getOff() > 0) {
                txtOff.setText(model.getOff() + "%");
                txtOff.setVisibility(VISIBLE);
            }

            txtDescription.setText(model.getDescription());
            String category = (String) Configuration.getCategories().get(model.getCategoryId());
            txtCategory.setText("در دسته" + "\"" + String.valueOf(category) + "\"");

            if(model.getBookStatus() != 1)
                txtStatus.setVisibility(INVISIBLE);
        } else if (viewSize == viewSize.CART ) {
            inflate(context, R.layout.inflate_list_cart_item, this);

            btnDelete = findViewById(R.id.btnDelete);
            txtCity = findViewById(R.id.txtCity);

            txtCity.setText("ارسال از \"" + Configuration.getCities().get(model.getCityId()) + "\"");
        } else if (viewSize == viewSize.INVOICE) {
            inflate(context, R.layout.inflate_invoice_item, this);

            txtCity = findViewById(R.id.txtCity);

            txtCity.setText("ارسال از \"" + Configuration.getCities().get(model.getCityId()) + "\"");
        }

        txtTile = findViewById(R.id.txtTitle);
        txtPrice = findViewById(R.id.txtPrice);
        imgProduct = findViewById(R.id.imgProduct);

        txtTile.setText(model.getTitle());
        txtPrice.setText(new DecimalFormat("#,###").format(model.getPrice()));

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

    public void setMenuButtonClickListerner(View.OnClickListener onClickListener) {
        View btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setVisibility(VISIBLE);
        btnMenu.setOnClickListener(onClickListener);

        txtStatus.setVisibility(GONE);
    }

    public void setBtnDeleteListener(OnClickListener clickListener) {
        if (btnDelete != null) {
            btnDelete.setOnClickListener(clickListener);
        }
    }


}
