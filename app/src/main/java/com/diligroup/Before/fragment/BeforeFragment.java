package com.diligroup.Before.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseFragment;
import com.diligroup.home.FoodDetailsActivity;
import com.diligroup.net.Urls;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.ShareUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.utils.WebViewUtils;

import butterknife.Bind;

/**
 * Created by Kevin on 2016/7/4.
 */
public class BeforeFragment extends BaseFragment {
    @Bind(R.id.web_before)
    WebView webView_before;
    @Bind(R.id.title_root)
    View rootView;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    private String loadUrl;
    @Bind(R.id.before_rootview)
    FrameLayout before_rootview;
    LinearLayout noNet;//无网布局
    private ViewSwitcherHelper myHelper;
    private String random;

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_before;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void setViews() {
        myHelper = new ViewSwitcherHelper(getActivity(), before_rootview);
        tv_title.setText("餐前指导");
        ivShare.setVisibility(View.VISIBLE);
        random = DateUtils.getDateFormatString(System.currentTimeMillis());

        webView_before.addJavascriptInterface(new MaintainJavaScriptInterface(), "androidHandler");
        webView_before.setSaveEnabled(false);
        webView_before.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView_before.setWebChromeClient(new MyWebChromeClient());
        WebViewUtils.settingsWebView(getActivity(), webView_before);
//        webView_before.setWebViewClient(new MyWebViewClient());
        loadUrl =  Urls.HtmlHOST+Urls.BeforeUrL + "userId=" + UserManager.getInstance().getUserId()+ "&random=" + random;
        LogUtils.e("loadBeforeUrl=============" + loadUrl);
        myHelper.showLoading();
//        webView_before.loadUrl(loadUrl);
        loadDate(loadUrl);
    }
    public void loadDate(String url){
        webView_before.loadUrl(loadUrl);
    }
    @Override
    public void setListeners() {
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareUtils(getActivity(), true, Urls.BeforeUrL + "userId=" + UserManager.getInstance().getUserId()+ "&random=" + random, view).openSharebord();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
    if(!hidden){
        myHelper.showLoading();
        random = DateUtils.getDateFormatString(System.currentTimeMillis());
        //url重新赋值，userid避免重复
//        loadUrl = Urls.HtmlHOST+Urls.BeforeUrL + "userId=" + UserManager.getInstance().getUserId()+ "&random=" + random;
//        webView_before.loadUrl(loadUrl);
        loadDate(loadUrl);
        LogUtils.e("loadBeforeUrl=============" + loadUrl);
    }
    }

    /**
     * JS调用本地方法
     */
    public class MaintainJavaScriptInterface {
        @JavascriptInterface
        public void jumpFoodDetails(String foodCode) {
            LogUtils.i("js调用到了我的方法");
            Intent mIntent = new Intent(getActivity(), FoodDetailsActivity.class);
            mIntent.putExtra("foodCode", foodCode);
            startActivity(mIntent);
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
                myHelper.showContent();
            }
        }
    }
}
