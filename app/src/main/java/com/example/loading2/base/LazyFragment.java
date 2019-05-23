package com.example.loading2.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by 陈健宇 at 2019/5/23
 */
public abstract class LazyFragment extends Fragment{

    private boolean isViewCreated = false;
    private boolean isLoadData = false;
    private boolean isFirstVisible = true;

    abstract protected void onLazyLoadData();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isViewCreated = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isFragmentVisible(this) && this.isAdded()){
            if (this.getParentFragment() == null || isFragmentVisible(this.getParentFragment())) {
                onLazyLoadData();
                isLoadData = true;
                if(isFirstVisible)
                    isFirstVisible = false;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isFragmentVisible(this) && !isLoadData && isViewCreated && this.isAdded()){
            onLazyLoadData();
            isLoadData = true;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden && !this.isResumed())
            return;
        if(!hidden && isFirstVisible && this.isAdded()){
            onLazyLoadData();
            isFirstVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        isLoadData = false;
        isFirstVisible = true;
    }

    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden() && fragment.getUserVisibleHint();
    }

}
