package com.diligroup.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.diligroup.net.NetChangeObserver;
import com.diligroup.net.NetStateReceiver;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.NetUtils;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by Kevin on 2016/6/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements RequestManager.ResultCallback {
    //    @Bind(R.id.comm_title)
//    public TextView tv_title;
//    Context mContext;
//    @Bind(R.id.iv_back)
//    public ImageView iv_back;
//    @Bind(R.id.iv_share)
//    protected ImageView ivShare;
//    @Bind(R.id.title_root)
//    protected View title_root;
//    public TextView title_infos;
    protected abstract int getContentViewLayoutID();
//    protected  abstract boolean isShowBackIcon

    /**
     * network connected
     */
    protected abstract void onNetworkConnected(NetUtils.NetType type);

    /**
     * network disconnected
     */
    protected abstract void onNetworkDisConnected();

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            initViewAndData();
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        AppManager.getAppManager().addActivity(this);
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);
        NetStateReceiver.registerNetworkStateReceiver(this);
        NetStateReceiver.checkNetworkState(this);

    }

    protected abstract void initViewAndData();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
//        tv_title = ButterKnife.findById(this, R.id.comm_title);
//        title_infos = ButterKnife.findById(this, R.id.tv_title_info);
//        iv_back = ButterKnife.findById(this, R.id.iv_back);
//        ivShare = ButterKnife.findById(this, R.id.iv_share);
//        title_root = ButterKnife.findById(this, R.id.title_root);
//        if (tv_title != null) {
//            setTitle();
//        }
//        if (title_infos != null) {
//            setTitle();
//        }
//        if (iv_back != null) {
//            isShowBack(false);
//        }
    }


//    public void setTitle() {
////        tv_title.setText("");
//    }
//    public void isShowBack(Boolean isShow) {
//        if (isShow) {
//            iv_back.setVisibility(View.VISIBLE);
//            iv_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = DiliApplication.getRefWatcher(getApplicationContext());
        refWatcher.watch(this);
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        AppManager.getAppManager().finishActivity(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
    }

    /**
     * @Description:关闭软键盘
     */
    public void closeSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * startActivity then
     * finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
