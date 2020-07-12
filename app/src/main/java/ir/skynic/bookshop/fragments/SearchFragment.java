package ir.skynic.bookshop.fragments;

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

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.view.BookView;

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

    private String searchKeyword = "";
    private int cityId = 0;
    private int categoryId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search, null);

        initUi();
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
    }

    public void setCategory(int key) {
        categoryId = key;
    }

    public void setCity(int key) {
        cityId = key;
    }

    public void search() {
        progressBar.setVisibility(View.VISIBLE);
        btnClearKeyword.setVisibility(View.INVISIBLE);

        updateFilterViews();

        String request[] = {"get-book", Configuration.getUsername(getActivity()), "", categoryId > 0 ? String.valueOf(categoryId) : "", cityId > 0 ? String.valueOf(cityId) : "", searchKeyword};
        ApiClient.getModel(request, "book", Book.class, o -> {
            if(o != null) {

                List<Book> bookList = (List) o[1];

                productContainer.removeAllViews();

                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.LARGE, book);
                    productContainer.addView(bookView);
                }

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
                txtSelectedCategory.setText("دسته بندی: \"" + (String)Configuration.getCategories().get(categoryId) + "\"");
            else
                txtSelectedCategory.setVisibility(View.GONE);

        } else {
            rltFiltersContainer.setVisibility(View.GONE);
        }
    }
}
