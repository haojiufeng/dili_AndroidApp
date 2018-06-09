package com.diligroup.my.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Urls;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.utils.WebViewUtils;

import butterknife.Bind;
import okhttp3.Request;

public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.web_aboutus)
    WebView web_aboutus;
    private ViewSwitcherHelper myHelper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        myHelper = new ViewSwitcherHelper(this,web_aboutus);
        tv_title.setText("关于我们");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WebViewUtils.settingsWebView(this, web_aboutus);
        web_aboutus.setWebChromeClient(new MyWebChromeClient());
        String  loadUrl =  Urls.HtmlHOST+Urls.aboutUsUrL;

        myHelper.showLoading();
        web_aboutus.loadUrl(loadUrl);
        LogUtils.e("关于我们url=",loadUrl);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        myHelper.showNotify("网络不稳定，请稍后重试");
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {

    }
    /**
     * 监控WebView状态JS等
     */
    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                myHelper.showContent();
            }
        }
    }
}
