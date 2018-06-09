package com.diligroup.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hjf on 2016/7/20.
 */
public class DateUtils {

    private static int year;
    private static int month;
    private static int day;
    private static int hours;//当前是几点

    /**
     * 判断date3 距离前两个日期哪个更接近一些
     *
     * @param DATE1
     * @param DATE2
     * @param date3
     * @return
     */
    public static String compare_date(String DATE1, String DATE2, String date3) {
        DateFormat df = new SimpleDateFormat("yyyy-M-d");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            Date dt3 = df.parse(date3);
            Date nearDate = Math.abs(dt3.getTime() - dt1.getTime()) < Math.abs(dt3.getTime() - dt2.getTime()) ? dt1 : dt2;
            String nearStr = df.format(nearDate);
//            System.out.println("最近的日期是====" + nearStr);
//            System.out.println("两个日期相等吗====" + nearStr.equals(DATE1));
            return nearStr;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期 2016年7月20日
     *
     * @return
     */
    public static String getCurrentDate() {
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DATE);
        getNetDate();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LogUtils.e("网络时间==" + year + "年" + (month + 1) + "月" + day + "日");
        return year + "年" + (month) + "月" + day + "日";
    }

    public static void getNetDate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = null;//取得资源对象
                    url = new URL("http://www.baidu.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间

                    Date date = new Date(ld); //转换为标准时间对象
                    String todayStr = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(date);
                    String[] dates = todayStr.split(" ")[0].split("-");
                    year = Integer.parseInt(dates[0]);
                    month = Integer.parseInt(dates[1]);
                    day = Integer.parseInt(dates[2]);

                    String[] times = todayStr.split(" ")[1].split("-");
                    hours = Integer.parseInt(times[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取当前日期 2016年7月20日
     *
     * @return
     */
    public static int getCurrentTime() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hours;
    }

    /**
     * 获取当前星期
     *
     * @return
     */
    public static String getWeekDay() {
        Calendar calendar = Calendar.getInstance();
            calendar.set(year,month-1,day);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        switch (weekDay) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
        }
        return "周" + weekDay;
    }

    // 指定某年中的某月的第一天是星期几
    public static String getWeekdayOfMonth(int year, int month, int day) {
        String[] arr = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return arr[dayOfWeek];
    }

    /**
     * 判断一个日期是不是今天日期
     *
     * @param y
     * @param mon
     * @param d
     * @return
     */
    public static boolean isToday(int y, int mon, int d) {
        getCurrentDate();
        if (year == y && (month + 1) == mon && day == d) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 推断一个日期一个间隔时间段后的日期
     *
     * @param startdate
     */
    public static String getDate(String startdate, int delayDay) {
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy年M月d日")).parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, delayDay);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String dateStr = new SimpleDateFormat("yyyy年M月d日").format(cal.getTime());
//        if(dateStr.charAt(5)=='0' )
        switch (dayOfWeek) {
            case 0:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周日";
            case 1:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周一";
            case 2:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周二";
            case 3:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周三";
            case 4:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周四";
            case 5:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周五";
            case 6:
                return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime()) + " 周六";
        }
        return new SimpleDateFormat("yyyy年M月d日").format(cal.getTime());
    }

    /*
    日期格式转换 2016年7月26日 转为 2016-7-26
     */
    public static String dateFormatChagee(String date) {
        return date.replace("年", "-").replace("月", "-").replace("日", "-").trim();
    }

    /**
     * 2016-09-09 转换为2016-9-9
     *
     * @param date
     * @return
     */
    public static String dateFormatChanged(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        try {
            return formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2016-9-9 转换为2016-09-09
     *
     * @param date
     * @return
     */
    public static String dateFormatChanged_2(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 首页的日期集合转换
     *
     * @param mList
     * @return
     */
    public static ArrayList dateList(ArrayList<String> mList) {
        for (int i = 0; i < mList.size(); i++) {
            mList.set(i, dateFormatChanged(mList.get(i)));
        }
        return mList;
    }

    /**
     * 比较两个日期大小
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compareDate(String str1, String str2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d");
        int tem = formatter.parse(str1).compareTo(formatter.parse(str2));
        return tem;
    }

    /**
     * 比较两个日期大小
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compareDate2(String str1, String str2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        int tem = formatter.parse(str1).compareTo(formatter.parse(str2));
        return tem;
    }

    /**
     * 比较一个日期是否在两个日期之间
     *
     * @param str1 6月前
     * @param str2 现在  str1<str2<str3
     * @return
     */
    public static boolean isBetween(String str1, String str2, String str3) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        int tem1 = formatter.parse(str3).compareTo(formatter.parse(str2));
        int tem2 = formatter.parse(str3).compareTo(formatter.parse(str1));
        if ((tem1 == -1 || tem1 == 0) && tem2 == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取格式化的时间
     *
     * @param time
     * @return
     */
    public static String getDateFormatString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date(time));
    }

    /**
     * 获取今天日期yyyy-MM-dd
     *
     * @return
     */
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.print("网络时间2==" + sdf.format(date));
        return sdf.format(date);
    }

}
