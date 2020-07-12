package ir.skynic.bookshop.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.view.BookView;


public class HomeFragment extends Fragment {

    private View mView;

    private ViewGroup productContainer;
    private ViewGroup userContainer;
    private ViewGroup newProductContainer;
    private ViewGroup categoryContainer;

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
    }

    private void initUi() {
        productContainer = mView.findViewById(R.id.lnrProductContainer);
        productContainer.setVisibility(View.GONE);
        userContainer = mView.findViewById(R.id.lnrUserContainer);
//        userContainer.setVisibility(View.GONE);
        newProductContainer = mView.findViewById(R.id.lnrNewProductContainer);
        newProductContainer.setVisibility(View.GONE);
        categoryContainer = mView.findViewById(R.id.lnrCategoryContainer);
//        categoryContainer.setVisibility(View.GONE);
    }
}
