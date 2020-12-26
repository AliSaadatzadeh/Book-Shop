package com.skynic.ketabkhoneh.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.model.Book;
import com.skynic.ketabkhoneh.view.BookView;
import com.skynic.ketabkhoneh.view.OopsView;

public class CartFragment extends Fragment {

    private View mView;

    private ViewGroup bookContainer;
    private TextView txtTotalPrice;
    private View btnPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, null);

        initUi();
        showInformation();

        return mView;
    }

    private void showInformation() {
        bookContainer.removeAllViews();

        int totalPrice = 0;
        for (Book book : Configuration.getCartList()) {
            totalPrice += (book.getPrice() - (book.getPrice() * book.getOff() / 100));
            BookView bookView = new BookView(getActivity(), BookView.ViewSize.CART, book);
            bookView.setBtnDeleteListener(view -> {
                Configuration.removeFromCart(book);
                showInformation();
            });
            bookContainer.addView(bookView);
            btnPayment.setVisibility(View.VISIBLE);
        }

        if (Configuration.getCartList().isEmpty()) {
            bookContainer.addView(new OopsView(getActivity()));
            btnPayment.setVisibility(View.GONE);
        }

        txtTotalPrice.setText(totalPrice + "");
    }

    private void initUi() {
        bookContainer = mView.findViewById(R.id.lnrBookContainer);
        txtTotalPrice = mView.findViewById(R.id.txtTotalPrice);
        btnPayment = mView.findViewById(R.id.btnPayment);

        btnPayment.setOnClickListener(view -> {
            if (!Configuration.getCartList().isEmpty()) {
                InvoiceFragment invoiceFragment = new InvoiceFragment();
                MainActivity.showFragment(getActivity(), invoiceFragment);
            } else {
                Toast.makeText(getActivity(), "سبد خرید شما خالی است", Toast.LENGTH_LONG).show();
            }

        });
    }
}
