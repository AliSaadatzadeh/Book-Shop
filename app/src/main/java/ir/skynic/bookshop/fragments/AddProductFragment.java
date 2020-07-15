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
import java.util.List;
import java.util.Map;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.activities.RegisterActivity;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
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
    private TextView txtStatus;
    private TextView txtCategory;

    private int productId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_product, null);

        initUi();

        if (productId != -1) {
            getProductInformation();
        }
        return mView;
    }

    private void getProductInformation() {
        progressBar.setVisibility(View.VISIBLE);
        String request[] = {"get-user-book", Configuration.getUsername(getActivity()), "", String.valueOf(productId)};
        ApiClient.getModel(request, "book", Book.class, o -> {
            if(o != null) {

                List<Book> bookList = (List) o[1];
                Book book = bookList.get(0);

                edtTitle.setText(book.getTitle());
                edtAuthor.setText(book.getAuthor());
                edtDescription.setText(book.getDescription());
                edtTranslator.setText(book.getTranslator());
                edtPublication.setText(book.getPublisher());
                edtPublicationYear.setText(String.valueOf(book.getPublicationYear()));
                edtPrice.setText(String.valueOf(book.getPrice()));
                sendingCost.setChecked(book.getTransferring() == 1);

                String value = (String) Configuration.getCategories().get(book.getCategoryId());
                txtCategory.setText(value);
                selectedCategoryId = book.getCategoryId();

                if (book.getBookStatus() == 1) {
                    txtStatus.setText("کاملا نو");
                    selectedStatusId = 1;
                } else if (book.getBookStatus() == 2) {
                    txtStatus.setText("سالم");
                    selectedStatusId = 2;
                } else if (book.getBookStatus() == 3) {
                    txtStatus.setText("دچار زدگی");
                    selectedStatusId = 3;
                } else if (book.getBookStatus() == 3) {
                    txtStatus.setText("بسیار استفاده شده");
                    selectedStatusId = 3;
                }

                new Thread(() -> {
                    try {
                        Bitmap bitmap = Utils.getImageFromUrl(book.getImageLink());
                        Utils.runOnMainThread(() -> imgBook.setImageBitmap(bitmap));
                    } catch (Exception ingnore) {}
                }).start();
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);
        });
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
        txtStatus = mView.findViewById(R.id.txtStatusSelection);
        txtCategory = mView.findViewById(R.id.txtCategorySelection);

        sendingCost = mView.findViewById(R.id.checkBoxSendingCost);

        progressBar = mView.findViewById(R.id.progressBar);
        btnSubmit = mView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {
            submitBook();
        });


        mView.findViewById(R.id.imgBook).setOnClickListener(view -> {
            selectImage();
        });

        txtCategory.setOnClickListener(view -> {
            showCategorySelectionPopup();
        });


        txtStatus.setOnClickListener(view -> {
            showStatusSelectionPopup();
        });
    }

    private void showStatusSelectionPopup() {
        PopupListView popupListView = new PopupListView(getActivity(), "انتخاب دسته بندی");

        popupListView.addItem("کاملا نو", () -> {txtStatus.setText("کاملا نو");
            selectedStatusId = 1;});
        popupListView.addItem("سالم", () -> {txtStatus.setText("سالم");
            selectedStatusId = 2;});
        popupListView.addItem("دچار زدگی", () -> {txtStatus.setText("دچار زدگی");
            selectedStatusId = 3;});
        popupListView.addItem("بسیار استفاده شده", () -> {txtStatus.setText("بسیار استفاده شده");
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
                txtCategory.setText(value);
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
            RunnableParam runnableParam;
            String requests[];
            if (productId == -1) {
                runnableParam = o -> {
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
                    restView();
                };

                requests = new String[]{"add-book", Configuration.getUsername(getActivity()), String.valueOf(productId), strTitle, strAuthor,
                        String.valueOf(selectedCategoryId), strPrice, strTranslator, strPublication, strPublicationYear,
                        String.valueOf(selectedStatusId), strDescription, String.valueOf(strSendingCost)};
            } else {
                runnableParam = o -> {
                    if (o != null) {
                        int errorCode = (int) o[0];
                        if(errorCode == 0) {
                            Toast.makeText(getActivity(), "تغییرات شما با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "اطلاعات وارد شده نامعبتر است.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "خطایی در ثبت اطلاعات رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                    }

                    setProgressingEnabled(false);
                };

                requests = new String[]{"edit-book", Configuration.getUsername(getActivity()), String.valueOf(productId), strTitle, strAuthor,
                        String.valueOf(selectedCategoryId), strPrice, strTranslator, strPublication, strPublicationYear,
                        String.valueOf(selectedStatusId), strDescription, String.valueOf(strSendingCost)};
            }


            if(selectedImage != null) {
                ApiClient.executeCommandByImageUpload(selectedImage, requests, runnableParam);
            } else {
                ApiClient.executeCommand(requests, runnableParam);
            }

            setProgressingEnabled(true);
        }
    }

    private void restView() {
        edtTitle.setText("");
        edtAuthor.setText("");
        edtDescription.setText("");
        edtTranslator.setText("");
        edtPublicationYear.setText("");
        edtPublication.setText("");
        edtPrice.setText("");
        sendingCost.setChecked(false);
    }

    private void setProgressingEnabled(boolean enabled) {
        progressBar.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnSubmit.setEnabled(!enabled);
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
