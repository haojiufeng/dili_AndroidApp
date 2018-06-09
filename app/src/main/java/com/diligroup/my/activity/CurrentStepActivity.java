package com.diligroup.my.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.Constant;
import com.diligroup.service.StepService;
import com.diligroup.utils.LogUtils;

public class CurrentStepActivity extends Activity implements Handler.Callback {
    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL = 500;
    private TextView text_step;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Intent intent;
    private String isFromNotification;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                // 更新界面上的步数
                text_step.setText(msg.getData().getInt("step") + "");
                delayHandler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, TIME_INTERVAL);
                LogUtils.e("what==1");
                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                    LogUtils.e("what==2");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_step);
        init();
    }
    private void init() {
        text_step = (TextView) findViewById(R.id.text_step);
        delayHandler = new Handler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupService();
    }

    private void setupService() {
        intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ServiceCenter.class));
//        finish();
        super.onBackPressed();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("activity onDestory方法执行");
        unbindService(conn);
        startService(intent);
    }
}
