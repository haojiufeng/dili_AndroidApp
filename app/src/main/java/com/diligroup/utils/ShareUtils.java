package com.diligroup.utils;

import android.app.Activity;
import android.view.View;

import com.diligroup.R;
import com.diligroup.dialog.SharePopwindow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


public class ShareUtils implements SharePopwindow.PlantFormClickLinstener {
    private Activity activity;
    private UMImage image;
    //    private UMusic music;
//    private UMVideo video;
    private String wxAftertitle = "快来看看上食上饮根据我的饮食情况，为我定制的餐后解决方案吧。";
    private String wxAftercontent = "这里有根据我的饮食情况提供的加餐指导或者是运动方案，快来看看吧。";
    private String wxBeforetitle = "快来看看上食上饮根据我的个人情况，为我定制的餐前指导吧。";
    private String wxBeforecontent = "这里有根据我的个人情况提供的能量、营养素和饮食结构指导，快来看看吧。";

    private String wxCircleBefore = "这里有根据我的情况提供的能量、营养素和饮食结构指导，快来看看吧。";
    private String wxCircleAfter = "这里有根据我的饮食情况提供的加餐指导或者是运动方案，快来看看吧。";

    private String qqBeforetitle = "快来看看我的餐前指导。";
    private String qqBeforecontent = "这里有根据我的情况提供的能量、营养素和饮食结构指导，快来看看吧。";
    private String qqAfterTitle = "快来看看我的餐后评价。";
    private String qqAfterContent = "这里有根据我的饮食情况提供的加餐指导或者是运动方案，快来看看吧。";


    private String toUrl;
    boolean isBefore;//是否是餐前指导
    View rootView;
    //分享类型-普通产品分享
    public final static int SHARE_TYPE_GENERAL_PRODUCT = 100;
    public final static int SHARE_TYPE_PING_GOU = 200;


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(activity, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(activity, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    public ShareUtils(Activity activity, boolean isBefore, String toUrl, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
        this.isBefore=isBefore;
        this.toUrl=toUrl;
        image = new UMImage(activity, R.mipmap.ic_launcher);
//        image = new UMImage(activity, "http://www.umeng.com/images/pic/social/integrated_3.png");
//        music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        music.setThumb(new UMImage(activity, "http://www.umeng.com/images/pic/social/chart_1.png"));
//        video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");

    }

    /**
     * 打开分享面板
     */
    public void openSharebord() {
        /**普通分享面板，分享相同一致内容**/
        SharePopwindow popwindow = new SharePopwindow(activity, rootView, this);
    }

    @Override
    public void onClickWX() {
        if (isBefore) {

            new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                    .withTitle(wxBeforetitle)
                    .withMedia(image)
                    .withText(wxBeforecontent)
                    .withTargetUrl(toUrl)
                    .share();
        } else {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                    .withTitle(wxAftertitle)
                    .withMedia(image)
                    .withText(wxAftercontent)
                    .withTargetUrl(toUrl)
                    .share();
        }
    }

    @Override
    public void onClickWXCircle() {
        if (isBefore) {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                    .withTitle(wxCircleBefore)
                    .withMedia(image)
                    .withText(wxCircleBefore)
                    .withTargetUrl(toUrl)
                    .share();
        } else {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                   .withText(wxCircleAfter)
                    .withMedia(image)
                    .withTitle(wxCircleAfter)
                    .withTargetUrl(toUrl)
                    .share();
        }
    }

    @Override
    public void onClickQQ() {
        if (isBefore) {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                    .withText(qqBeforecontent)
                    .withMedia(image)
                    .withTitle(qqBeforetitle)
                    .withTargetUrl(toUrl)
                    .share();
        } else {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                    .withText(qqAfterContent)
                    .withMedia(image)
                    .withTitle(qqAfterTitle)
                    .withTargetUrl(toUrl)
                    .share();
        }
    }

    @Override
    public void onClickQQZone() {
        if (isBefore) {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                    .withText(qqBeforecontent)
                    .withMedia(image)
                    .withTitle(qqBeforetitle)
                    .withTargetUrl(toUrl)
                    .share();
        } else {
            new ShareAction(activity).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                    .withText(qqAfterContent)
                    .withMedia(image)
                    .withTitle(qqAfterTitle)
                    .withTargetUrl(toUrl)
                    .share();
        }
    }

    class WZShareBoardlistener implements ShareBoardlistener {

        Activity context;
        String msg;

        public WZShareBoardlistener(Activity context, String msg) {
            this.context = context;
            this.msg = msg;
        }

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            new ShareAction(context).setPlatform(share_media).setCallback(umShareListener)
                    .withText(msg)
                    .withMedia(image)
                    .withTitle("qqshare")
                    .withTargetUrl(toUrl)
                    .share();
        }
    }

}
