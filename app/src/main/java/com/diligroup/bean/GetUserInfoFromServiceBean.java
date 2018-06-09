package com.diligroup.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class GetUserInfoFromServiceBean extends CommonBean {

    /**
     * alipay : 18612473550
     * birthday : 1990-09-28
     * email : www.443415475@qq.com
     * height : 163
     * homeAdd : 左权街道
     * homeCityCode : 430200
     * homeDistrictId : 430281
     * homeProvinceCode : 430000
     * lastLoginTime : 1472001690198
     * microblog : 18612473550
     * mobileNum : 18612473550
     * nationCode : 1
     * password : 71B596CB42EE254F7416043D184FC970
     * qq : 443415475
     * registerTime : 1472001690198
     * resouce : 1
     * sex : 0
     * status : 1
     * userCode : 123456
     * userId : 2
     * userName : 九九
     * weChat : 18612473550
     */

    private UserBean user;
    /**
     * allergyName : 香菜
     * carbohydrates : 258.75
     * chronicDiseaseCode : 240001
     * chronicDiseaseNames : 高血压
     * currentAdd : 大望路3
     * currentCityCode : 430200
     * currentDistrictId : 430281
     * currentProvinceCode : 430000
     * energyKC : 1800.0
     * fat : 50.0
     * headPhotoAdd : http://192.168.100.67/images.ypp2015.com/ypp/upload/img/2.jpg
     * id : 1
     * job : 研发人员
     * jobName :
     * jobType : jobType
     * jobTypeName :
     * otherReq : 250002
     * otherReqName : 增肌
     * periodEndTime :
     * periodNum :
     * periodStartTime :
     * protein : 55.0
     * reqType : 0
     * specialCrowdCode :
     * specialCrowdName :
     * storeId : 8
     * storeName :
     * tabooCode : 40005
     * tabooNames :
     * targetWeight : 60
     * tasteCode : 1
     * tasteNames :
     * userId : 2
     * weight : 64
     */

    private UserDetailBean userDetail;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserDetailBean getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetailBean userDetail) {
        this.userDetail = userDetail;
    }

    public static class UserBean {
        private String alipay;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        private int age;
        private String birthday;
        private String email;
        private String height;
        private String homeAdd;
        private String homeCityCode;
        private String homeCityName;
        private String homeDistrictName;
        private String homeDistrictId;
        private String homeProvinceCode;
        private String homeProvinceName;
        private long lastLoginTime;

        public String getHomeDistrictName() {
            return homeDistrictName;
        }

        public void setHomeDistrictName(String homeDistrictName) {
            this.homeDistrictName = homeDistrictName;
        }

        public String getHomeProvinceName() {
            return homeProvinceName;
        }

        public void setHomeProvinceName(String homeProvinceName) {
            this.homeProvinceName = homeProvinceName;
        }

        public String getHomeCityName() {
            return homeCityName;
        }

        public void setHomeCityName(String homeCityName) {
            this.homeCityName = homeCityName;
        }

        private String microblog;
        private String mobileNum;
        private String nationCode;
        private String password;
        private String qq;
        private long registerTime;
        private String resouce;
        private String sex;
        private String status;
        private String userCode;
        private int userId;
        private String userName;
        private String weChat;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getHomeAdd() {
            return homeAdd;
        }

        public void setHomeAdd(String homeAdd) {
            this.homeAdd = homeAdd;
        }

        public String getHomeCityCode() {
            return homeCityCode;
        }

        public void setHomeCityCode(String homeCityCode) {
            this.homeCityCode = homeCityCode;
        }

        public String getHomeDistrictId() {
            return homeDistrictId;
        }

        public void setHomeDistrictId(String homeDistrictId) {
            this.homeDistrictId = homeDistrictId;
        }

        public String getHomeProvinceCode() {
            return homeProvinceCode;
        }

        public void setHomeProvinceCode(String homeProvinceCode) {
            this.homeProvinceCode = homeProvinceCode;
        }

        public long getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(long lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getMicroblog() {
            return microblog;
        }

        public void setMicroblog(String microblog) {
            this.microblog = microblog;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getNationCode() {
            return nationCode;
        }

        public void setNationCode(String nationCode) {
            this.nationCode = nationCode;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
        }

        public String getResouce() {
            return resouce;
        }

        public void setResouce(String resouce) {
            this.resouce = resouce;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getWeChat() {
            return weChat;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }
    }

    public static class UserDetailBean {
        private String allergyName;
        private double carbohydrates;
        private String chronicDiseaseCode;
        private String chronicDiseaseNames;
        private String currentAdd;
        private String currentCityCode;
        private String currentCityName;
        private String currentDistrictId;
        private String currentDistrictName;
        private String currentProvinceCode;

        public String getCurrentProvinceName() {
            return currentProvinceName;
        }

        public void setCurrentProvinceName(String currentProvinceName) {
            this.currentProvinceName = currentProvinceName;
        }

        public String getCurrentDistrictName() {
            return currentDistrictName;
        }

        public void setCurrentDistrictName(String currentDistrictName) {
            this.currentDistrictName = currentDistrictName;
        }

        public String getCurrentCityName() {
            return currentCityName;
        }

        public void setCurrentCityName(String currentCityName) {
            this.currentCityName = currentCityName;
        }

        private String currentProvinceName;
        private double energyKC;
        private double fat;
        private String headPhotoAdd;
        private int id;
        private String job;
        private String jobName;
        private String jobType;
        private String jobTypeName;
        private String otherReq;
        private String otherReqName;
        private String periodEndTime;
        private String periodNum;
        private String periodStartTime;
        private double protein;
        private String reqType;
        private String specialCrowdCode;
        private String specialCrowdName;
        private int storeId;
        private String storeName;
        private String tabooCode;
        private String tabooNames;
        private String targetWeight;
        private String tasteCode;
        private String tasteNames;
        private int userId;
        private String weight;

        public ArrayList<GetJiaoQinBean.ListBean> getTaste() {
            return taste;
        }

        public void setTaste(ArrayList<GetJiaoQinBean.ListBean> taste) {
            this.taste = taste;
        }

        private ArrayList<GetJiaoQinBean.ListBean> taste;

        public List<GetJiaoQinBean.ListBean> getHeathyHistory() {
            return heathyHistory;
        }

        public void setHeathyHistory(List<GetJiaoQinBean.ListBean> heathyHistory) {
            this.heathyHistory = heathyHistory;
        }

        private List<GetJiaoQinBean.ListBean> heathyHistory;

        public String getAllergyName() {
            return allergyName;
        }

        public void setAllergyName(String allergyName) {
            this.allergyName = allergyName;
        }

        public double getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(double carbohydrates) {
            this.carbohydrates = carbohydrates;
        }

        public String getChronicDiseaseCode() {
            return chronicDiseaseCode;
        }

        public void setChronicDiseaseCode(String chronicDiseaseCode) {
            this.chronicDiseaseCode = chronicDiseaseCode;
        }

        public String getChronicDiseaseNames() {
            return chronicDiseaseNames;
        }

        public void setChronicDiseaseNames(String chronicDiseaseNames) {
            this.chronicDiseaseNames = chronicDiseaseNames;
        }

        public String getCurrentAdd() {
            return currentAdd;
        }

        public void setCurrentAdd(String currentAdd) {
            this.currentAdd = currentAdd;
        }

        public String getCurrentCityCode() {
            return currentCityCode;
        }

        public void setCurrentCityCode(String currentCityCode) {
            this.currentCityCode = currentCityCode;
        }

        public String getCurrentDistrictId() {
            return currentDistrictId;
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

        public double getEnergyKC() {
            return energyKC;
        }

        public void setEnergyKC(double energyKC) {
            this.energyKC = energyKC;
        }

        public double getFat() {
            return fat;
        }

        public void setFat(double fat) {
            this.fat = fat;
        }

        public String getHeadPhotoAdd() {
            return headPhotoAdd;
        }

        public void setHeadPhotoAdd(String headPhotoAdd) {
            this.headPhotoAdd = headPhotoAdd;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getJobTypeName() {
            return jobTypeName;
        }

        public void setJobTypeName(String jobTypeName) {
            this.jobTypeName = jobTypeName;
        }

        public String getOtherReq() {
            return otherReq;
        }

        public void setOtherReq(String otherReq) {
            this.otherReq = otherReq;
        }

        public String getOtherReqName() {
            return otherReqName;
        }

        public void setOtherReqName(String otherReqName) {
            this.otherReqName = otherReqName;
        }

        public String getPeriodEndTime() {
            return periodEndTime;
        }

        public void setPeriodEndTime(String periodEndTime) {
            this.periodEndTime = periodEndTime;
        }

        public String getPeriodNum() {
            return periodNum;
        }

        public void setPeriodNum(String periodNum) {
            this.periodNum = periodNum;
        }

        public String getPeriodStartTime() {
            return periodStartTime;
        }

        public void setPeriodStartTime(String periodStartTime) {
            this.periodStartTime = periodStartTime;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public String getReqType() {
            return reqType;
        }

        public void setReqType(String reqType) {
            this.reqType = reqType;
        }

        public String getSpecialCrowdCode() {
            return specialCrowdCode;
        }

        public void setSpecialCrowdCode(String specialCrowdCode) {
            this.specialCrowdCode = specialCrowdCode;
        }

        public String getSpecialCrowdName() {
            return specialCrowdName;
        }

        public void setSpecialCrowdName(String specialCrowdName) {
            this.specialCrowdName = specialCrowdName;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getTabooCode() {
            return tabooCode;
        }

        public void setTabooCode(String tabooCode) {
            this.tabooCode = tabooCode;
        }

        public String getTabooNames() {
            return tabooNames;
        }

        public void setTabooNames(String tabooNames) {
            this.tabooNames = tabooNames;
        }

        public String getTargetWeight() {
            return targetWeight;
        }

        public void setTargetWeight(String targetWeight) {
            this.targetWeight = targetWeight;
        }

        public String getTasteCode() {
            return tasteCode;
        }

        public void setTasteCode(String tasteCode) {
            this.tasteCode = tasteCode;
        }

        public String getTasteNames() {
            return tasteNames;
        }

        public void setTasteNames(String tasteNames) {
            this.tasteNames = tasteNames;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
