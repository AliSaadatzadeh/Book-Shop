package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.activities.MainActivity;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.activities.RegisterActivity;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Book;
import ir.skynic.bookshop.model.User;
import ir.skynic.bookshop.view.BookView;
import ir.skynic.bookshop.view.PopupListView;
import ir.skynic.bookshop.view.UserView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class UserProfileFragment extends Fragment {

    private View mView;
    private User model;

    private TextView txtUsername;
    private TextView txtName;
    private TextView txtFollowing;
    private TextView txtFollower;
    private TextView txtBooksCount;
    private ImageView imgUser;
    private ProgressBar progressBar;
    private CheckBox chkFollow;
    private FollowingFragment followingFragment;
    private View btnEditProfile;

    enum BookType{READY, SOLD, BOUGHT}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_profile, null);
        followingFragment = new FollowingFragment();
        followingFragment.setUsername(model.getUserName());

        initUi();
        getUser();
        getBooks(BookType.READY);

        return mView;
    }

    void initUi() {
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        mView.findViewById(R.id.lnrFollower).setOnClickListener(view -> {
            followingFragment.setFollowingMode(false);
            MainActivity.showFragment(getActivity(), followingFragment);
        });

        mView.findViewById(R.id.lnrFollowing).setOnClickListener(view -> {
            followingFragment.setFollowingMode(true);
            MainActivity.showFragment(getActivity(), followingFragment);
        });

        txtUsername = mView.findViewById(R.id.txtUsername);
        txtName = mView.findViewById(R.id.txtName);
        txtFollowing = mView.findViewById(R.id.txtFollowing);
        txtFollower = mView.findViewById(R.id.txtFollower);
        txtBooksCount = mView.findViewById(R.id.txtBooksCount);
        imgUser = mView.findViewById(R.id.imgUser);
        progressBar = mView.findViewById(R.id.progressBar);

        txtUsername.setText("@" + model.getUserName());
        txtName.setText(model.getName());

        TabLayout tabLayout = mView.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    getBooks(BookType.BOUGHT);
                } else if (tab.getPosition() == 1) {
                    getBooks(BookType.SOLD);
                } else {
                    getBooks(BookType.READY);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        chkFollow = mView.findViewById(R.id.chkFollow);
        btnEditProfile = mView.findViewById(R.id.btnEditProfile);

        if(model.getUserName().equals(Configuration.getUsername())) {
            chkFollow.setVisibility(View.GONE);
            btnEditProfile.setVisibility(VISIBLE);
        }

        chkFollow.setOnCheckedChangeListener(chkFollowOnCheckedChangeListener);

        btnEditProfile.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        });
    }

    private CompoundButton.OnCheckedChangeListener chkFollowOnCheckedChangeListener = (compoundButton, b) -> {
        String followMode = b ? "add-following" : "remove-following";
        chkFollow.setText(b ? "دنبال شده" : "دنبال کنید");

        String requests[] = {followMode, Configuration.getUsername(), model.getUserName()};
        ApiClient.executeCommand(requests, o -> {
            if (o != null && ((int) o[0]) == 0) {
                int followerCount = Integer.valueOf(txtFollower.getText().toString());
                followerCount += b ? 1 : -1;
                txtFollower.setText(String.valueOf(followerCount));

            } else {
                Toast.makeText(getActivity(), "خطایی رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    };

    void getUser() {
        progressBar.setVisibility(View.VISIBLE);

        String userRequest[] = {"get-user", Configuration.getUsername(getActivity()), model.getUserName()};
        ApiClient.getModel(userRequest, "user", User.class, o -> {
            if(o != null) {
                model = (User)((List) o[1]).get(0);
                showInformation();

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(INVISIBLE);
        });
    }

    private void showInformation() {

        txtFollowing.setText(String.valueOf(model.getFollowingCount()));
        txtFollower.setText(String.valueOf(model.getFollowerCount()));
        txtBooksCount.setText(String.valueOf(model.getBookCount()));
        if(model.isFollowing() == 1) {
            chkFollow.setOnCheckedChangeListener(null);
            chkFollow.setChecked(true);
            chkFollow.setOnCheckedChangeListener(chkFollowOnCheckedChangeListener);
            chkFollow.setText("دنبال شده");
        }

        new Thread(() -> {
            try {
                Bitmap bitmap = Utils.getImageFromUrl(model.getImageLink());
                Utils.runOnMainThread(() -> imgUser.setImageBitmap(bitmap));
            } catch (Exception ignored) {}
        }).start();
    }

    public void setModel(User model) {
        this.model = model;
    }

    public void getBooks(BookType bookType) {

        String strBookType = "1";
        if(bookType == BookType.READY)
            strBookType = "2";
        else if(bookType == BookType.BOUGHT)
            strBookType = "3";

        String request[] = {"get-user-book", Configuration.getUsername(getActivity()), strBookType};
        ApiClient.getModel(request, "book", Book.class, o -> {
            if(o != null) {
                List<Book> bookList = (List) o[1];

                ViewGroup productContainer = mView.findViewById(R.id.productContainer);
                productContainer.removeAllViews();

                for (Book book : bookList) {
                    BookView bookView = new BookView(getActivity(), BookView.ViewSize.LARGE, book);

                    if(bookType == BookType.READY) {
                        initBookMenuButton(bookView, book);
                    }

                    productContainer.addView(bookView);
                }

            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initBookMenuButton(BookView bookView, Book book) {
        bookView.setMenuButtonClickListerner(view -> {
            PopupListView popupListView = new PopupListView(getActivity(), "انتخاب کنید");

            popupListView.addItem("ویرایش", () -> {
                AddProductFragment addProductFragment = new AddProductFragment();
                //addProductFragment.setProductId(book.getId());
                MainActivity.showFragment(getActivity(), addProductFragment);
            });

            popupListView.addItem("حذف", () -> {
                String request1[] = {"remove-book", Configuration.getUsername(getActivity()), String.valueOf(book.getId())};
                ApiClient.executeCommand(request1, o -> {
                    if (o != null) {
                        int errorCode = (int) o[0];
                        if(errorCode == 0) {
                            bookView.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getActivity(), "خطایی داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "خطایی داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            popupListView.show();
        });
    }
}
