package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.view.BookView;

public class InvoiceFragment extends Fragment {

    private View mView;
    private ViewGroup productContainer;
    private TextView txtTotalPrice;
    private ViewGroup priceContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_invoice, null);
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });
        mView.findViewById(R.id.btnPayment).setOnClickListener(view -> {
            payment();
        });

        initUi();
        showInformation();

        return mView;
    }

    private void showInformation() {
        productContainer.removeAllViews();

        int totalPrice = 0;
        for (Book book : Configuration.getCartList()) {
            totalPrice += (book.getPrice() - (book.getPrice() * book.getOff() / 100));
            BookView bookView = new BookView(getActivity(), BookView.ViewSize.INVOICE, book);
            productContainer.addView(bookView);
        }

        txtTotalPrice.setText(totalPrice + "");
    }

    private void initUi() {
        productContainer = mView.findViewById(R.id.lnrProductContainer);
        txtTotalPrice = mView.findViewById(R.id.txtTotalPrice);
    }

    private void payment() {
        String productId = Configuration.getCartList().get(0).getId() + "";
        for (int i = 1 ; i < Configuration.getCartList().size() ; i ++) {
            productId += ("/" + Configuration.getCartList().get(i));
        }
        String request[] = {"payment", Configuration.getUsername(getActivity()), productId};
        ApiClient.getValue(request, "paymentLink", new RunnableParam() {
            @Override
            public void run(Object... o) {
                if(o != null && (int) o[0] == 0) {
                    String link = (String) o[1];

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
