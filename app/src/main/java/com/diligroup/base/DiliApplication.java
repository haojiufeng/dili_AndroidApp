package com.diligroup.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.diligroup.other.LocationService;
import com.diligroup.service.StepService;
import com.diligroup.service.StepService2;
import com.diligroup.utils.record_steps.BootCompleteReceiver;
import com.diligroup.utils.record_steps.BootCompleteReceiver2;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by Kevin on 2016/7/4.
 */
public class DiliApplication extends Application {
    public LocationService locationService;
    private static DiliApplication instance;
    public static String flagArea;

    private DaemonClient mDaemonClient;
    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        DiliApplication application = (DiliApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }

//    在自己的Application中添加如下代码
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        refWatcher = LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(this);
        //微信 appid appsecret
        PlatformConfig.setWeixin(Constant.WX_APPID, Constant.WX_APPSECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Constant.WB_APPID,Constant.WB_SECRET);
        //支付宝分享 appId
        PlatformConfig.setAlipay("2016081601755137");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(Constant.QQ_APPID,Constant.QQ_KEY);

        CrashHandler.getInstance().setCustomCrashInfo(this);

//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",APP_ID,APP_PRIVATE_KEY,"json","GBK",ALIPAY_PUBLIC_KEY);
//        alipayClient.execute()
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=57901ca4");
    }
    public static Context getContext() {
        return instance;
    }
    /**
     * you can override this method instead of {@link android.app.Application attachBaseContext}
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }
    /**
     * give the configuration to lib in this callback
     * @return
     */
//    @Override
    protected DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.diligroup:process1",
                StepService.class.getCanonicalName(),
                BootCompleteReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.diligroup:process2",
                StepService2.class.getCanonicalName(),
                BootCompleteReceiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}
