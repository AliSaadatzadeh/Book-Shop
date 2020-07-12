package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.view.BookView;

public class CartFragment extends Fragment {

    private View mView;

    private ViewGroup bookContainer;
    private TextView txtTotalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, null);

        mView.findViewById(R.id.btnPayment).setOnClickListener(view -> {

            InvoiceFragment invoiceFragment = new InvoiceFragment();

            MainActivity.showFragment(getActivity(), invoiceFragment);
        });

        initUi();

        showInformation();

        return mView;
    }

    private void showInformation() {
        bookContainer.removeAllViews();

        int totalPrice = 0;
        for (Book book : Configuration.getBuys()) {
            totalPrice += (book.getPrice() - (book.getPrice() * book.getOff() / 100));
            BookView bookView = new BookView(getActivity(), BookView.ViewSize.CART, book);
            bookView.setBtnDeleteListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Configuration.removeFromCart(book);
                    showInformation();
                }
            });
            bookContainer.addView(bookView);
        }

        txtTotalPrice.setText(totalPrice + "");
    }

    private void initUi() {
        bookContainer = mView.findViewById(R.id.lnrBookContainer);
        txtTotalPrice = mView.findViewById(R.id.txtTotalPrice);
    }
}
