package com.diligroup.net;

/**
 * Created by hjf on 2016/6/27 0027.
 */
public class Urls {
//    public static String HOST = "http://192.168.100.67:8181";
    public static String HOST = "http://116.90.83.216:8081/";
//        public static  String HOST = "http://192.168.101.2:8181";//业务调试主机
    //    private static final String HOST = "http://192.168.100.246:8181";//符平主机
//    public static  String HtmlHOST = "http://192.168.101.2:8180";//业务调试主机
//    public static String HtmlHOST = "http://192.168.100.67:8180";//业务调试主机
    public static String HtmlHOST = "http://h5app.ypp2015.com/";//业务调试主机

    public static String AfterUrl ="/tmpl/guide_after.html?";
    public static String BeforeUrL ="/tmpl/guide.html?";
    public static String instructionUrL ="/tmpl/using_help.html?";//使用说明web页面
    public static String aboutUsUrL ="/tmpl/about_us.html?";//关于我们web页面


    public static String thirdPartUrl = "/gateway/dis/prepose.action";
    public static String alipaytUrl = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?";

    public static String BASE = "/gateway/dis/prepose.action";

    /*登录*/
    public static String LOGIN = "/gateway/dis/prepose.action";
    /*注册*/
    public static String REGISTER = "/gateway/dis/prepose.action";

    /*获取验证码*/
    public static String SMSCODE = "/gateway/dis/prepose.action";
    /*修改密码*/
    public static String MODIFYPSD = "/gateway/dis/prepose.action";
    public static String LOGINOUT = "/gateway/dis/prepose.action";

    /*获取首页轮播图  */
    public static String GETBANNER = "/gateway/dis/prepose.action";
    public static String UPDATA_USERINFOS = "/gateway/dis/prepose.action";
    public static String DISHEVALUATE = "/gateway/dis/prepose.action";
    /*获取自定义菜品成品分类 */
    public static String GET_COSTOMER_FOOD_LIST = "/gateway/dis/prepose.action";
    /*获取自定义菜品搜索 */
    public static String COSTOMER_SEARCH = "/gateway/dis/prepose.action";
    /* 上传头像 */
    public static String UPLOAD_PHOTO = "/gateway/dis/uploadByteArray.action";
    /*获取  公共职业信息列表数据*/
    public static String GET_WORK_TYPE = "/gateway/dis/prepose.action";
    /*获取  饮食禁忌的 食材*/
    public static String GET_NO_EAT = "/gateway/dis/prepose.action";
    /*获取过敏食材*/
    public static String GET_ALLERGY = "/gateway/dis/prepose.action";
    /* 首页获取门店供应列表 */
    public static String GET_HOMELIST = "/gateway/dis/prepose.action";
    /* 自定义根据成品分类查询菜品列表 */
    public static String CUSTOMER_FINDBY_CATEGORYID = "/gateway/dis/prepose.action";

    public static final String APK_PATH = "/sdcard/diliapp/apk/";
//    public static final String    APP             =   "http://images.ypp2015.com/ypp/upload/apk/apk_debug_15.apk";
}
