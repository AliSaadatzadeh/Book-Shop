package ir.skynic.bookshop;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.model.Category;
import ir.skynic.bookshop.model.City;

import static android.content.Context.MODE_PRIVATE;

public class Configuration {
    private static Map<Integer,String> categoryMap;
    private static Map<Integer,String> cityMap;
    private static Map<Integer,String> bookStatuses;
    private static String username = null;
    private static List<Book> buys = new ArrayList<>();

    public static void fetchCategories(RunnableParam onFinished) {
        if(categoryMap == null) {
            String request[] =  {"get-subject"};

            ApiClient.getModel(request, "subject", Category.class, o -> {
                if(o != null) {
                    List<Category> categoryList = (List) o[1];
                    categoryMap = new HashMap<>();
                    for (Category category : categoryList) {
                        categoryMap.put(category.getId(), category.getName());
                    }
                    onFinished.run(true);
                } else {
                    onFinished.run(false);
                }
            });
        } else {
            onFinished.run(true);
        }
    }

    public static void fetchCities(RunnableParam onFinished) {
        if(cityMap == null) {
            String request[] =  {"get-city"};

            ApiClient.getModel(request, "city", City.class, o -> {
                if(o != null) {
                    List<City> cityList = (List) o[1];
                    cityMap = new HashMap<>();
                    for (City city : cityList) {
                        cityMap.put(city.getId(), city.getName());
                    }
                    onFinished.run(true);
                } else {
                    onFinished.run(false);
                }
            });
        } else {
            onFinished.run(true);
        }
    }

    public static List<Book> getBuys() {
        return buys;
    }

    public static void addToCart(Book book) {
        if (!buys.contains(book)) {
            Configuration.buys.add(book);
        }
    }

    public static void removeFromCart(Book book) {
        Configuration.buys.remove(book);
    }

    public static Map getCategories() {
        return categoryMap;
    }

    public static Map getCities() {
        return cityMap;
    }

    public static Map getBookStatuses() {

        if(bookStatuses == null) {
            bookStatuses = new HashMap<>();

            bookStatuses.put(1,"کاملا نو");
            bookStatuses.put(2,"سالم");
            bookStatuses.put(3,"دچار زدگی");
            bookStatuses.put(4,"بسیار استفاده شده");
        }

        return bookStatuses;
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", MODE_PRIVATE);
        return username = sharedPref.getString("username", null);
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();
    }
}
