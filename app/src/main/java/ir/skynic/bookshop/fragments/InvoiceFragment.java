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
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.BookView;

public class InvoiceFragment extends Fragment {

    private View mView;
    private ViewGroup productContainer;
    private TextView txtTotalPrice;
    private ViewGroup priceContainer;

    private TextView txtPhoneNumber;
    private TextView txtAddress;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_invoice, null);
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        initUi();
        showInformation();

        mView.findViewById(R.id.btnPayment).setOnClickListener(view -> {
            if (user.getAddress() == null || user.getPostCode() == null ||  user.getAddress().equals("") || user.getPostCode().equals("")) {
                Toast.makeText(getActivity(), "برای خرید باید آدرس و کد پستی خود را وارد نمایید", Toast.LENGTH_LONG).show();
            } else {
                payment();
            }

        });

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

        getUserInformation();
    }

    private void getUserInformation() {

        String request[] = {"get-user", Configuration.getUsername(getActivity()), Configuration.getUsername(getActivity())};
        ApiClient.getModel(request, "user", User.class, o -> {
            if(o != null) {

                List<User> userList = (List) o[1];
                user = userList.get(0);

                txtAddress.setText(user.getAddress());
                txtPhoneNumber.setText(user.getPhoneNumber());
                if (user.getShabaNumber() == null || user.getShabaNumber().equals("")) {
                    Toast.makeText(getActivity(), "لطفا شماره شبای خود را وارد نمایید.", Toast.LENGTH_SHORT).show();
                }

                if (user.getPostCode() == null || user.getPostCode().equals("")) {
                    Toast.makeText(getActivity(), "لطفا کد پستی خود را وارد نمایید.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        productContainer = mView.findViewById(R.id.lnrProductContainer);
        txtTotalPrice = mView.findViewById(R.id.txtTotalPrice);
        txtAddress = mView.findViewById(R.id.txtAddress);
        txtPhoneNumber = mView.findViewById(R.id.txtPhoneNumber);
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
