package com.skynic.ketabkhoneh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skynic.ketabkhoneh.R;


public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        TextView textView = findViewById(R.id.txtContent);
        TextView titleText = findViewById(R.id.txtTitle);

        titleText.setText(getIntent().getExtras().getString("TITLE"));
        textView.setText(getIntent().getExtras().getString("CONTENT")) ;

        findViewById(R.id.btnBack).setOnClickListener(view -> {
            finish();
        });
    }
}
