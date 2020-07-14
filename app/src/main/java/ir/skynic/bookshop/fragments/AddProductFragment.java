package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.activities.RegisterActivity;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.view.PopupListView;
import ir.skynic.bookshop.R;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {

    private View mView;
    private Bitmap selectedImage = null;
    private int selectedCategoryId;
    private int selectedStatusId;

    private Button btnSubmit;
    private ProgressBar progressBar;
    private ImageView imgBook;
    private EditText edtTitle;
    private EditText edtAuthor;
    private EditText edtDescription;
    private EditText edtTranslator;
    private EditText edtPublication;
    private EditText edtPublicationYear;
    private EditText edtPrice;
    private CheckBox sendingCost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_product, null);

        initUi();

        return mView;
    }

    private void initUi() {
        imgBook = mView.findViewById(R.id.imgBook);
        edtTitle = mView.findViewById(R.id.txtTitle);
        edtAuthor = mView.findViewById(R.id.txtAuthor);
        edtDescription = mView.findViewById(R.id.txtDescription);
        edtTranslator = mView.findViewById(R.id.txtTranslator);
        edtPublication = mView.findViewById(R.id.txtPublication);
        edtPublicationYear = mView.findViewById(R.id.txtPublicationYear);
        edtPrice = mView.findViewById(R.id.txtPrice);

        sendingCost = mView.findViewById(R.id.checkBoxSendingCost);

        progressBar = mView.findViewById(R.id.progressBar);
        btnSubmit = mView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {
            submitBook();
        });


        mView.findViewById(R.id.imgBook).setOnClickListener(view -> {
            selectImage();
        });

        mView.findViewById(R.id.txtCategorySelection).setOnClickListener(view -> {
            showCategorySelectionPopup();
        });


        mView.findViewById(R.id.txtStatusSelection).setOnClickListener(view -> {
            showStatusSelectionPopup();
        });
    }

    private void showStatusSelectionPopup() {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب دسته بندی");

        popupListView.addItem("کاملا نو", () -> {((TextView) mView.findViewById(R.id.txtStatusSelection)).setText("کاملا نو");
            selectedStatusId = 1;});
        popupListView.addItem("سالم", () -> {((TextView) mView.findViewById(R.id.txtStatusSelection)).setText("سالم");
            selectedStatusId = 2;});
        popupListView.addItem("دچار زدگی", () -> {((TextView) mView.findViewById(R.id.txtStatusSelection)).setText("دچار زدگی");
            selectedStatusId = 3;});
        popupListView.addItem("بسیار استفاده شده", () -> {((TextView) mView.findViewById(R.id.txtStatusSelection)).setText("بسیار استفاده شده");
            selectedStatusId = 4;});

        popupListView.show();
    }

    private void selectImage() {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب عکس");

        popupListView.addItem("استفاده از دوربین", () -> {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
        });

        popupListView.addItem("از عکس های موجود", () -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);
        });

        popupListView.show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {

            try {
                Uri uri = imageReturnedIntent.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                bitmap = Utils.scaleDown(bitmap, 600, true);
                bitmap = Utils.cropToSquare(bitmap);
                imgBook.setImageBitmap(bitmap);

                selectedImage = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitBook() {
        String strTitle = edtTitle.getText().toString();
        String strAuthor = edtAuthor.getText().toString();
        String strDescription = edtDescription.getText().toString();
        String strTranslator  = edtTranslator.getText().toString();
        String strPublicationYear = edtPublicationYear.getText().toString();
        String strPublication = edtPublication.getText().toString();
        String strPrice = edtPrice.getText().toString();
        String strSendingCost = sendingCost.isChecked() ? "1" : "0";

        if (strTitle.length() < 3)
            Toast.makeText(getActivity(), "نام وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strAuthor.length() < 3)
            Toast.makeText(getActivity(), "نام نویسنده وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strDescription.length() < 10)
            Toast.makeText(getActivity(), "توضیحات وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strTranslator.length() < 3)
            Toast.makeText(getActivity(), "نام مترجم شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strPublication.length() < 3)
            Toast.makeText(getActivity(), "نام انتشارات وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
//        else if (strPrice.length() < 3)
//            Toast.makeText(getActivity(), "قیمت وارد شده درست نیست!", Toast.LENGTH_SHORT).show();
        else if (strPublicationYear.length() > 0 && strPublicationYear.length() < 4)
            Toast.makeText(getActivity(), "سال انتشارات وارد شده صحیح نیست!", Toast.LENGTH_SHORT).show();
        else {

            RunnableParam runnableParam = o -> {
                if (o != null) {
                    int errorCode = (int) o[0];
                    if(errorCode == 0) {
                        Toast.makeText(getActivity(), "کتاب شما ثبت شد.", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "اطلاعات وارد شده نامعبتر است.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "خطایی در ثبت اطلاعات رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                }

                setProgressingEnabled(false);
            };

            String requests[] = new String[]{"add-book", Configuration.getUsername(getActivity()), strTitle, strAuthor,
                    String.valueOf(selectedCategoryId), strPrice, strTranslator, strPublication, strPublicationYear,
                    String.valueOf(selectedStatusId), strDescription, String.valueOf(strSendingCost)};

            if(selectedImage != null) {
                ApiClient.executeCommandByImageUpload(selectedImage, requests, runnableParam);
            } else {
                ApiClient.executeCommand(requests, runnableParam);
            }

            setProgressingEnabled(true);
        }
    }

    private void setProgressingEnabled(boolean enabled) {
        progressBar.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnSubmit.setEnabled(!enabled);
    }
}
