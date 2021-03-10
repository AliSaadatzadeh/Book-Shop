package com.skynic.ketabkhoneh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.skynic.ketabkhoneh.Configuration;
import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.RunnableParam;
import com.skynic.ketabkhoneh.api.ApiClient;
import com.skynic.ketabkhoneh.model.User;

public class SmsConfirmActivity extends AppCompatActivity {

    private int elapsedTime = 60;
    private String phone;
    private EditText edtConfirmCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_confirm);

        phone = getIntent().getStringExtra("phone");
        requestConfirmCode(phone);
    }

    private void initUi() {
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            validateConfirmCode();
        });

        findViewById(R.id.btnResend).setOnClickListener(view -> {
            requestConfirmCode(phone);
            view.setVisibility(View.INVISIBLE);
        });

        edtConfirmCode = findViewById(R.id.edtConfirmCode);
    }

    private void validateConfirmCode() {

        String validationCode = edtConfirmCode.getText().toString().trim();

        ApiClient.getModel(new String[]{"login", phone, validationCode}, "user", User.class, o -> {
            if(o != null) {
                int error = (int)o[0];
                if(error == 0) {
                    List<User> users = (List) o[1];
                    if(users.size() > 0) {
                        User user = users.get(0);//TODO
                        Configuration.setUsername(SmsConfirmActivity.this, user.getUserName());
                        startActivity(new Intent(SmsConfirmActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SmsConfirmActivity.this, RegisterActivity.class).putExtra("phone", phone));
                    }

                    finish();
                } else if(error == 7) {
                    Toast.makeText(SmsConfirmActivity.this, "کد فعال سازی صحیح نیست. لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SmsConfirmActivity.this, "خطایی نامشخص رخ داده است. لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestConfirmCode(String phone) {

        ApiClient.executeCommand(new String[]{"request-confirm-code", phone}, o -> {
            if(o != null && ((int)o[0]) == 0) {
                initUi();
                initTimer();
            } else {
                Toast.makeText(SmsConfirmActivity.this, "خطایی در دریافت کد فعالسازی رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(elapsedTime > 0) {
                        elapsedTime--;
                        ((TextView) findViewById(R.id.txtTimer)).setText("00:" + String.format("%02d", elapsedTime));
                    } else {
                        timer.cancel();
                        elapsedTime = 60;

                        findViewById(R.id.btnResend).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.txtTimer)).setText("01:00");
                    }
                });
            }
        },1000, 1000);
    }
}
