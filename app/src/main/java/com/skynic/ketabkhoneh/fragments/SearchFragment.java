package com.skynic.ketabkhoneh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.Book;
import com.skynic.ketabkhoneh.view.BookView;
import com.skynic.ketabkhoneh.view.OopsView;

public class SearchFragment extends Fragment {

    private View mView;

    private EditText edtSearch;
    private ViewGroup productContainer;
    private ProgressBar progressBar;
    private ViewGroup rltFiltersContainer;
    private TextView txtSelectedCategory;
    private TextView txtSelectedCity;
    private View btnClearFilters;
    private View btnClearKeyword;

    private boolean canEditRequest = false;

    private String searchKeyword = "";
    private int cityId = 0;
    private int categoryId = 0;
    private String request[] = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search, null);

        initUi();
        if(request == null)
            request = new String[]{"get-book", Configuration.getUsername(getActivity()), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};

        search();
        return mView;
    }

    void initUi() {

        productContainer = mView.findViewById(R.id.lnrProductContainer);
        productContainer.removeAllViews();

        progressBar = mView.findViewById(R.id.progressBar);
        rltFiltersContainer = mView.findViewById(R.id.rltFiltersContainer);

        btnClearFilters = mView.findViewById(R.id.btnClearFilters);
        btnClearFilters.setOnClickListener(view -> {
            clearFilters();
            search();
        });

        txtSelectedCategory = mView.findViewById(R.id.txtSelectedCategory);
        txtSelectedCity = mView.findViewById(R.id.txtSelectedCity);

        mView.findViewById(R.id.btnFilter).setOnClickListener(view -> {
            showFilterFragment();
        });

        btnClearKeyword = mView.findViewById(R.id.btnClearKeyword);
        btnClearKeyword.setOnClickListener(view -> {
            searchKeyword = "";
            edtSearch.setText("");
            search();
        });

        edtSearch = mView.findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH ||
                    i == EditorInfo.IME_ACTION_DONE ||
                    keyEvent != null &&
                            keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (keyEvent == null || !keyEvent.isShiftPressed()) {

                    edtSearch.clearFocus();
                    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

                    searchKeyword = edtSearch.getText().toString();
                    search();

                    return true;
                }
            }
            return false;
        });
    }

    public void clearFilters() {
        searchKeyword = "";
        categoryId = 0;
        cityId = 0;
        request = new String[]{"get-book", Configuration.getUsername(), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};
    }

    public void setCategory(int key) {
        categoryId = key;
        request = new String[]{"get-book", Configuration.getUsername(), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};
    }

    public void setCity(int key) {
        cityId = key;
        request = new String[]{"get-book", Configuration.getUsername(), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};
    }

    public void search() {
        progressBar.setVisibility(View.VISIBLE);
        btnClearKeyword.setVisibility(View.INVISIBLE);

        updateFilterViews();

        if(!canEditRequest)
            request = new String[]{"get-book", Configuration.getUsername(), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};

        canEditRequest = false;

        ApiClient.getModel(request, "book", Book.class, o -> {
            if(o != null) {

                List<Book> bookList = (List) o[1];

                productContainer.removeAllViews();

                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.LARGE, book);
                    productContainer.addView(bookView);
                }

                if(bookList.isEmpty())
                    productContainer.addView(new OopsView(getActivity()));

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.INVISIBLE);

            if(searchKeyword.length() > 0)
                btnClearKeyword.setVisibility(View.VISIBLE);
        });
    }

    private void showFilterFragment() {
        SearchFilterFragment searchFilterFragment = new SearchFilterFragment();
        searchFilterFragment.setSearchFragment(this);
        searchFilterFragment.setSelectedCategoryId(categoryId);
        searchFilterFragment.setSelectedCityId(cityId);
        MainActivity.showFragment(getActivity(), searchFilterFragment);
    }

    private void updateFilterViews() {

        if(cityId > 0 || categoryId > 0) {
            rltFiltersContainer.setVisibility(View.VISIBLE);
            txtSelectedCategory.setVisibility(View.VISIBLE);
            txtSelectedCity.setVisibility(View.VISIBLE);

            if(cityId > 0)
                txtSelectedCity.setText("شهر: \"" + (String) Configuration.getCities().get(cityId) + "\"");
            else
                txtSelectedCity.setVisibility(View.GONE);


            if(categoryId > 0)
                txtSelectedCategory.setText("دسته بندی: \"" + ((String[])Configuration.getCategories().get(categoryId))[0] + "\"");
            else
                txtSelectedCategory.setVisibility(View.GONE);

        } else {
            rltFiltersContainer.setVisibility(View.GONE);
        }
    }

    public void showAllOff() {
        canEditRequest = true;
        request = new String[]{"get-dis-counted", Configuration.getUsername(), "200"};
    }

    public void showAllNew() {
        request = new String[]{"get-book", Configuration.getUsername()};
    }
}
