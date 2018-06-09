package com.diligroup.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by hjf on 2016/9/4.
 */
public class WebViewUtils {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void settingsWebView(final Activity activity, final WebView webView) {
//		if (getPhoneAndroidSDK() >= 14) {// 4.0需打开硬件加速
//			activity.getWindow().setFlags(
//					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//		}
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染优先级
//        settings.setBlockNetworkImage(true);//图片加载放在最后面
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);//支持2.2以上所有版本
//        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        // settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; MB525 Build/3.4.2-117) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        // settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(false);
        settings.setDatabaseEnabled(false);
        settings.setDomStorageEnabled(false);

    }
}
