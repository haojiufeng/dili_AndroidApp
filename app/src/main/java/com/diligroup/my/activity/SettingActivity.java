package com.diligroup.my.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.UpdateVersionBean;
import com.diligroup.dialog.ReminderDialog;
import com.diligroup.login.LoginActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.DeviceUtils;
import com.diligroup.utils.FileUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.service.VersionDownload;

import butterknife.Bind;
import okhttp3.Request;

/**
 * Created by Kevin on 2016/6/21.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.rl_clearcache)
    RelativeLayout rl_clearCache;//清楚缓存
    @Bind(R.id.bt_exit)
    Button bt_exit;
    @Bind(R.id.settings_instructions)
    RelativeLayout settings_instructions;//使用说明
    @Bind(R.id.check_version)
    RelativeLayout check_version;//检查更新

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.user_setting;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.UPDTE_VERSION) {
            UpdateVersionBean bean = (UpdateVersionBean) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS) && DeviceUtils.getVersionCode(this) == (Integer.parseInt(bean.getMap().getVersionCode()))) {
                ToastUtil.showLong(SettingActivity.this, "您的版本是最新版");
                return;
            } else if (DeviceUtils.getVersionCode(this) < (Integer.parseInt(bean.getMap().getVersionCode()))) {
                // 开启更新服务UpdateService
                // 这里为了把update更好模块化，可以传一些updateService依赖的值
                // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, VersionDownload.class);
                intent.putExtra("downloadUrl", ((UpdateVersionBean) object).getMap().getDownloadPath());
                intent.putExtra("name", FileUtils.getFileName(((UpdateVersionBean) object).getMap().getDownloadPath()));
//                intent.putExtra("flag", "startDownload");
                startService(intent);
            }
        }
    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("设置");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);

        bt_exit.setOnClickListener(this);
        rl_clearCache.setOnClickListener(this);
        settings_instructions.setOnClickListener(this);
        check_version.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_exit:
                final ReminderDialog dialog = new ReminderDialog(this, "确定要退出登录吗？", new ReminderDialog.OnSureClickedLisener() {
                    @Override
                    public void onSureClick() {
                        UserManager.clear();
                        Intent mIntent = new Intent(SettingActivity.this, LoginActivity.class);
                        mIntent.putExtra("exit", true);
                        startActivity(mIntent);
                        AppManager.getAppManager().finishActivity(SettingActivity.this);
                    }
                });
                dialog.show();
                break;
            case R.id.rl_clearcache://清楚缓存
                ReminderDialog dialog1 = new ReminderDialog(this, "确定要清除缓存吗？", new ReminderDialog.OnSureClickedLisener() {
                    @Override
                    public void onSureClick() {
                        //清楚缓存操作
                        UserManager.getInstance().clear();
                    }
                });
                dialog1.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.settings_instructions://使用说哦名
                startActivity(new Intent(this, InstructionsActivity.class));
                break;
            case R.id.check_version://版本更新
                ReminderDialog update_dialog = new ReminderDialog(this, "确定要更新版本吗？", new ReminderDialog.OnSureClickedLisener() {
                    @Override
                    public void onSureClick() {
                        ToastUtil.showLong(SettingActivity.this, "正在检查更新...");
                        Api.updateVersion(SettingActivity.this);
                    }
                });
                update_dialog.show();
                break;
        }
    }
}
