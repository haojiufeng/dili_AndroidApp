package com.diligroup.utils.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "Download";
    /**
     * 数据库版本号
     */
    private static final int DB_VERSION = 1;
    /**
     * 创建download_info表sql语句
     */
    private static final String CREATE_DOWNLOADINFO_TABLE = "create table download_info ("
            + "_id integer primary key autoincrement,"
            + "thread_id integer,"
            + "start_pos integer, "
            + "end_pos integer, "
            + "compelete_size integer," + "url varchar)";
    /**
     * 创建local_info表sql语句 用来保存下载文件的状态信息
     */
    private static final String CREATE_LOCALINFO_TABLE = "create table local_info ("
            + "_id integer primary key autoincrement,"
            + "name varchar,"
            + "url varchar, "
            + "state integer, "
            + "completeSize integer,"
            + "fileSize integer)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOWNLOADINFO_TABLE);
        db.execSQL(CREATE_LOCALINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

