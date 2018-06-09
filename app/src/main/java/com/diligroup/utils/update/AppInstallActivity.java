package com.diligroup.utils.update;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.diligroup.net.Urls;

import java.io.File;

public class AppInstallActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();
		/*Intent intent=new Intent("closeNotification");
		sendBroadcast(intent);
		installApk();*/
	}

	public void installApk()  {
		File filePath = new File(Urls.APK_PATH);
		String[] files = filePath.list();
		if (files==null || files.length != 1) {
			Toast.makeText(this, "文件错误,请从新下载安装!", Toast.LENGTH_SHORT);
		} else {
			String fileName=null;
			for (String name : files) {
				fileName = name;
			}
			File appFile = new File(Urls.APK_PATH + fileName);
			if (appFile.exists()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(appFile),
						"application/vnd.android.package-archive");
				startActivity(intent);
			}
		}
		finish();
	}
	
	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}
}

