package com.diligroup.my.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseFragment;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.GetUserInfoFromServiceBean;
import com.diligroup.mode.UserSetBean;
import com.diligroup.my.activity.ServiceCenter;
import com.diligroup.my.activity.SettingActivity;
import com.diligroup.my.activity.UserInfoActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.DeviceUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UpLoadPhotoUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.view.CircleImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by Kevin on 2016/7/4.
 */
public class UserSetFragment extends BaseFragment implements View.OnClickListener, RequestManager.ResultCallback {

    @Bind(R.id.iv_user_header)
    CircleImageView iv_user_header;
    @Bind(R.id.tv_numb_phone)
    TextView tv_numb_phone;
    @Bind(R.id.bt_submit)
    Button bt_submit;
    @Bind(R.id.et_suggestions)
    EditText et_suggestions;//编辑框
    @Bind(R.id.text_counts)
    TextView text_counts;//提交意见的字数限制
    private Bundle savedState;
    private Bitmap bitmap;
    ;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (bitmap != null) {
                iv_user_header.setImageBitmap(bitmap);
            }
        }
    };

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_user;
    }

    @Override
    public void setViews() {
        Api.getUserInfo(this);
        tv_numb_phone.setText(TextUtils.isEmpty(UserManager.getInstance().getPhone()) ? UserManager.getInstance().getNickName() : UserManager.getInstance().getPhone());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        et_suggestions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 500) {
                    ToastUtil.showLong(getActivity(), "亲：超过字数限制了");
                    return;
                }
                int tempLength = editable.length();
                text_counts.setText(tempLength + "/500字");
            }
        });
    }

    @Override
    public void setListeners() {
        iv_user_header.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }


    @OnClick(R.id.rl_go_userinfo)
    public void jumpUserInfo() {
        GoActivity(UserInfoActivity.class);
    }

    @OnClick(R.id.rl_go_setting)
    public void jumpSetView() {

        GoActivity(SettingActivity.class);
    }

    @OnClick(R.id.rl_go_service)
    public void jumpService() {
        GoActivity(ServiceCenter.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_header:
                new UpLoadPhotoUtils(getActivity()).pickImage();
                break;
            case R.id.bt_submit:
                if (!TextUtils.isEmpty(et_suggestions.getText().toString().trim())) {
                    Api.appEvaluate(UserManager.getInstance().getUserId(), "", et_suggestions.getText().toString().trim(), DeviceUtils.getVersion(getActivity()), this);
                } else {
                    ToastUtil.showLong(getActivity(), "请输入对产品的意见，我们将会不断改进");
                    return;
                }
                break;
        }
    }

    public void chageHeadIcon(String url) {
        if (null != mRootView && mRootView.size() > 0) {
            LogUtils.i("homeActivity调用fragmeng中方法");
            Picasso.with(getActivity()).load(url).placeholder(R.mipmap.head_default).noFade().into(iv_user_header);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            View view = inflater.inflate(getLayoutXml(), null);
            mRootView = new ArrayList<View>();
            mRootView.add(view);
        }
        ButterKnife.bind(this, mRootView.get(0));
        setViews();
        setListeners();
        return mRootView.get(0);
    }

    int header_id;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("header_id", R.id.iv_user_header);
        outState.putString("text", "wowoow");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            header_id = savedInstanceState.getInt("header_id", 0);
            LogUtils.e(savedInstanceState.getString("text"));
        }
    }

    @Subscribe
    public void onEvent(UserSetBean bean) {
        if (!TextUtils.isEmpty(bean.getHeadUrl())) {
            chageHeadIcon(bean.getHeadUrl());
        }else{
            Picasso.with(getActivity()).load(R.mipmap.head_default).noFade().into(iv_user_header);
        }
        if (!TextUtils.isEmpty(bean.getPhoneNum())) {
            tv_numb_phone.setText(bean.getPhoneNum());
        }
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
//        if(!NetUtils.isMobileConnected(getActivity())){
//            ToastUtil.showLong(getActivity(),"网络不好，提交失败了");
//        }else{
        switch (action) {
            case APPEVALUATE:
                ToastUtil.showLong(getActivity(), "网络不好，提交失败了");
                break;
        }
//        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.APPEVALUATE && ((CommonBean) object).getCode().equals(Constant.RESULT_SUCESS)) {
            ToastUtil.showLong(getActivity(), "您的意见已经提交成功，我们将会不断的改进产品，感谢您的支持");
            et_suggestions.setText("");
        } else if (object != null && action == Action.GET_USER_INFO) {
            GetUserInfoFromServiceBean infoBean = (GetUserInfoFromServiceBean) object;
            if (infoBean.getCode().equals(Constant.RESULT_SUCESS)) {
                String headurl = infoBean.getUserDetail().getHeadPhotoAdd();
//                            .load("http://images.ypp2015.com/ypp/upload/img/Screenshot_2016-06-27-16-09-43-225.jpeg")
                if (!TextUtils.isEmpty(headurl)) {
                    Picasso.with(getActivity())
                            .load(headurl)
                            .placeholder(R.mipmap.head_default)
                            .noFade()
                            .into(iv_user_header);
                }
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            Api.getUserInfo(this);
        }
    }
}
