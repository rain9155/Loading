package com.example.loading.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.loading.Loading;
import com.example.loading.R;
import com.example.loading.StatusView;

public class ActionBarActivity extends AppCompatActivity {

    StatusView mStatusView;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        mHandler = new Handler(Looper.getMainLooper());

        mStatusView = Loading.beginBuildStatusView(this)
                .warp(this)
                .withReload(() -> {
                    mStatusView.showLoading();
                    mHandler.postDelayed(() -> mStatusView.showSuccess(), 3000);
                }, R.id.iv_reload)
                .create();

        mStatusView.showLoading();
        mHandler.postDelayed(() -> mStatusView.showError(), 3000);

    }
}
