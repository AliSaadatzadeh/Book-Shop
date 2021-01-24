package com.skynic.ketabkhoneh.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.view.PopupListView;
import com.skynic.ketabkhoneh.R;

public class SearchFilterFragment extends Fragment {

    private View mView;
    private int selectedCityId = 0;
    private int selectedCategoryId = 0;
    private SearchFragment searchFragment;
    private TextView txtCitySelection;
    private TextView txtCategorySelection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_filter, null);

        initUi();
        return mView;
    }

    public void setSelectedCityId(int selectedCityId) {
        this.selectedCityId = selectedCityId;
    }

    public void setSelectedCategoryId(int selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public void setSearchFragment(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    private void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.btnCitySelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCitySelectionPopup();
            }
        });

        mView.findViewById(R.id.btnCategorySelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategorySelectionPopup(0);
            }
        });

        txtCitySelection = mView.findViewById(R.id.txtCitySelection);
        txtCategorySelection = mView.findViewById(R.id.txtCategorySelection);

        if(selectedCityId > 0)
            txtCitySelection.setText((String)Configuration.getCities().get(selectedCityId));

        if(selectedCategoryId > 0)
            txtCategorySelection.setText(((String[])Configuration.getCategories().get(selectedCategoryId))[0]);

        mView.findViewById(R.id.btnSubmit).setOnClickListener(view -> submit());
    }

    private void showCitySelectionPopup() {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب شهر");

        Map cities = Configuration.getCities();
        for (Object o : cities.keySet()) {
            int key = (int) o;
            String value = (String) cities.get(key);
            popupListView.addItem(value, () -> {
                ((TextView) mView.findViewById(R.id.txtCitySelection)).setText(value);
                selectedCityId = key;
            });
        }

        popupListView.show();
    }

    private void showCategorySelectionPopup(int i) {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب دسته بندی");

        Map categories = Configuration.getCategories();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String [] values = (String[]) categories.get(key);
            if(values[1].equals(String.valueOf(i))) {
                popupListView.addItem(values[0], () -> {
                    if(!Configuration.hasCategoryChild(key))
                        showCategorySelectionPopup(key);

                    txtCategorySelection.setText(values[0]);
                    selectedCategoryId = key;
                });
            }
        }

        popupListView.show();
    }

    private void submit() {
        getFragmentManager().popBackStack();
        searchFragment.setCity(selectedCityId);
        searchFragment.setCategory(selectedCategoryId);
        searchFragment.search();
    }
}
