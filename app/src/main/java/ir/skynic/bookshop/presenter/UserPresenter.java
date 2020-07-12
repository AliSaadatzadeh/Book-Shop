package ir.skynic.bookshop.presenter;

import java.util.ArrayList;
import java.util.List;

import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.model.User;

public class UserPresenter {

    private UserView userView;
    private String keyWord;

    public UserPresenter(UserView userView) {
        this.userView = userView;
    }

    public void setKeyword(String keyword) {
        this.keyWord = keyword;
    }

    public void search() {
        //TODO: get data from API
        List<User> list = new ArrayList<>();

        userView.onSearchResult(list);
    }

    public interface UserView {
        void onSearchResult(List<User> result);
    }
}
