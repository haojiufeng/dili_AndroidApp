package com.diligroup.bean;

import java.util.List;

/**
 * Created by hjf on 2016/8/31.
 */
public class OtherRequestBean extends CommonBean {
    /**
     * list : [{"code":"2510000849","createTime":1472119238778,"dictId":511,"dictName":"增重","dictType":"25","isShow":"1","isSpecial":"1","priority":0,"remark":"","status":"1"},{"code":"250001","createTime":1470639391135,"dictId":396,"dictName":"减脂","dictType":"25","isShow":"1","isSpecial":"1","priority":1,"remark":"","status":"1"},{"code":"250002","createTime":1472622071073,"dictId":397,"dictName":"增肌","dictType":"25","isShow":"1","isSpecial":"1","priority":2,"remark":"","status":"1"},{"code":"250003","createTime":1472622071073,"dictId":398,"dictName":"补钙","dictType":"25","isShow":"1","isSpecial":"1","priority":3,"remark":"","status":"1"},{"code":"250004","createTime":1472622071073,"dictId":399,"dictName":"补铁","dictType":"25","isShow":"1","isSpecial":"1","priority":4,"remark":"","status":"1"},{"code":"250005","createTime":1472622071073,"dictId":400,"dictName":"护眼","dictType":"25","isShow":"1","isSpecial":"1","priority":5,"remark":"","status":"1"}]
     * totalCount : 6
     */

    private int totalCount;
    /**
     * code : 2510000849
     * createTime : 1472119238778
     * dictId : 511
     * dictName : 增重
     * dictType : 25
     * isShow : 1
     * isSpecial : 1
     * priority : 0
     * remark :
     * status : 1
     */

    private List<GetJiaoQinBean.ListBean> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GetJiaoQinBean.ListBean> getList() {
        return list;
    }

    public void setList(List<GetJiaoQinBean.ListBean> list) {
        this.list = list;
    }
}
