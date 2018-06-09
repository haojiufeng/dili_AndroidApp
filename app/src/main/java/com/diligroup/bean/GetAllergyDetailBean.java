package com.diligroup.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GetAllergyDetailBean extends CommonBean {

    /**
     * list : [{"allergyType1":"谷类","list":[{"allergyName":"大麦","allergyType1":"谷类","allergyType2":"大麦","id":1,"status":"1"},{"allergyName":"面筋","allergyType1":"谷类","allergyType2":"小麦","id":2,"status":"1"},{"allergyName":"大米","allergyType1":"谷类","allergyType2":"大米","id":3,"status":"1"}],"status":"1"},{"allergyType1":"豆类","list":[{"allergyName":"大麦","allergyType1":"豆类","allergyType2":"大麦","id":4,"status":"2"},{"allergyName":"大豆","allergyType1":"豆类","allergyType2":"大豆","id":6,"status":"1"},{"allergyName":"豆豆","allergyType1":"豆类","allergyType2":"黄豆","id":26,"status":"1"},{"allergyName":"大豆","allergyType1":"豆类","allergyType2":"大豆","id":38,"status":"1"}],"status":"1"}]
     * totalCount : 0
     */

    private int totalCount;
    /**
     * allergyType1 : 谷类
     * list : [{"allergyName":"大麦","allergyType1":"谷类","allergyType2":"大麦","id":1,"status":"1"},{"allergyName":"面筋","allergyType1":"谷类","allergyType2":"小麦","id":2,"status":"1"},{"allergyName":"大米","allergyType1":"谷类","allergyType2":"大米","id":3,"status":"1"}]
     * status : 1
     */

    private List<ListBigBean> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBigBean> getList() {
        return list;
    }

    public void setList(List<ListBigBean> list) {
        this.list = list;
    }

    public static class ListBigBean {
        private String allergyType1;
        private String status;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        private boolean isSelected;
        /**
         * allergyName : 大麦
         * allergyType1 : 谷类
         * allergyType2 : 大麦
         * id : 1
         * status : 1
         */

        private List<ListBean> list;

        public String getAllergyType1() {
            return allergyType1;
        }

        public void setAllergyType1(String allergyType1) {
            this.allergyType1 = allergyType1;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String allergyName;
            private String allergyType1;
            private String allergyType2;
            private int id;
            private String status;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            private boolean isSelected;
            public String getAllergyName() {
                return allergyName;
            }

            public void setAllergyName(String allergyName) {
                this.allergyName = allergyName;
            }

            public String getAllergyType1() {
                return allergyType1;
            }

            public void setAllergyType1(String allergyType1) {
                this.allergyType1 = allergyType1;
            }

            public String getAllergyType2() {
                return allergyType2;
            }

            public void setAllergyType2(String allergyType2) {
                this.allergyType2 = allergyType2;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
