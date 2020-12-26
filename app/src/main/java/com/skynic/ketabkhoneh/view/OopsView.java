package com.skynic.ketabkhoneh.view;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skynic.ketabkhoneh.R;
import com.skynic.ketabkhoneh.activities.MainActivity;
import com.skynic.ketabkhoneh.fragments.SearchFragment;

public class OopsView extends LinearLayout {

    public OopsView(Context context) {
        super(context);
        inflate(context, R.layout.inflate_oops, this);
    }
}
