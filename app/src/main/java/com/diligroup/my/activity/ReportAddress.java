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
 * 上报 现住地
 */
public class ReportAddress extends BaseActivity {
    @Bind(R.id.select_address)
    CityPicker select_address;
    @Bind(R.id.bt_later_address)
    Button bt_later_report;
    @Bind(R.id.bt_ok_address)
    Button bt_address;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_info)
    TextView title_infos;

    String provinceCode = "110000";//北京市
    String cityCode = "110100";//北京市";
    String dictrictCode = "110101";//东城区

    String provinceName = "北京";//北京市
    String cityName = "北京市";
    String dictrictName = "东城区";//东城区
    String nameStr;
    private String addressCode;//省-市-区code

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report_address;
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @OnClick(R.id.bt_ok_address)
    public void reprotAddress() {
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);

            UserInfoBean.getInstance().setCurrentProvinceCode(provinceCode);
            UserInfoBean.getInstance().setCurrentCityCode(cityCode);
            UserInfoBean.getInstance().setCurrentDistrictId(dictrictCode);
            bt_address.setText("下一步");
            bt_later_report.setVisibility(View.VISIBLE);
            readyGo(ReportTaste.class);
        } else {
            HashMap map = new HashMap();
            map.put("currentProvinceCode", provinceCode);
            map.put("currentCityCode", cityCode);
            map.put("currentDistrictId", dictrictCode);
            Api.updataUserInfo(map, this);
        }

    }

    @OnClick(R.id.bt_later_address)//稍后再说
    public void jumpAddress() {
        Api.setUserInfo(this);
    }

    @Override
    protected void initViewAndData() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title.setText("现居住地");
        title_infos.setText("请选择您的常居住地");

        nameStr= getIntent().getStringExtra("address");
        addressCode = getIntent().getStringExtra("addressCode");
        if (!TextUtils.isEmpty(nameStr)) {
            provinceName = nameStr.split("-")[0];
            cityName= nameStr.split("-")[1];
            dictrictName= nameStr.split("-")[2];

//            select_address.setProvinceName(provinceName);
//            select_address.setCityName(cityName);
//            select_address.setDictrictName(dictrictName);
        }
        if(!TextUtils.isEmpty(addressCode)){
            provinceCode=addressCode.split("-")[0];
            cityCode=addressCode.split("-")[1];
            dictrictCode=addressCode.split("-")[2];
        }
        if (UserManager.getInstance().isFirstRecordInfo()) {
            bt_address.setText("下一步");
            AppManager.getAppManager().addActivity(this);
            bt_later_report.setVisibility(View.VISIBLE);
        }
        select_address.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {
                if (selected) {
                    provinceCode = select_address.getProvinceCode();
                    cityCode = select_address.getCity_code_string();
                    dictrictCode = select_address.getDistrictCode();

                    provinceName = select_address.getProvinceStr();
                    cityName=select_address.getCityStr();
                    dictrictName=select_address.getDictrictStr();

                    LogUtils.e("provinceCode=====" + provinceCode);
                    LogUtils.e("cityCode=====" + cityCode);
                    LogUtils.e("dictrictCode=====" + dictrictCode);
                }
            }
        });
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
                intent.putExtra("address", provinceName+"-"+cityName+"-"+dictrictName);
                intent.putExtra("addressCode", provinceCode+"-"+cityCode+"-"+dictrictCode);
                UserManager.getInstance().setUserCurrentAdress(provinceName + "-" +cityName + "-" +dictrictName);
                setResult(0x80, intent);
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
