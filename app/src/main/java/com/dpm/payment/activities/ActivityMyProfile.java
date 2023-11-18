package com.dpm.payment.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.payment.R;


public class ActivityMyProfile extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView toolbar_iv_home;
    TextView toolbar_tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        initializeViews();
        initializeListener();
    }

    private void initializeListener() {
        toolbar_iv_home.setOnClickListener(this);

    }

    private void initializeViews() {

        toolbar_iv_home = findViewById(R.id.toolbar_iv_home);
        toolbar_tv_header = findViewById(R.id.toolbar_tv_header);
        toolbar_tv_header.setText(R.string.my_profile);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_iv_home:
                onBackPressed();
                break;

        }
    }
}
