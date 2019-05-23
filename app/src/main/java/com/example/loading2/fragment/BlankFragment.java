package com.example.loading2.fragment;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loading.Loading;
import com.example.loading.StatusView;
import com.example.loading2.R;
import com.example.loading2.base.LazyFragment;

public class BlankFragment extends LazyFragment {

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
        return mStatusView.getWrappedView();
    }

    @Override
    protected void onLazyLoadData() {
        mStatusView.showLoading();
        mHandler.postDelayed(() -> mStatusView.showError(), 3000);//模拟网络加载
    }
}
