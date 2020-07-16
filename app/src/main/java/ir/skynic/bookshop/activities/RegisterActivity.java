package ir.skynic.bookshop.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.PopupListView;

public class RegisterActivity extends AppCompatActivity {
    private boolean isEdit = false;

    private int selectedCityId = 0;
    private String phone;
    private Bitmap selectedImage = null;

    private Button btnSubmit;
    private ProgressBar progressBar;
    private ImageView imgProfile;
    private EditText edtName;
    private EditText edtUsername;
    private EditText edtAddress;
    private EditText edtPostalCode;
    private EditText edtShabaCode;
    private TextView txtCity;

    private boolean isAcceptedRules = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();

        phone = getIntent().getStringExtra("phone");
        if (phone == null) {
            getUserInformation();
            isEdit = true;
        }
        //phone = "09142367752";
    }

    private void getUserInformation() {
        progressBar.setVisibility(View.VISIBLE);
        String request[] = {"get-user", Configuration.getUsername(this), Configuration.getUsername(this)};
        ApiClient.getModel(request, "user", User.class, o -> {
            if(o != null) {

                List<User> userList = (List) o[1];
                User user = userList.get(0);

                new Thread(() -> {
                    try {
                        Bitmap bitmap = Utils.getImageFromUrl(user.getImageLink());
                        Utils.runOnMainThread(() -> imgProfile.setImageBitmap(bitmap));
                    } catch (Exception ingnore) {}
                }).start();
                edtName.setText(user.getName());
                edtUsername.setText(user.getUserName());
                edtAddress.setText(user.getAddress());
                edtPostalCode.setText(user.getPostCode());
                edtShabaCode.setText(user.getShabaNumber());
                String value = (String) Configuration.getCities().get(user.getCityId());
                txtCity.setText(value);
                selectedCityId = user.getCityId();
                phone = user.getPhoneNumber();
            } else {
                Toast.makeText(this, "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        });
    }

    private void initUi() {
        imgProfile = findViewById(R.id.imgProfile);
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtAddress = findViewById(R.id.edtAddress);
        edtPostalCode = findViewById(R.id.edtPostalCode);
        edtShabaCode = findViewById(R.id.edtShabaCode);
        progressBar = findViewById(R.id.progressBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtCity = findViewById(R.id.txtCitySelection);

        btnSubmit.setOnClickListener(view -> {
            submitUser();
        });

        findViewById(R.id.txtCitySelection).setOnClickListener(view -> {
            showCitySelectionPopup();
        });

        findViewById(R.id.btnSelectImage).setOnClickListener(view -> {
            selectImage();
        });
    }

    private void selectImage() {
        PopupListView popupListView = new PopupListView(RegisterActivity.this, "انتخاب عکس");

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

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {

            try {
                Uri uri = imageReturnedIntent.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = Utils.scaleDown(bitmap, 600, true);
                bitmap = Utils.cropToSquare(bitmap);
                imgProfile.setImageBitmap(bitmap);

                selectedImage = bitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showCitySelectionPopup() {
        PopupListView popupListView = new PopupListView(RegisterActivity.this, "انتخاب شهر");

        Map cities = Configuration.getCities();
        for (Object o : cities.keySet()) {
            int key = (int) o;
            String value = (String) cities.get(key);
            popupListView.addItem(value, () -> {
                txtCity.setText(value);
                selectedCityId = key;
            });
        }

        popupListView.show();
    }

    private void submitUser() {
        if (!isEdit && !isAcceptedRules) {
            View view = getLayoutInflater().inflate(R.layout.dialog_content, null);
            view.findViewById(R.id.txtContent);

            CheckBox checkBox = view.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(view1 -> {
                isAcceptedRules = checkBox.isChecked();

                if (isAcceptedRules)
                    submitUser();
            });

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);

            AlertDialog mAlertDialog = alertDialogBuilder.create();
            mAlertDialog.setCanceledOnTouchOutside(true);
            mAlertDialog.show();

            return;
        }

//        int ii = 0;
//        if (ii == 0) return;

        String strName = edtName.getText().toString();
        String strUsername = edtUsername.getText().toString();
        String strAddress = edtAddress.getText().toString();
        String strPostalCode = edtPostalCode.getText().toString();
        String strShabaCode = edtShabaCode.getText().toString();

        if (strName.length() < 3)
            Toast.makeText(RegisterActivity.this, "نام وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strUsername.length() < 3)
            Toast.makeText(RegisterActivity.this, "نام کاربری وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strAddress.length() < 10)
            Toast.makeText(RegisterActivity.this, "آدرس وارد شده کوتاه است", Toast.LENGTH_SHORT).show();
        else if (strPostalCode.length() > 0 && strPostalCode.length() < 10)
            Toast.makeText(RegisterActivity.this, "کد پستی وارد شده صحیح نیست!", Toast.LENGTH_SHORT).show();
        else if (strShabaCode.length() > 0 && strShabaCode.length() < 24)
            Toast.makeText(RegisterActivity.this, "کد شبا وارد شده صحیح نیست!", Toast.LENGTH_SHORT).show();
        else {
            RunnableParam runnableParam;
            String requests[];
            if (isEdit) {
                runnableParam = o -> {
                    if (o != null) {
                        int errorCode = (int) o[0];
                        if(errorCode == 0) {
                            Toast.makeText(RegisterActivity.this, "اطلاعات شما با موفقیت بروزرسانی شد.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "اطلاعات وارد شده نامعبتر است.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "خطایی در ثبت اطلاعات رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                    }

                    setProgressingEnabled(false);
                };

                requests = new String[]{"edit-account", Configuration.getUsername(this), strName, strUsername, phone, String.valueOf(selectedCityId), strAddress, strPostalCode, strShabaCode};
            } else {
                runnableParam = o -> {
                    if (o != null) {
                        int errorCode = (int) o[0];
                        if(errorCode == 0) {
                            Configuration.setUsername(RegisterActivity.this, strUsername);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else if(errorCode == 5) {
                            Toast.makeText(RegisterActivity.this, "متاسفانه این نام کاربری قبلا وجود دارد", Toast.LENGTH_SHORT).show();
                        } else if(errorCode == 4) {
                            Toast.makeText(RegisterActivity.this, "اطلاعات وارد شده نامعبتر است.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "خطایی در ثبت اطلاعات رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                    }

                    setProgressingEnabled(false);
                };

                requests = new String[]{"create-account", strName, strUsername, phone, String.valueOf(selectedCityId), strAddress, strPostalCode, strShabaCode};

            }


            if(selectedImage != null) {
                ApiClient.executeCommandByImageUpload(selectedImage, requests, runnableParam);
            } else {
                ApiClient.executeCommand(requests, runnableParam);
            }

            setProgressingEnabled(true);
        }
    }

    private void setProgressingEnabled(boolean enabled) {
        progressBar.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        btnSubmit.setEnabled(!enabled);
    }
}
