package com.diligroup.bean;

import java.util.List;

/**
 * Created by hjf on 2016/8/11.
 */
public class StoreSupplySearchBean extends CommonBean {

    private JsonBean json;
    /**
     * json : {"dishesSupplyList":[{"dishesSupplyDtlList":[{"accessoriesList":[{"cCode":"V07007","createTime":1474181258380,"creatorId":0,"dishesCode":"B101007011","dishesCompId":24532,"foodName":"洋葱","netWeight":1,"priority":2,"proMethod":"","proMethodId":"120002","rawWeight":1.11,"remark":"","status":"1","type":"2","yieldRate":90}],"dishesCode":"B101007011","dishesName":"红烧鱼丸","imagesURL":"http://images.ypp2015.com/ypp/upload/dishesLib/101007011.jpg","imagesURL1":"http://images.ypp2015.com/ypp/upload/dishesLib/101007011-1.jpg","ingredients":{"cCode":"S01031","createTime":1474181258380,"creatorId":0,"dishesCode":"B101007011","dishesCompId":24531,"foodName":"鱼丸","netWeight":4,"priority":1,"proMethod":"","proMethodId":"120008","rawWeight":4,"remark":"","status":"1","type":"1","yieldRate":100},"num":19,"proWeight":5,"weight":260}]}],"mealTypeList":[{"code":"20001","createTime":1474181258307,"dictId":0,"dictName":"早餐","dictType":"","isShow":"1","isSpecial":"1","priority":0,"remark":"","status":"1"},{"code":"20002","createTime":1474181258307,"dictId":0,"dictName":"午餐","dictType":"","isShow":"1","isSpecial":"1","priority":0,"remark":"","status":"1"},{"code":"20003","createTime":1474181258307,"dictId":0,"dictName":"晚餐","dictType":"","isShow":"1","isSpecial":"1","priority":0,"remark":"","status":"1"}],"tmplDateList":["2016-09-12","2016-09-13","2016-09-14","2016-09-15","2016-09-16","2016-09-18","2016-09-19","2016-09-20","2016-09-21","2016-09-22"]}
     * totalCount : 0
     */

    private int totalCount;

    public JsonBean getJson() {
        return json;
    }

    public void setJson(JsonBean json) {
        this.json = json;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public static class JsonBean {
        private List<DishesSupplyListBean> dishesSupplyList;

        public List<DishesSupplyListBean> getDishesSupplyList() {
            return dishesSupplyList;
        }

        public void setDishesSupplyList(List<DishesSupplyListBean> dishesSupplyList) {
            this.dishesSupplyList = dishesSupplyList;
        }

        public static class DishesSupplyListBean {
            /**
             * accessoriesList : [{"cCode":"V07007","createTime":1474181258380,"creatorId":0,"dishesCode":"B101007011","dishesCompId":24532,"foodName":"洋葱","netWeight":1,"priority":2,"proMethod":"","proMethodId":"120002","rawWeight":1.11,"remark":"","status":"1","type":"2","yieldRate":90}]
             * dishesCode : B101007011
             * dishesName : 红烧鱼丸
             * imagesURL : http://images.ypp2015.com/ypp/upload/dishesLib/101007011.jpg
             * imagesURL1 : http://images.ypp2015.com/ypp/upload/dishesLib/101007011-1.jpg
             * ingredients : {"cCode":"S01031","createTime":1474181258380,"creatorId":0,"dishesCode":"B101007011","dishesCompId":24531,"foodName":"鱼丸","netWeight":4,"priority":1,"proMethod":"","proMethodId":"120008","rawWeight":4,"remark":"","status":"1","type":"1","yieldRate":100}
             * num : 19.0
             * proWeight : 5.0
             * weight : 260.0
             */

            private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> dishesSupplyDtlList;

            public List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> getDishesSupplyDtlList() {
                return dishesSupplyDtlList;
            }

            public void setDishesSupplyDtlList(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> dishesSupplyDtlList) {
                this.dishesSupplyDtlList = dishesSupplyDtlList;
            }

            public static class DishesSupplyDtlListBean {
            }
        }
    }
}
