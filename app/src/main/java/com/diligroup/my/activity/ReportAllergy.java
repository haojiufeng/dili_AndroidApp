package com.diligroup.my.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.diligroup.bean.GetAllergyDetailBean;
import com.diligroup.bean.GetFoodTypeBean;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.my.adapter.AllergyAdapter;
import com.diligroup.my.adapter.AllergyFoodRightAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.FoodTypeUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.StringUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.view.DividerItemDecoration;
import com.diligroup.view.FlowLayout;
import com.diligroup.view.TagAdapter;
import com.diligroup.view.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报 过敏 食材
 */
public class ReportAllergy extends BaseActivity implements MyItemClickListener {

    @Bind(R.id.list_foods_detail)
    ListView lv_foodDetail;
    AllergyFoodRightAdapter adapter;
    @Bind(R.id.tag_allergy)
    TagFlowLayout taglayout;
    List<String> foodIdList;
    TagAdapter tagAdapter;
    List<GetAllergyDetailBean.ListBigBean> allergyList;//后台返回的过敏原集合
    String allergyName = "";//用户选择的过敏原的名称集合
    List<String> foodNameList;//用户选择的过敏原的名称集合
    //    ViewHolder foodHolder;a
    @Bind(R.id.typwleft_listView)
    RecyclerView rv_left;
    AllergyAdapter allergyAdapter;
    List<GetFoodTypeBean> typeNameList;
    DividerItemDecoration dividerItemDecoration;
    @Bind(R.id.bt_commit_allergy)
    Button bt_allergy;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    @Bind(R.id.allergy_rootview)
    LinearLayout allergy_rootview;
    private int currentLeftIndex = 0;//默认左侧选中item
    private ViewSwitcherHelper helper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report_allergy;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    /**
     * 上报按钮
     **/
    @OnClick(R.id.bt_commit_allergy)
    public void reportAllergy() {
        if (UserManager.getInstance().isFirstRecordInfo()) {
            if (StringUtils.listToString(foodNameList, ',').length() > 1) {
                UserInfoBean.getInstance().setAllergyFood(StringUtils.listToString(foodNameList, ','));
            } else {
                UserInfoBean.getInstance().setAllergyFood("");
            }
            readyGo(ReportWhere.class);
            return;
        }
        HashMap map = new HashMap();
        if (StringUtils.listToString(foodNameList, ',').length() > 1) {
            map.put("allergyName", StringUtils.listToString(foodNameList, ','));
        } else {
            map.put("allergyName", "");//么有任何选中项
        }
        Api.updataUserInfo(map, this);
    }


    @Override
    protected void initViewAndData() {
        tv_title.setText("过敏食材");
        ivBack.setVisibility(View.VISIBLE);
        helper = new ViewSwitcherHelper(this, allergy_rootview);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allergyName = getIntent().getStringExtra("allergyStr");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_allergy.setText("下一步");
            AppManager.getAppManager().addActivity(this);
        }
        typeNameList = FoodTypeUtils.GetFoodTypeList();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_left.setLayoutManager(layoutManager);
        CommonUtils.initRerecyelerView(this, rv_left, 1);

        foodIdList = new ArrayList<>();
        foodNameList = new ArrayList<>();

        helper.showLoading();
        Api.getAllergyDetails(this);
        lv_foodDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (allergyList.get(currentLeftIndex).getList().get(position).isSelected()) {
                    allergyList.get(currentLeftIndex).getList().get(position).setSelected(false);
                    removeUnChecked(allergyList.get(currentLeftIndex).getList().get(position).getAllergyName());
                } else {
                    allergyList.get(currentLeftIndex).getList().get(position).setSelected(true);
                    if (!foodNameList.contains(allergyList.get(currentLeftIndex).getList().get(position).getAllergyName())) {
                        foodNameList.add(allergyList.get(currentLeftIndex).getList().get(position).getAllergyName());
                    }
                }
                adapter.setDate(allergyList.get(currentLeftIndex).getList());
                setTagLayout();
                tagAdapter.notifyDataChanged();
                taglayout.setVisibility(View.VISIBLE);
            }
        });
    }

    //移除删除掉的标签
    public void removeUnChecked(String foodName) {
        if (foodNameList.size() > 0) {
            for (int i = 0; i < foodNameList.size(); i++) {
                if (foodNameList.get(i).equals(foodName)) {
                    foodNameList.remove(foodNameList.get(i));
                }
            }
        }
        if (foodNameList.size() == 0) {
            taglayout.setVisibility(View.GONE);
        }
    }

    //选中的过敏食材布局
    private void setTagLayout() {
        tagAdapter = new TagAdapter(foodNameList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) LayoutInflater.from(ReportAllergy.this).inflate(R.layout.tv,
                        taglayout, false);
                tv.setText(o.toString());
                return tv;
            }

            @Override
            public void setSelectedList(Set set) {
                super.setSelectedList(set);
            }
        };
        taglayout.setAdapter(tagAdapter);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        helper.showError(request, action, this);
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.GET_ALLERGY_DETAILS) {
            GetAllergyDetailBean allergyDetailBean = (GetAllergyDetailBean) object;
            if (allergyDetailBean.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                allergyList = allergyDetailBean.getList();
                if (allergyAdapter == null) {
                    allergyAdapter = new AllergyAdapter(this, allergyList, this);
                    rv_left.setAdapter(allergyAdapter);
                }
                allergyAdapter.selectPosion(0);
                if (allergyName != null) {//找出集合中用户选择的过敏原，然后让其checked
                    String[] temp = allergyName.split(",");
                    for (int i = 0; i < allergyList.size(); i++) {
                        for (int j = 0; j < allergyList.get(i).getList().size(); j++) {
                            if (Arrays.asList(temp).contains(allergyList.get(i).getList().get(j).getAllergyName()) && !foodNameList.contains(allergyList.get(i).getList().get(j).getAllergyName())) {
                                foodNameList.add(allergyList.get(i).getList().get(j).getAllergyName());
                                allergyList.get(i).getList().get(j).setSelected(true);
                            }
                        }
                    }
                }
                if (adapter == null) {
                    adapter = new AllergyFoodRightAdapter(ReportAllergy.this, allergyList.get(0).getList());
                    lv_foodDetail.setAdapter(adapter);
                } else {
                    adapter.setDate(allergyList.get(0).getList());
                }
            } else {
                helper.showError(request, action, this);
            }
        } else if (action == Action.UPDATA_USERINFO && object != null) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("allergyList", StringUtils.listToString(foodNameList, ','));
                setResult(0x60, intent);
                this.finish();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (adapter != null && position < allergyList.size()) {
            adapter.setDate(allergyList.get(position).getList());
            currentLeftIndex = position;
        }
    }
}
