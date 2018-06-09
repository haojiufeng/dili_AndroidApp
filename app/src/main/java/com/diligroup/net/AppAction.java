package com.diligroup.net;
import com.diligroup.bean.AfterNoFoodBean;
import com.diligroup.bean.BannerBean;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.CostomerCategory;
import com.diligroup.bean.CustomerSearchResultBean;
import com.diligroup.bean.FindFoodByCategory;
import com.diligroup.bean.GetAllergyDetailBean;
import com.diligroup.bean.GetCityCode;
import com.diligroup.bean.GetDietRecordBean;
import com.diligroup.bean.GetFoodDetailsBean;
import com.diligroup.bean.GetFoodTypeBean;
import com.diligroup.bean.GetJiaoQinBean;
import com.diligroup.bean.GetJobBean;
import com.diligroup.bean.GetShopBean;
import com.diligroup.bean.GetUserInfoFromServiceBean;
import com.diligroup.bean.GetWhereBean;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.bean.OtherRequestBean;
import com.diligroup.bean.ProvingCodeBean;
import com.diligroup.bean.StoreSupplySearchBean;
import com.diligroup.bean.UpdateVersionBean;
import com.diligroup.bean.UploadInfo;
import com.diligroup.bean.UserBeanFromService;

import java.util.HashMap;
/**
 * Created by hjf on 2016/9/24.
 */
public class AppAction {
    public  enum Action {
        /*登陆*/
        LOGIN,
        THIRD_PART_LOGIN,
        ALIPAY_LOGIN,
        /*注册*/
        REGISTER,
        /*获取验证码*/
        SMSCODE,
        /*修改密码*/
        MODIFY(),
        BANNER,
        /*菜品评价*/
        DISEVALUATE,
        APPEVALUATE,
        /*上传头像*/
        UPLOAD_PHOTO,
        /*获取首页门店供应列表*/
        GET_HOME_LIST,
        /*上报更新用户信息*/
        SET_INFOS,
        GET_WORK_TYPE,
        GET_WHERE,
        GET_NO_EAT,
        GET_ALLERGY,
        GET_OTHER,
        GET_SPECIAL,
        GET_TASTE,
        GET_HISTORY,
        GET_ALLERGY_DETAILS,
        /*自定义菜品成品分类*/
        GET_COSTOMER_FOODLIST,
        /**
         * 获取饮食记录
         */
        GET_DIET_RECORD,
        /*自定义菜品搜索*/
        CUSTOMER_SEARCH,
        /*添加菜品完成接口*/
        ADD_FOOD_COMPLETE,
        /*门店供应搜索*/
        STORESUPPLY_SEARCH,
        /*无三餐饮食评价*/
        NOFOOD_INVALUATE,
        SELECT_USER_INFO,
        CUSTOMER_FIND_BY_CATEGORYID,
        /**
         * 获取菜品详情
         */
        GET_DETAILS,
        /**
         * 获取 门店附近的
         */
        GET_SHOP_NEARBY,
        Get_CityCode,
        GET_USER_INFO,
        GET_OTHER_REQ,
        UPDTE_VERSION,
        UPDATA_USERINFO;

        /**
         * 根据Action获取解析类
         *
         * @param action
         * @return
         */
        public static Class getAction(Action action) {
            switch (action) {
                case LOGIN:
                    return UserBeanFromService.class;
                case REGISTER:
                    return CommonBean.class;
                case SMSCODE:
                    return ProvingCodeBean.class;
                case MODIFY:
                    return CommonBean.class;
                case GET_USER_INFO:
                    return GetUserInfoFromServiceBean.class;
                case BANNER:
                    return BannerBean.class;
                case DISEVALUATE:
                    return CommonBean.class;
                case SET_INFOS:
                    return CommonBean.class;
                case UPLOAD_PHOTO:
                    return UploadInfo.class;
                case GET_WORK_TYPE:
                    return GetJobBean.class;
                case GET_NO_EAT:
                    return GetJiaoQinBean.class;
                case GET_ALLERGY:
                    return GetFoodTypeBean.class;
                case GET_OTHER:
                    return CommonBean.class;
                case GET_SPECIAL:
                    return GetJiaoQinBean.class;
                case GET_TASTE:
                    return GetJiaoQinBean.class;
                case GET_HISTORY:
                    return GetJiaoQinBean.class;
                case GET_HOME_LIST:
                    return HomeStoreSupplyList.class;
                case GET_ALLERGY_DETAILS:
                    return GetAllergyDetailBean.class;
                case GET_DETAILS:
                    return GetFoodDetailsBean.class;
                case GET_COSTOMER_FOODLIST:
                    return CostomerCategory.class;
                case GET_DIET_RECORD:
                    return GetDietRecordBean.class;
                case CUSTOMER_SEARCH:
                    return CustomerSearchResultBean.class;
                case CUSTOMER_FIND_BY_CATEGORYID:
                    return FindFoodByCategory.class;
                case STORESUPPLY_SEARCH:
                    return StoreSupplySearchBean.class;
                case ADD_FOOD_COMPLETE:
                    return CommonBean.class;
                case UPDATA_USERINFO:
                    return CommonBean.class;
                case GET_WHERE:
                    return GetWhereBean.class;
                case GET_SHOP_NEARBY:
                    return GetShopBean.class;
                case THIRD_PART_LOGIN:
                    return UserBeanFromService.class;
                case ALIPAY_LOGIN:
                    return CommonBean.class;
                case SELECT_USER_INFO:
                    return UserBeanFromService.class;
                case Get_CityCode:
                    return GetCityCode.class;
                case NOFOOD_INVALUATE:
                    return AfterNoFoodBean.class;
                case GET_OTHER_REQ:
                    return OtherRequestBean.class;
                case APPEVALUATE:
                    return CommonBean.class;
                case UPDTE_VERSION:
                    return UpdateVersionBean.class;
            }
            return null;
        }
//        private String value;
//
//        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
//        Action(String value) {
//            this.value = value;
//        }
//        public String getValue() {
//            return value;
//        }
    }
public static  HashMap<Action,String> actionMap=new HashMap<Action,String>(){
    {
    put(Action.LOGIN,Urls.LOGIN);
    put(Action.THIRD_PART_LOGIN, Urls.REGISTER);
    put(Action.ALIPAY_LOGIN,Urls.alipaytUrl);

    put(Action.REGISTER,Urls.REGISTER);
    put(Action.SMSCODE,Urls.SMSCODE);
    put(Action.MODIFY,Urls.MODIFYPSD);

    put(Action.BANNER,Urls.GETBANNER);
    put(Action.DISEVALUATE,Urls.DISHEVALUATE);
    put(Action.APPEVALUATE,Urls.BASE);

    put(Action.UPLOAD_PHOTO,Urls.UPLOAD_PHOTO);
    put(Action.GET_HOME_LIST,Urls.GET_HOMELIST);
    put(Action.SET_INFOS,Urls.UPDATA_USERINFOS);

    put(Action.GET_WORK_TYPE,Urls.GET_WORK_TYPE);
    put(Action.GET_WHERE,Urls.BASE);
    put(Action.GET_NO_EAT,Urls.GET_NO_EAT);

    put(Action.GET_ALLERGY,Urls.GET_ALLERGY);
    put(Action.GET_OTHER,Urls.BASE);
    put(Action.GET_SPECIAL,Urls.BASE);

    put(Action.GET_TASTE,Urls.BASE);
    put(Action.GET_HISTORY,Urls.BASE);
    put(Action.GET_ALLERGY_DETAILS,Urls.BASE);

    put(Action.GET_COSTOMER_FOODLIST,Urls.GET_COSTOMER_FOOD_LIST);
    put(Action.GET_DIET_RECORD,Urls.BASE);
    put(Action.CUSTOMER_SEARCH,Urls.GET_COSTOMER_FOOD_LIST);

    put(Action.ADD_FOOD_COMPLETE,Urls.BASE);
    put(Action.STORESUPPLY_SEARCH,Urls.GET_HOMELIST);
    put(Action.NOFOOD_INVALUATE,Urls.BASE);

    put(Action.SELECT_USER_INFO,Urls.LOGIN);
    put(Action.CUSTOMER_FIND_BY_CATEGORYID,Urls.CUSTOMER_FINDBY_CATEGORYID);
    put(Action.GET_DETAILS,Urls.BASE);

    put(Action.GET_SHOP_NEARBY,Urls.BASE);
    put(Action.Get_CityCode,Urls.BASE);
    put(Action.GET_USER_INFO,Urls.BASE);

    put(Action.GET_OTHER_REQ,Urls.BASE);
    put(Action.UPDTE_VERSION,Urls.BASE);
    put(Action.UPDATA_USERINFO,Urls.BASE);
    }
};
}
