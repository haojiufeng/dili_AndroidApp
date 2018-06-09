package com.diligroup.my.activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.OtherRequestBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.home.HomeActivity;
import com.diligroup.my.adapter.OtherRequestAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.StringUtils;
import com.diligroup.utils.UserManager;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Request;

public class ReportOther extends BaseActivity implements AdapterView.OnItemClickListener, OtherRequestAdapter.OnWeightChangeListener, View.OnClickListener {
    String otherTarget = "0";//上传服务器做不同需求区分
    int beginWeight;//开始体重

//    @Bind(R.id.bt_report_user)
    Button bt_other;//确定按钮
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.other_request_list)
    ListView other_request_list;//
    private String currentWeight;//当前体重字符串
    private String otherReq;////表示选中的是哪一项
    private OtherRequestAdapter myAdapter;
    private OtherRequestBean bean;//后台其他需求bean
    private float targetWeight;//目标体重

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.other_request;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("其他需求");
        title_infos.setText("请选择要达到的其他目的");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currentWeight = UserManager.getInstance().getNowWeight();
        String tempWeight=getIntent().getStringExtra("targetWeight");
        targetWeight=Float.parseFloat( TextUtils.isEmpty(tempWeight)?"0.0":tempWeight);
       if(targetWeight==0){//没有目标体重的话，根据性别选择默认目标体重
        targetWeight = UserManager.getInstance().getSex().equals("0") ? Long.parseLong("50") : Long.parseLong("70");
       }
        Api.getOtherRequest(this);
        otherReq = getIntent().getStringExtra("otherRequest");
        other_request_list.setOnItemClickListener(this);

        View rootView=View.inflate(this,R.layout.my_info_bottomview,null);
        bt_other= (Button) rootView.findViewById(R.id.bt_commit_work);
        other_request_list.addFooterView(rootView);
        bt_other.setOnClickListener(this);
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_other.setText("完成");
            AppManager.getAppManager().addActivity(this);
        }
    }
    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.UPDATA_USERINFO && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                Intent intent = new Intent();
                intent.putExtra("otherRequest", otherReq);
                intent.putExtra("targetWeight",targetWeight+"");
                setResult(0x120, intent);
                this.finish();
            }
        } else if (action == Action.SET_INFOS && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                AppManager.getAppManager().finishAllActivity();
                readyGo(HomeActivity.class);
                UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
            }
        } else if (object != null && action == Action.GET_OTHER_REQ) {
            bean = (OtherRequestBean) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                for (int i = 0; i < bean.getList().size(); i++) {
                    if (bean.getList().get(i).getDictName().equals(otherReq)) {
                        bean.getList().get(i).setChecked(true);
                    }
                }
                if (myAdapter == null) {
                    myAdapter = new OtherRequestAdapter(this, bean.getList(), this,targetWeight);
                    other_request_list.setAdapter(myAdapter);
                } else {
                    myAdapter.setDate(bean.getList());
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (bean.getList().get(position).isChecked()) {
            bean.getList().get(position).setChecked(false);
            otherReq = "";
        } else {
            bean.getList().get(position).setChecked(true);
            for (int i = 0; i < bean.getList().size(); i++) {
                if (i != position) {
                    bean.getList().get(i).setChecked(false);
                }
            }
            //1.增重需求 2.减重需求 0.默认(除了增重减重的其他需求)
            if (bean.getList().get(position).getCode().contains("250002") || bean.getList().get(position).getDictName().contains("增重") || bean.getList().get(position).getDictName().contains("增脂")) {
                otherTarget = "1";
            } else if (bean.getList().get(position).getCode().equals("250001") || bean.getList().get(position).getDictName().contains("减重") || bean.getList().get(position).getDictName().contains("减脂")) {
                otherTarget = "2";
            } else {
                otherTarget = "3";
            }
            otherReq = bean.getList().get(position).getDictName();
        }
        myAdapter.setDate(bean.getList());
    }

    @Override
    public float getCurrentWeight(float weight) {//监听标尺滚动变化
        targetWeight = weight;
        return weight;
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scroll();
//            }
//        }, 100);
//    }

//    private void scroll() {
//        weightRuler.smoothScrollTo((1970 - beginWeight) * 20, 0);
//    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_commit_work:
                String otherReqCode = "";
                if (!TextUtils.isEmpty(otherReq)) {
                    for (int i = 0; i < bean.getList().size(); i++) {
                        if (bean.getList().get(i).getDictName().equals(otherReq)) {
                            otherReqCode = bean.getList().get(i).getCode();
                        }
                    }
                }
                if (UserManager.getInstance().isFirstRecordInfo()) {
                    UserInfoBean.getInstance().setReqType(String.valueOf(otherTarget));// 1.增重需求 2.减重需求 0.默认(除了增重减重的其他需求)
                    UserInfoBean.getInstance().setOtherReq(otherReqCode);//其他需求（数据字典）code 值
                    if (otherTarget.equals("2")) {
                        UserInfoBean.getInstance().setTargetWeight(targetWeight + "");
                    } else {
                        UserInfoBean.getInstance().setTargetWeight("");
                    }
                    Api.setUserInfo(this);
                } else {
                    if (otherTarget != null) {
                        HashMap<String, String> map = new HashMap<>();
                        if (otherTarget.equals("2")) {
                            map.put("targetWeight", StringUtils.DecimalTwoFloat(targetWeight) + "");
                        } else {
                            map.put("targetWeight", "");
                        }
                        map.put("reqType", String.valueOf(otherTarget));//1.增重需求 2.减重需求 0.默认(除了增重减重的其他需求)
                        map.put("weight", currentWeight);
                        map.put("otherReq", otherReqCode);
                        Api.updataUserInfo(map, this);
                    }
                }
                break;
        }
    }
}
