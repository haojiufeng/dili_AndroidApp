package com.diligroup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.diligroup.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * //首页刷新
 * Created by hjf on 2016/8/25.
 */
public class CustomerPtrHeader extends FrameLayout implements PtrUIHandler {
    private ImageView webView;
//    private ImageView customer_progress;
    ListViewLoadListener listener;
    private Context mContext;
    private WebSettings settings;

    @SuppressLint("SetJavaScriptEnabled")
    public CustomerPtrHeader(Context context, ListViewLoadListener listener) {
        super(context);
        this.listener=listener;
        this.mContext=context;
        initView();
    }

    private void initView() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.customer_header, this);
        webView = (ImageView) header.findViewById(R.id.custom_progress);
        webView.setBackgroundColor(Color.TRANSPARENT);
    }
    //复位
    @Override
    public void onUIReset(PtrFrameLayout frame) {
    //当内容视图已达到顶部和刷新已完成时，视图将被重置。
    }
    //刷新准备
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }
    //刷新开始
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        if(webView.getBackground() instanceof AnimationDrawable){
            ((AnimationDrawable)webView.getBackground()).start();
        }
        listener.refreshBegin(frame);
    }
    //刷新完成
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        if(webView.getBackground() instanceof AnimationDrawable) {
            ((AnimationDrawable) webView.getBackground()).stop();
        }
        listener.refreshComplete(frame);
    }
    //位置变化
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
    public interface  ListViewLoadListener{
     void   refreshBegin(PtrFrameLayout frame);
        void refreshComplete(PtrFrameLayout frame);
    }
}
