package com.skynic.ketabkhoneh.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.Utils;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.User;
import com.skynic.ketabkhoneh.view.PopupListView;

public class RegisterActivity extends AppCompatActivity {
    private boolean isEdit = false;

    private int selectedCityId = 0;
    private String phone;
    private Bitmap selectedImage = null;
    private Uri imageUri;

    private Runnable onPermissionGrantedRunnable;

    private Button btnSubmit;
    private Button btnLogout;
    private ProgressBar progressBar;
    private ImageView imgProfile;
    private EditText edtName;
    private EditText edtUsername;
    private EditText edtAddress;
    private EditText edtPostalCode;
    private EditText edtShabaCode;
    private TextView txtCity;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();

        phone = getIntent().getStringExtra("phone");
        if (phone == null) {
            getUserInformation();
            isEdit = true;
            txtTitle.setText("ویرایش حساب کاربری");
            btnLogout = findViewById(R.id.btnLogout);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogout.setOnClickListener(view -> {
                Configuration.userLogout(RegisterActivity.this);

                startActivity(new Intent(RegisterActivity.this, SplashActivity.class));
                finish();
            });

        }
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
        txtTitle = findViewById(R.id.txtTitle);

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
        PopupListView popupListView = new PopupListView(this, "انتخاب عکس");

        Runnable cameraRunnable = () -> {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 0);
        };

        Runnable galleryRunnable = () -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);
        };

        popupListView.addItem("استفاده از دوربین", ()->{
            checkForPermission(this, cameraRunnable);
        });

        popupListView.addItem("از عکس های موجود", ()->{
            checkForPermission(this, galleryRunnable);
        });

        popupListView.show();
    }

    private boolean checkForPermission(Activity activity, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else {
                runnable.run();
                return true;
            }
        }

        onPermissionGrantedRunnable = runnable;

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.onPermissionGrantedRunnable.run();
        }
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            Bitmap bitmap;
            try {
                if(requestCode == 0)
                    bitmap = rotateImage(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri), 90);
                else {
                    Uri uri = imageReturnedIntent.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                }
                bitmap = Utils.scaleDown(bitmap, 600, true);
                bitmap = Utils.cropToSquare(bitmap);
                bitmap = Utils.scaleDown(bitmap, 600, true);
                bitmap = Utils.cropToSquare(bitmap);
                imgProfile.setImageBitmap(bitmap);

                selectedImage = bitmap;
            } catch (Exception e) {
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
                            Configuration.setUsername(RegisterActivity.this, strUsername);
                            Toast.makeText(RegisterActivity.this, "اطلاعات شما با موفقیت بروزرسانی شد.", Toast.LENGTH_SHORT).show();
                            finish();
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
