package com.skynic.ketabkhoneh.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.Utils;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.Book;
import com.skynic.ketabkhoneh.model.Comment;
import com.skynic.ketabkhoneh.model.User;
import com.skynic.ketabkhoneh.view.CommentView;

import static android.view.View.VISIBLE;
import static android.view.View.combineMeasuredStates;

public class ProductFragment extends Fragment {

    private View mView;
    private Book model;

    private TextView txtTile;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtCategory;
    private TextView txtStatus;
    private TextView txtOff;
    private ImageView imgProduct;
    private ImageView imgUser;
    private TextView txtCity;
    private TextView txtUser;
    private TextView txtAuthor;
    private TextView txtTranslator;
    private TextView txtPublisher;
    private TextView txtPublicationYear;
    private TextView txtDate;
    private ViewGroup addRemoveCart;
    private TextView txtAddRemoveCart;

    private ViewGroup commentContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_product, null);

        initUi();

        return mView;
    }

    public void setModel(Book model) {
        this.model = model;
    }

    private void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.btnComments).setOnClickListener(view -> {
            CommentsFragment commentsFragment = new CommentsFragment();
            commentsFragment.setBookId(model.getId());
            MainActivity.showFragment(getActivity(), commentsFragment);
        });

        txtTile = mView.findViewById(R.id.txtTitle);
        txtDescription = mView.findViewById(R.id.txtDescription);
        txtPrice = mView.findViewById(R.id.txtPrice);
        txtCategory = mView.findViewById(R.id.txtCategory);
        txtStatus = mView.findViewById(R.id.txtStatus);
        txtOff = mView.findViewById(R.id.txtOff);
        imgProduct = mView.findViewById(R.id.imgProduct);
        imgUser = mView.findViewById(R.id.imgUser);
        txtCity = mView.findViewById(R.id.txtCity);
        txtUser = mView.findViewById(R.id.txtUser);
        txtAuthor = mView.findViewById(R.id.txtAuthor);
        txtTranslator = mView.findViewById(R.id.txtTranslator);
        txtPublisher = mView.findViewById(R.id.txtPublisher);
        txtPublicationYear = mView.findViewById(R.id.txtPublicationYear);
        txtDate = mView.findViewById(R.id.txtDate);
        txtAddRemoveCart = mView.findViewById(R.id.txtAddRemoveCart);
        addRemoveCart = mView.findViewById(R.id.btnAddRemoveCart);

        commentContainer = mView.findViewById(R.id.lnrCommentContainer);

        if (Configuration.isCartExist(model)) {
            addRemoveCart.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.finalize_cart_background));
            txtAddRemoveCart.setText("از سبد خرید حذف کن");
        } else {
            addRemoveCart.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.add_to_cart_background));
            txtAddRemoveCart.setText("به سبد خرید اضافه کن");
        }

        addRemoveCart.setOnClickListener(view -> {
            if (Configuration.isCartExist(model)) {
                Configuration.removeFromCart(model);
                addRemoveCart.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.add_to_cart_background));
                txtAddRemoveCart.setText("به سبد خرید اضافه کن");
            } else {
                Configuration.addToCart(model);
                addRemoveCart.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.finalize_cart_background));
                txtAddRemoveCart.setText("از سبد خرید حذف کن");
            }
        });

        if(!model.isCanBuy())
            addRemoveCart.setVisibility(View.INVISIBLE);

        showInformation();
        getComments();
    }

    private void getComments() {
        String request[] = {"get-comment", Configuration.getUsername(getActivity()), String.valueOf(model.getId()), "6"};
        ApiClient.getModel(request, "comment", Comment.class, o -> {
            if(o != null) {
                commentContainer.removeAllViews();

                List<Comment> commentList = (List) o[1];
                for (Comment comment : commentList) {
                    CommentView commentView = new CommentView(getActivity(), comment);
                    commentContainer.addView(commentView);
                }
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInformation() {
        txtTile.setText(model.getTitle());
        txtDescription.setText(model.getDescription());

        txtAuthor.setText(model.getAuthor().length() > 0 ? model.getAuthor() : "—");
        txtTranslator.setText(model.getTranslator().length() > 0 ?  model.getTranslator() : "—");
        txtPublisher.setText(model.getPublisher().length() > 0 ?  model.getPublisher() : "—");
        txtPublicationYear.setText(model.getPublicationYear() > 0 ? String.valueOf(model.getPublicationYear()) : "—");
        txtDate.setText(model.getDate());

        txtPrice.setText(new DecimalFormat("#,###").format(model.getPrice()));

        txtStatus.setText((String) Configuration.getBookStatuses().get(model.getBookStatus()));

        String category = ((String[]) Configuration.getCategories().get(model.getCategoryId()))[0];
        txtCategory.setText("در دسته " + "\"" + category + "\"");

        String city = (String) Configuration.getCities().get(model.getCityId());
        txtCity.setText("ارسال از \"" + city + "\" - " + (model.getTransferring() == 0 ? "پسکرایه" : "پیشکرایه"));

        txtUser.setText(model.getSeller());
        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getSellerImage());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ingnore) {}
        }).start();

        imgUser.setOnClickListener(view -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            userProfileFragment.setModel(new User(model.getSeller(), model.getSellerImage(), model.getSellerName()));
            MainActivity.showFragment(getActivity(), userProfileFragment);
        });

        if(model.getOff() > 0) {
            txtOff.setText(model.getOff() + "% تخفیف");
            txtOff.setVisibility(VISIBLE);
        }

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getImageLink());
                Utils.runOnMainThread(() -> imgProduct.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();
    }
}
