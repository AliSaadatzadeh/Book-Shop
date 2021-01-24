package com.skynic.ketabkhoneh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skynic.ketabkhoneh.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btnBack).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.btnAboutUs).setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, ContentActivity.class).putExtra("CONTENT", "درباره ما...").putExtra("TITLE", "درباره ما"));
        });

        findViewById(R.id.btnConditions).setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, ContentActivity.class).putExtra("CONTENT", "قوانین ما...").putExtra("TITLE", "قوانین و شرایط استفاده"));
        });

    }
}
