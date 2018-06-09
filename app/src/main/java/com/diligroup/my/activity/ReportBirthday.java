package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.BirthdayUtils;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.view.WheelView;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报 生日
 */
public class ReportBirthday extends BaseActivity {
    @Bind(R.id.wv_height)
    WheelView wheelView;
    @Bind(R.id.wv_height2)
    WheelView wheelView2;
    @Bind(R.id.wv_height3)
    WheelView wheelView3;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;


    String year = "1972";
    String month = "5";
    String day = "5";
    String brithday;//录入的生日信息
    private String periodStartTime;//手动修改信息，与服务器后台数据同步
    private String periodEndTime;
    private String periodNum;
    private String specialCrowdCode;

    //    int currentYear;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_birthday;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {
    }

    @Override
    protected void initViewAndData() {
        ivBack.setVisibility(View.VISIBLE);
        tv_title.setText("基础信息");
        title_infos.setText("您的生日？");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WindowManager wm = this.getWindowManager();
        brithday = getIntent().getStringExtra("birthday");
        periodStartTime = getIntent().getStringExtra("periodStartTime");
        periodEndTime = getIntent().getStringExtra("periodEndTime");
        periodNum = getIntent().getStringExtra("periodNum");
        specialCrowdCode = getIntent().getStringExtra("specialCrowdCode");
        if (brithday != null) {
            year = brithday.split("-")[0];
            month = brithday.split("-")[1];
            day = brithday.split("-")[2];
        }
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_birthday.setText("下一步");
        }
        ViewGroup.LayoutParams params = wheelView.getLayoutParams();
        params.height = wheelView.getHeight();
        params.width = wm.getDefaultDisplay().getWidth() / 3;
        wheelView.setLayoutParams(params);
        wheelView2.setLayoutParams(params);
        wheelView3.setLayoutParams(params);
        wheelView.setOffset(3);
        wheelView2.setOffset(3);
        wheelView3.setOffset(3);

        wheelView.setSeletion(2);
        wheelView2.setSeletion(2);
        wheelView3.setSeletion(2);

        wheelView.setItems(BirthdayUtils.getYears());

        wheelView2.setItems(BirthdayUtils.getMonth());
        wheelView3.setItems(BirthdayUtils.get31Day());
//        滚动到指定位置
        for (int i = 0; i < BirthdayUtils.getYears().size(); i++) {
            if (BirthdayUtils.getYears().get(i).replace("年", "").equals(year)) {
                wheelView.setSeletion(i);
                break;
            }
        }
        for (int i = 0; i < BirthdayUtils.getMonth().size(); i++) {
            if (BirthdayUtils.getMonth().get(i).replace("月", "").equals(month)) {
                wheelView2.setSeletion(i);
                break;
            }
        }
        for (int i = 0; i < BirthdayUtils.get31Day().size(); i++) {
            if (BirthdayUtils.get31Day().get(i).replace("日", "").equals(day)) {
                wheelView3.setSeletion(i);
                break;
            }
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                year = item.replace("年", "");
            }
        });

        wheelView2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                month = item.replace("月", "");
                if (item.equals("1月") || item.equals("3月") || item.equals("5月") || item.equals("7月") || item.equals("8月") || item.equals("10月") || item.equals("12月")) {
//                    wheelView3.setItems(BirthdayUtils.get31Day());
//                    wheelView3.notify();
                }
                if (item.equals("4月") || item.equals("6月") || item.equals("9月") || item.equals("11月")) {
//                    wheelView3.setItems(BirthdayUtils.get30Day());
//                    wheelView3.notify();
                }
                if (item.equals("2月")) {
//                    wheelView3.setItems(BirthdayUtils.get29Day());
//                    wheelView3.notify();
                }
            }
        });
        wheelView3.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                day = item.replace("日", "");
            }
        });
    }
//确定键
    @Bind(R.id.bt_getTime)
    Button bt_birthday;

    @OnClick(R.id.bt_getTime)
    public void getBirthday() {
        brithday = year + "-" + month + "-" + day;
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            UserInfoBean.getInstance().setBirthday(DateUtils.dateFormatChanged_2(brithday.trim()));
            UserInfoBean.getInstance().setAge(nowYear - Integer.parseInt(year));
            readyGo(ReportWork.class);
        } else {
            HashMap map = new HashMap();
            map.put("birthday", DateUtils.dateFormatChanged_2(brithday));
            //年龄>65岁，上传职业为重体力的，修改为“其他”职业
            if (UserManager.getInstance().getSex().equals("0") && nowYear - Integer.parseInt(year) >= 9 && nowYear - Integer.parseInt(year) <50 && TextUtils.isEmpty(specialCrowdCode)) {
//                map.put("specialCrowdCode", "");
//                map.put("periodStartTime", "");
//                map.put("periodEndTime", "");
//                map.put("periodNum", "");
            }else{
                map.put("specialCrowdCode", "");
                map.put("periodStartTime", "");
                map.put("periodEndTime", "");
                map.put("periodNum", "");
            }
            Api.updataUserInfo(map, this);
        }
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
                intent.putExtra("brithday", brithday);
                if (!TextUtils.isEmpty(brithday)) {
                    int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(brithday.split("-")[0]);
                    intent.putExtra("age", age);
                }
                setResult(0x10, intent);
                this.finish();

            }
        }
    }
}
