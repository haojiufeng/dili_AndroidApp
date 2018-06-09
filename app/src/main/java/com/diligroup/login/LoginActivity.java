package com.diligroup.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.bean.UserBeanFromService;
import com.diligroup.dialog.SwitchIpDialog;
import com.diligroup.home.HomeActivity;
import com.diligroup.mode.UserSetBean;
import com.diligroup.my.activity.ReportSex;
import com.diligroup.net.Api;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Urls;
import com.diligroup.utils.DigestUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.SharedPreferenceUtil;
import com.diligroup.utils.StringUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.alipaylogin.AuthResult;
import com.diligroup.utils.alipaylogin.OrderInfoUtil2_0;
import com.diligroup.view.TogglePasswordVisibilityEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 登录 Activity
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.input_username)
    EditText phoneNumber;
    @Bind(R.id.input_password)
    TogglePasswordVisibilityEditText et_password;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.switch_ip)
    Button switchIp;//切换ip地址
    boolean isFirst = true;
    UserBeanFromService userInfo;
    SharedPreferenceUtil spUtils;
    //    @Bind(R.id.bt_login)
//    Button bt_login;
    String phoneNum;
    String passdWord;
    @Bind(R.id.tv_notice_number)
    TextView tv_number;
    @Bind(R.id.tv_notice_password)
    TextView tv_psd;
    @Bind(R.id.login_qq)
    LinearLayout login_qq;
    @Bind(R.id.login_alipay)
    LinearLayout login_alipay;
    @Bind(R.id.login_wb)
    LinearLayout loginWb;
    @Bind(R.id.login_wx)
    LinearLayout loginWX;

    UMShareAPI mShareAPI;
    SHARE_MEDIA platform;
    private static final int SDK_AUTH_FLAG = 2;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016081601755137";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088221786995186";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "2016091125";

    private String openid;//第三方开放平台的openid
    private String userId;//自己服务器返回的用户id
    private String nickName;

    private String headImagUrl;
    private String sex;
    private String currentPlatFrom;

    //    @Bind(R.id.switch_ip)
    Spinner switchip_spinner;//切换ip按钮
    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMQyISfgimoFeGmDWz9YC7OomVa+58N22OfTJ4P+4xFc7/aqEzs8IuJjwqo+p+tsgNmcB+47oN4QCfi78Xhqe7mp4SK2O/S/s1WGP29DowK7AHH+tOVctJgMEQFogX0XrRIWrAuJcmG2C2Fo4diUmyk234VMhi4zfaO7CWZApZNrAgMBAAECgYBgKl4cCLBvlSzXMv53xvU9Y2d9oGdDZK6eut4EkdvEt/QayHRStYA3zUQuZDW0bGOfxh4RBIMuNVhd5elO54qqsiG9n5DwY2KCbZDCaG1KUG789c88UsfT+24Ni1PXSFxm1/exdQzsSEvzZptx6drobARN0h8gaAGz5KOKCyfvgQJBAP1dOdEflIw0hPltzMlymLi8LyHZRsCXEUeO1gE9UryGYRKyjK12HA/a6DeS64UnUz99Ei46reb3av7lz3wtjqsCQQDGPKZM7F5hrb1iFeylj5IGds7IU8XNlHqm8Rx/ZPp7gmCeGxlW7w5jSN673W4bHv3gpnAJ7Pbdua49kvSV1Q5BAkEAmIgqiaLIjIwFziBzXIf4N6dbfLZRKRsJlRoB7qcbi1IfWOFTXg6wID969BIoZnZhYOSMMHa1QUqNCL4T5r+KlwJBALunMe1jWzyv0LSG+IsIyzxfPwOXeYlP4oMhfs6BcjN0ia1hDa2jgkUt99pylAYMYltEco6SyGW/nVcgQ3OKSYECQQC1OFUG3eUB0u3M1VXscAEkd0FXsDnMBZ9EtUGFlVk8jXDafe4kz5Hg7/q8EivyJkbeXdeAuzCnEzO6uJxRDmrK";
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umdelAuthListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };
    /**
     * delauth callback interface
     **/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {

        @Override
        //删除授权成功
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            switch (platform) {
                case WEIXIN:
                    openid = data.get("unionid");
                    nickName = data.get("nickname");
                    headImagUrl = data.get("headimgurl");
                    sex = data.get("sex");
                    currentPlatFrom = "wx";
                    Api.selectUserInfo("wx", openid, LoginActivity.this);//先查询，是已注册用户直接登录，否则调用注册接口
                    break;
                case QQ:
                    openid = data.get("openid");
                    nickName = data.get("screen_name");
                    sex = data.get("gender");
                    headImagUrl = data.get("profile_image_url");
                    currentPlatFrom = "qq";
                    Api.selectUserInfo("qq", openid, LoginActivity.this);
                    break;
                case SINA:
                    currentPlatFrom = "microblog";
                    String jsonMap = data.get("result");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonMap);
                        openid = jsonObject.get("id") + "";
                        nickName = (String) jsonObject.get("name");
//                    sex = jsonObject.get("");
                        headImagUrl = (String) jsonObject.get("profile_image_url");
                        Api.selectUserInfo("microblog", openid, LoginActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

        //授权失败
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        // 授权取消
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(getApplicationContext(), "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(LoginActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
                    } else {
                        // 其他状态值则为授权失败
//                        Toast.makeText(LoginActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };
    private boolean exit;//是否已经退出登录了
    private String currentUserWeight;//当前用户的weighg值，判断是否是已经录入用户信息

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        exit = getIntent().getBooleanExtra("exit", false);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title.setText("用户登录");
        mShareAPI = UMShareAPI.get(this);

        login_alipay.setOnClickListener(this);
        login_qq.setOnClickListener(this);
        loginWb.setOnClickListener(this);
        loginWX.setOnClickListener(this);

        spUtils = new SharedPreferenceUtil();
        if (!TextUtils.isEmpty(UserManager.getInstance().getPhone()) && !TextUtils.isEmpty(UserManager.getInstance().getPassword())) {
            phoneNumber.setText(UserManager.getInstance().getPhone());
            et_password.setText(UserManager.getInstance().getPassword());
//                    doLogin();
        }
//        text();
        switchIp.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }

    @OnClick(R.id.bt_login)
    public void doLogin() {
        phoneNum = phoneNumber.getText().toString();
        passdWord = et_password.getText().toString();
        if (!TextUtils.isEmpty(phoneNum)) {
            tv_number.setVisibility(View.INVISIBLE);
            if (StringUtils.isMobileNO(phoneNum)) {
                tv_number.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(passdWord)) {
                    tv_psd.setVisibility(View.INVISIBLE);
                    Api.login(phoneNum, DigestUtils.stringMD5(passdWord), this);
                    LogUtils.e(DigestUtils.stringMD5(passdWord));
                } else {
                    tv_psd.setVisibility(View.VISIBLE);
                    tv_psd.setText("密码不能为空!");
                }
            } else {
                tv_number.setVisibility(View.VISIBLE);
                tv_number.setText("手机号码格式不正确!");
            }

        } else {
            tv_number.setVisibility(View.VISIBLE);
            tv_number.setText("请输入手机号码!");
        }

    }

    /* 去注册*/
    @OnClick(R.id.bt_regist)
    public void doRegist() {
        readyGo(RegistActivity.class);
    }

    /* 忘记密码*/
    @OnClick(R.id.tv_forget)
    public void forgetPsd() {
        readyGo(ModifyPSDActivity.class);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.LOGIN) {
            userInfo = (UserBeanFromService) object;
            if (userInfo.getCode().equals(Constant.RESULT_SUCESS)) {
                tv_psd.setVisibility(View.INVISIBLE);
                if (userInfo.getUserDetail() != null) {
                    UserManager.getInstance().saveUser(userInfo.getUser().getUserId() + "", et_password.getText().toString(), phoneNumber.getText().toString().trim(), userInfo.getUserDetail().getHeadPhotoAdd(), "", "");
                    if (userInfo.getUserDetail() != null && userInfo.getUserDetail().getStoreId() != 0) {
                        UserManager.getInstance().setStoreId(userInfo.getUserDetail().getStoreId() + "");
                    }
                    if (userInfo.getStoreCust() != null && !TextUtils.isEmpty(userInfo.getStoreCust().getName())) {
                        UserManager.getInstance().setStoreName(userInfo.getStoreCust().getName());
                    }
                    if (userInfo.getStoreCust() != null && !TextUtils.isEmpty(userInfo.getStoreCust().getCityName()) && !TextUtils.isEmpty(userInfo.getStoreCust().getDistrictName())) {
                        UserManager.getInstance().setStoreAddress(userInfo.getStoreCust().getCityName() + userInfo.getStoreCust().getDistrictName());
                    }
                    //通知我的首页更换电话号码
                    UserSetBean setBean = new UserSetBean();
                    setBean.setPhoneNum(userInfo.getUser().getMobileNum());
                    setBean.setHeadUrl(userInfo.getUserDetail().getHeadPhotoAdd());
                    EventBus.getDefault().post(setBean);
                    isFirstEnter(userInfo.getUserDetail().getWeight());
                } else {
                    LogUtils.e("返回null的detail对象");
                }
            } else if (userInfo.getCode().equals("APP_C010005")) {
                tv_psd.setVisibility(View.VISIBLE);
                tv_psd.setText("密码不正确!");
                return;
            } else if (userInfo.getCode().equals("APP_C010004")) {
                ToastUtil.showLong(this, userInfo.getMessage());
                return;
            } else if (userInfo.getCode().equals("APP_C010001")) {
                tv_psd.setVisibility(View.VISIBLE);
                tv_psd.setText("密码不正确!");
                return;
            } else if (userInfo.getCode().equals("APP_C010009")) {//无此用户
                ToastUtil.showLong(this, userInfo.getMessage());
                return;
            } else if (userInfo.getCode().equals("APP_C010010")) {//连续5次输入密码错误，账号已经冻结
                ToastUtil.showLong(this, userInfo.getMessage());
                return;
            } else if (userInfo.getCode().equals("APP_C010011")) {//用户未启用
                ToastUtil.showLong(this, userInfo.getMessage());
                return;
            }
        } else if (object != null && action == Action.THIRD_PART_LOGIN) {//第三方注册返回
            UserBeanFromService commonBean = (UserBeanFromService) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {//第三方注册成功，完善用户信息
                //调用完善信息
                if (commonBean.getUserDetail() != null) {
                    currentUserWeight = commonBean.getUserDetail().getWeight();
                }
                userId = commonBean.getUser().getUserId() + "";
                Api.perfectInfoAfterThirdLogin(userId, headImagUrl, nickName, sex, this);
            }
        } else if (object != null && action == Action.SET_INFOS) {//第三方注册完成。上传同步信息
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                //调用完善信息 存储openid，下次免登陆
                UserManager.getInstance().saveUser(userId, "", "", headImagUrl, nickName, sex);
                //通知我的首页更换头像和电话号码
                UserSetBean setBean = new UserSetBean();
                setBean.setHeadUrl(headImagUrl);
                setBean.setPhoneNum(nickName);
                EventBus.getDefault().post(setBean);
                isFirstEnter(currentUserWeight);
            }
        } else if (object != null && action == Action.SELECT_USER_INFO) {//第三方注册之前查询数据库，是否是老用户
            UserBeanFromService serviceBean = (UserBeanFromService) object;
            if (serviceBean.getCode().equals("C010008")) {//没有这个用户 ，去注册
                Api.threePartlogin(currentPlatFrom, openid, LoginActivity.this);
            } else if (serviceBean.getCode().equals(Constant.RESULT_SUCESS) && serviceBean.getUser() != null) {
                spUtils.putString(Constant.USER_ID, serviceBean.getUser().getUserId() + "");
                UserManager.getInstance().saveUser(serviceBean.getUser().getUserId() + "", "", "", headImagUrl, nickName, sex);
                //通知我的首页更换头像和电话号码
                UserSetBean setBean = new UserSetBean();
                setBean.setHeadUrl(headImagUrl);
                setBean.setPhoneNum(nickName);
                EventBus.getDefault().post(setBean);
                isFirstEnter(serviceBean.getUserDetail().getWeight());
            }
        }
    }

    /**
     * 是否是第一次进入app,没有录入个人信息
     *
     * @param weight
     */
    private void isFirstEnter(String weight) {
        // 如果是第一次登陆用户信息为kong则填写用户信息 否则进入首页面
        if (TextUtils.isEmpty(weight)) {
            UserManager.getInstance().setFirstRecordInfo(true);//是没有录入信息
            readyGo(ReportSex.class);
            finish();
        } else {
            UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
            AppManager.getAppManager().finishActivity(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_alipay:
                Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID);
                String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
                String sign = OrderInfoUtil2_0.getSign(authInfoMap, RSA_PRIVATE);
                final String authInfo = info + "&" + sign;
                Runnable authRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造AuthTask 对象
                        AuthTask authTask = new AuthTask(LoginActivity.this);
                        // 调用授权接口，获取授权结果
                        Map<String, String> result = authTask.authV2(authInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_AUTH_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread authThread = new Thread(authRunnable);
                authThread.start();
//                        Api.alipaylogin("","",this);
                break;
            case R.id.login_qq:
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.login_wx:
                platform = SHARE_MEDIA.WEIXIN;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.login_wb:
                platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.switch_ip:
                SwitchIpDialog popwindow = new SwitchIpDialog(this, Urls.HOST, new MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case R.id.ip_test:
                                Urls.HOST = "http://192.168.100.67:8181";
                                Urls.HtmlHOST = "http://192.168.100.67:8180";
                                break;
                            case R.id.ip_yewu:
                                Urls.HOST = "http://192.168.101.2:8181";
                                Urls.HtmlHOST = "http://192.168.101.2:8180";
                                break;
                        }
                    }
                });
                popwindow.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exit) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
