package com.example.loading.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.loading.Loading;
import com.example.loading.R;
import com.example.loading.StatusView;

public class SingleViewActivity extends AppCompatActivity {

    StatusView mStatusView;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        mHandler = new Handler(Looper.getMainLooper());
        mStatusView = Loading.beginBuildStatusView(this)
                .warp(findViewById(R.id.iv_view))
                .addEmptyView(R.layout.empty_view)
                .create();
        mStatusView.showLoading();
        mHandler.postDelayed(() -> mStatusView.showSuccess(), 3000);//模拟网络加载

    }
}
