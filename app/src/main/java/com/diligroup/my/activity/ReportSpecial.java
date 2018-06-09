package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diligroup.home.HomeActivity;
import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.GetJiaoQinBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.my.adapter.ListitemAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报特殊人群
 * Created by Kevin on 2016/6/20.
 */
public class ReportSpecial extends BaseActivity {
    @Bind(R.id.bt_report_special)
    Button bt_specail;
    @Bind(R.id.bt_jump_special)
    Button bt_later_report;
    private String specialCode;//用户选中的特殊人群
    private ListitemAdapter adapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_special;
    }

    @Bind(R.id.lv_special)
    ListView lv_special;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    GetJiaoQinBean specialBean;

    private GetJiaoQinBean.ListBean selectedBean;
    List<GetJiaoQinBean.ListBean> specList;

    @OnClick(R.id.bt_report_special)
    public void ReprotSpecial() {
        if (UserManager.getInstance().isFirstRecordInfo()) {
            if ((selectedBean != null && TextUtils.isEmpty(selectedBean.getCode())) || selectedBean == null) {
                UserInfoBean.getInstance().setSpecialCrowdCode("");
                if (UserInfoBean.getInstance().getAge() >= 9 && UserInfoBean.getInstance().getAge() < 50) {
                    readyGo(PhysiologicalPeriodActivity.class);
                }else{
                    readyGo(ReportOther.class);
                }
            } else {
                UserInfoBean.getInstance().setSpecialCrowdCode(selectedBean.getCode());
                readyGo(ReportOther.class);
            }
        } else {
            HashMap<String, String> map = new HashMap<>();
            if (selectedBean != null) {
                map.put("specialCrowdCode", selectedBean.getCode());
            } else {
                map.put("specialCrowdCode", "");
                map.put("specialCrowdCode", "");
                map.put("periodStartTime", "");
                map.put("periodEndTime", "");
                map.put("periodNum", "");
            }
            Api.updataUserInfo(map, this);
        }

    }

    @OnClick(R.id.bt_jump_special)
    public void jumpSpecial() {
        Api.setUserInfo(this);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Api.getSpecial(this);
        specialCode = getIntent().getStringExtra("special");
        tv_title.setText("特殊人群");
        title_infos.setText("请选择你当前状态");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            bt_specail.setText("下一步");
            bt_later_report.setVisibility(View.VISIBLE);
        } else {
            bt_later_report.setVisibility(View.GONE);

        }
        specList = new ArrayList<>();
        lv_special.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!specList.get(position).isChecked()) {
                    specList.get(position).setChecked(true);
                    selectedBean = specList.get(position);
                } else {
                    specList.get(position).setChecked(false);
                    selectedBean = null;
                }
                for (int i = 0; i < specList.size(); i++) {
                    if (i != position) {
                        specList.get(i).setChecked(false);
                    }
                }
                adapter.setDate(specList);
            }
        });
    }

    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {

        if (action == Action.GET_SPECIAL && object != null) {
            specialBean = (GetJiaoQinBean) object;
            if (specialBean.getCode().equals("000000")) {
                specList = specialBean.getList();
                for (int i = 0; i < specList.size(); i++) {
                    if (specList.get(i).getCode().equals(specialCode)) {
                        specList.get(i).setChecked(true);
                        selectedBean = specList.get(i);
                    }
                }
                adapter = new ListitemAdapter(this, specList);
                lv_special.setAdapter(adapter);
            }
        }
        if (object != null && action == Action.UPDATA_USERINFO) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("special", selectedBean);
                setResult(0x110, intent);
                this.finish();
            }
        } else if (object != null && action == Action.SET_INFOS) {
            if (((CommonBean) object).getCode().equals(Constant.RESULT_SUCESS)) {
                UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
                AppManager.getAppManager().finishAllActivity();
                readyGo(HomeActivity.class);
            }
        }
    }
}
