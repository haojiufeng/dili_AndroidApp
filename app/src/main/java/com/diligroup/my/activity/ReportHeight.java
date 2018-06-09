package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.view.WheelView;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报身高
 */
public class ReportHeight extends BaseActivity {
    @Bind(R.id.wv_height)
    WheelView wheelView;
    String selectHeight;
//    Boolean isFrist;
//    Bundle bundle;
    @Bind(R.id.bt_ok_height)
    Button bt_height;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    private String sex;//录入的性别
    private String height;//录入的身高

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_height;
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
        title_infos.setText("您的身高?");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        Intent intent = getIntent();
//        bundle = intent.getExtras();
//        isFrist = bundle.getBoolean("isFrist");
        sex = getIntent().getStringExtra("sex");
        height = getIntent().getStringExtra("height");
        wheelView.setOffset(3);
        String[] height_arrs = getResources().getStringArray(R.array.height);
        wheelView.setItems(Arrays.asList(height_arrs));
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_height.setText("下一步");
            if (UserInfoBean.getInstance().getSex().equals("0")) {
                wheelView.setSeletion(13);
                selectHeight = "163";
            } else {
                wheelView.setSeletion(25);
                selectHeight = "175";
            }
        } else {
            if (!TextUtils.isEmpty(sex) && !TextUtils.isEmpty(height)) {
                selectHeight = height;
                for (int i = 0; i < height_arrs.length; i++) {
                    if ((selectHeight + " cm").equals(height_arrs[i])) {
                        wheelView.setSeletion(i);
                        break;
                    }
                }
            }
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                selectHeight = item.replace(" ", "").replace("cm", "");
                Log.d("====================", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
    }

    @OnClick(R.id.bt_ok_height)
    public void reprotHeight() {
        LogUtils.e("身高======" + selectHeight);
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            UserInfoBean.getInstance().setHeight(selectHeight);
            readyGo(ReportWeight.class);

        } else {
            HashMap map = new HashMap();
            map.put("height", selectHeight);
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
                intent.putExtra("height", selectHeight);
                setResult(0x30, intent);
                this.finish();
            }
        }
    }
}
