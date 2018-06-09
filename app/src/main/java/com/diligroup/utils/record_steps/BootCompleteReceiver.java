package com.diligroup.utils.record_steps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.diligroup.service.MyPushService;
import com.diligroup.service.StepService;

/**
 * 开机完成广播
 *
 * Created by xf on 2016/3/1.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, StepService.class);
        context.startService(i);
        Intent m= new Intent(context, MyPushService.class);
        context.startService(m);
    }
}
