package ir.skynic.bookshop.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;

public class SearchPresenter {

    private SearchView searchView;
    private String keyWord;
    private int cityId;
    private int category;

    public interface SearchView {
        void onSearchResult(List<Book> result);
    }

    public SearchPresenter(SearchView searchView) {
        this.searchView = searchView;
    }

    public void setKeyword(String keyword) {
        this.keyWord = keyword;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void search() {
        String request[] =  {"get-book","BB"};

//        ApiClient.getModel(request, Book.class, list ->{
//            //searchView.onSearchResult(list);
//        });
    }
}
