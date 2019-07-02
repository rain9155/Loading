package com.example.loading;

import android.app.Activity;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * 一个控制视图切换逻辑的帮助类
 * Created by 陈健宇 at 2019/4/26
 */
public class Loading {

    private final SparseIntArray mStatusViews = new SparseIntArray(4){{
        put(Status.LOADING.ordinal(), -1);
        put(Status.ERROR.ordinal(), -1);
        put(Status.EMPTY.ordinal(), -1);
        put(Status.CUSTOM.ordinal(), -1);
    }};
    private static volatile Loading sintance = null;

    private Loading(){}

    private static Loading getInstance(){
        if(sintance == null){
            sintance = new Loading();
        }
        return sintance;
    }

    public static Builder beginBuildStatusView(Context context){
        return new Builder(context);
    }

    public static Builder beginBuildCommit(){
        return new Builder();
    }


    public static class Builder{

        private Loading mLoading;
        private View mWarppedView;
        private View mLoadingView;
        private View mErrorView;
        private View mEmptyView;
        private View mCustomView;
        private View.OnClickListener mReloadClick;
        private int mRetryId = -1;
        private int mLoadingViewId = -1;
        private int mErrorViewId = -1;
        private int mEmptyViewId = -1;
        private int mCustomViewId = -1;
        private Context mContext;
        private LayoutInflater mInflater;

        public Builder(){
            mLoading = Loading.getInstance();
        }

        public Builder(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mLoading = Loading.getInstance();
        }

        /**
         * 添加一个加载中的视图
         * @param id 视图id
         */
        public Builder addLoadingView(int id){
            mLoadingViewId = id;
            return this;
        }

        /**
         * 添加一个加载错误的视图
         * @param id 视图id
         */
        public Builder addErrorView(int id){
            mErrorViewId = id;
            return this;
        }

        /**
         * 添加一个空数据视图
         * @param id 视图id
         */
        public Builder addEmptyView(int id){
            mEmptyViewId = id;
            return this;
        }

        /**
         * 添加一个自定义的视图
         * @param id 视图id
         */
        public Builder addCustomView(int id){
            mCustomViewId = id;
            return this;
        }

        /**
         * 取出Activity中的包裹着contentView的ViewGroup
         */
        public Builder warpActivity(Activity activity){
            mWarppedView = activity.findViewById(android.R.id.content);
            return this;
        }

        /**
         * 新建一个ViewGroup包裹着传进来的View
         * @param view 要被包裹的View
         */
        public Builder warpView(View view){
            //新建一个ViewGroup
            FrameLayout wrapper = new FrameLayout(view.getContext());
            ViewGroup.LayoutParams viewLp = view.getLayoutParams();
            if(viewLp != null) {
                wrapper.setLayoutParams(viewLp);
            }
            if(view.getParent() != null){
                ViewGroup parent = ((ViewGroup)view.getParent());
                int index = parent.indexOfChild(view);
                //从view的父布局移除View
                parent.removeView(view);
                //把ViewGroup添加到view的父布局中，并使用view原来的布局参数
                parent.addView(wrapper, index);
            }
            //把ViewGroup包裹着View
            FrameLayout.LayoutParams wrapperLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            wrapper.addView(view, wrapperLp);
            mWarppedView = wrapper;
            return this;
        }

        /**
         * 重新加载任务
         * @param task 触发重新加载的逻辑的任务
         * @param retryId 因为触发重新加载一把是错误布局视图显示的时候，所以retryId代表errorView中触发重新加载的逻辑的控件的id
         */
        public Builder withReload(final Runnable task, int retryId){
            mReloadClick = v -> task.run();
            mRetryId = retryId;
            return this;
        }

        /**
         * 暂存各种视图id到缓存
         */
        public void commit(){
            mLoading.mStatusViews.put(Status.LOADING.ordinal(), mLoadingViewId);
            mLoading.mStatusViews.put(Status.ERROR.ordinal(), mErrorViewId);
            mLoading.mStatusViews.put(Status.EMPTY.ordinal(), mEmptyViewId);
            mLoading.mStatusViews.put(Status.CUSTOM.ordinal(), mCustomViewId);
        }

        /**
         * 创建一个StatusView，每个Activity或Fragment都应持有一个StatusView，用来控制视图切换逻辑
         * @return 创建好的StatusView实例
         */
        public StatusView create(){
            verify();
            return budiler(new StatusView.Builder(mContext)).create();
        }

        private StatusView.Builder budiler(StatusView.Builder builder) {
            builder.setContentView(mWarppedView);

            if((mLoadingView = getView(mLoadingViewId, Status.LOADING)) != null){
                removeParent(mLoadingView.getParent(), mLoadingView);
                builder.setLoadingView(mLoadingView);
            }

            if((mErrorView = getView(mErrorViewId, Status.ERROR)) != null){
                if(mReloadClick != null && mRetryId != -1){
                    View reloadView = mErrorView.findViewById(mRetryId);
                    if(reloadView != null)
                        reloadView.setOnClickListener(mReloadClick);
                    builder.setReloadClick(mReloadClick);
                }
                removeParent(mErrorView.getParent(), mErrorView);
                builder.setErrorView(mErrorView);
            }

            if((mEmptyView = getView(mEmptyViewId, Status.EMPTY)) != null){
                removeParent(mEmptyView.getParent(), mEmptyView);
                builder.setEmptyView(mEmptyView);
            }

            if((mCustomView = getView(mCustomViewId, Status.CUSTOM)) != null){
                removeParent(mCustomView.getParent(), mCustomView);
                builder.setCustomView(mCustomView);
            }

            return builder;
        }

        private void verify() {
            if(mWarppedView == null) throw new NullPointerException("must warpActivity Activity or Fragment or View");
            if(mInflater == null) throw new NullPointerException("can't call Loading.beginBuildCommit() before create()");
        }

        private void removeParent(ViewParent parent, View removeView) {
            if(parent != null)
                ((ViewGroup)parent).removeView(removeView);
        }

        private View getView(int builderStatus, Status cacheStatus){
            if(builderStatus != -1) return mInflater.inflate(builderStatus, null);
            int cacheId = mLoading.mStatusViews.get(cacheStatus.ordinal());
            if(cacheId != -1) return mInflater.inflate(cacheId, null);
            return null;
        }
    }

}
