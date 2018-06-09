package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/***
 * 食物禁忌上报
 */
public class ReportNoeat extends BaseActivity {

    @Bind(R.id.list_no_eat)
    ListView lv_noeat;
    List<GetJiaoQinBean.ListBean> datalist;//后台返回禁忌列表
    ArrayList<String> noeat_list;//用户选择的禁忌列表
    GetJiaoQinBean getJiaoQinBean;
    @Bind(R.id.bt_ok_noeat)
    Button bt_noEat;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.rooview)
    RelativeLayout rooview;
    private ListitemAdapter listitemAdapter;
    private String tabooCode;
    private ViewSwitcherHelper helper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_noeat;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    protected void initViewAndData() {
        tv_title.setText("饮食禁忌");
        title_infos.setText("请选择您的饮食禁忌");
        helper = new ViewSwitcherHelper(this, rooview);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helper.showLoading();
        Api.getNoEatFood(this);
        tabooCode = getIntent().getStringExtra("tabooCode");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_noEat.setText("下一步");
            AppManager.getAppManager().addActivity(this);
        }
        datalist = new ArrayList<>();
        noeat_list = new ArrayList<>();
        lv_noeat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datalist.get(position).isChecked()) {
                    datalist.get(position).setChecked(false);
                    noeat_list.remove(datalist.get(position).getCode());
                } else {
                    datalist.get(position).setChecked(true);
                    noeat_list.add(datalist.get(position).getCode());
                }
                listitemAdapter.setDate(datalist);
            }
        });
    }

    @OnClick(R.id.bt_ok_noeat)
    public void reportNoeat() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datalist.size(); i++) {
            if (datalist.get(i).isChecked()) {
                sb.append(datalist.get(i).getCode() + ",");
            }
        }

        if (UserManager.getInstance().isFirstRecordInfo()) {//如果是第一次录入用户信息
            if (sb.toString().trim().length() > 1) {
                UserInfoBean.getInstance().setNoEatFood(sb.toString().trim().substring(0, sb.length() - 1));
            } else {
                UserInfoBean.getInstance().setNoEatFood("");
            }
            readyGo(ReportAllergy.class);
        } else {
            HashMap map = new HashMap();
            if (sb.toString().length() > 1) {
                map.put("tabooCode", sb.toString().trim().substring(0, sb.length() - 1));
            } else {
                map.put("tabooCode", "");//代表没有任何选项
            }
            Api.updataUserInfo(map, this);
        }
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        helper.showError(request, action, this);
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.GET_NO_EAT) {
            getJiaoQinBean = (GetJiaoQinBean) object;
            if (getJiaoQinBean.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                if (!TextUtils.isEmpty(tabooCode)) {
                    for (int i = 0; i < getJiaoQinBean.getList().size(); i++) {
                        if (Arrays.asList(tabooCode.split(",")).contains(getJiaoQinBean.getList().get(i).getCode())) {
                            getJiaoQinBean.getList().get(i).setChecked(true);
                            noeat_list.add(getJiaoQinBean.getList().get(i).getCode());
                        }
                    }
                }
                datalist = getJiaoQinBean.getList();
                listitemAdapter = new ListitemAdapter(ReportNoeat.this, datalist);
                lv_noeat.setAdapter(listitemAdapter);
            } else {
                helper.showError(request, action, this);
            }
        } else if (action == Action.UPDATA_USERINFO && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("noeat", noeat_list);
                setResult(0x50, intent);
                this.finish();
            }
        }
    }
}
