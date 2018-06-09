package com.diligroup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.diligroup.R;
import com.diligroup.base.DiliApplication;
import com.diligroup.bean.Cityinfo;
import com.diligroup.bean.PrinceAndCitiesBean;
import com.diligroup.utils.FileUtil;
import com.diligroup.utils.UserManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市Picker
 *
 * @author zd
 */
public class CityPicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = 0;//滚动时候的省市索引
    private int temCityIndex = 0;
    private int tempCounyIndex = 0;
    private Context context;
    private List<Cityinfo> province_list = new ArrayList<>();
    private String city_string;

    String provinceName = "北京";//北京市
    String cityName = "北京市";
    String dictrictName = "东城区";//东城区
    private String dictrictCode = "110101";//东城区;
    private String provinceCode = "110000";//北京;
    private String cityCode = "110100";//北京市;
    private PrinceAndCitiesBean princeAndCitiesBean;


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDictrictName(String dictrictName) {
        this.dictrictName = dictrictName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getaddressinfo();
    }

    public CityPicker(Context context) {
        super(context);
        this.context = context;
        getaddressinfo();
    }

    // 获取城市信息
    private void getaddressinfo() {
        String area_str = FileUtil.readAssets(context, "area.json");
        princeAndCitiesBean = new Gson().fromJson(area_str, PrinceAndCitiesBean.class);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
//        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        if (!TextUtils.isEmpty(DiliApplication.flagArea) && DiliApplication.flagArea.equals("homeAddress")) {
            if (!TextUtils.isEmpty(UserManager.getInstance().getUserHomeAdress()) && UserManager.getInstance().getUserHomeAdress().split("-").length==3) {
                provinceName = UserManager.getInstance().getUserHomeAdress().split("-")[0];
                cityName = UserManager.getInstance().getUserHomeAdress().split("-")[1];
                dictrictName = UserManager.getInstance().getUserHomeAdress().split("-")[2];
            }
        }
        if(!TextUtils.isEmpty(DiliApplication.flagArea) && DiliApplication.flagArea.equals("currentAddress") && UserManager.getInstance().getUserHomeAdress().split("-").length==3){
            if (!TextUtils.isEmpty(UserManager.getInstance().getUserCurrentAdress())) {
                provinceName = UserManager.getInstance().getUserCurrentAdress().split("-")[0];
                cityName = UserManager.getInstance().getUserCurrentAdress().split("-")[1];
                dictrictName = UserManager.getInstance().getUserCurrentAdress().split("-")[2];
            }
        }
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
        provincePicker.setData(princeAndCitiesBean.getDistrictList(), 1);
        for (int i = 0; i < princeAndCitiesBean.getDistrictList().size(); i++) {
            if (princeAndCitiesBean.getDistrictList().get(i).getName().equals(provinceName)) {
                provincePicker.setDefault(i);
                tempProvinceIndex = i;
                break;
            }
        }
        cityPicker.setData(princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList(), 2);
        for (int i = 0; i < princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().size(); i++) {
            if (princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(i).getName().equals(cityName)) {
                cityPicker.setDefault(i);
                temCityIndex = i;
                break;
            }
        }
        counyPicker.setData(princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(temCityIndex).getList(), 3);
        for (int i = 0; i < princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(temCityIndex).getList().size(); i++) {
            if (princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(temCityIndex).getList().get(i).getName().equals(dictrictName)) {
                counyPicker.setDefault(i);
                tempCounyIndex = i;
                break;
            }
        }
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int index, String text) {
//                System.out.println("index-->" + index + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                if (provincePicker.getListSize() == 0) {
                    return;
                }
                provinceCode = princeAndCitiesBean.getDistrictList().get(index).getSortCode();
                cityCode = princeAndCitiesBean.getDistrictList().get(index).getList().get(0).getSortCode();
                dictrictCode = princeAndCitiesBean.getDistrictList().get(index).getList().get(0).getList().get(0).getSortCode();
                int lastDay = Integer.valueOf(provincePicker.getListSize());
                if (index > lastDay) {//如果>总集合。就默认选中最后一项
                    provincePicker.setDefault(lastDay - 1);
                }
                if (tempProvinceIndex != index) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    cityPicker.setData(princeAndCitiesBean.getDistrictList().get(index).getList(), 2);
                    cityPicker.setDefault(0);
                    counyPicker.setData(princeAndCitiesBean.getDistrictList().get(index).getList().get(0).getList(), 3);
                    counyPicker.setDefault(0);

                }
                tempProvinceIndex = index;
                temCityIndex = 0;
                tempCounyIndex = 0;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int index, String text) {
                System.out.println("selecting=====state===index====" + text);
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int index, String text) {
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != index) {
                    cityCode = princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(index).getSortCode();
                    dictrictCode = princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(index).getList().get(0).getSortCode();
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    counyPicker.setData(princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(index).getList(), 3);
                    counyPicker.setDefault(0);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (index > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = index;
                tempCounyIndex = 0;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int index, String text) {
                dictrictCode = princeAndCitiesBean.getDistrictList().get(tempProvinceIndex).getList().get(temCityIndex).getList().get(index).getSortCode();
//                System.out.println("dictrictCode=dictrictCode====dictrictCode==" + dictrictCode + text);
                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != index) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
//                    city_code_string = citycodeUtil.getCouny_list_code()
//                            .get(index);
                    int lastDay = Integer.valueOf(counyPicker.getListSize());
                    if (index > lastDay) {
                        counyPicker.setDefault(lastDay - 1);
                        tempCounyIndex = 0;//重新赋值，以便进入下一轮循环
                    } else {
                        tempCounyIndex = index;
                    }
                }
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getDistrictCode() {
        return dictrictCode;
    }

    public String getCity_string() {
        city_string = provincePicker.getSelectedText()
                + cityPicker.getSelectedText() + counyPicker.getSelectedText();
        return city_string;
    }

    public interface OnSelectingListener {

        void selected(boolean selected);
    }

    /**
     * 获取具体详细的省\
     *
     * @return
     */
    public String getProvinceStr() {
        return provincePicker.getSelectedText();
    }

    /**
     * 获取具体详细的市\
     *
     * @return
     */
    public String getCityStr() {
        return cityPicker.getSelectedText();
    }

    /**
     * 获取具体详细的省\
     *
     * @return
     */
    public String getDictrictStr() {
        return counyPicker.getSelectedText();
    }
}