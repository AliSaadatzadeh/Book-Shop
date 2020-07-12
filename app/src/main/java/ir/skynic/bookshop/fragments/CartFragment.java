package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;

public class CartFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, null);

        mView.findViewById(R.id.btnPayment).setOnClickListener(view -> {

            InvoiceFragment invoiceFragment = new InvoiceFragment();

            MainActivity.showFragment(getActivity(), invoiceFragment);
        });

        return mView;
    }
}
