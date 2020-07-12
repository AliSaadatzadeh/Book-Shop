package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.view.PopupListView;
import ir.skynic.bookshop.R;

public class AddProductFragment extends Fragment {

    private View mView;

    private int selectedCategoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_product, null);

        initUi();

        return mView;
    }

    private void initUi() {
        mView.findViewById(R.id.txtCategorySelection).setOnClickListener(view -> {
            showCategorySelectionPopup();
        });

        mView.findViewById(R.id.txtStatusSelection).setOnClickListener(view -> {
            PopupListView popupListView = new PopupListView(getActivity(), "انتخاب دسته بندی");

            popupListView.addItem("کاملا نو", () -> popupListView.hide());
            popupListView.addItem("سالم", () -> popupListView.hide());
            popupListView.addItem("دچار زدگی", () -> popupListView.hide());
            popupListView.addItem("بسیار استفاده شده", () -> popupListView.hide());

            popupListView.show();
        });
    }


    private void showCategorySelectionPopup() {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب دسته بندی");

        Map categories = Configuration.getCategories();
        for (Object o : categories.keySet()) {
            int key = (int) o;
            String value = (String) categories.get(key);
            popupListView.addItem(value, () -> {
                ((TextView) mView.findViewById(R.id.txtCategorySelection)).setText(value);
                selectedCategoryId = key;
            });
        }

        popupListView.show();
    }
}
