package com.diligroup.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.diligroup.R;
import com.diligroup.base.Constant;
import com.diligroup.net.Urls;
import com.diligroup.utils.FileUtil;
import com.diligroup.utils.update.AppInstallActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionDownload extends Service{
	private NotificationManager  notificationManager ;
	private Notification notification;
	private RemoteViews remoteView;
	private Broadcastdeleate broadcast;
	private String downloadUrl;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
				broadcast=new Broadcastdeleate();
				IntentFilter filter=new IntentFilter("closeNotification");
				registerReceiver(broadcast, filter);
				
				new Thread() {
					@Override
					public void run() {
						createNotification();
						downLoadApk();
					}
				}.start();
		} else {
			Toast.makeText(this, "请先插入SD卡!",Toast.LENGTH_SHORT).show();
			this.stopSelf();
		}
	}
	private Handler msgHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == 1){
				remoteView.setProgressBar(R.id.progresss, 100, msg.arg1, false);
				remoteView.setTextViewText(R.id.texts, "下载进度   "+msg.arg1+"%");
				notificationManager.notify(0, notification);
			}
			if(msg.arg1 == 100){
				remoteView.setTextViewText(R.id.texts, "点击进行安装");
				notificationManager.notify(0,notification);
				Toast.makeText(VersionDownload.this, "下载完成", Toast.LENGTH_LONG).show();
				installApk();
				notificationManager.cancel(0);
				VersionDownload.this.stopSelf();
			}
		}
	};
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		downloadUrl = intent.getStringExtra("downloadUrl");
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * 创建notification
	 */
	public  void createNotification(){
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification(R.mipmap.ic_launcher,"更新",System.currentTimeMillis());
		notification.flags = Notification.FLAG_INSISTENT;
		
	    remoteView  =  new RemoteViews(this.getPackageName(),R.layout.notification);
		Intent intent = new Intent(VersionDownload.this,AppInstallActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(VersionDownload.this, 0, intent,0);
		notification.contentIntent = pendingIntent;
		notification.contentView = remoteView;
	}
	
	private void downLoadApk(){
		File  localFile = new File(Urls.APK_PATH);
		boolean b =localFile.mkdirs();
		
		String[] files =   localFile.list();
		if(files!=null){
			for(String file :files){
				FileUtil.deleteFile(Urls.APK_PATH, file);
			}
		}
		
		BufferedOutputStream localBufferedOutputStream = null;
		FileOutputStream localFileOutputStream = null;
		try {
			File apk = File.createTempFile("diliapp",".apk",localFile);
			localFileOutputStream = new FileOutputStream(apk);
			localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
			HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(downloadUrl).openConnection();
			localHttpURLConnection.connect();
			
			int index = 0;
			int contentLength = localHttpURLConnection.getContentLength();
			InputStream localInputStream = localHttpURLConnection.getInputStream();
			byte[] arrayOfByte = new byte [10240];
			int currentSchedule = 0;
			int progress = 0;
			while(true){
				currentSchedule =localInputStream.read(arrayOfByte);
				if(currentSchedule < 0){
					break;
				}else{
					index += currentSchedule;
					progress = index*100/contentLength;

					// 发送广播更新下载管理的进度
					Intent intent = new Intent();
					intent.setAction(Constant.DOWNLOADMANAGEACTION);
					if(progress%5==0){
					intent.putExtra("progress", progress);
					VersionDownload.this.sendBroadcast(intent);
					}
					Message localMessage = new Message();
					localMessage.what = 1;
					localMessage.arg1 = progress;
					
					localBufferedOutputStream.write(arrayOfByte,0,currentSchedule);
					if(progress%5 == 0){
						msgHandler.sendMessage(localMessage);
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			//失败关闭
			notificationManager.cancel(0);
			VersionDownload.this.stopSelf();
		}finally{
			try {
				if(localBufferedOutputStream!=null){
				localBufferedOutputStream.close();
				}
				if(localFileOutputStream!=null){
				localFileOutputStream.close();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	class Broadcastdeleate extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			notificationManager.cancel(0);
			VersionDownload.this.stopSelf();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcast);
	}
	
	public void installApk()  {
		File filePath = new File(Urls.APK_PATH);
		String[] files = filePath.list();
		if (files==null || files.length != 1) {
			Toast.makeText(this, "文件错误,请从新下载安装!", Toast.LENGTH_SHORT).show();
		} else {
			String fileName=null;
			for (String name : files) {
				fileName = name;
			}
			File appFile = new File(Urls.APK_PATH + fileName);
			if (appFile.exists()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				intent.setDataAndType(Uri.fromFile(appFile),
						"application/vnd.android.package-archive");
				startActivity(intent);
			}
		}
		
	}

}
