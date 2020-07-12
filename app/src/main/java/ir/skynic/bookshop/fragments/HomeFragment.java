package ir.skynic.bookshop.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.BookView;
import ir.skynic.bookshop.view.CategoryView;
import ir.skynic.bookshop.view.UserView;


public class HomeFragment extends Fragment {

    private View mView;

    private ViewGroup productContainer;
    private ViewGroup userContainer;
    private ViewGroup newProductContainer;
    private ViewGroup categoryContainer;

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        mView = inflater.inflate(R.layout.fragment_home, null);
//        mView.findViewById(R.id.btnProfile).setOnClickListener(view -> {
//            UserProfileFragment userProfileFragment = new UserProfileFragment();
//            MainActivity.showFragment(getActivity(), userProfileFragment);
//        });
//
//        return mView;

        mView = inflater.inflate(R.layout.fragment_home, null);

        initUi();
        getInformation();

        return mView;
    }

    private void getInformation() {
        new Thread(() -> {
            try {
                String viewRequest[] = {"get-view", Configuration.getUsername(getActivity())};
                String jsonContent = ApiClient.getJson(viewRequest);

                if(jsonContent != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        String error = jsonObject.getString("error");

                        if(error.equals("0")) {
                            String view = jsonObject.getJSONArray("view").getJSONObject(0).optString("viewLink");

                            Bitmap bitmap = Utils.getImageFromUrl(view);
                            Utils.runOnMainThread(() -> imageView.setImageBitmap(bitmap));
                        }
                    } catch (Exception ignore) { }
                }

            } catch (Exception ignored) {}
        }).start();


//        ApiClient.getModel(viewRequest, "view", Book.class, o -> {
//            if(o != null) {
//
//                List<Book> bookList = (List) o[1];
//
//                new Thread(() -> {
//                    try {
//                        Bitmap bitmap = Utils.getImageFromUrl(model.getThumbnailImageLink());
//                        Utils.runOnMainThread(() -> imageView.setImageBitmap(bitmap));
//                    } catch (Exception ignored) {}
//                }).start();
//
//            } else {
//                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
//            }
//        });


        String disCountedRequest[] = {"get-dis-counted", Configuration.getUsername(getActivity()), "30"};
        ApiClient.getModel(disCountedRequest, "book", Book.class, o -> {
            if(o != null) {

                List<Book> bookList = (List) o[1];

                productContainer.removeAllViews();

                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.SMALL, book);
                    productContainer.addView(bookView);
                }

                productContainer.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });

        String userRequest[] = {"get-top-seller", Configuration.getUsername(getActivity()), "30"};
        ApiClient.getModel(userRequest, "user", User.class, o -> {
            if(o != null) {

                List<User> userList = (List) o[1];

                userContainer.removeAllViews();

                for (User user : userList) {
                    UserView userView = new UserView(getActivity(), UserView.ViewSize.SMALL, user);
                    userContainer.addView(userView);
                }

                userContainer.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });

        String newProductRequest[] = {"get-book", Configuration.getUsername(getActivity())};
        ApiClient.getModel(newProductRequest, "book", Book.class, o -> {
            if(o != null) {

                List<Book> bookList = (List) o[1];

                newProductContainer.removeAllViews();

                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.SMALL, book);
                    newProductContainer.addView(bookView);
                }
                newProductContainer.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });

        Map categories = Configuration.getCategories();
        categoryContainer.removeAllViews();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String value = (String) categories.get(key);

            CategoryView categoryView = new CategoryView(getActivity(), CategoryView.ViewSize.SMALL, value);
            categoryView.setOnClickListener(view -> {

                SearchFragment searchFragment = new SearchFragment();
                searchFragment.clearFilters();
                searchFragment.setCategory(key);

                MainActivity.showFragment(HomeFragment.this.getActivity(), searchFragment);
            });

            categoryContainer.addView(categoryView);
        }

        categoryContainer.setVisibility(View.VISIBLE);
    }

    private void initUi() {
        imageView = mView.findViewById(R.id.imageView);

        productContainer = mView.findViewById(R.id.lnrProductContainer);
        productContainer.setVisibility(View.GONE);

        userContainer = mView.findViewById(R.id.lnrUserContainer);
        userContainer.setVisibility(View.GONE);

        newProductContainer = mView.findViewById(R.id.lnrNewProductContainer);
        newProductContainer.setVisibility(View.GONE);

        categoryContainer = mView.findViewById(R.id.lnrCategoryContainer);
        categoryContainer.setVisibility(View.GONE);
    }
}
