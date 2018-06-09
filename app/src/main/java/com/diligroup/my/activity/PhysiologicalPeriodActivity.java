package com.diligroup.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.UserInfoBean;
import com.diligroup.home.HomeActivity;
import com.diligroup.my.calendar.CalendarAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.StringUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.view.MyGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 上报生理周期
 */
public class PhysiologicalPeriodActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.currentMonth)
    TextView currentMonth;
    /**
     * 当前的年月，现在日历顶端
     */
    @Bind(R.id.prevMonth)
    ImageView prevMonth;
    @Bind(R.id.nextMonth)
    ImageView nextMonth;
    @Bind(R.id.flipper)
    ViewFlipper flipper;
    @Bind(R.id.add)
    TextView add;//“+“
    @Bind(R.id.reduce)
    TextView reduce;//"-"
    @Bind(R.id.cycle_num)
    TextView cycle_num;//周期数
    @Bind(R.id.nextstep)
    Button nextStep;//下一步
    @Bind(R.id.say_laater)
    Button say_latter;//稍后再说
    @Bind(R.id.cycle_title_info)
    TextView cycle_title_info;
    @Bind(R.id.sunday)
    TextView sunday;
    @Bind(R.id.monday)
    TextView monday;
    @Bind(R.id.tuesday)
    TextView tuesday;
    @Bind(R.id.wednesday)
    TextView wednesday;
    @Bind(R.id.thursday)
    TextView thursday;
    @Bind(R.id.friday)
    TextView friday;
    @Bind(R.id.saturday)
    TextView saturday;
    @Bind(R.id.physiological_layout)
    RelativeLayout physiological_layout;//日历选择需要gone掉的头布局
    @Bind(R.id.gone_fl)
    FrameLayout gone_fl;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";

    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private MyGridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private Drawable image1;
    private Drawable image2;

    //设置选中日期的方法
    public void setTempList(TreeMap<String, View> tempList) {
        if (tempList != null && tempList.size() == 1) {
            this.tempList.putAll(tempList);
        } else {
            this.tempList = tempList;
        }
    }

    TreeMap<String, View> tempList = new TreeMap<>();
    /**
     * 添加选中的日期
     *
     * @param date
     */
//    public void addDate(String date) {
//        if (selectDate.size() < 2){
//            if(selectDate.size()==1 && !date.equals(selectDate.get(0)))
//            selectDate.add(date);
//        }
//    }

    ArrayList<String> selectDate = new ArrayList<>();//用户的选中的生理周期集合
    ArrayList<String> templateDateList = new ArrayList<>();
    ArrayList<String> dayList = new ArrayList<>();
    Intent mIntent = new Intent();
    ;
    private Typeface typeFace;
    private boolean isFromHome;//
    private boolean isFromAfter;
    private String homeDate;
    int cycles = 28;//生理周期天数
    private boolean isFromMy;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_physiological_period;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        ButterKnife.bind(this);
        tv_title.setText("您的生理期");
        cycle_title_info.setText("请选择您上次经期的开始和结束时间");
        ivBack.setVisibility(View.VISIBLE);

        isFromHome = getIntent().getBooleanExtra("isFromHome", false);
        //是否来自用户中心
        isFromMy = getIntent().getBooleanExtra("isFromMy", false);
        //是否来自餐后评价
        isFromAfter = getIntent().getBooleanExtra("isFromAfter", false);
        String tempCycle = getIntent().getStringExtra("cycleNum");
        if (!TextUtils.isEmpty(tempCycle)) {
            cycles = Integer.parseInt(tempCycle);
        }
        String tempStr = getIntent().getStringExtra("physiologDate");//获取用户的生理周期
        if (!TextUtils.isEmpty(tempStr) && tempStr.split("至").length == 2) {
            selectDate.clear();
//            if (tempStr.split("至")[0].length() < 9) {
//                selectDate.add("2016-" + tempStr.split("至")[0]);
//                selectDate.add("2016-" + tempStr.split("至")[1]);
//            } else {
            selectDate.add(tempStr.split("至")[0]);
            if (!tempStr.split("至")[1].equals(tempStr.split("至")[0])) {
                selectDate.add(tempStr.split("至")[1]);
//                }
            }
        }
        //TextView记载图片
        image1 = getResources().getDrawable(R.drawable.circle_red);
        image1.setBounds(0, 0, image1.getMinimumWidth(), image1.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
        image2 = getResources().getDrawable(R.drawable.circle_green);
        image2.setBounds(0, 0, image2.getMinimumWidth(), image2.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
        homeDate = getIntent().getStringExtra("currentDate");
        templateDateList = (ArrayList<String>) getIntent().getSerializableExtra("dateList");
        setViews();
        setListeners();
    }

    private void setViews() {
        cycle_num.setText(cycles + "");
        if (UserManager.getInstance().isFirstRecordInfo()) {
            AppManager.getAppManager().addActivity(this);
            nextStep.setText("下一步");
            say_latter.setVisibility(View.VISIBLE);
        } else {
            nextStep.setText("确定");
            say_latter.setVisibility(View.GONE);
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        if (isFromHome) {
            physiological_layout.setVisibility(View.GONE);
            gone_fl.setVisibility(View.GONE);
            nextStep.setVisibility(View.GONE);
            say_latter.setVisibility(View.GONE);
            currentDate = DateUtils.dateFormatChagee(homeDate);
            tv_title.setText("选择门店供应菜品的日期");
        } else if (isFromAfter) {
            physiological_layout.setVisibility(View.GONE);
            gone_fl.setVisibility(View.GONE);
            nextStep.setVisibility(View.GONE);
            say_latter.setVisibility(View.GONE);
            currentDate = DateUtils.dateFormatChagee(homeDate);
            tv_title.setText("请选择您的用餐日期");
        } else {
            currentDate = sdf.format(date); // 当期日期
        }
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        gestureDetector = new GestureDetector(this, new MyGestureListener());
        flipper.removeAllViews();
        if (isFromHome) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_HOME, templateDateList);
        } else if (isFromMy) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_MY, selectDate);
        } else if (isFromAfter) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_AFTER, null);
        } else {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        }
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);

        sunday.setTypeface(typeFace);
        monday.setTypeface(typeFace);
        tuesday.setTypeface(typeFace);
        wednesday.setTypeface(typeFace);
        thursday.setTypeface(typeFace);
        friday.setTypeface(typeFace);
        saturday.setTypeface(typeFace);
    }

    private void setListeners() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        nextStep.setOnClickListener(this);

        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        say_latter.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            case R.id.nextstep://确定按钮
//                if (TextUtils.isEmpty(StringUtils.getMapKeystr(tempList))) {//什么也没有选择
//
//                }
                if (!(TextUtils.isEmpty(StringUtils.getMapKeystr(tempList))) && StringUtils.getMapKeystr(tempList).split(",").length == 1) {
                    ToastUtil.showLong(this, "请选择正确的生理周期");
                    return;
                }
                try {
                    //如果是第一次录入用户信息，需要跳转reportOtheractivity 按钮文字展示“下一步” 否则按钮文字展示确定
                    if (UserManager.getInstance().isFirstRecordInfo()) {
                        if (TextUtils.isEmpty(StringUtils.getMapKeystr(tempList))) {
                            UserInfoBean.getInstance().setPeriodStartTime("");
                            UserInfoBean.getInstance().setPeriodEndTime("");
                            UserInfoBean.getInstance().setPeriodNum("");
                        } else {
                            if (DateUtils.compareDate2(StringUtils.getMapKeystr(tempList).split(",")[0], StringUtils.getMapKeystr(tempList).split(",")[1]) > 0) {//日期排序
                                UserInfoBean.getInstance().setPeriodStartTime(DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[1]));
                                UserInfoBean.getInstance().setPeriodEndTime(DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[0]));
                            } else {
                                UserInfoBean.getInstance().setPeriodStartTime(DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[0]));
                                UserInfoBean.getInstance().setPeriodEndTime(DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[1]));
                            }
                            UserInfoBean.getInstance().setPeriodNum(cycles + "");
                        }
                        say_latter.setVisibility(View.VISIBLE);
                        readyGo(ReportOther.class);
                    } else if (isFromMy) {
                        HashMap map = new HashMap();
                        if (TextUtils.isEmpty(StringUtils.getMapKeystr(tempList))) {
                            map.put("periodStartTime", "");
                            map.put("periodEndTime", "");
                            map.put("periodNum", "");
                        } else {
                            String str1 = StringUtils.getMapKeystr(tempList).split(",")[0];
                            String str2 = StringUtils.getMapKeystr(tempList).split(",")[1];
                            if (DateUtils.compareDate2(str1, str2) > 0) {
                                map.put("periodStartTime", DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[1]));
                                map.put("periodEndTime", DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[0]));
                            } else {
                                map.put("periodStartTime", DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[0]));
                                map.put("periodEndTime", DateUtils.dateFormatChanged_2(StringUtils.getMapKeystr(tempList).split(",")[1]));
                            }
                            map.put("periodNum", cycles + "");
                        }
                        Api.updataUserInfo(map, this);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.add:
                cycles = Integer.parseInt(cycle_num.getText().toString().trim()) + 1;
                cycle_num.setText(cycles + "");
                break;
            case R.id.reduce:
                if (Integer.parseInt(cycle_num.getText().toString().trim()) > 1) {
                    cycles = Integer.parseInt(cycle_num.getText().toString().trim()) - 1;
                    cycle_num.setText(cycles + "");
                } else {
                    ToastUtil.showLong(this, "亲：你的填写有误哦！");
                }
                break;
            case R.id.say_laater://稍后再说
                //上传所有用户输入信息
                Api.setUserInfo(this);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        if (isFromHome) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_HOME, templateDateList);
        } else if (isFromMy) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_MY, selectDate);
        } else if (isFromAfter) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_AFTER, selectDate);
        } else {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        }
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

//        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        if (isFromHome) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_HOME, templateDateList);
        } else if (isFromMy) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_MY, selectDate);
        } else if (isFromAfter) {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, Constant.FROM_AFTER, selectDate);
        } else {
            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        }
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        gridView = new MyGridView(this);
        gridView.setNumColumns(7);
//        gridView.setColumnWidth(47);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (width == 720 && height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return PhysiologicalPeriodActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String str = getItemClickDate(position);
                View tempBg = arg1.findViewById(R.id.calendar_selected_pic);//红色和绿色的背景圆圈
                if (str == null) {//invalidate date
                    return;
                }
                if (isFromHome || isFromAfter) {
                    mIntent = new Intent();
                    mIntent.putExtra("current", getItemClickDate(position));
                    setResult(20, mIntent);
                    finish();
                }
                if (tempList.size() == 0) {
                    tempList.put(getItemClickDate(position), arg1);//日期作为键 对应的view作为值放在map中
                    arg1.setTag("red");//
//                    arg1.findViewById(R.id.tvtext).setBackgroundResource(R.drawable.circle_red);
                    tempBg.setBackgroundResource(R.drawable.circle_red);
                    tempBg.setVisibility(View.VISIBLE);
                    ((Button) tempBg).setText(calV.getDateByClickItem(position).split("\\.")[0]);
//                    ((TextView) arg1.findViewById(R.id.tvtext)).setTextColor(getResources().getColor(R.color.white));

                } else if (tempList.size() == 1 && !StringUtils.getMapKeystr(tempList).equals(getItemClickDate(position))) {//添加的不是同一个日期
                    tempList.put(getItemClickDate(position), arg1);
                    arg1.setTag("green");
//                    arg1.findViewById(R.id.tvtext).setBackgroundResource(R.drawable.circle_green);
//                    ((TextView) arg1.findViewById(R.id.tvtext)).setTextColor(getResources().getColor(R.color.white));
                    tempBg.setBackgroundResource(R.drawable.circle_green);
                    tempBg.setVisibility(View.VISIBLE);
                    ((Button) tempBg).setText(((TextView) arg1.findViewById(R.id.tvtext)).getText().toString());
                } else if (tempList.size() == 2) {//需要判断距离哪一个日期近，然后替换哪一个日期
                    String nearDate = DateUtils.compare_date(StringUtils.getMapKeystr(tempList).split(",")[0], StringUtils.getMapKeystr(tempList).split(",")[1], getItemClickDate(position));
                    View tempView;
                    if (StringUtils.getMapKeystr(tempList).split(",")[0].equals(nearDate)) {
//                        removeDate=StringUtils.getMapKeystr(tempList).split(",")[0];
                        tempView = tempList.remove(StringUtils.getMapKeystr(tempList).split(",")[0]);
                    } else {
//                        removeDate=StringUtils.getMapKeystr(tempList).split(",")[1];
                        tempView = tempList.remove(StringUtils.getMapKeystr(tempList).split(",")[1]);
                    }
                    //把相近日期背景色还原，新选择日期背景色加重显示
                    if (tempView.getTag().equals("red")) {
//                        arg1.findViewById(R.id.tvtext).setBackgroundResource(R.drawable.circle_red);
//                        ((TextView) arg1.findViewById(R.id.tvtext)).setTextColor(getResources().getColor(R.color.white));
                        tempBg.setVisibility(View.VISIBLE);
                        tempBg.setBackgroundResource(R.drawable.circle_red);
                        ((Button) tempBg).setText(calV.getDateByClickItem(position).split("\\.")[0]);
                        arg1.setTag("red");
                    } else if (tempView.getTag().equals("green")) {
                        tempBg.setVisibility(View.VISIBLE);
                        tempBg.setBackgroundResource(R.drawable.circle_green);
                        ((TextView) tempBg).setText(calV.getDateByClickItem(position).split("\\.")[0]);
                        arg1.setTag("green");
                    }
                    //还原被反选的日期
//                    (tempView).findViewById(R.id.tvtext).setBackgroundColor(getResources().getColor(R.color.white));
                    ((TextView) tempView.findViewById(R.id.tvtext)).setTextColor(getResources().getColor(R.color.black1));
                    ((TextView) tempView.findViewById(R.id.tvtext)).setText(((TextView) tempView.findViewById(R.id.tvtext)).getText().toString());
                    tempView.findViewById(R.id.calendar_selected_pic).setVisibility(View.GONE);
                    tempList.put(getItemClickDate(position), arg1);

                } else {
                    Toast.makeText(PhysiologicalPeriodActivity.this, "选择日期有误！！", Toast.LENGTH_LONG).show();
                }

            }
        });
        gridView.setLayoutParams(params);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/FZLTCXHJW.TTF");
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append(calV.getShowDay()).append("\t");
        view.setTypeface(typeFace);
        view.setText(textDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (object != null && action == Action.SET_INFOS) {
            CommonBean bean = (CommonBean) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                UserManager.getInstance().setFirstRecordInfo(false);//已经录入信息
                AppManager.getAppManager().finishAllActivity();
                readyGo(HomeActivity.class);
            }
        } else if (object != null && action == Action.UPDATA_USERINFO) {
            CommonBean bean_ = (CommonBean) object;
            if (bean_.getCode().equals(Constant.RESULT_SUCESS) && isFromMy) {
                mIntent.putExtra("cycle", StringUtils.getMapKeystr(tempList));
                mIntent.putExtra("cycleNum", cycle_num.getText().toString().trim());
                setResult(0x140, mIntent);
                this.finish();
            }
        } else {
            LogUtils.i("上传信息时出错了");
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }

    /**
     * 根据所点击item，获取日期信息
     *
     * @param position
     * @return
     */
    public String getItemClickDate(int position) {
        int startPosition = calV.getStartPositon();
        int endPosition = calV.getEndPosition();
        String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
        if (isFromHome) {
            for (int i = 0; i < templateDateList.size(); i++) {
                String[] tempArr = templateDateList.get(i).split("-");
                if (tempArr[0].equals(calV.getShowYear()) && tempArr[1].equals(calV.getShowMonth())) {
                    dayList.add(tempArr[2]);
                }
            }
            if (startPosition <= position + 7 && position <= endPosition - 7 && dayList.contains(scheduleDay)) {
                String scheduleYear = calV.getShowYear();
                String scheduleMonth = calV.getShowMonth();
//            Toast.makeText(PhysiologicalPeriodActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
//                scheduleMonth = scheduleMonth.length() == 1 ? "0" + scheduleMonth : scheduleMonth;
//                scheduleDay = scheduleDay.length() == 1 ? "0" + scheduleDay : scheduleDay;
                return scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;
            }
        } else if (isFromAfter) {
            Calendar calendar = Calendar.getInstance();
            String str2 = new SimpleDateFormat("yyyy-M-d").format(calendar.getTime());//现在日期
            calendar.add(Calendar.MONTH, -6);
            String str1 = new SimpleDateFormat("yyyy-M-d").format(calendar.getTime());//6月之前
            try {
                if (DateUtils.isBetween(str1, str2, calV.getShowYear() + "-" + calV.getShowMonth() + "-" + scheduleDay)) {
                    return calV.getShowYear() + "-" + calV.getShowMonth() + "-" + scheduleDay;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (startPosition <= position + 7 && position <= endPosition - 7) {

            // String scheduleLunarDay =
            // calV.getDateByClickItem(position).split("\\.")[1];
            // //这一天的阴历
            String scheduleYear = calV.getShowYear();
            String scheduleMonth = calV.getShowMonth();
//            Toast.makeText(PhysiologicalPeriodActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
//            scheduleMonth = scheduleMonth.length() == 1 ? "0" + scheduleMonth : scheduleMonth;
//            scheduleDay = scheduleDay.length() == 1 ? "0" + scheduleDay : scheduleDay;
            return scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;

        }
        return null;
    }

    /**
     * 根据所点击item，获取月份信息
     *
     * @param position
     * @return
     */
    public int getItemClickMonth(int position) {
        int currentMouth = Integer.parseInt(calV.getShowMonth());
        return currentMouth;
    }

    /**
     * 根据所点击item，获取年份信息
     *
     * @param position
     * @return
     */
    public int getItemClickYear(int position) {
        int currentYear = Integer.parseInt(calV.getShowYear());
        return currentYear;
    }

    @Override
    protected void onStop() {
        super.onStop();
        jumpMonth = 0;
        jumpYear = 0;
    }
}
