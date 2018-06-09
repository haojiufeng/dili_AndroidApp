package com.diligroup.mode;

/**
 * Created by hjf on 2016/9/8.
 * 用于我的页面之间头像和电话号码改变监听
 */
public class UserSetBean {
    private String headUrl;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    private String phoneNum;
}
