package com.diligroup.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;

import com.diligroup.R;
import com.diligroup.base.Constant;
import com.diligroup.mode.StepData;
import com.diligroup.my.activity.CurrentStepActivity;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.RecordStep;
import com.diligroup.utils.record_steps.CountDownTimer;
import com.diligroup.utils.record_steps.DbUtils;
import com.diligroup.utils.record_steps.StepDcretor;

import java.util.Calendar;
import java.util.List;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class StepService extends Service implements SensorEventListener {
    private final String TAG = "StepService";
    //默认为30秒进行一次存储
    private static int duration = 30000;

    private SensorManager sensorManager;
    private StepDcretor stepDetector;
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private Messenger messenger = new Messenger(new MessenerHandler());
    private BroadcastReceiver mBatInfoReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    //测试
    private static int i = 0;
    private String DB_NAME = "basepedo";
    private static String CURRENTDATE = "";
    private boolean isSwitchDate;//是否是手动切换的日期

    private static class MessenerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    try {
                        LogUtils.e("what===0");
                        Messenger messenger_1 = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constant.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("step", StepDcretor.CURRENT_SETP);
                        replyMsg.setData(bundle);
                        messenger_1.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTodayData();
//        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");
        return START_REDELIVER_INTENT;
    }
    /**
     * 查询和切换日期
     */
    private void initTodayData() {
        DbUtils.createDb(this, DB_NAME);
        //获取当天的数据，用于展示
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtils.getTodayDate()});
        if (list.size() == 0 || list.isEmpty()) {
            StepDcretor.CURRENT_SETP = 0;
//            updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");//hjf
        } else if (list.size() == 1) {
            StepDcretor.CURRENT_SETP = Integer.parseInt(list.get(0).getStep());
//            updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");//hjf
        } else {
            LogUtils.e(TAG, "出错了！");
        }
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //日期修改
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        //时间变化，每分钟发送此广播
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    LogUtils.e(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    LogUtils.e(TAG, "screen off");
                    save();//hjf
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    LogUtils.e(TAG, "screen unlock");
//                    RecordStep.save();
                    save();
                    //改为30秒一存储
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    LogUtils.e(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
//                    RecordStep.save();
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    LogUtils.e(TAG, " receive ACTION_SHUTDOWN");
//                    RecordStep.save();
                    save();
                } else if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) || Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
                    LogUtils.e(TAG, " receive ACTION_TIME_CHANGED");
//                    LogUtils.e(TAG,"今天的日期是=="+ DateUtils.getTodayDate());
//                    RecordStep.save();//hjf
                    initTodayData();
                    clearStepData();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    private void clearStepData() {
        i = 0;
        RecordStep.CURRENTDATE = "0";
    }

    private void startTimeCount() {
        time = new TimeCount(duration, 1000);
        time.start();
    }

    /**
     * 更新通知
     */
    private void updateNotification(String content) {
//        if(builder==null){

        builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_MAX);
        Intent mIntent = new Intent(this, CurrentStepActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                mIntent, 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(getResources().getString(R.string.app_name));
        builder.setContentTitle(getResources().getString(R.string.app_name));
        //设置不可清除
        builder.setOngoing(true);
//        }
        builder.setContentText(content);
        Notification notification = builder.build();
        startForeground(0, notification);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private void startStepDetector() {
        if (sensorManager != null && stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
            sensorManager = null;
            stepDetector = null;
        }
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        getLock(this);
        //android4.4以后可以使用计步传感器
//        int VERSION_CODES = android.os.Build.VERSION.SDK_INT;
//        if (VERSION_CODES >= 19) {
//            addCountStepListener();
//        } else {
//            addBasePedoListener();
//        }

        addBasePedoListener();
        addCountStepListener();
    }

    private void addCountStepListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (detectorSensor != null) {
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_UI);
        } else if (countSensor != null) {
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_UI);
            //        addBasePedoListener();
        } else {
            LogUtils.e(TAG, "Count sensor not available!");
        }
    }

    private void addBasePedoListener() {
        stepDetector = new StepDcretor(this);
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // sensorManager.unregisterListener(stepDetector);
        sensorManager.registerListener(stepDetector, sensor,
                SensorManager.SENSOR_DELAY_UI);
        stepDetector
                .setOnSensorChangeListener(new StepDcretor.OnSensorChangeListener() {

                    @Override
                    public void onChange() {
//                        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + "," + i + " 步");
//                        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");
                    }
                });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        i++;
        //   StepDcretor.CURRENT_SETP++;
//        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + "," + i + " 步");
//        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
//            RecordStep.save();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    @Override
    public void onDestroy() {
//        LogUtils.e("service执行了onDestory方法 销毁了");
        //取消前台进程
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);
        Intent intent = new Intent(this, StepService.class);
        intent.putExtra("isFormNotification", true);
        startService(intent);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

//    private  void unlock(){
//        setLockPatternEnabled(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED,false);
//    }
//
//    private void setLockPatternEnabled(String systemSettingKey, boolean enabled) {
//        //推荐使用
//        android.provider.Settings.Secure.putInt(getContentResolver(), systemSettingKey,enabled ? 1 : 0);
//    }

    synchronized private WakeLock getLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }
        return (mWakeLock);
    }

    /**
     新增和累加每日数据
     */
    private void save() {
        int tempStep = StepDcretor.CURRENT_SETP;
        CURRENTDATE=DateUtils.getTodayDate();
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});

        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENTDATE);
            data.setStep(tempStep + "");
            DbUtils.insert(data);
//            Log.e("存入一条数据",CURRENTDATE);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        } else {
        }
        LogUtils.e("当前的日期 save()", CURRENTDATE + "步数=" + StepDcretor.CURRENT_SETP);
    }

}
