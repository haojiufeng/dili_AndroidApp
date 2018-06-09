package com.diligroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.diligroup.R;

/**
 * 温馨提示对话框
 *
 * @author zhx
 */
public class ReminderDialog extends Dialog implements OnClickListener {
    private static ReminderDialog mInstance;
    private Context context;
    private TextView ok;
    private TextView not;

    private String content;
    private TextView tv_tel;
    OnSureClickedLisener lisener;
    boolean isShowCancel=true;//默认取消按钮
    public ReminderDialog(Context context, String content,OnSureClickedLisener lisener,boolean isShowCancel) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.content = content;
        this.lisener=lisener;
        this.isShowCancel=isShowCancel;
    }
    public ReminderDialog(Context context, String content,OnSureClickedLisener lisener) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.content = content;
        this.lisener=lisener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_dialog);
        ok = (TextView) findViewById(R.id.ok);
        not = (TextView) findViewById(R.id.not);
        tv_tel = (TextView) findViewById(R.id.tv_dialog_content);

        tv_tel.setText(content);
        ok.setOnClickListener(this);
        not.setOnClickListener(this);
        if(!isShowCancel){
            not.setVisibility(View.GONE);
        }else{
            not.setVisibility(View.VISIBLE);
        }
        getWindow().setWindowAnimations(R.style.CustomDialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                lisener.onSureClick();
                this.dismiss();
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
}
