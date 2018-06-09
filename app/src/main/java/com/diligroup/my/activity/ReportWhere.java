package com.diligroup.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.home.HomeActivity;
import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.view.CityPicker;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 上报籍贯
 */
public class ReportWhere extends BaseActivity {
    @Bind(R.id.select_where)
    CityPicker cityPicker;
    @Bind(R.id.bt_ok_where)
    Button bt_where;
    @Bind(R.id.bt_later_where)
    Button bt_later_report;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;
    String provinceCode = "110000";//北京
    String cityCode = "110100";//北京市
    String dictrictCode = "110101";//东城区

    String provinceName="北京";
    String cityName="北京市";
    String dictrictName="东城区";
    private String nameStr;//用户选择的籍贯的省-市-区code
    private String codeStr;//用户选择的籍贯的省-市-区code

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_where;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("籍贯");
        title_infos.setText("请选择您的籍贯");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameStr = getIntent().getStringExtra("where");
        codeStr = getIntent().getStringExtra("whereCode");
        if (!TextUtils.isEmpty(nameStr)) {
            provinceName = nameStr.split("-")[0];
            cityName= nameStr.split("-")[1];
            dictrictName= nameStr.split("-")[2];

//            cityPicker.setProvinceName(provinceName);
//            cityPicker.setCityName(cityName);
//            cityPicker.setDictrictName(dictrictName);
        }
        if(!TextUtils.isEmpty(codeStr)){
            provinceCode=codeStr.split("-")[0];
            cityCode=codeStr.split("-")[1];
            dictrictCode=codeStr.split("-")[2];
        }

        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_where.setText("下一步");
            bt_later_report.setVisibility(View.VISIBLE);
        }
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {
                if (selected) {
                    provinceName = cityPicker.getProvinceStr();
                    cityName=cityPicker.getCityStr();
                    dictrictName=cityPicker.getDictrictStr();

                    provinceCode = cityPicker.getProvinceCode();
                    cityCode = cityPicker.getCity_code_string();
                    dictrictCode = cityPicker.getDistrictCode();

                    LogUtils.e("select_city=====" + provinceName);
                    LogUtils.e("provinceCode=====" + provinceCode);
                    LogUtils.e("cityCode=====" + cityCode);
                    LogUtils.e("dictrictCode=====" + dictrictCode);
                }
            }
        });
    }

    @OnClick(R.id.bt_ok_where)
    public void reportWhere() {
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);

            UserInfoBean.getInstance().setHomeProvinceCode(provinceCode);
            UserInfoBean.getInstance().setHomeCityCode(cityCode);
            UserInfoBean.getInstance().setHomeDistrictId(dictrictCode);
            UserManager.getInstance().setUserHomeAdress(provinceName + "-" +cityName + "-" +dictrictName);
            readyGo(ReportAddress.class);
            return;
        }
        HashMap map = new HashMap();
        map.put("homeProvinceCode", provinceCode);
        map.put("homeCityCode", cityCode);
        map.put("homeDistrictId", dictrictCode);
        Api.updataUserInfo(map, this);
    }

    @OnClick(R.id.bt_later_where)
    public void reportLater() {
        Api.setUserInfo(this);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.UPDATA_USERINFO) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals("000000")) {
                Intent intent = new Intent();
                intent.putExtra("where", provinceName + "-" +cityName + "-" +dictrictName);//省-市-区str，方便查询
                intent.putExtra("wherecode", provinceCode + "-" + cityCode + "-" + dictrictCode);//省-市-区
                UserManager.getInstance().setUserHomeAdress(provinceName + "-" +cityName + "-" +dictrictName);
                setResult(0x70, intent);
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
