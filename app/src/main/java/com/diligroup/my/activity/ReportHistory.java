package com.diligroup.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.diligroup.utils.ViewSwitcherHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报健康史
 */
public class ReportHistory extends BaseActivity {
    @Bind(R.id.lv_history)
    ListView lv_history;
    GetJiaoQinBean historyBean;
    private ArrayList<GetJiaoQinBean.ListBean> id_list = new ArrayList<>();//存储用户选中项
    List<GetJiaoQinBean.ListBean> hisList;
    @Bind(R.id.bt_jump_history)
    Button bt_later_report;
    @Bind(R.id.bt_report_history)
    Button bt_history;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.history_rootview)
    LinearLayout history_rootview;
    private String healthyHository;// 对应的name值
    private ListitemAdapter adapter;
    private ViewSwitcherHelper helper;

    @OnClick(R.id.bt_report_history)
    public void ReportHisty() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id_list.size(); i++) {
            sb.append(id_list.get(i).getCode() + ",");
        }
        if (UserManager.getInstance().isFirstRecordInfo()) {
            if (sb.toString().trim().length() > 1) {
                UserInfoBean.getInstance().setChronicDiseaseCode(sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
            } else {
                UserInfoBean.getInstance().setChronicDiseaseCode("");
            }
            if (UserInfoBean.getInstance().getSex().equals("0") && UserInfoBean.getInstance().getAge() >= 18 && UserInfoBean.getInstance().getAge() <= 50) {
                readyGo(ReportSpecial.class);
            } else {
                readyGo(ReportOther.class);
            }
        } else {
            HashMap map = new HashMap();
            if (sb.toString().trim().length() > 1) {
                map.put("chronicDiseaseCode", sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
            } else {
                map.put("chronicDiseaseCode", "");
            }
            Api.updataUserInfo(map, this);
        }
    }

    @OnClick(R.id.bt_jump_history)
    public void jumpHistory() {
        Api.setUserInfo(this);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report_history;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("健康史");
        title_infos.setText("请选择您的历史健康记录");

        helper = new ViewSwitcherHelper(this, history_rootview);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helper.showLoading();
        Api.getHistory(this);
        healthyHository = getIntent().getStringExtra("healthyHository");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_history.setText("下一步");
            bt_later_report.setVisibility(View.VISIBLE);
            AppManager.getAppManager().addActivity(this);
        } else {
            bt_later_report.setVisibility(View.GONE);

        }
        hisList = new ArrayList<>();
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hisList.get(position).isChecked()) {
                    hisList.get(position).setChecked(false);
                    removeUnChecked(hisList.get(position).getDictName());
                } else {
                    hisList.get(position).setChecked(true);
                    if (!id_list.contains(hisList.get(position))) {
                        id_list.add(hisList.get(position));
                    }
                }
                adapter.setDate(hisList);
            }
        });
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        if (!NetUtils.isMobileConnected(this)) {
            helper.showError(request, action, this);
        } else {
            helper.showNotify("数据加载失败，请稍后重试");
        }
    }

    /**
     * 根据名称删除bean
     *
     * @param foodName
     */
    public void removeUnChecked(String foodName) {
        if (id_list.size() > 0) {
            for (int i = 0; i < id_list.size(); i++) {
                if (id_list.get(i).getDictName().equals(foodName)) {
                    id_list.remove(id_list.get(i));
                }
            }
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.GET_HISTORY) {
            historyBean = (GetJiaoQinBean) object;
            if (historyBean.getCode().equals(Constant.RESULT_SUCESS))
            {
                helper.showContent();
                hisList = historyBean.getList();
                if (healthyHository != null && healthyHository.length() > 1) {
                    String[] temp = healthyHository.split(",");
                    for (int i = 0; i < hisList.size(); i++) {
                        if (Arrays.asList(temp).contains(hisList.get(i).getDictName()) && !id_list.contains(hisList.get(i))) {
                            id_list.add(hisList.get(i));
                            hisList.get(i).setChecked(true);
                        }
                    }
                }
                adapter = new ListitemAdapter(this, hisList);
                lv_history.setAdapter(adapter);
            }else{
                helper.showNotify(historyBean.getMessage());
            }
        }
        if (object != null && action == Action.UPDATA_USERINFO) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                Intent intent = new Intent();
                intent.putExtra("history", id_list);
                setResult(0x100, intent);
                this.finish();

            }
        } else if (object != null && action == Action.SET_INFOS) {
            if (((CommonBean) object).getCode().equals(Constant.RESULT_SUCESS)) {
                AppManager.getAppManager().finishAllActivity();
                UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
                readyGo(HomeActivity.class);
            }
        }
    }
}
