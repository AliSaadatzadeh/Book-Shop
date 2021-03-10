package com.skynic.ketabkhoneh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.skynic.ketabkhoneh.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
    }

    private void initUi() {
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            String phone = ((EditText)findViewById(R.id.edtPhone)).getText().toString().trim();

            if(phone.length() != 11 || !phone.startsWith("09")) {
                Toast.makeText(LoginActivity.this, "شماره تلفن صحیح نیست!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, SmsConfirmActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        });
    }


}
