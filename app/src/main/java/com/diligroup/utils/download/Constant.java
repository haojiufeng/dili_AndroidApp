package com.diligroup.utils.download;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/6/17.
 */
public class Constant {
    /**
     * 下载的url地址
     */
    public static final String URL1 = "http://downloadc.dewmobile.net/z/kuaiya282.apk";
    public static final String URL2 = "http://gdown.baidu.com/data/wisegame/1b9392eadc3bddf1/WeChat_480.apk";

    /**
     * 本地保存地址
     */
    public static final String LOCALPATH =getLocalpath();
    public static String getLocalpath(){
        String path=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/qmc/apk/");
            if (!file.exists()) file.mkdirs();
            path=file.getAbsolutePath();

        }else {

            path=null;
        }
        return path;
    }


    /**
     * 文件名
     */
    public static String getFileName(String url){
        String appName = url.substring(url.lastIndexOf("/"));
        return appName;
    }

    /**
     * 下载的线程数量
     */
    public static final int THREADCOUNT = 4;

}

