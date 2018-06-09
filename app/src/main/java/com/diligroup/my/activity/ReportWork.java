package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.GetJobBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.my.adapter.SelectWorkAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Request;

/**
 * 上报  职业
 * Created by Kevin on 2016/6/16.
 */
public class ReportWork extends BaseActivity implements SelectWorkAdapter.SelectedChangeListener, View.OnClickListener {

//    @Bind(R.id.bt_commit_work)
    Button bt_ok_work;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.select_work_list)
    ListView select_work_list;
    @Bind(R.id.job_rootview)
    LinearLayout job_rootview;
    List<List<GetJobBean.ListBean>> workList = new ArrayList<>();
    private SelectWorkAdapter myAdapter;
    private String jobType;//我的资料中的用户的选择的jobcode值 ,中，轻，重
    private String jobCode;
    private String jobName;
    private ViewSwitcherHelper helper;
    private String birthday;//生日，进而判断年龄

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_work;
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
        title_infos.setText("您的职业?");

        helper = new ViewSwitcherHelper(this, select_work_list);
        ivBack.setVisibility(View.VISIBLE);
        jobType = getIntent().getStringExtra("jobType");
        jobCode = getIntent().getStringExtra("jobCode");
        jobName = getIntent().getStringExtra("jobName");
        birthday = getIntent().getStringExtra("birthday");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helper.showLoading();
        View footView = View.inflate(this, R.layout.my_info_bottomview, null);
        bt_ok_work= (Button) footView.findViewById(R.id.bt_commit_work);
        bt_ok_work.setOnClickListener(this);
        select_work_list.addFooterView(footView);
        Api.getWorkType(this);
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_ok_work.setText("下一步");
        }

    }


    @Override
    public void onError(Request request, Action action, Exception e) {
        helper.showError(request, action, this);
        helper.showNotify("获取职业信息失败");
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.GET_WORK_TYPE) {
            GetJobBean jobdata = (GetJobBean) object;
            if (jobdata.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                if (jobdata.getQlist() != null && jobdata.getQlist().size() > 0) {
                    if (jobdata.getQlist().size() > 0) {
                        workList.add(jobdata.getQlist());
                    }
                    if (jobdata.getZlist().size() > 0) {
                        workList.add(jobdata.getZlist());
                    }
                    if (UserManager.getInstance().isFirstRecordInfo()) {
                        birthday = UserInfoBean.getInstance().getBirthday();
                        //65岁以上的，不显示重体力劳动者
                    }
                    if (!TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(birthday.split("-")[0])) {
                        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
                        if (nowYear - Integer.parseInt(birthday.split("-")[0]) < 65) {
                            if (jobdata.getWlist().size() > 0) {
                                workList.add(jobdata.getWlist());
                            }
                        }
                    }
                    if (myAdapter == null) {
                        myAdapter = new SelectWorkAdapter(ReportWork.this, workList, jobType, jobName, this);
                        select_work_list.setAdapter(myAdapter);
                    } else {
                        myAdapter.setData(workList);
                    }
                }
            }
        } else if (object != null && action == Action.UPDATA_USERINFO) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("jobType", jobType);
                intent.putExtra("jobCode", jobCode);
                intent.putExtra("jobName", jobName);
                setResult(0x20, intent);
                this.finish();
            }
        }
    }

    //item点击变化时候的当前bean对象
    @Override
    public void getCurrentItem(GetJobBean.ListBean bean) {
        jobCode = bean.getCode();
        jobType = bean.getLaborCode();
        jobName = bean.getProfName();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_commit_work:
                if (jobName != null) {
                    if (UserManager.getInstance().isFirstRecordInfo()) {
                        AppManager.getAppManager().addActivity(this);
                        UserInfoBean.getInstance().setJobType(jobType);
                        UserInfoBean.getInstance().setJob(jobCode);
                        readyGo(ReportHeight_1.class);
                    } else {
                        HashMap map = new HashMap();
                        map.put("jobType", jobType);
                        map.put("job", jobCode);
                        Api.updataUserInfo(map, this);
                    }
                } else {
                    ToastUtil.showShort(ReportWork.this, "请选择职业");
                }
                break;
        }
    }
}
