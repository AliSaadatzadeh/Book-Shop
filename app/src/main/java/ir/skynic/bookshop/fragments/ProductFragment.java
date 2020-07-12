package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;

import static android.view.View.VISIBLE;

public class ProductFragment extends Fragment {

    private View mView;
    private Book model;

    private TextView txtTile;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtCategory;
    private TextView txtStatus;
    private TextView txtOff;
    private ImageView imgProduct;
    private TextView txtCity;
    private TextView txtAuthor;
    private TextView txtTranslator;
    private TextView txtPublisher;
    private TextView txtPublicationYear;
    private TextView txtDate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_product, null);

        initUi();

        return mView;
    }

    public void setModel(Book model) {
        this.model = model;
    }

    private void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.btnComments).setOnClickListener(view -> {
            MainActivity.showFragment(getActivity(), new CommentsFragment());
        });

        txtTile = mView.findViewById(R.id.txtTitle);
        txtDescription = mView.findViewById(R.id.txtDescription);
        txtPrice = mView.findViewById(R.id.txtPrice);
        txtCategory = mView.findViewById(R.id.txtCategory);
        txtStatus = mView.findViewById(R.id.txtStatus);
        txtOff = mView.findViewById(R.id.txtOff);
        imgProduct = mView.findViewById(R.id.imgProduct);
        txtCity = mView.findViewById(R.id.txtCity);
        txtAuthor = mView.findViewById(R.id.txtAuthor);
        txtTranslator = mView.findViewById(R.id.txtTranslator);
        txtPublisher = mView.findViewById(R.id.txtPublisher);
        txtPublicationYear = mView.findViewById(R.id.txtPublicationYear);
        txtDate = mView.findViewById(R.id.txtDate);

        showInformation();
    }

    private void showInformation() {
        txtTile.setText(model.getTitle());
        txtDescription.setText(model.getDescription());

        txtAuthor.setText(model.getAuthor().length() > 0 ? model.getAuthor() : "—");
        txtTranslator.setText(model.getTranslator().length() > 0 ?  model.getTranslator() : "—");
        txtPublisher.setText(model.getPublisher().length() > 0 ?  model.getPublisher() : "—");
        txtPublicationYear.setText(model.getPublicationYear() > 0 ? String.valueOf(model.getPublicationYear()) : "—");
        txtDate.setText(model.getDate());

        txtPrice.setText(new DecimalFormat("#,###").format(model.getPrice()));

        txtStatus.setText((String) Configuration.getBookStatuses().get(model.getBookStatus()));

        String category = (String) Configuration.getCategories().get(model.getCategoryId());
        txtCategory.setText("در دسته " + "\"" + category + "\"");

        String city = (String) Configuration.getCities().get(model.getCityId());
        txtCity.setText("ارسال از " + "\"" + city + "\"");

        if(model.getOff() > 0) {
            txtOff.setText(model.getOff() + "% تخفیف");
            txtOff.setVisibility(VISIBLE);
        }

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getImageLink());
                Utils.runOnMainThread(() -> imgProduct.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();
    }
}
