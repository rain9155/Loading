package com.example.loading;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 一种可以根据状态切换布局视图的View
 * Created by 陈健宇 at 2019/4/25
 */
public class StatusView extends RelativeLayout {

    private static final LayoutParams DEFAULT_LP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    protected View loadingView;
    protected View contentView;
    protected View errorView;
    protected View emptyView;
    protected View customView;
    protected OnClickListener reloadClick;
    protected View wrappedView;
    private Status mCurrentStatus = Status.SUCCESS;

    public StatusView(Context context) {
        super(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showSuccess(){
        showView(Status.SUCCESS);
    }

    public void showLoading(){
        showView(Status.LOADING);
    }

    public void showError(){
        showView(Status.ERROR);
    }

    public void showEmpty(){
        showView(Status.EMPTY);
    }

    public void showCustom(){
        showView(Status.CUSTOM);
    }

    public View getWrappedView() {
        return wrappedView;
    }

    public Status getCurrentStatus(){
        return mCurrentStatus;
    }

    /**
     * 根据status显示View视图
     * @param status 将要显示的View视图的status
     */
    private void showView(Status status){
        if(Looper.myLooper() == Looper.getMainLooper()){
            changeViewByStatus(status);
        }else {
            post(() -> changeViewByStatus(status));
        }
    }

    /**
     * 根据status改变View视图
     * @param status 将要显示的View视图的status
     */
    private void changeViewByStatus(Status status){
        if(mCurrentStatus == status) return;
        if(loadingView != null) if(status == Status.LOADING ) loadingView.setVisibility(VISIBLE);  else loadingView.setVisibility(GONE);
        if(errorView != null) if(status == Status.ERROR) errorView.setVisibility(VISIBLE); else errorView.setVisibility(GONE);
        if(emptyView != null) if(status == Status.EMPTY) emptyView.setVisibility(VISIBLE); else emptyView.setVisibility(GONE);
        if(customView != null) if(status == Status.CUSTOM) customView.setVisibility(VISIBLE); else customView.setVisibility(GONE);
        if(status == Status.SUCCESS) contentView.setVisibility(VISIBLE); else contentView.setVisibility(GONE);
        mCurrentStatus = status;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(reloadClick != null) reloadClick = null;
        clear(contentView, loadingView, emptyView, errorView);
    }

    /**
     * 把StatusView中的View全部移除
     * @param views View集合
     */
    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Builder{

        private StatusView mStatusView;

        public Builder(Context context){
            mStatusView = new StatusView(context);
        }

        /**
         * 把loadingView添加到StatusView中，
         */
        public Builder setLoadingView(View loadingView){
            mStatusView.loadingView = loadingView;
            mStatusView.loadingView.setVisibility(GONE);
            mStatusView.addView(loadingView, DEFAULT_LP);
            return this;
        }

        /**
         * 把errorView添加到StatusView中，
         */
        public Builder setErrorView(View errorView){
            mStatusView.errorView = errorView;
            mStatusView.errorView.setVisibility(GONE);
            mStatusView.addView(errorView, DEFAULT_LP);
            return this;
        }

        public Builder setReloadClick(OnClickListener click){
            mStatusView.reloadClick = click;
            return this;
        }

        /**
         * 把emptyView添加到StatusView中，
         */
        public Builder setEmptyView(View emptyView){
            mStatusView.emptyView = emptyView;
            mStatusView.emptyView.setVisibility(GONE);
            mStatusView.addView(emptyView, DEFAULT_LP);
            return this;
        }

        /**
         * 把customView添加到StatusView中，
         */
        public Builder setCustomView(View customView){
            mStatusView.customView = customView;
            mStatusView.customView.setVisibility(GONE);
            mStatusView.addView(customView, DEFAULT_LP);
            return this;
        }

        /**
         * 把contentView添加到StatusView中，并且把StatusView添加到contentView的父布局
         */
        public Builder setContentView(View warppedView){
            mStatusView.wrappedView = warppedView;
            //Loading中已经做了包装，这里一定是ViewGroup
            ViewGroup warpped  = (ViewGroup)warppedView;
            //从warpped(warrped是Farmelayout, 里面包裹着Activity或Fragment或View)中第一个View，是加载成功后显示的View
            View contentView = warpped.getChildAt(0);
            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
            //然后从warpped中移除contentView
            warpped.removeView(contentView);
            //把StatusView添加到warpped中去，替换掉contentView
            warpped.addView(mStatusView, lp);
            //然后把contentView设置给StatusView
            mStatusView.contentView = contentView;
            mStatusView.contentView.setVisibility(VISIBLE);
            mStatusView.addView(contentView, DEFAULT_LP);
            return this;
        }

        /**
         * 构造StatusView
         */
        public StatusView create(){
            return mStatusView;
        }

    }

}
