package ir.skynic.bookshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.RunnableParam;

public class SplashActivity extends AppCompatActivity {

    private View lnrErrorContainer;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    fetchInformation();
                });

                t.cancel();
            }
        }, 1000);

        lnrErrorContainer = findViewById(R.id.lnrErrorContainer);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnRetry).setOnClickListener(view -> {
            fetchInformation();
        });
    }

    private void fetchInformation() {
        lnrErrorContainer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        fetchCategories();
    }

    private void fetchCategories() {
        Configuration.fetchCategories(o -> {
            boolean result = (boolean) o[0];
            if(result) {
                fetchCities();
            } else {
                showErrorIndicators();
            }
        });
    }

    private void fetchCities() {
        Configuration.fetchCities(o -> {
            boolean result = (boolean) o[0];
            if (result) {
                startNextActivity();
            } else {
                showErrorIndicators();
            }
        });
    }

    private void showErrorIndicators() {
        lnrErrorContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
    
    private void startNextActivity() {
        if(Configuration.getUsername(this) != null)
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        else
            startActivity(new Intent(SplashActivity.this, MainActivity.class));

        finish();
    }
}
