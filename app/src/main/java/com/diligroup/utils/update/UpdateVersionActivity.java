package com.diligroup.utils.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.service.VersionDownload;

public class UpdateVersionActivity extends Activity{
	private TextView content,title;
	private  TextView cancelView,submitView;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.confirm_dialog);
			title= (TextView) findViewById(R.id.dialog_title);
			content = (TextView) findViewById(R.id.confirm_message);
			cancelView = (TextView) findViewById(R.id.confirm_cancel);
			submitView=(TextView) findViewById(R.id.confirm_submit);
			submitView.setText("确定");
			content.setText("发现新版本"+getIntent().getStringExtra("newVerName")+",是否需要更新?");
			title.setText("上食上饮");
			title.setTextColor(this.getResources().getColor(android.R.color.black));
			cancelView.setOnClickListener(l);
			submitView.setOnClickListener(l);
		}
		
		OnClickListener l=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm_submit:
					Intent intent=new Intent(UpdateVersionActivity.this, VersionDownload.class);
					startService(intent);
					UpdateVersionActivity.this.finish();
					break;
				case R.id.confirm_cancel:
					UpdateVersionActivity.this.finish();
					break;
				}

			}
		};
		
		
	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}
}
