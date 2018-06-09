package com.diligroup.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.diligroup.utils.LogUtils;

public class MyPushService extends Service {
    public MyPushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("jjjjjjjjjjjjjjjjjjjjjj");
    }
}
