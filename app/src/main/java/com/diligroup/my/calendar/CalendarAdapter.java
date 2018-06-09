package com.diligroup.my.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.Constant;
import com.diligroup.my.activity.PhysiologicalPeriodActivity;
import com.diligroup.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * 日历gridview中的每一个item显示的textview
 * 格式：2016-8-25
 *
 * @author Vincent Lee
 */
public class CalendarAdapter extends BaseAdapter {
    private boolean isLeapyear = false; // 是否为闰年
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
    // private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
    private SpecialCalendar sc = null;
    private LunarCalendar lc = null;
    private Resources res = null;
//	private Drawable drawable = null;

    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private int currentFlag = -1; // 用于标记当天
    private int[] schDateTagFlag = null; // 存储当月所有的日程日期

    private String showYear = ""; // 用于在头部显示的年份
    private String showMonth = ""; // 用于在头部显示的月份
    private Typeface typeFace;
    private TextView calendar_today;//“今天”文字

    public String getShowDay() {
        return sys_day;
    }

    public void setShowDay(String showDay) {
        this.showDay = showDay;
    }

    private String showDay = ""; // 用于在头部显示的日期

    private String animalsYear = "";
    private String leapMonth = ""; // 闰哪一个月
    private String cyclical = ""; // 天干地支
    // 系统当前时间
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    ArrayList<String> templateDateList;//首页展示日期列表
    ArrayList<String> dayList = new ArrayList<>();//首页展示日期列表
    ArrayList<String> selectDate = new ArrayList<>();//用户已选择的生理周期集合
    ArrayList<String> selectDays = new ArrayList<>();//用户已选择的生理周期集合
    TreeMap<String, View> tempMap = new TreeMap<>();
    boolean isFromMy;//是否来自我的个人资料页面
    boolean isFromHome;
    boolean isFromAfter;
    boolean hasDraw;

    private Drawable image1;
    private Drawable image2;//TextView中间加载图片
    public CalendarAdapter() {
        Date date = new Date();
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    }

    public CalendarAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c) {
        this();
        this.context = context;
        sc = new SpecialCalendar();
        lc = new LunarCalendar();
        this.res = rs;

        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/FZLTCXHJW.TTF");
        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }

            //TextView记载图片
            image1 = context.getResources().getDrawable(R.drawable.circle_red);
            image1.setBounds(0, 0, image1.getMinimumWidth(), image1.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
            image2 = context.getResources().getDrawable(R.drawable.circle_green);
            image2.setBounds(0, 0, image2.getMinimumWidth(), image2.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
        }

        currentYear = String.valueOf(stepYear); // 得到当前的年份
        currentMonth = String.valueOf(stepMonth); // 得到本月
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天
        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
    }

    public CalendarAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c, String flag, ArrayList<String> templateDateList) {
        this(context, rs, jumpMonth, jumpYear, year_c, month_c, day_c);
        if (flag.equals(Constant.FROM_HOME)) {
            this.isFromHome = true;
            for (int i = 0; i < templateDateList.size(); i++) {
                String[] tempArr = templateDateList.get(i).split("-");
                if (tempArr[0].equals(getShowYear()) && tempArr[1].equals(getShowMonth())) {
                    dayList.add(tempArr[2]);
                }
            }
        } else if (flag.equals(Constant.FROM_AFTER)) {
            this.isFromAfter = true;
        } else if (flag.equals(Constant.FROM_MY)) {
            this.isFromMy = true;
//            selectDate.add("2016-8-2");
//            selectDate.add("2016-8-1");
            if (templateDateList != null) {
                for (int i = 0; i < templateDateList.size(); i++) {
                    String[] tempArr = templateDateList.get(i).split("-");
                    if (tempArr[0].equals(getShowYear()) && tempArr[1].equals(getShowMonth())) {
                        selectDays.add(tempArr[2]);
                    }
                }
            }
        }
    }

    public CalendarAdapter(Context context, Resources rs, int year_c, int month_c, int day_c) {
        this();
        this.context = context;
        sc = new SpecialCalendar();
        lc = new LunarCalendar();
        this.res = rs;
        this.isFromMy = isFromMy;
        currentYear = String.valueOf(year_c);// 得到跳转到的年份
        currentMonth = String.valueOf(month_c); // 得到跳转到的月份
        currentDay = String.valueOf(day_c); // 得到跳转到的天
        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
        calendar_today = (TextView) convertView.findViewById(R.id.calendar_today);
        Button select_pic= (Button) convertView.findViewById(R.id.calendar_selected_pic);
        String d = dayNumber[position].split("\\.")[0];
        String dv = dayNumber[position].split("\\.")[1];

        SpannableString sp = new SpannableString(d);
//		SpannableString sp = new SpannableString(d + "\n" + dv);
        sp.setSpan(new StyleSpan(Typeface.NORMAL), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		if (dv != null || dv != "") {
//			sp.setSpan(new RelativeSizeSpan(0.75f), d.length() + 1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}去除农历显示，角标越界异常
        // sp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 14, 16,
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(sp);
        textView.setTypeface(typeFace);
        textView.setTextColor(context.getResources().getColor(R.color.calendar_gray_color));
        select_pic.setTypeface(typeFace);
        if (isFromHome && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            String[] temp = dayNumber[position].split("\\.");
            if (dayList.contains(temp[0])) {
                textView.setTextColor(context.getResources().getColor(R.color.black1));// 可用日期字体设黑
            }
        } else if (isFromAfter && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            Calendar calendar = Calendar.getInstance();
            String str2 = new SimpleDateFormat("yyyy-M-d").format(calendar.getTime());//现在日期
            calendar.add(Calendar.MONTH, -6);
            String str1 = new SimpleDateFormat("yyyy-M-d").format(calendar.getTime());//6月之前
            try {
                if (DateUtils.isBetween(str1, str2, getShowYear() + "-" + getShowMonth() + "-" + dayNumber[position].split("\\.")[0])) {
                    textView.setTextColor(context.getResources().getColor(R.color.black1));// 可用日期字体设黑
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (isFromMy && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {//如果已经选择生理日期，将生理日期选中
            String[] temp = dayNumber[position].split("\\.");
            if (selectDays.contains(temp[0])) {
                textView.setTextColor(context.getResources().getColor(R.color.white));//
                if (!hasDraw) {//是否已经画出了其中一个（一共2个）
//                    textView.setBackgroundResource(R.drawable.circle_red);
                    select_pic.setVisibility(View.VISIBLE);
                    select_pic.setText(temp[0]);
                    select_pic.setBackgroundResource(R.drawable.circle_red);
                    convertView.setTag("red");
                    tempMap.put(showYear + "-" + showMonth + "-" + temp[0], convertView);
//                    ((PhysiologicalPeriodActivity) context).addDate(showYear + "-" + showMonth + "-" + temp[0]);
                    hasDraw = true;
                } else {
//                    textView.setBackgroundResource(R.drawable.circle_green);
                    select_pic.setVisibility(View.VISIBLE);
                    select_pic.setText(temp[0]);
                    select_pic.setBackgroundResource(R.drawable.circle_green);
                    convertView.setTag("green");
                    tempMap.put(showYear + "-" + showMonth + "-" + temp[0], convertView);
//                    ((PhysiologicalPeriodActivity) context).addDate(showYear + "-" + showMonth + "-" + temp[0]);
                    ((PhysiologicalPeriodActivity) context).setTempList(tempMap);
                }
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.black1));// 当月字体设黑
            }
        } else if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 应用字体
            // 当前月信息显示
            textView.setTextColor(context.getResources().getColor(R.color.black1));// 当月字体设黑
        }
        if (currentFlag == position) {
            // 设置当天的背景
//		Drawable drawable = res.getDrawable(R.drawable.circle_red);
//			drawable = new ColorDrawable(Color.rgb(23, 126, 214));
//			textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_green));
            textView.setTextColor(context.getResources().getColor(R.color.common_orenge));
            calendar_today.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    // 得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
        Log.d("DAY", isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek + "  =========   " + lastDaysOfMonth);
        getweek(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNuber中
    private void getweek(int year, int month) {
        int j = 1;
        int flag = 0;
        String lunarDay = "";

        // 得到当前月的所有日程日期(这些日期需要标记)

        for (int i = 0; i < dayNumber.length; i++) {
            // 周一
            // if(i<7){
            // dayNumber[i]=week[i]+"."+" ";
            // }
            if (i < dayOfWeek) { // 前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                lunarDay = lc.getLunarDate(year, month - 1, temp + i, false);
                dayNumber[i] = (temp + i) + "." + lunarDay;

            } else if (i < daysOfMonth + dayOfWeek) { // 本月
                String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
                lunarDay = lc.getLunarDate(year, month, i - dayOfWeek + 1, false);
                dayNumber[i] = i - dayOfWeek + 1 + "." + lunarDay;
                // 对于当前月才去标记当前日期
                if (sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)) {
                    // 标记当前日期
                    currentFlag = i;
                }
                setShowYear(String.valueOf(year));
                setShowMonth(String.valueOf(month));
                setAnimalsYear(lc.animalsYear(year));
                setLeapMonth(lc.leapMonth == 0 ? "" : String.valueOf(lc.leapMonth));
                setCyclical(lc.cyclical(year));
            } else { // 下一个月
                lunarDay = lc.getLunarDate(year, month + 1, j, false);
                dayNumber[i] = j + "." + lunarDay;
                j++;
            }
        }

        String abc = "";
        for (int i = 0; i < dayNumber.length; i++) {
            abc = abc + dayNumber[i] + ":";
        }
        Log.d("DAYNUMBER", abc);

    }

    public void matchScheduleDate(int year, int month, int day) {

    }

    /**
     * 点击每一个item时返回item中的日期
     *
     * @param position
     * @return
     */
    public String getDateByClickItem(int position) {
        return dayNumber[position];
    }

    /**
     * 在点击gridView时，得到这个月中第一天的位置
     *
     * @return
     */
    public int getStartPositon() {
        return dayOfWeek + 7;
    }

    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     *
     * @return
     */
    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth + 7) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }

    public String getCyclical() {
        return cyclical;
    }

    public void setCyclical(String cyclical) {
        this.cyclical = cyclical;
    }
}
