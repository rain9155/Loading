package com.example.loading2.app;

import android.app.Application;

import com.example.loading.Loading;
import com.example.loading2.R;

/**
 * Created by 陈健宇 at 2019/5/22
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //在Application这里一键配置所以视图
        Loading.beginBuildCommit()
                .addEmptyView(R.layout.empty_view)
                .addLoadingView(R.layout.loading_view)
                .addErrorView(R.layout.reload_view)
                .addCustomView(R.layout.custom_view)
                .commit();
    }
}
