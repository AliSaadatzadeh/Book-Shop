package com.skynic.ketabkhoneh.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.Utils;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.activities.MenuActivity;
import com.skynic.ketabkhoneh.activities.RegisterActivity;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.Book;
import com.skynic.ketabkhoneh.model.User;
import com.skynic.ketabkhoneh.view.BookView;
import com.skynic.ketabkhoneh.view.CategoryView;
import com.skynic.ketabkhoneh.view.UserView;


public class HomeFragment extends Fragment {

    private View mView;

    private ViewGroup productContainer;
    private ViewGroup userContainer;
    private ViewGroup newProductContainer;
    private ViewGroup categoryContainer;

    private ImageView imageView;

    private ImageView btnProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);

        initUi();
        getInformation();

        return mView;
    }

    private void getInformation() {
        String viewRequest[] = {"get-view", Configuration.getUsername(getActivity())};
        ApiClient.getValue(viewRequest, "viewLink", new RunnableParam() {
            @Override
            public void run(Object... o) {
                if(o != null && (int) o[0] == 0) {
                    String view = (String) o[1];
                    new Thread(() -> {
                        try {
                            Bitmap bitmap = Utils.getImageFromUrl(view);
                            Utils.runOnMainThread(() -> imageView.setImageBitmap(bitmap));
                        } catch (Exception ingnore) {}
                    }).start();

                } else {
                    Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getTopSeller();
        getDisCounted();
        getNewProducts();
        getCategories();
    }

    private void initUi() {
        imageView = mView.findViewById(R.id.imageView);
        btnProfile = mView.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(view -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            userProfileFragment.setModel(new User(Configuration.getUsername(), "", ""));
            MainActivity.showFragment(getActivity(), userProfileFragment);
        });

        productContainer = mView.findViewById(R.id.lnrProductContainer);
        mView.findViewById(R.id.relProductContainer).setVisibility(View.GONE);

        userContainer = mView.findViewById(R.id.lnrUserContainer);
        mView.findViewById(R.id.relUserContainer).setVisibility(View.GONE);

        newProductContainer = mView.findViewById(R.id.lnrNewProductContainer);
        mView.findViewById(R.id.relNewProductContainer).setVisibility(View.GONE);

        categoryContainer = mView.findViewById(R.id.lnrCategoryContainer);
        mView.findViewById(R.id.relCategoryContainer).setVisibility(View.GONE);

        mView.findViewById(R.id.imgMenu).setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MenuActivity.class));
        });

        mView.findViewById(R.id.btnShowAllOff).setOnClickListener(view -> {
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.clearFilters();
            searchFragment.showAllOff();
            MainActivity.showFragment(getActivity(), searchFragment);
        });

        mView.findViewById(R.id.btnShowAllNew).setOnClickListener(view -> {
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.clearFilters();
            searchFragment.showAllNew();
            MainActivity.showFragment(getActivity(), searchFragment);
        });
    }

    private void getTopSeller() {
        String userRequest[] = {"get-top-seller", Configuration.getUsername(getActivity()), "30"};
        ApiClient.getModel(userRequest, "user", User.class, o -> {
            if(o != null) {
                userContainer.removeAllViews();

                List<User> userList = (List) o[1];
                for (User user : userList) {
                    UserView userView = new UserView(getActivity(), UserView.ViewSize.SMALL, user);
                    userContainer.addView(userView);
                }

                mView.findViewById(R.id.relUserContainer).setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDisCounted() {
        String disCountedRequest[] = {"get-dis-counted", Configuration.getUsername(getActivity()), "30"};
        ApiClient.getModel(disCountedRequest, "book", Book.class, o -> {
            if(o != null) {
                productContainer.removeAllViews();

                List<Book> bookList = (List) o[1];
                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.SMALL, book);
                    productContainer.addView(bookView);
                }

                mView.findViewById(R.id.relProductContainer).setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNewProducts() {
        String newProductRequest[] = {"get-book", Configuration.getUsername(getActivity())};
        ApiClient.getModel(newProductRequest, "book", Book.class, o -> {
            if(o != null) {
                newProductContainer.removeAllViews();

                List<Book> bookList = (List) o[1];
                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.SMALL, book);
                    newProductContainer.addView(bookView);
                }

                mView.findViewById(R.id.relNewProductContainer).setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCategories() {
        Map categories = Configuration.getCategories();
        categoryContainer.removeAllViews();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String[] value = (String[]) categories.get(key);

            if(value[1].equals("0")) {
                CategoryView categoryView = new CategoryView(getActivity(), CategoryView.ViewSize.SMALL, value[0], key);
                categoryContainer.addView(categoryView);
            }
        }
        mView.findViewById(R.id.relCategoryContainer).setVisibility(View.VISIBLE);
    }
}
