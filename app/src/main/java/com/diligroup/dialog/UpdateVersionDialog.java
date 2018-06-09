package com.diligroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diligroup.R;

/**
 * 版本更新提示对话框
 *
 * @author zhx
 */
public class UpdateVersionDialog extends Dialog implements OnClickListener {
    private static UpdateVersionDialog mInstance;
    private Context context;
    private TextView ok;
    private TextView not;

    private String content;
    private TextView tv_tel;
    OnSureClickedLisener lisener;
    boolean isShowCancel = true;//默认取消按钮
    private ProgressBar progressBar;

    public UpdateVersionDialog(Context context, String content, OnSureClickedLisener lisener, boolean isShowCancel) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.content = content;
        this.lisener = lisener;
        this.isShowCancel = isShowCancel;
    }

    public UpdateVersionDialog(Context context, String content, OnSureClickedLisener lisener) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.content = content;
        this.lisener = lisener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_dialog);
        ok = (TextView) findViewById(R.id.ok);
        not = (TextView) findViewById(R.id.not);
        tv_tel = (TextView) findViewById(R.id.tv_dialog_content);
        progressBar = (ProgressBar) findViewById(R.id.update_progress);
       TextView tv_tip=  (TextView) findViewById(R.id.tv_tip);
        tv_tip.setText("发现新版本");
        progressBar.setVisibility(View.VISIBLE);

        tv_tel.setText(content);
        ok.setOnClickListener(this);
        not.setOnClickListener(this);
        if (!isShowCancel) {
            not.setVisibility(View.GONE);
            setCancelable(false);
        } else {
            not.setVisibility(View.VISIBLE);
        }
        getWindow().setWindowAnimations(R.style.CustomDialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                lisener.onSureClick();
//                this.dismiss(); 版本更新，禁止让对话框消失
                break;
            case R.id.not:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnSureClickedLisener {
        void onSureClick();
    }

    //设置确定键要展示的文字
    public void setSureText(String sureText) {
        ok.setText(sureText);
    }
    //设置确定键要展示的文字
    public void setSureDisenable() {
        ok.setClickable(false);
    }
    //设置确定键要展示的文字
    public void setCurrentProgress(int progress) {
       progressBar.setProgress(progress);
    }
}
