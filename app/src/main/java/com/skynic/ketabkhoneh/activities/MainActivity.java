package com.skynic.ketabkhoneh.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.fragments.AddProductFragment;
import com.skynic.ketabkhoneh.fragments.CartFragment;
import com.skynic.ketabkhoneh.fragments.CategoriesFragment;
import com.skynic.ketabkhoneh.fragments.HomeFragment;
import com.skynic.ketabkhoneh.fragments.ProductFragment;
import com.skynic.ketabkhoneh.fragments.SearchFragment;
import com.skynic.ketabkhoneh.fragments.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment searchFragment;
    private Fragment addFragment;
    private Fragment cartFragment;
    private Fragment categoriesFragment;

    private static Runnable onPermissionGrantedRunnable;

    private int lastSelectedMenu = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            searchFragment = new SearchFragment();
            addFragment = new AddProductFragment();
            cartFragment = new CartFragment();
            categoriesFragment = new CategoriesFragment();
        }

        initView();
    }

    public static boolean checkForPermission(Activity activity, Runnable runnable) {
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

    private void initView() {
        BottomNavigationView navigationView = findViewById(R.id.navBottom);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    showFragment(homeFragment, item.getOrder());
                    return true;
                case R.id.action_search:
                    showFragment(searchFragment, item.getOrder());
                    return true;
                case R.id.action_add:
                    showFragment(addFragment, item.getOrder());
                    return true;
                case R.id.action_cart:
                    showFragment(cartFragment, item.getOrder());
                    return true;
                case R.id.action_categories:
                    showFragment(categoriesFragment, item.getOrder());
                    return true;
            }

            return false;
        });

        showFragment(homeFragment, 1);
    }

    private void showFragment(Fragment fragment, int menuIndex) {
        int count = getFragmentManager().getBackStackEntryCount();
        while (count-- > 0)
            getFragmentManager().popBackStack();

        if(menuIndex > lastSelectedMenu)
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out_left, R.animator.slide_in_right).replace(R.id.frmFragment, fragment).commit();
        else
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right).replace(R.id.frmFragment, fragment).commit();

        lastSelectedMenu = menuIndex;
    }

    public static void showFragment(Activity activity, Fragment fragment) {
        activity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up, R.animator.slide_out_up, R.animator.slide_out_up)
                .add(R.id.frmFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
