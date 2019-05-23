package com.example.loading.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loading.Loading;
import com.example.loading.R;
import com.example.loading.StatusView;

public class BlankFragment extends Fragment {

    StatusView mStatusView;
    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        mHandler = new Handler(Looper.getMainLooper());
        mStatusView = Loading.beginBuildStatusView(view.getContext())
                .warp(view)
                .addErrorView(R.layout.error_view)
                .create();
        mStatusView.showLoading();
        mHandler.postDelayed(() -> mStatusView.showError(), 3000);//模拟网络加载
        return mStatusView.getWrappedView();
    }

}
