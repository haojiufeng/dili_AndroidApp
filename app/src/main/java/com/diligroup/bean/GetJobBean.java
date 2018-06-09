package com.diligroup.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class GetJobBean extends CommonBean {

    /**
     * qlist : [{"code":"1","createTime":1,"dictName":"轻","id":1,"laborCode":"15001","priority":1,"profName":"管理人员","status":"1"},{"code":"3","createTime":0,"dictName":"轻","id":3,"laborCode":"15001","priority":2,"profName":"研发人员","status":"1"}]
     * totalCount : 0
     * wlist : [{"code":"4","createTime":0,"dictName":"重","id":4,"laborCode":"15003","priority":1,"profName":"生产人员","status":"1"},{"code":"5","createTime":0,"dictName":"重","id":5,"laborCode":"15003","priority":2,"profName":"工人","status":"1"}]
     * zlist : [{"code":"2","createTime":0,"dictName":"中","id":2,"laborCode":"15002","priority":1,"profName":"销售人员","status":"1"}]
     */

    private int totalCount;
    /**
     * code : 1
     * createTime : 1
     * dictName : 轻
     * id : 1
     * laborCode : 15001
     * priority : 1
     * profName : 管理人员
     * status : 1
     */

    private List<ListBean> qlist;
    /**
     * code : 4
     * createTime : 0
     * dictName : 重
     * id : 4
     * laborCode : 15003
     * priority : 1
     * profName : 生产人员
     * status : 1
     */

    private List<ListBean> wlist;
    /**
     * code : 2
     * createTime : 0
     * dictName : 中
     * id : 2
     * laborCode : 15002
     * priority : 1
     * profName : 销售人员
     * status : 1
     */

    private List<ListBean> zlist;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBean> getQlist() {
        return qlist;
    }

    public void setQlist(List<ListBean> qlist) {
        this.qlist = qlist;
    }

    public List<ListBean> getWlist() {
        return wlist;
    }

    public void setWlist(List<ListBean> wlist) {
        this.wlist = wlist;
    }

    public List<ListBean> getZlist() {
        return zlist;
    }

    public void setZlist(List<ListBean> zlist) {
        this.zlist = zlist;
    }

    public static class ListBean implements Serializable {
        private String code;
        private String createTime;
        private String dictName;
        private int id;
        private String laborCode;
        private int priority;
        private String profName;
        private String status;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        private boolean isSelected;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDictName() {
            return dictName;
        }

        public void setDictName(String dictName) {
            this.dictName = dictName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLaborCode() {
            return laborCode;
        }

        public void setLaborCode(String laborCode) {
            this.laborCode = laborCode;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getProfName() {
            return profName;
        }

        public void setProfName(String profName) {
            this.profName = profName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
