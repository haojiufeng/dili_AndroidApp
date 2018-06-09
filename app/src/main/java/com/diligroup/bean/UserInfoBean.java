package com.diligroup.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/21.
 */
public  class UserInfoBean  {
    public static UserInfoBean  infoBean=null;
    public static UserInfoBean getInstance(){
        if (infoBean==null){
            infoBean=new UserInfoBean();
        }
        return infoBean;
    }

    //性别  1boy  0girl
    public String  sex="-1";
    //生日 yyyy-MM-dd格式
    public String birthday;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //年龄
    public int age;
    //职业
    public String job;
    public String jobType;
    //身高
    public String height;
    //体重
    public String weight;
    // 目标体重
    public  String targetWeight = "";
    //食物禁忌
    public String noEatFood="";
    //过敏食材
    public String allergyFood="";

//    //籍贯
//    public String homeAddress;
    public String homeProvinceCode="";
    public String homeCityCode="" ;

    public String homeDistrictId="" ;

    /**
     *现居区县code
     */
    private String currentDistrictId="";
    /**
     *现居省份code
     */
    private String currentProvinceCode="";
    /**
     *现居市code
     */
    private String currentCityCode="";

    public String getTasteCode() {
        return tasteCode;
    }

    public void setTasteCode(String tasteCode) {
        this.tasteCode = tasteCode;
    }

    private String tasteCode="";//口味code值

    public String getCurrentDistrictId() {
        if (currentDistrictId!=null){
            return currentDistrictId;
        }
        return "";
    }

    public void setCurrentDistrictId(String currentDistrictId) {
        this.currentDistrictId = currentDistrictId;
    }

    public String getCurrentProvinceCode() {
        return currentProvinceCode;
    }

    public void setCurrentProvinceCode(String currentProvinceCode) {
        this.currentProvinceCode = currentProvinceCode;
    }

    public String getCurrentCityCode() {
        if (currentCityCode!=null){
            return currentCityCode;
        }
        return "";

    }

    public void setCurrentCityCode(String currentCityCode) {
        this.currentCityCode = currentCityCode;
    }

    //常住地
    public String currentAddress="";

    //口味
    public List<GetJiaoQinBean.ListBean> taste;

    public String getTasteName() {
        return tasteName;
    }

    public void setTasteName(String tasteName) {
        this.tasteName = tasteName;
    }

    public String tasteName="";

    //健康史
    public String chronicDiseaseCode="";

    //特殊人群
    public  String specialCrowdCode="";

    //其他要求
    public  String otherReqCode="";
    /**
     *头像地址
     */
    public  String headPhotoAdd;
    public  String reqType = "0";			// 1.增重需求 2.减重需求 0.默认(除了增重减重的其他需求)
    public   String periodNum = "";			// 生理期间隔天数
    public  String periodStartTime = "";			// 生理期开始时间
    public   String periodEndTime = "";			// 生理期结束时间

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getNoEatFood() {
        return noEatFood;
    }

    public void setNoEatFood(String noEatFood) {
        this.noEatFood = noEatFood;
    }

    public String getAllergyFood() {
        return allergyFood;
    }

    public void setAllergyFood(String allergyFood) {
        this.allergyFood = allergyFood;
    }

//    public String getHomeAddress() {
//        return homeAddress;
//    }
//
//    public void setHomeAddress(String homeAddress) {
//        this.homeAddress = homeAddress;
//    }

    public String getHomeProvinceCode() {
        return homeProvinceCode;
    }

    public void setHomeProvinceCode(String homeProvinceCode) {
        this.homeProvinceCode = homeProvinceCode;
    }

    public String getHomeCityCode() {
        return homeCityCode;
    }

    public void setHomeCityCode(String homeCityCode) {
        this.homeCityCode = homeCityCode;
    }

    public String getHomeDistrictId() {
        if (homeDistrictId!=null){
            return homeDistrictId;
        }
        return "";
    }

    public void setHomeDistrictId(String homeDistrictId) {
        this.homeDistrictId = homeDistrictId;
    }

    public String getCurrentAddress() {
        if (currentAddress!=null){
            return currentAddress;
        }
        return "";
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public List<GetJiaoQinBean.ListBean> getTaste() {
        if (null!=taste && taste.size()>0){
            return taste;
        }
        return null;
    }

    public void setTaste(List<GetJiaoQinBean.ListBean> taste) {
        this.taste = taste;
    }

    public String getChronicDiseaseCode() {
        if (chronicDiseaseCode!=null){
            return chronicDiseaseCode;
        }
        return "";

    }

    public void setChronicDiseaseCode(String chronicDiseaseCode) {
        this.chronicDiseaseCode = chronicDiseaseCode;
    }

    public String getSpecialCrowdCode() {
        if (specialCrowdCode!=null){
            return specialCrowdCode;
        }
        return "";
    }

    public void setSpecialCrowdCode(String specialCrowdCode) {
        this.specialCrowdCode = specialCrowdCode;
    }

    public String getOtherReqCode() {
        if (otherReqCode!=null){
            return otherReqCode;
        }
        return "";
    }

    public void setOtherReq(String otherReq) {
        this.otherReqCode = otherReq;
    }

    public String getHeadPhotoAdd() {
        return headPhotoAdd;
    }

    public void setHeadPhotoAdd(String headPhotoAdd) {
        this.headPhotoAdd = headPhotoAdd;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(String periodNum) {
        this.periodNum = periodNum;
    }

    public String getPeriodStartTime() {
        if (periodStartTime!=null){
            return periodStartTime;
        }
   return "";
    }

    public void setPeriodStartTime(String periodStartTime) {
        this.periodStartTime = periodStartTime;
    }

    public String getPeriodEndTime() {
        if (periodEndTime!=null){
            return periodEndTime;

        }
        return "";
    }

    public void setPeriodEndTime(String periodEndTime) {
        this.periodEndTime = periodEndTime;
    }
}
