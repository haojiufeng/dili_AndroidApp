package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
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
 * 上报 口味
 */
public class ReportTaste extends BaseActivity {
    @Bind(R.id.lv_taste)
    ListView lv_taste;//列表
    GetJiaoQinBean tasteBean;
    private ArrayList<GetJiaoQinBean.ListBean> id_list = new ArrayList<>();
    ;//用户选择的口味
    List<GetJiaoQinBean.ListBean> tasteBean_list;
    @Bind(R.id.bt_jump_taste)
    Button bt_later_report;
    @Bind(R.id.bt_report_taste)
    Button bt_taste;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.taste_rootview)
    LinearLayout taste_rootview;
    private String tasteName;//用户选择的口味字符串
    private ListitemAdapter adapter;//口味适配器
    private ViewSwitcherHelper helper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_taste;
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

        helper = new ViewSwitcherHelper(this, taste_rootview);
        helper.showLoading();
        Api.getTaste(this);
        tv_title.setText("口味");
        title_infos.setText("请选择您的口味喜好");
//        isFrist = bundle.getBoolean("isFrist");
        tasteName = getIntent().getStringExtra("taste");

        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            bt_taste.setText("下一步");
            bt_later_report.setVisibility(View.VISIBLE);
        } else {
            bt_later_report.setVisibility(View.GONE);
        }

        lv_taste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tasteBean_list.get(position).isChecked()) {
                    tasteBean_list.get(position).setChecked(false);
                    removeUnChecked(tasteBean_list.get(position).getDictName());
                } else {
                    tasteBean_list.get(position).setChecked(true);
                    if (id_list != null && !id_list.contains(tasteBean_list.get(position))) {
                        id_list.add(tasteBean_list.get(position));
                    }
                }
                adapter.setDate(tasteBean_list);
            }
        });
    }

    public void removeUnChecked(String foodName) {
        for (int i = 0; i < id_list.size(); i++) {
            if (id_list.get(i).getDictName().equals(foodName)) {
                id_list.remove(id_list.get(i));
            }
        }
    }

    @OnClick(R.id.bt_report_taste)
    public void ReporTaste() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id_list.size(); i++) {
            sb.append(id_list.get(i).getCode() + ",");
        }
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            if (sb.toString().trim().length() > 1) {
                UserInfoBean.getInstance().setTasteCode(sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
            } else {
                UserInfoBean.getInstance().setTasteCode("");
            }
            readyGo(ReportHistory.class);
            return;
        }
        HashMap map = new HashMap();
        if (sb.toString().trim().length() > 1) {
            map.put("tasteCode", sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
        } else {
            map.put("tasteCode", "");
        }
        Api.updataUserInfo(map, this);
    }

    @OnClick(R.id.bt_jump_taste)
    public void jumpTaste() {
        Api.setUserInfo(this);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        if (!NetUtils.isMobileConnected(this)) {
            helper.showError(request, action, this);
        } else {
            helper.showNotify("数据加载失败，请稍后重试");
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.GET_TASTE) {
            tasteBean = (GetJiaoQinBean) object;
            if (tasteBean.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                tasteBean_list = tasteBean.getList();
                adapter = new ListitemAdapter(this, tasteBean_list);
                lv_taste.setAdapter(adapter);
                if (!TextUtils.isEmpty(tasteName)) {//我的资料传递信息加进集合，用户没有点击，也能正常回传
                    for (int i = 0; i < tasteBean_list.size(); i++) {
                        if (Arrays.asList(tasteName.split(",")).contains(tasteBean_list.get(i).getDictName()) && !id_list.contains(tasteBean_list.get(i))) {
                            id_list.add(tasteBean_list.get(i));
                            tasteBean_list.get(i).setChecked(true);
                        }
                    }
                }
            } else {
                helper.showNotify(tasteBean.getMessage());
            }
        } else if (action == Action.UPDATA_USERINFO && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                Intent intent = new Intent();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < id_list.size(); i++) {
                    sb.append(id_list.get(i).getDictName() + ",");
                }
                if (sb.toString().trim().length() > 1) {
                    intent.putExtra("taste", sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
                }
                setResult(0x90, intent);
                this.finish();
            }else{
                helper.showNotify(commonBean.getMessage());
            }
        } else if (object != null && action == Action.SET_INFOS) {//上传所有信息
            if (((CommonBean) object).getCode().equals(Constant.RESULT_SUCESS)) {
                UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
                AppManager.getAppManager().finishAllActivity();
                readyGo(HomeActivity.class);
            }
        }
    }
}
