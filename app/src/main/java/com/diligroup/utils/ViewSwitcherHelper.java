package com.diligroup.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.diligroup.R;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.RequestManager;
import okhttp3.Request;

/**
 * Created by cwj on 2016/4/6.
 * 网络请求loading框，网络错误信息，其他信息
 */
public class ViewSwitcherHelper {

    private Context context;
    private View targetView;

    ViewSwitcher switcher;

    View container;

    private ImageView loading;
    private LinearLayout loadingWrapper;
    private LinearLayout netWrapper;
    private TextView netRetry;
    private LinearLayout otherWrapper;
    private LinearLayout centerMessageWrapper;
    private TextView centerMessage;
    private AnimationDrawable animationDrawable;

    public ViewSwitcherHelper(Context context, View targetView) {

        this.context = context;
        this.targetView = targetView;
        initContainer();
        initSwitcher();
    }

    private void initSwitcher(){
        switcher = new ViewSwitcher(context);
        ViewSwitcher.LayoutParams params = new ViewSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switcher.setLayoutParams(params);
        ViewGroup parent = (ViewGroup) targetView.getParent();
        int index = 0;
        View tempView = targetView;
        if(parent!=null){
            index = parent.indexOfChild(targetView);
            parent.removeView(targetView);
        }

        switcher.addView(container, 0);
        switcher.addView(tempView, 1);
        switcher.setDisplayedChild(1);
        if(parent!=null){
            parent.addView(switcher,index);
        } else {
            ((Activity)context).setContentView(switcher);
        }


    }

    private void initContainer(){
        container = LayoutInflater.from(context).inflate(R.layout.exception_loading_content,null);

        loadingWrapper = (LinearLayout) container.findViewById(R.id.loading_wrapper);
        netWrapper = (LinearLayout) container.findViewById(R.id.net_wrapper);
        netRetry = (TextView) container.findViewById(R.id.net_retry);
        otherWrapper = (LinearLayout) container.findViewById(R.id.other_wrapper);
        centerMessage = (TextView) container.findViewById(R.id.notify_message);
        centerMessageWrapper = (LinearLayout) container.findViewById(R.id.center_notify_wrapper);
        loading = (ImageView) container.findViewById(R.id.iv_loading);
        animationDrawable = (AnimationDrawable) loading.getDrawable();

    }

    /**
     * 显示页面内容
     */
    public void showContent(){
        switcher.setDisplayedChild(1);
        if(animationDrawable.isRunning()){
            animationDrawable.stop();
        }
        otherWrapper.setVisibility(View.GONE);
        netWrapper.setVisibility(View.GONE);
        centerMessageWrapper.setVisibility(View.GONE);
    }

    /**
     * 显示加载框
     */
    public void showLoading(){
        if(switcher!=null && switcher.getDisplayedChild() !=0){
            switcher.setDisplayedChild(0);
        }
        otherWrapper.setVisibility(View.GONE);
        netWrapper.setVisibility(View.GONE);
        centerMessageWrapper.setVisibility(View.GONE);
        loadingWrapper.setVisibility(View.VISIBLE);

        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }

    }
    /**
     * 网络错误信息
     */
    public void showError(final Request request, final Action action, final RequestManager.ResultCallback callback){
        if(switcher!=null && switcher.getDisplayedChild() !=0){
            switcher.setDisplayedChild(0);
        }
        loadingWrapper.setVisibility(View.GONE);
        otherWrapper.setVisibility(View.GONE);
        centerMessageWrapper.setVisibility(View.GONE);
        netWrapper.setVisibility(View.VISIBLE);
        netWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                RequestManager.getInstance().deliveryResult(action, callback, request);
            }
        });
    }
    /**
     * 其他信息
     */
    public void showNotify(){
        if(switcher!=null && switcher.getDisplayedChild() !=0){
            switcher.setDisplayedChild(0);
        }
        loadingWrapper.setVisibility(View.GONE);
        netWrapper.setVisibility(View.GONE);
        otherWrapper.setVisibility(View.VISIBLE);
        centerMessageWrapper.setVisibility(View.GONE);
    }

    /**
     * 显示居中的提示信息
     * @param message
     */
    public void showNotify(String message){
        if(switcher!=null && switcher.getDisplayedChild() !=0){
            switcher.setDisplayedChild(0);
        }
        loadingWrapper.setVisibility(View.GONE);
        netWrapper.setVisibility(View.GONE);
        otherWrapper.setVisibility(View.GONE);
        centerMessageWrapper.setVisibility(View.VISIBLE);
        centerMessage.setText(message);
    }
}

