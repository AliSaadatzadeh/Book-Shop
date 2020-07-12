package ir.skynic.bookshop.activities;

import android.app.Activity;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ir.skynic.bookshop.R;
import ir.skynic.bookshop.fragments.AddProductFragment;
import ir.skynic.bookshop.fragments.CartFragment;
import ir.skynic.bookshop.fragments.CategoriesFragment;
import ir.skynic.bookshop.fragments.HomeFragment;
import ir.skynic.bookshop.fragments.ProductFragment;
import ir.skynic.bookshop.fragments.SearchFragment;
import ir.skynic.bookshop.fragments.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment searchFragment;
    private Fragment addFragment;
    private Fragment cartFragment;
    private Fragment categoriesFragment;

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

    public void showProduct(View v) {
        showFragment(this, new ProductFragment());
    }

    public void showUser(View v) {
        showFragment(this, new UserProfileFragment());
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
