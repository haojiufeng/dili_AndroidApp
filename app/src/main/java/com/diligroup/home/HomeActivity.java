package com.diligroup.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.diligroup.Before.fragment.BeforeFragment;
import com.diligroup.R;
import com.diligroup.after.fragment.AfterFragment;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.UploadInfo;
import com.diligroup.dialog.UpdateVersionDialog;
import com.diligroup.home.fragment.HomeFragment;
import com.diligroup.login.LoginActivity;
import com.diligroup.my.fragment.UserSetFragment;
import com.diligroup.net.Api;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.FileUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.PictureFileUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UpLoadPhotoUtils;
import com.diligroup.utils.UserManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.Request;

public class HomeActivity extends BaseActivity implements RequestManager.ResultCallback {
    private static final int REQUEST_IMAGE = 2;
    private static final int CROP_CODE = 3;
    private ArrayList<String> mSelectPath;
    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.tab_bar)
    RadioGroup tabBar;
    private List<String> titles;
    private String path;
    private String fileName;
    private File file;
    private String imagPath;
    private String lastTag;
    private Map<String, Class<? extends Fragment>> fragments;
    private String tag;//当前需要展示的tab
    private FragmentManager fm;
    private long exitTime = 0;

    private UpdateProgressBroadcastReceiver receiver;
    private UpdateVersionDialog update_dialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x10) {
                int progress = (Integer) msg.obj;
                if (progress == 100) {
                    update_dialog.setSureText("已完成");
                    update_dialog.dismiss();
                    return;
                }
                update_dialog.setCurrentProgress(progress);
            }
        }
    };
    private UpLoadPhotoUtils photoUtils;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        isApkDebugable(this);
//        Intent intent = getIntent();
//        String scheme = intent.getScheme();
//        Uri uri = intent.getData();
//        System.out.println("scheme:"+scheme);
//        if (uri != null) {
//            String host = uri.getHost();
//            String dataString = intent.getDataString();
//            String id = uri.getQueryParameter("d");
//            String path = uri.getPath();
//            String path1 = uri.getEncodedPath();
//            String queryString = uri.getQuery();
//            System.out.println("host:"+host);
//            System.out.println("dataString:"+dataString);
//            System.out.println("id:"+id);
//            System.out.println("path:"+path);
//            System.out.println("path1:"+path1);
//            System.out.println("queryString:"+queryString);
//        }
//        Api.updateVersion(this);
        ActivityInfo info;
        fm = getSupportFragmentManager();
        if (!TextUtils.isEmpty(UserManager.getInstance().getStoreId())) {
            initFragment();
        } else {
            startActivityForResult(new Intent(this, GetCityShopActivity.class), 20);
            AppManager.getAppManager().addActivity(this);
        }

        tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_home:
                        changeFragment("tab1");
                        tabBar.check(R.id.tab_home);
                        break;
                    case R.id.tab_before:
//                        if (UserManager.getInstance().isLogin()) {
                            changeFragment("tab2");
                            tabBar.check(R.id.tab_before);
//                        } else {
//                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                            changeFragment("tab1");
//                            tabBar.check(R.id.tab_home);
//                        }
                        break;
                    case R.id.tab_after:
//                        if (UserManager.getInstance().isLogin()) {
                            changeFragment("tab3");
                            tabBar.check(R.id.tab_after);
//                        } else {
//                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                            changeFragment("tab1");
//                            tabBar.check(R.id.tab_home);
//                        }
                        break;
                    case R.id.tab_my:
//                        if (UserManager.getInstance().isLogin()) {
                            changeFragment("tab4");
                            tabBar.check(R.id.tab_my);
//                        } else {
//                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                            changeFragment("tab1");
//                            tabBar.check(R.id.tab_home);
//                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
* 初始化Fragment，添加第一个tab
* */
    private void initFragment() {
        fragments = new Hashtable<String, Class<? extends Fragment>>();
        fragments.put("tab1", HomeFragment.class);
        fragments.put("tab2", BeforeFragment.class);
        fragments.put("tab3", AfterFragment.class);
        fragments.put("tab4", UserSetFragment.class);
        tabBar.check(R.id.tab_home);
        changeFragment("tab1");
    }

    /**
     * 显示和隐藏Fragment
     *
     * @param tag 切换的tab
     */
    public void
    changeFragment(String tag) {
        if (lastTag != null && lastTag.equals(tag)) {
            return;
        }
        Fragment last = fm.findFragmentByTag(lastTag);
        Fragment current = fm.findFragmentByTag(tag);
        if (last != null && last.isAdded()) {
            fm.beginTransaction().hide(last).commit();
        }
        if (current == null) {
            try {
                current = fragments.get(tag).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (current.isAdded()) {
            if (current instanceof BeforeFragment) {
                fm.beginTransaction().show(current).commitAllowingStateLoss();
            } else {
                fm.beginTransaction().show(current).commit();
            }
        } else {
            fm.beginTransaction().add(R.id.container, current, tag).commit();
        }
        lastTag = tag;
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        switch (action) {
            case UPLOAD_PHOTO:
                ToastUtil.showLong(this, "上传头像失败");
                break;
            case SET_INFOS:
                ToastUtil.showLong(this, "完善用户信息失败了");
                break;
            case UPDTE_VERSION:
//                LogUtils.e(this, "完善用户信息失败了");
                break;
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.UPLOAD_PHOTO && null != object) {
            UploadInfo bean = (UploadInfo) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                ToastUtil.showLong(this, "上传成功");
                imagPath = bean.getFilePath();
                Api.perfectInfoAfterUpLoad(UserManager.getInstance().getUserId(), bean.getFilePath(), this);
            } else if (!NetUtils.isNetworkAvailable(this)) {
                ToastUtil.showLong(this, "没网啦");
            } else {
                ToastUtil.showLong(this, "上传失败");
            }
        } else if (action == Action.SET_INFOS && object != null) {
            CommonBean bean1 = (CommonBean) object;
            if (bean1.getCode().equals(Constant.RESULT_SUCESS)) {
                final UserSetFragment userSetFragment;
                userSetFragment = (UserSetFragment) (fm.findFragmentByTag("tab4"));
                if (null != imagPath) {
                    userSetFragment.chageHeadIcon(imagPath);
//                    UserManager.getInstance().setHeadPath(imagPath);
                }
            } else if (!NetUtils.isNetworkAvailable(this)) {
                ToastUtil.showLong(this, "没网啦");
            } else {
                ToastUtil.showLong(this, "上传失败");
            }
//        } else if (object != null && action == Action.UPDTE_VERSION) {
//            final UpdateVersionBean versionBean = (UpdateVersionBean) object;
//            if (versionBean.getCode().equals(Constant.RESULT_SUCESS)) {
//                if (DeviceUtils.getVersionCode(this) < Integer.parseInt(versionBean.getMap().getVersionCode())) {
//                    update_dialog = new UpdateVersionDialog(this, "发现新版本了", new UpdateVersionDialog.OnSureClickedLisener() {
//                        @Override
//                        public void onSureClick() {
//                            receiver = new UpdateProgressBroadcastReceiver();
//                            IntentFilter filter = new IntentFilter();
//                            filter.addAction(Constant.DOWNLOADMANAGEACTION);
//                            registerReceiver(receiver, filter);
//                            update_dialog.setSureText("下载中……");
//                            //                        ToastUtil.showLong(HomeActivity.this, "正在检查更新...");
//                            // 开启更新服务UpdateService
//                            // 这里为了把update更好模块化，可以传一些updateService依赖的值
//                            // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
//                            Intent intent = new Intent();
//                            intent.putExtra("downloadUrl", versionBean.getMap().getDownloadPath());
//                            intent.putExtra("name", FileUtils.getFileName(versionBean.getMap().getDownloadPath()));
////                            intent.putExtra("flag", "startDownload");
//                            intent.setClass(HomeActivity.this, VersionDownload.class);
//                            startService(intent);
//                        }
//                    }, !versionBean.getMap().isForceUpdate());
//                    update_dialog.show();
//                }else{
//                    if(update_dialog.isShowing()){
////                        update_dialog.dismiss();
//                        update_dialog.cancel();
//                    }
//                }
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri head = data.getData();
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        sb.append(p);
                    }
                    String tempStr = sb.toString();
                    fileName = tempStr.substring(tempStr.lastIndexOf("/") + 1);
                    file = new File(sb.toString());
                    photoUtils = new UpLoadPhotoUtils(this);
                    photoUtils.startPhotoZoom(Uri.fromFile(file));
                }
                break;
            case CROP_CODE:
                if (resultCode == RESULT_OK) {
//                    Bundle extras = data.getExtras();//拿到bitmap，占用内存太大
//                    if (headUri != null) {
//                        Bitmap photo = extras.getParcelable("data");
//                        photo = PictureFileUtils.zoomImage(photo, 150, 150);
//                        Api.upLoadPicture(new String(Base64.encode(FileUtils.Bitmap2Bytes(photo), Base64.DEFAULT)), fileName, this);
//                    }else{
//                        LogUtils.e("剪裁时出错了");
//                    }
                    Bitmap photo = null;
                    try {
//                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(),new UpLoadPhotoUtils(this).getPath());
//                        photo = PictureFileUtils.zoomImage(photo, 150, 150);
                        if (!TextUtils.isEmpty(photoUtils.getPath())) {
                            File file = PictureFileUtils.compressImage(photoUtils.getPath(), 80);
                            photo = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            photo = PictureFileUtils.zoomImage(photo, 150, 150);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Api.upLoadPicture(new String(Base64.encode(FileUtils.Bitmap2Bytes(photo), Base64.DEFAULT)), photoUtils.getPath().substring(photoUtils.getPath().lastIndexOf("/") + 1), this);
                }
                break;
        }
        if (resultCode == 0x111) {//门店返回id。进入首页
            initFragment();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showLong(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义广播接收者，时时接受下载进度值
     */
    class UpdateProgressBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progress", 0);
            LogUtils.e("接受到的下载的进度===", progress + "");
            Message msg = mHandler.obtainMessage();
            msg.what = 0x10;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            System.out.println((info.flags& ApplicationInfo.FLAG_DEBUGGABLE)!=0);
            return (info.flags& ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }
}
