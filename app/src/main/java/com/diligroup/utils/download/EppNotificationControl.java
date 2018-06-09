package com.diligroup.utils.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.diligroup.R;
import com.diligroup.base.DiliApplication;

import java.io.File;
/**
 * Created by Administrator on 2016/6/17.
 */
public class EppNotificationControl {
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    String urlPath;

    int progress;
    final int NOTIFYCATIONID = 1001;

    public EppNotificationControl(String urlPath) {
        initNotifycation();
        this.urlPath = urlPath;
    }

    /**
     *
     * 功能描述: <br>
     * 〈功能详细描述〉 初始化一个builder Author: 14052012 zyn Date: 2014年11月6日 下午7:13:14
     *
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private void initNotifycation() {
        mNotificationManager = (NotificationManager) DiliApplication.getContext()
                .getSystemService( DiliApplication.getContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder( DiliApplication.getContext());
        mBuilder.setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
    }

    /**
     *
     * 功能描述: <br>
     * 〈功能详细描述〉 首次展示通知栏 Author: 14052012 zyn Date: 2014年11月6日 下午7:13:39
     *
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void showProgressNotify() {
        mBuilder.setContentTitle("等待下载").setContentText("进度:").setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        // 确定进度的
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        // 设置为true就是不确定的那种进度条效果
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

    /** 设置下载进度 */
    public void updateNotification(int progress) {
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        mBuilder.setContentText("下载中...").setContentTitle("");
        if (progress >= 100) {
            mBuilder.setContentText("").setContentTitle("完成");
            new Thread(new Runnable() {
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.sendToTarget();
                }
            }).start();

        }
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

    /**
     * 异步安装apk
     */
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Intent apkIntent = new Intent();
            apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            apkIntent.setAction(Intent.ACTION_VIEW);
            File apkFile = new File(Constant.LOCALPATH, Constant.getFileName(urlPath));
            Uri uri = Uri.fromFile(apkFile);
            apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            DiliApplication.getContext().startActivity(apkIntent);
            mNotificationManager.cancel(NOTIFYCATIONID);// 删除一个特定的通知ID对应的通知
        };
    };

}