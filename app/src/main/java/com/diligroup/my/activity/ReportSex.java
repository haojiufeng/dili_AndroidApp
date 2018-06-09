package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报性别
 */
public class ReportSex extends BaseActivity {

    @Bind(R.id.ib_boy)
    CheckBox cb_boy;
    @Bind(R.id.ib_gril)
    CheckBox cb_girl;
    @Bind(R.id.bt_ok_sex)
    Button bt_sex;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    private String preSex = "-1";//上一个页面的性别 0 女，1男
    private String birthday;//修改个人信息时候，当前用户年龄
    private String special;//是否是特殊人群

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_sex;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    protected void initViewAndData() {
        tv_title.setText("基础信息");
        title_infos.setText("您的性别?");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            iv_back.setVisibility(View.GONE);
        } else {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        preSex = getIntent().getStringExtra("sex");
        birthday = getIntent().getStringExtra("birthday");
        special = getIntent().getStringExtra("special");
        if (null != preSex && preSex.equals("0")) {
            cb_girl.setChecked(true);
            preSex = "0";
            cb_boy.setChecked(false);
        } else if (null != preSex && preSex.equals("1")) {
            cb_girl.setChecked(false);
            cb_boy.setChecked(true);
            preSex = "1";
        } else {
            preSex = "-1";
        }
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_sex.setText("下一步");
        }
        cb_boy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_girl.setChecked(false);
                    preSex = "1";
                }
            }
        });
        cb_girl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_boy.setChecked(false);
                    preSex = "0";
                }
            }
        });
    }

    @OnClick(R.id.bt_ok_sex)
    public void reportSex() {
        int  birthdayYear=0;
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        if(!TextUtils.isEmpty(birthday)){
        birthdayYear =Integer.parseInt(birthday.split("-")[0]);
        }
        if (!preSex.equals("1") && !preSex.equals("0")) {//没有选择
            ToastUtil.showShort(ReportSex.this, "请选择性别");
        } else {
            if (UserManager.getInstance().isFirstRecordInfo()) {
                AppManager.getAppManager().addActivity(this);
                UserInfoBean.getInstance().setSex(preSex);
                readyGo(ReportBirthday.class);
            } else {
                HashMap<String, String> map = new HashMap();
                map.put("sex", preSex);
                //女性9-50岁，且不在生理期
                if (UserManager.getInstance().getSex().equals("0") && nowYear -birthdayYear>= 9 && nowYear - birthdayYear <50 && TextUtils.isEmpty(special)) {
                } else{
                    map.put("specialCrowdCode", "");
                    map.put("periodStartTime", "");
                    map.put("periodEndTime", "");
                    map.put("periodNum", "");
                }
                Api.updataUserInfo(map, this);
            }
        }
        UserManager.getInstance().setSex(preSex);
    }


    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.UPDATA_USERINFO && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("sex", preSex);
                setResult(0x00, intent);
                this.finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (UserManager.getInstance().isFirstRecordInfo()) {
            return;
        } else {
            finish();
        }
    }
}
