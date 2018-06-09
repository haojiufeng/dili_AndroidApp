package com.diligroup.after.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Urls;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ShareUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.utils.WebViewUtils;

import butterknife.Bind;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/8/24.
 */
public class AfterEvaluate extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.web_before)
    WebView webViewAfter;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.title_root)
    View rootView;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.before_rootview)
    FrameLayout before_rootview;
    @Bind(R.id.no_net)
    LinearLayout noNet;//无网布局
    String loadUrl;//
    private ViewSwitcherHelper afterHelper;
    private int flag;
    private String date;
    private TimeCount time;//倒计时，加载超时

    private int currentProgress;//当前webView加载的进度
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_before;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    @Override
    protected void onNetworkDisConnected() {
        noNet.setVisibility(View.VISIBLE);
        noNet.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void initViewAndData() {
        tv_title.setText("餐后评价");
        time=new TimeCount(10000,1000);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        ivShare.setVisibility(View.VISIBLE);
        ivShare.setOnClickListener(this);

        afterHelper = new ViewSwitcherHelper(this, before_rootview);
        date = getIntent().getStringExtra("time");
        flag = getIntent().getIntExtra("flag", 0);

        //启用支持javascript
//        webViewAfter.getSettings().setDefaultTextEncodingName("utf-8");
//        webViewAfter.getSettings().setJavaScriptEnabled(true);
        webViewAfter.setSaveEnabled(false);
        webViewAfter.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        WebViewUtils.settingsWebView(this, webViewAfter);
        webViewAfter.setWebChromeClient(new MyWebChromeClient());
        webViewAfter.setWebViewClient(new MyWebClient());
        String random = DateUtils.getDateFormatString(System.currentTimeMillis());

        if (flag == 1) {
            loadUrl =  Urls.HtmlHOST+Urls.AfterUrl + "userId=" + UserManager.getInstance().getUserId() + "&flag=1" + "&time=" + date + "&random=" + random;
        } else {
            loadUrl =  Urls.HtmlHOST+Urls.AfterUrl + "userId=" + UserManager.getInstance().getUserId() + "&time=" + date + "&random=" + random;
        }
        LogUtils.e("loadAfterUrl=============" + loadUrl);
        afterHelper.showLoading();
        time.start();
        webViewAfter.loadUrl(loadUrl);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
    }

    @Override
    public void onClick(View view) {
        String shareUrl;
        switch (view.getId()) {
            case R.id.iv_back:
                if (webViewAfter.canGoBack()) {
                    webViewAfter.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.no_net:
                webViewAfter.loadUrl(loadUrl);
                break;
            case R.id.iv_share:
                if (flag == 1) {
                    shareUrl =  Urls.HtmlHOST+Urls.AfterUrl + "userId=" + UserManager.getInstance().getUserId() + "&flag=1" + "&time=" + date;
                    new ShareUtils(this, false, shareUrl, view).openSharebord();
                } else {
                    shareUrl = Urls.HtmlHOST+ Urls.AfterUrl + "userId=" + UserManager.getInstance().getUserId() + "&time=" + date;
                    new ShareUtils(this, false, shareUrl, view).openSharebord();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewAfter.canGoBack()) {
            webViewAfter.goBack();
        } else {
            finish();
        }
    }

    /**
     * 监控WebView状态JS等
     */
    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            LogUtils.e("餐后评价webView进度==",newProgress+"");
            if (newProgress == 100) {
                currentProgress = newProgress;
                afterHelper.showContent();
               time.onFinish();
                if (null!=webViewAfter && !webViewAfter.canGoBack()) {
                    if (ivShare != null) {
                        ivShare.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ivShare != null) {
                        ivShare.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (webViewAfter.canGoBack()) {
                tv_title.setText(title);
            } else {
                tv_title.setText("餐后评价");
            }
        }
    }

    /**
     * webView启用Android自带浏览器
     */
    public class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
class TimeCount extends CountDownTimer {
    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {// 计时完毕
       if(currentProgress<100){
           afterHelper.showNotify("加载超时，请稍后重试");
       }
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
    }
}
}
