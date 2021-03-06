package com.skynic.ketabkhoneh;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.Book;
import com.skynic.ketabkhoneh.model.Category;
import com.skynic.ketabkhoneh.model.City;

import static android.content.Context.MODE_PRIVATE;

public class Configuration {
    private static Map<Integer,String[]> categoryMap;
    private static Map<Integer,String> cityMap;
    private static Map<Integer,String> bookStatuses;
    private static String username = null;
    private static List<Book> cartList = new ArrayList<>();

    public static void fetchCategories(RunnableParam onFinished) {
        if(categoryMap == null) {
            String request[] =  {"get-subject"};

            ApiClient.getModel(request, "subject", Category.class, o -> {
                if(o != null) {
                    List<Category> categoryList = (List) o[1];
                    categoryMap = new LinkedHashMap<>();
                    for (Category category : categoryList) {
                        categoryMap.put(category.getId(), new String[]{ category.getName(), String.valueOf(category.getParentId()) });
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
                    cityMap = new LinkedHashMap<>();
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

    public static List<Book> getCartList() {
        return cartList;
    }

    public static void addToCart(Book book) {
        Configuration.cartList.add(book);
    }

    public static boolean isCartExist(Book book) {
        for (Book item : cartList) {
            if (item.getId() == book.getId()) {
                return true;
            }
        }
        return false;
    }

    public static void removeFromCart(Book book) {
        int i = 0;
        for (Book item : cartList) {
            if (item.getId() == book.getId()) {
                break;
            }
            i++;
        }
        cartList.remove(i);
//        Configuration.cartList.remove(book);
    }

    public static Map getCategories() {
        return categoryMap;
    }

    public static boolean hasCategoryChild(int mKey) {
        Map categories = Configuration.getCategories();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String[] value = (String[]) categories.get(key);
            if(value[1].equals(String.valueOf(String.valueOf(mKey)))) {
                return false;
            }
        }
        return true;
    }

    public static Map getCities() {
        return cityMap;
    }

    public static Map getBookStatuses() {

        if(bookStatuses == null) {
            bookStatuses = new LinkedHashMap<>();

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

    public static void unSetUsername(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
    public static void setUsername(Context context, String username) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static void userLogout(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }
}
