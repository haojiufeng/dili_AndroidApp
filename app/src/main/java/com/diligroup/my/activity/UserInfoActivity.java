package com.diligroup.my.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.base.DiliApplication;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.GetJiaoQinBean;
import com.diligroup.bean.GetUserInfoFromServiceBean;
import com.diligroup.bean.UploadInfo;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.login.RegistActivity;
import com.diligroup.mode.UserSetBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.FileUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.PictureFileUtils;
import com.diligroup.utils.StringUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UpLoadPhotoUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.view.CircleImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.Request;

/**
 * Created by Kevin on 2016/6/14.
 * 用户信息详情
 */
public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.tv_time_of_month)
    TextView tv_time_of_month;
    @Bind(R.id.user_icon)
    CircleImageView userIcon;
    //    @Bind(R.id.change_headicon)
//    TextView change_headicon;//更换用户头像
    @Bind(R.id.rl_time_of_month)
    RelativeLayout rl_time_of_month;//生理期rl布局
    ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    private static final int CROP_CODE = 3;
    private String fileName;
    @Bind(R.id.tv_user_phone_number)
    TextView tv_user_number;
    @Bind(R.id.tv_sex)
    TextView tv_sex;
    @Bind(R.id.tv_birth)
    TextView tv_birthday;
    @Bind(R.id.tv_work)
    TextView tv_job;//职业
    @Bind(R.id.tv_noeat)
    TextView tv_noeat;//饮食禁忌
    @Bind(R.id.tv_height)
    TextView tv_height;
    @Bind(R.id.tv_weight)
    TextView tv_weight;
    @Bind(R.id.tv_where)
    TextView tv_where;//籍贯
    @Bind(R.id.tv_now_address)
    TextView tv_address;//常住地
    @Bind(R.id.tv_special)
    TextView tv_special;//特殊人群
    @Bind(R.id.tv_allergy)
    TextView tv_alLeryFood;//过敏食材
    @Bind(R.id.tv_taste)
    TextView tv_taste;
    @Bind(R.id.tv_history)
    TextView tv_history;//健康史
    @Bind(R.id.tv_other)
    TextView tv_Other;//其它需求
    Boolean isFrist;
    Bundle bundle;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.tv_shoujihao)
    TextView tv_shoujihao;//手机号文字
    private String physiologcalDate;//生理周期
    private String circle;//生理周期天数
    @Bind(R.id.rl_special)
    RelativeLayout rl_special;
    @Bind(R.id.my_rootView)
    ScrollView my_rootView;
    private GetUserInfoFromServiceBean userInfoBean;//服务器端返回bean
    private UploadInfo bean;//上传头像bean
    private ViewSwitcherHelper viewHelper;
    private String tempStr;
    private UpLoadPhotoUtils photoUtils;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.user_info;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        viewHelper = new ViewSwitcherHelper(this, my_rootView);
        viewHelper.showLoading();
        tv_title.setText("我的资料");
        Api.getUserInfo(this);
        bundle = new Bundle();
        bundle.putBoolean("isFrist", false);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (TextUtils.isEmpty(UserManager.getInstance().getPhone())) {
            tv_user_number.setText(UserManager.getInstance().getNickName());
            tv_shoujihao.setText("昵称：");

        } else {
            tv_user_number.setText(UserManager.getInstance().getPhone());
            tv_shoujihao.setText("手机号：");
        }
    }

    /* 生理期*/
    @OnClick(R.id.rl_time_of_month)
    public void ClickPhysiolog() {
        Intent mIntent = new Intent(UserInfoActivity.this, PhysiologicalPeriodActivity.class);
        mIntent.putExtra("isFromMy", true);
        if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodStartTime()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodEndTime())) {
            mIntent.putExtra("physiologDate", DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodStartTime()) + "至" + DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodEndTime()));
        }
        mIntent.putExtra("cycleNum", userInfoBean.getUserDetail().getPeriodNum());
        startActivityForResult(mIntent, 0x140);
    }

    /* 性别*/
    @OnClick(R.id.rl_sex)
    public void ClickSex() {

        Intent intent = new Intent(this, ReportSex.class);
        intent.putExtras(bundle);
        intent.putExtra("sex", userInfoBean.getUser().getSex());
        intent.putExtra("birthday", DateUtils.dateFormatChanged(userInfoBean.getUser().getBirthday()));
        intent.putExtra("special", userInfoBean.getUserDetail().getSpecialCrowdCode());
        startActivityForResult(intent, 0x00);

    }

    //生日
    @OnClick(R.id.rl_birthday)
    public void ClickBirthday() {
//        readyGo(ReportBirthday.class, bundle);
        Intent intent = new Intent(this, ReportBirthday.class);
        intent.putExtras(bundle);
        intent.putExtra("birthday", DateUtils.dateFormatChanged(userInfoBean.getUser().getBirthday()));
        intent.putExtra("periodStartTime", userInfoBean.getUserDetail().getPeriodStartTime());
        intent.putExtra("periodEndTime", userInfoBean.getUserDetail().getPeriodEndTime());
        intent.putExtra("periodNum", userInfoBean.getUserDetail().getPeriodNum());
        intent.putExtra("specialCrowdCode", userInfoBean.getUserDetail().getSpecialCrowdCode());
        startActivityForResult(intent, 0x10);
    }

    //身高
    @OnClick(R.id.rl_height)
    public void ClickHeight() {
//        readyGo(ReportHeight.class, bundle);

        Intent intent = new Intent(this, ReportHeight_1.class);
        intent.putExtras(bundle);
        intent.putExtra("sex", userInfoBean.getUser().getSex());
        intent.putExtra("height", userInfoBean.getUser().getHeight());
        startActivityForResult(intent, 0x30);
    }

    //其他需求
    @OnClick(R.id.rl_other)
    public void ClickOther() {
//        readyGo(ReportOther.class, bundle);
        Intent intent = new Intent(this, ReportOther.class);
        intent.putExtras(bundle);
        intent.putExtra("otherRequest", userInfoBean.getUserDetail().getOtherReqName());
        intent.putExtra("targetWeight", userInfoBean.getUserDetail().getTargetWeight());
        startActivityForResult(intent, 0x120);
    }

    @OnClick(R.id.rl_special)
    public void ClickTsrq() {
        Intent intent = new Intent(this, ReportSpecial.class);
        intent.putExtras(bundle);
        if (userInfoBean.getUserDetail().getSpecialCrowdCode() != null)
            intent.putExtra("special", userInfoBean.getUserDetail().getSpecialCrowdCode());
        startActivityForResult(intent, 0x110);
    }

    @OnClick(R.id.rl_taste)
    public void ClickTaste() {
        Intent intent = new Intent(this, ReportTaste.class);
        intent.putExtras(bundle);
        intent.putExtra("taste", userInfoBean.getUserDetail().getTasteNames());
        startActivityForResult(intent, 0x90);
    }

    //体重
    @OnClick(R.id.rl_weight)
    public void ClickWeight() {
        Intent intent = new Intent(this, ReportWeight.class);
        intent.putExtras(bundle);
        intent.putExtra("sex", userInfoBean.getUser().getSex());
        intent.putExtra("weight", userInfoBean.getUserDetail().getWeight().replace("kg", ""));
        startActivityForResult(intent, 0x40);
//        readyGo(ReportWeight.class, bundle);
    }

    /* 饮食禁忌 */
    @OnClick(R.id.rl_noeat)
    public void ClickYsjj() {
        Intent intent = new Intent(this, ReportNoeat.class);
        intent.putExtras(bundle);
        intent.putExtra("tabooCode", userInfoBean.getUserDetail().getTabooCode());
        startActivityForResult(intent, 0x50);
//        readyGo(ReportNoeat.class, bundle);
    }

    //职业
    @OnClick(R.id.rl_work)
    public void ClickWork() {
        Intent intent = new Intent(this, ReportWork.class);
        intent.putExtras(bundle);
        intent.putExtra("jobCode", userInfoBean.getUserDetail().getJob());
        intent.putExtra("jobType", userInfoBean.getUserDetail().getJobType());//jobName对应的code
        intent.putExtra("jobName", userInfoBean.getUserDetail().getJobName());//jobName
        intent.putExtra("birthday", userInfoBean.getUser().getBirthday());
        startActivityForResult(intent, 0x20);
//        readyGo(ReportWork.class, bundle);
    }

    @OnClick(R.id.rl_history)
    public void ClickHistory() {
        Intent intent = new Intent(this, ReportHistory.class);
        String history = userInfoBean.getUserDetail().getChronicDiseaseNames();
        intent.putExtra("healthyHository", history);
        startActivityForResult(intent, 0x100);
    }

    /*过敏食材*/
    @OnClick(R.id.rl_allergy)
    public void ClickAllergy() {
        Intent intent = new Intent(this, ReportAllergy.class);
        intent.putExtras(bundle);
        intent.putExtra("allergyStr", userInfoBean.getUserDetail().getAllergyName());
        startActivityForResult(intent, 0x60);
    }

    @OnClick(R.id.rl_where)
    public void ClickWhere() {
        Intent intent = new Intent(this, ReportWhere.class);
        intent.putExtras(bundle);
        if (!TextUtils.isEmpty(userInfoBean.getUser().getHomeProvinceName()) && !TextUtils.isEmpty(userInfoBean.getUser().getHomeCityName()) && !TextUtils.isEmpty(userInfoBean.getUser().getHomeDistrictName())) {
            intent.putExtra("where", userInfoBean.getUser().getHomeProvinceName() + "-" + userInfoBean.getUser().getHomeCityName() + "-" + userInfoBean.getUser().getHomeDistrictName());
        }
        if (!TextUtils.isEmpty(userInfoBean.getUser().getHomeProvinceCode()) && !TextUtils.isEmpty(userInfoBean.getUser().getHomeCityCode()) && !TextUtils.isEmpty(userInfoBean.getUser().getHomeDistrictId())) {
            intent.putExtra("whereCode", userInfoBean.getUser().getHomeProvinceCode() + "-" + userInfoBean.getUser().getHomeCityCode() + "-" + userInfoBean.getUser().getHomeDistrictId());
        }
        DiliApplication.flagArea = "homeAddress";
        startActivityForResult(intent, 0x70);
    }

    @OnClick(R.id.change_headicon)
    public void ChangeHeadPhoto() {
        new UpLoadPhotoUtils(this).pickImage();
    }

    @OnClick(R.id.rl_now)
    public void ClickAddress() {
//        readyGo(ReportAddress.class, bundle);
        Intent intent = new Intent(this, ReportAddress.class);
        intent.putExtras(bundle);
        if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentProvinceName()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentCityName()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentDistrictName())) {
            intent.putExtra("address", userInfoBean.getUserDetail().getCurrentProvinceName() + "-" + userInfoBean.getUserDetail().getCurrentCityName() + "-" + userInfoBean.getUserDetail().getCurrentDistrictName());
        }
        if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentProvinceCode()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentCityCode()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getCurrentDistrictId())) {
            intent.putExtra("addressCode", userInfoBean.getUserDetail().getCurrentProvinceCode() + "-" + userInfoBean.getUserDetail().getCurrentCityCode() + "-" + userInfoBean.getUserDetail().getCurrentDistrictId());
        }
        DiliApplication.flagArea = "currentAddress";
        startActivityForResult(intent, 0x80);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x00:
                if (null != data) {
                    String sexTag = data.getStringExtra("sex");
                    if (sexTag.equals("1")) {
                        tv_sex.setText("男");
                        rl_special.setVisibility(View.GONE);
                        rl_special.setVisibility(View.GONE);
                        rl_time_of_month.setVisibility(View.GONE);
                        userInfoBean.getUserDetail().setPeriodStartTime("");
                        userInfoBean.getUserDetail().setPeriodEndTime("");

                    } else {
                        tv_sex.setText("女");
                        UserInfoBean.getInstance().setSex("0");
                        if (userInfoBean.getUser().getAge() != 0 && userInfoBean.getUser().getAge() >= 18 && userInfoBean.getUser().getAge() < 50) {
                            rl_special.setVisibility(View.VISIBLE);
                            userInfoBean.getUserDetail().setSpecialCrowdCode("");
                            tv_special.setText("");
                        } else {
                            rl_special.setVisibility(View.GONE);
                        }
                        if (userInfoBean.getUser().getAge() != 0 && userInfoBean.getUser().getAge() >= 9 && userInfoBean.getUser().getAge() < 50 && TextUtils.isEmpty(userInfoBean.getUserDetail().getSpecialCrowdCode())) {//女性 9-50岁，不属于特殊人群
                            rl_time_of_month.setVisibility(View.VISIBLE);
                            userInfoBean.getUserDetail().setPeriodStartTime("");
                            userInfoBean.getUserDetail().setPeriodEndTime("");
                            userInfoBean.getUserDetail().setPeriodNum("");
                            tv_time_of_month.setText("");
                        } else {
                            rl_time_of_month.setVisibility(View.GONE);
                        }
                    }
                    userInfoBean.getUser().setSex(sexTag);
                }
                break;
            case 0x10:
                if (null != data) {
                    String brithday = data.getStringExtra("brithday");
                    int age = data.getIntExtra("age", 0);
                    userInfoBean.getUser().setBirthday(DateUtils.dateFormatChanged_2(brithday));
                    tv_birthday.setText(brithday);
                    if (userInfoBean.getUser().getSex().equals("0") && age >= 18 && age < 50) {//18至50岁的女性才展示这几个选项
                        rl_special.setVisibility(View.VISIBLE);
                        tv_special.setText(userInfoBean.getUserDetail().getSpecialCrowdName());
                    } else {
                        rl_special.setVisibility(View.GONE);
                    }
                    if (userInfoBean.getUser().getSex().equals("0") && age >= 9 && age < 50 && TextUtils.isEmpty(userInfoBean.getUserDetail().getSpecialCrowdCode())) {
                        rl_time_of_month.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodStartTime()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodEndTime())) {
                            tv_time_of_month.setText(DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodStartTime()).substring(5) + "至" + DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodEndTime()).substring(5));
                        }
                    } else {
                        rl_time_of_month.setVisibility(View.GONE);
                        userInfoBean.getUserDetail().setPeriodStartTime("");
                        userInfoBean.getUserDetail().setPeriodEndTime("");
                    }
                    //如果age>65岁，职业为重体力的话，修改职业为默认“其它”
                    if(age>=65 && (userInfoBean.getUserDetail().getJobTypeName().equals("重") || userInfoBean.getUserDetail().getJobType().equals("150003"))){
                        tv_job.setText("其他");
                        userInfoBean.getUserDetail().setJobType("150002"); //对应的code值
                        userInfoBean.getUserDetail().setJobName("其他"); //对应的code值
//                        userInfoBean.getUserDetail().setJob(jobCode);
                    }
                    UserInfoBean.getInstance().setBirthday(DateUtils.dateFormatChanged_2(brithday));
                    UserInfoBean.getInstance().setAge(age);
                    userInfoBean.getUser().setBirthday(brithday);
                }
                break;
            case 0x20:
                if (null != data) {
                    String jobType = data.getStringExtra("jobType");
                    String jobCode = data.getStringExtra("jobCode");
                    String jobName = data.getStringExtra("jobName");
//                    UserInfoBean.getInstance().setJob(bean.getDictName());
                    tv_job.setText(jobName);
                    userInfoBean.getUserDetail().setJobType(jobType); //对应的code值
                    userInfoBean.getUserDetail().setJob(jobCode);
                    userInfoBean.getUserDetail().setJobName(jobName);
                }
                break;
            case 0x30:
                if (null != data) {
                    String height = data.getStringExtra("height");
                    if (!TextUtils.isEmpty(height)) {
                        tv_height.setText(height + "cm");
                        UserInfoBean.getInstance().setHeight(height);
                        userInfoBean.getUser().setHeight(height);
                    }
                }
                break;
            case 0x40:
                if (null != data) {
                    String weight = data.getStringExtra("weight");
                    if (!TextUtils.isEmpty(weight)) {
                        tv_weight.setText(weight.replace("kg", "") + "kg");
                    }
                    UserInfoBean.getInstance().setWeight(weight);
                    userInfoBean.getUserDetail().setWeight(weight);
                    UserManager.getInstance().setNowWeight(userInfoBean.getUserDetail().getWeight().replace("kg", ""));
                }
                break;
            case 0x50:
                if (null != data) {
                    ArrayList<String> noeat = (ArrayList<String>) data.getSerializableExtra("noeat");
                    if (noeat != null && noeat.size() > 0) {
                        tv_noeat.setText("已选" + noeat.size() + "项");
                    } else {
                        tv_noeat.setText("");
                    }
                    userInfoBean.getUserDetail().setTabooCode(StringUtils.listToString(noeat, ','));
                }
                break;
            case 0x60:
                if (null != data) {
                    String allergy = data.getStringExtra("allergyList");
                    if (allergy != null && allergy.contains(",") && allergy.split(",").length > 1) {
//                        UserInfoBean.getInstance().setAllergyFood(allergy);//一次上传用
                        tv_alLeryFood.setText("已选" + allergy.split(",").length + "项");
                    } else if (allergy != null && !allergy.contains(",") && allergy.length() > 1) {
                        tv_alLeryFood.setText("已选" + 1 + "项");
                    } else {
                        tv_alLeryFood.setText("");
                    }
                    userInfoBean.getUserDetail().setAllergyName(allergy);
                }
                break;
            case 0x70:
                if (null != data) {
                    String where = data.getStringExtra("where");//
                    String codestr = data.getStringExtra("wherecode");//省-市-区
                    if (!TextUtils.isEmpty(where)) {
                        tv_where.setText(where.split("-")[0]);
                        userInfoBean.getUser().setHomeProvinceName(where.split("-")[0]);
                        userInfoBean.getUser().setHomeCityName(where.split("-")[1]);
                        userInfoBean.getUser().setHomeDistrictName(where.split("-")[2]);

                        userInfoBean.getUser().setHomeProvinceCode(codestr.split("-")[0]);
                        userInfoBean.getUser().setHomeCityCode(codestr.split("-")[1]);
                        userInfoBean.getUser().setHomeDistrictId(codestr.split("-")[2]);
                    } else {
                        userInfoBean.getUser().setHomeProvinceName("");//只展示省份
                        userInfoBean.getUser().setHomeCityName("");
                        userInfoBean.getUser().setHomeDistrictName("");

                        userInfoBean.getUser().setHomeProvinceCode("");
                        userInfoBean.getUser().setHomeCityCode("");
                        userInfoBean.getUser().setHomeDistrictId("");
                    }
                }
                break;
            case 0x80:
                if (null != data) {
                    String address = data.getStringExtra("address");
                    if (!TextUtils.isEmpty(address)) {
                        tv_address.setText(address.split("-")[0]);
                        userInfoBean.getUserDetail().setCurrentProvinceName(address.split("-")[0]);
                        userInfoBean.getUserDetail().setCurrentCityName(address.split("-")[1]);
                        userInfoBean.getUserDetail().setCurrentDistrictName(address.split("-")[2]);
                    } else {
                        userInfoBean.getUserDetail().setCurrentProvinceName("");
                        userInfoBean.getUserDetail().setCurrentCityName("");
                        userInfoBean.getUserDetail().setCurrentDistrictName("");
                    }
                }
                break;
            case 0x90:
                if (null != data) {
                    String taste = data.getStringExtra("taste");
                    tv_taste.setText(taste);
                    userInfoBean.getUserDetail().setTasteNames(taste);
                }
                break;
            case 0x100:
                if (null != data) {
                    ArrayList<GetJiaoQinBean.ListBean> history = (ArrayList<GetJiaoQinBean.ListBean>) data.getSerializableExtra("history");
                    if (history != null && history.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < history.size(); i++) {
                            sb.append(history.get(i).getDictName() + ",");
                        }
                        userInfoBean.getUserDetail().setChronicDiseaseNames(sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
                        tv_history.setText("已选" + history.size() + "项");
                    } else {
                        tv_history.setText("");
                        userInfoBean.getUserDetail().setChronicDiseaseNames("");
                    }
                }
                break;
            case 0x110:
                if (null != data) {
                    GetJiaoQinBean.ListBean special = (GetJiaoQinBean.ListBean) data.getSerializableExtra("special");
                    if (special != null) {
                        tv_special.setText(special.getDictName().toString().trim());
                        userInfoBean.getUserDetail().setSpecialCrowdCode(special.getCode());
                        rl_time_of_month.setVisibility(View.GONE);
                        userInfoBean.getUserDetail().setPeriodStartTime("");
                        userInfoBean.getUserDetail().setPeriodEndTime("");
                    } else {
                        userInfoBean.getUserDetail().setSpecialCrowdCode("");
                        tv_special.setText("");
                        if (userInfoBean.getUser().getSex().equals("0") && userInfoBean.getUser().getAge() >= 9 && userInfoBean.getUser().getAge() < 50) {
                            rl_time_of_month.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;

            case 0x120:
                if (null != data) {
                    String otherRequest = data.getStringExtra("otherRequest");
                    String targetWeight = data.getStringExtra("targetWeight");
                    if (!TextUtils.isEmpty(otherRequest)) {
                        tv_Other.setText(otherRequest);
                    } else {
                        tv_Other.setText("");
                    }
                    userInfoBean.getUserDetail().setTargetWeight(targetWeight);
                    userInfoBean.getUserDetail().setOtherReqName(otherRequest);
                }
                break;
            case 0x140:
                if (null != data) {
                    String selectDate = data.getStringExtra("cycle");
                    String cycleNum = data.getStringExtra("cycleNum");
                    if (selectDate != null && selectDate.length() > 1) {
                        try {
                            if (DateUtils.compareDate2(selectDate.split(",")[0], selectDate.split(",")[1]) > 0) {//哪一个日期靠前
                                tv_time_of_month.setText(selectDate.split(",")[1].substring(5) + "至" + selectDate.split(",")[0].substring(5));
                                userInfoBean.getUserDetail().setPeriodStartTime(selectDate.split(",")[1]);
                                userInfoBean.getUserDetail().setPeriodEndTime(selectDate.split(",")[0]);
                            } else {
                                tv_time_of_month.setText(selectDate.split(",")[0].substring(5) + "至" + selectDate.split(",")[1].substring(5));
                                userInfoBean.getUserDetail().setPeriodStartTime(selectDate.split(",")[0]);
                                userInfoBean.getUserDetail().setPeriodEndTime(selectDate.split(",")[1]);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        userInfoBean.getUserDetail().setPeriodNum(cycleNum);
                    }
                }
                break;
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        sb.append(p);
                    }
                    tempStr = sb.toString();
                    fileName = tempStr.substring(tempStr.lastIndexOf("/") + 1);
                    photoUtils = new UpLoadPhotoUtils(this);
                    photoUtils.startPhotoZoom(Uri.fromFile(new File(sb.toString())));
                }
                break;
            case CROP_CODE:
                if (resultCode == RESULT_OK) {
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Bitmap photo = extras.getParcelable("data");
//                        photo = PictureFileUtils.zoomImage(photo, 150, 150);
//                        Api.upLoadPicture(new String(Base64.encode(FileUtils.Bitmap2Bytes(photo), Base64.DEFAULT)), fileName, this);
//                    }
                    Bitmap photo = null;
                    try {
                        if (!TextUtils.isEmpty(photoUtils.getPath())) {
                            File file = PictureFileUtils.compressImage(photoUtils.getPath(), 80);
                            photo = BitmapFactory.decodeFile(file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Api.upLoadPicture(new String(Base64.encode(FileUtils.Bitmap2Bytes(photo), Base64.DEFAULT)), photoUtils.getPath().substring(photoUtils.getPath().lastIndexOf("/") + 1), this);
                }
                break;
        }
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        //出现错误，这个页面不可见，不然user为null，点击那里都是异常
        switch (action) {
            case GET_USER_INFO:
                viewHelper.showError(request, action, this);
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.GET_USER_INFO && object != null) {
            userInfoBean = (GetUserInfoFromServiceBean) object;
            if (userInfoBean!=null && userInfoBean.getCode().equals(Constant.RESULT_SUCESS)) {
                viewHelper.showContent();
                tv_sex.setText(getUserById(userInfoBean.getUser().getSex()));
                UserManager.getInstance().setSex(userInfoBean.getUser().getSex());
                if (userInfoBean.getUser().getSex().equals("0") && userInfoBean.getUser().getAge() >= 18 && userInfoBean.getUser().getAge() < 50) {//女
                    rl_special.setVisibility(View.VISIBLE);
                } else {
                    rl_special.setVisibility(View.GONE);
                }
                if (userInfoBean.getUser().getSex().equals("0") && userInfoBean.getUser().getAge() >= 9 && userInfoBean.getUser().getAge() < 50 && TextUtils.isEmpty(userInfoBean.getUserDetail().getSpecialCrowdCode())) {
                    rl_time_of_month.setVisibility(View.VISIBLE);
                    if (userInfoBean.getUserDetail() != null) {
                        circle = userInfoBean.getUserDetail().getPeriodNum();
                        tv_special.setText(userInfoBean.getUserDetail().getSpecialCrowdName());
                        if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodStartTime()) && !TextUtils.isEmpty(userInfoBean.getUserDetail().getPeriodEndTime())) {
                            tv_time_of_month.setText(DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodStartTime()).substring(5) + "至" + DateUtils.dateFormatChanged(userInfoBean.getUserDetail().getPeriodEndTime()).substring(5));
                        }
                    }
                } else {
                    rl_time_of_month.setVisibility(View.GONE);
                }
                if (userInfoBean.getUserDetail() != null) {
                    if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getHeadPhotoAdd())) {
                        Picasso.with(this)
                                .load(userInfoBean.getUserDetail().getHeadPhotoAdd())
                                .error(R.mipmap.myinfo_head_default)
                                .noFade()
                                .into(userIcon);
                    }
                    tv_birthday.setText(DateUtils.dateFormatChanged(userInfoBean.getUser().getBirthday()));
                    tv_job.setText(userInfoBean.getUserDetail().getJobName());
                    tv_height.setText(userInfoBean.getUser().getHeight() + "cm");
                    tv_weight.setText(userInfoBean.getUserDetail().getWeight().replace("kg", "") + "kg");
                    UserManager.getInstance().setNowWeight(userInfoBean.getUserDetail().getWeight().replace("kg", ""));
                    if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getTabooCode()) && userInfoBean.getUserDetail().getTabooCode().contains(",")) {
                        tv_noeat.setText("已选" + userInfoBean.getUserDetail().getTabooCode().split(",").length + "项");
                    } else if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getTabooCode()) && !userInfoBean.getUserDetail().getTabooCode().contains(",")) {
                        tv_noeat.setText("已选" + 1 + "项");
                    }
                    if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getAllergyName()) && userInfoBean.getUserDetail().getAllergyName().contains(",")) {
                        tv_alLeryFood.setText("已选" + userInfoBean.getUserDetail().getAllergyName().split(",").length + "项");
                    } else if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getAllergyName()) && !userInfoBean.getUserDetail().getAllergyName().contains(",")) {
                        tv_alLeryFood.setText("已选" + 1 + "项");
                    }
                    tv_where.setText(userInfoBean.getUser().getHomeProvinceName());
                    UserManager.getInstance().setUserHomeAdress(userInfoBean.getUser().getHomeProvinceName() + "-" + userInfoBean.getUser().getHomeCityName() + "-" + userInfoBean.getUser().getHomeDistrictName());
                    tv_address.setText(userInfoBean.getUserDetail().getCurrentProvinceName());
                    UserManager.getInstance().setUserCurrentAdress(userInfoBean.getUserDetail().getCurrentProvinceName() + "-" + userInfoBean.getUserDetail().getCurrentCityName() + "-" + userInfoBean.getUserDetail().getCurrentDistrictName());
                    tv_taste.setText(userInfoBean.getUserDetail().getTasteNames());
                    if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getChronicDiseaseCode()) && userInfoBean.getUserDetail().getChronicDiseaseCode().contains(",")) {
                        tv_history.setText("已选" + userInfoBean.getUserDetail().getChronicDiseaseCode().split(",").length + "项");
                    } else if (!TextUtils.isEmpty(userInfoBean.getUserDetail().getChronicDiseaseCode()) && !userInfoBean.getUserDetail().getChronicDiseaseCode().contains(",")) {
                        tv_history.setText("已选" + 1 + "项");
                    }
                    tv_special.setText(userInfoBean.getUserDetail().getSpecialCrowdName());
                    tv_Other.setText(userInfoBean.getUserDetail().getOtherReqName());
                }
            } else if (userInfoBean.getCode().equals("C010008")) {
                startActivity(new Intent(this, RegistActivity.class));
            } else {
                viewHelper.showNotify(userInfoBean.getMessage());
            }
        }
        if (action == Action.UPLOAD_PHOTO && null != object) {
            bean = (UploadInfo) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                ToastUtil.showLong(this, "上传成功");
                Api.perfectInfoAfterUpLoad(UserManager.getInstance().getUserId(), bean.getFilePath(), this);
            } else if (!NetUtils.isNetworkAvailable(this)) {
                ToastUtil.showLong(this, "没网啦");
            } else {
                ToastUtil.showLong(this, "上传失败");
            }
        } else if (action == Action.SET_INFOS && object != null) {
            CommonBean bean1 = (CommonBean) object;
            if (bean1.getCode().equals(Constant.RESULT_SUCESS)) {
                if (!TextUtils.isEmpty(bean.getFilePath())) {
                    Picasso.with(this)
                            .load(bean.getFilePath())
                            .noFade()
                            .error(R.mipmap.myinfo_head_default)
                            .into(userIcon);
                    //通知我的首页更换头像
                    UserSetBean setBean = new UserSetBean();
                    setBean.setHeadUrl(bean.getFilePath());
                    EventBus.getDefault().post(setBean);
                }
//                UserManager.getInstance().setHeadPath(bean.getFilePath());
            } else if (!NetUtils.isNetworkAvailable(this)) {
                ToastUtil.showLong(this, "没网啦");
            } else {
                ToastUtil.showLong(this, "上传失败");
            }
        }
    }

    private String getUserById(String sex) {
        return sex.equals("0") ? "女" : "男";
    }
}
