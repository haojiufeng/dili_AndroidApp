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

/**
 * 使用说明activiviyt
 */
public class InstructionsActivity extends BaseActivity {
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.web_instructions)
    WebView web_instructions;
    private ViewSwitcherHelper myHelper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_instructions;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        myHelper = new ViewSwitcherHelper(this,web_instructions);
        tv_title.setText("帮助中心");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WebViewUtils.settingsWebView(this, web_instructions);
        web_instructions.setWebChromeClient(new MyWebChromeClient());
      String  loadUrl =  Urls.HtmlHOST+Urls.instructionUrL;

        myHelper.showLoading();
        web_instructions.loadUrl(loadUrl);
        LogUtils.e("使用说明url=",loadUrl);
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
