package com.diligroup.after.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.after.adapter.AfterFragmentAdapter;
import com.diligroup.base.BaseFragment;
import com.diligroup.base.Constant;
import com.diligroup.bean.AfterNoFoodBean;
import com.diligroup.bean.GetDietRecordBean;
import com.diligroup.my.activity.PhysiologicalPeriodActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.view.stickyListView.StickyListHeadersListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import okhttp3.Request;

/**
 * 餐后评价
 * Created by Kevin on 2016/7/4.
 */
public class AfterFragment extends BaseFragment implements RequestManager.ResultCallback, View.OnClickListener {
    @Bind(R.id.home_preDay)
    TextView homePreDay;//前一天
    @Bind(R.id.home_today)
    TextView afterToday;//当天日期，页面上显示 的
    @Bind(R.id.home_weekday)
    TextView homeWeekday;//今天是哪一天，周几
    @Bind(R.id.home_nextDay)
    TextView homeNextDay;//后一天
    String currentDay;//今天  只代表今天日期 字符串

    List<GetDietRecordBean.MealBean> beanList;
    @Bind(R.id.after_fg_recyclerView)
    StickyListHeadersListView after_fg_listView;//展示3餐listView
    @Bind(R.id.ll_no_record)
    LinearLayout ll_NoRecord;//无3餐布局展示
    @Bind(R.id.after_nutrientContent)
    TextView after_nutrientContent;//营养素模板内容
    @Bind(R.id.after_structureContent)
    TextView after_structureContent;//饮食结构模板内容
    @Bind(R.id.after_canlendar)
    LinearLayout after_calendar;
    //    @Bind(R.id.iv_share)
//    ImageView ivShare;
    @Bind(R.id.title_root)
    View rootView;
    @Bind(R.id.bt_over_see)
    Button seeEvaluate;//去看评价
    @Bind(R.id.ll_lunch)
    LinearLayout llLunch;//午餐
    @Bind(R.id.ll_breakfast)
    LinearLayout llBreakfest;//早餐
    @Bind(R.id.ll_dinner)
    LinearLayout llDinner;//晚餐
    @Bind(R.id.after_remind)
    LinearLayout after_remind;//温馨提示
    AfterFragmentAdapter afterAdapter;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.bind_remind)
    View bind_remind;//跟温馨提示绑定的背景区域
    @Bind(R.id.after_rootview)
    FrameLayout after_rootview;
    private AfterFragmentAdapter adapter_1;
    int flag = 0;//跳转html5 浏览和生成评价区分标志
    private ViewSwitcherHelper afterHelper;

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_after;
    }

    @Override
    public void setViews() {
        tv_title.setText("餐后评价");
        afterHelper = new ViewSwitcherHelper(getActivity(), after_fg_listView);
        currentDay = DateUtils.getCurrentDate();
        afterToday.setText(currentDay);
        homeWeekday.setText("(今天 " + DateUtils.getWeekDay() + ")");
        homeNextDay.setTextColor(getResources().getColor(R.color.graycolor));
        homeNextDay.setClickable(false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        afterHelper.showLoading();
        Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString().trim())), this);
    }

    @Override
    public void setListeners() {
        after_calendar.setOnClickListener(this);
        homePreDay.setOnClickListener(this);

        seeEvaluate.setOnClickListener(this);
        llBreakfest.setOnClickListener(this);
        llLunch.setOnClickListener(this);
        llDinner.setOnClickListener(this);
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
//        ll_NoRecord.setVisibility(View.VISIBLE);
//        seeEvaluate.setVisibility(View.GONE);

        if (!NetUtils.isMobileConnected(getActivity())) {
            afterHelper.showError(request, action, this);
        } else {
            afterHelper.showNotify("加载数据失败，请稍后重试");
        }
        seeEvaluate.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        afterHelper.showContent();
        if (action == Action.GET_DIET_RECORD && object != null) {
            GetDietRecordBean dietRecordBean = (GetDietRecordBean) object;
            if (dietRecordBean.getCode().equals(Constant.RESULT_SUCESS)) {

                //3餐都没有
                if (dietRecordBean.getMorn().size() + dietRecordBean.getEven().size() + dietRecordBean.getAfternoon().size() == 0) {
                    Api.getAfterFgNoFood(this);
                    return;
                } else if (dietRecordBean.getMorn().size() == 0 || dietRecordBean.getEven().size() == 0 || dietRecordBean.getAfternoon().size() == 0) {
                    after_remind.setVisibility(View.VISIBLE);
                    bind_remind.setVisibility(View.GONE);
                    ll_NoRecord.setVisibility(View.GONE);
                    after_fg_listView.setVisibility(View.VISIBLE);
                    seeEvaluate.setVisibility(View.VISIBLE);
                } else if (dietRecordBean.getMorn().size() != 0 && dietRecordBean.getEven().size() != 0 && dietRecordBean.getAfternoon().size() != 0) {
                    after_remind.setVisibility(View.GONE);
                    ll_NoRecord.setVisibility(View.GONE);
                    bind_remind.setVisibility(View.GONE);
                    after_fg_listView.setVisibility(View.VISIBLE);
                    seeEvaluate.setVisibility(View.VISIBLE);
                }
                if (adapter_1 == null) {
                    adapter_1 = new AfterFragmentAdapter(getActivity(), dietRecordBean);
                    after_fg_listView.setAdapter(adapter_1);
                } else {
                    adapter_1.setDate(dietRecordBean);
                }

            } else {
//                afterHelper.showNotify("aaaaaaaa");
            }
        } else if (action == Action.NOFOOD_INVALUATE && object != null) {
            AfterNoFoodBean noFoodBean = (AfterNoFoodBean) object;
            if (noFoodBean.getCode().equals(Constant.RESULT_SUCESS)) {
                after_fg_listView.setVisibility(View.GONE);
                ll_NoRecord.setVisibility(View.VISIBLE);
                seeEvaluate.setVisibility(View.GONE);

                after_remind.setVisibility(View.VISIBLE);
                bind_remind.setVisibility(View.VISIBLE);
                if (noFoodBean.getDataNomeals() != null) {
                    after_nutrientContent.setText(noFoodBean.getDataNomeals().getNutrientContent());
                    after_structureContent.setText(noFoodBean.getDataNomeals().getStructureContent());
                }
            } else {
//                afterHelper.showError(request,action,this);
            }
        }

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.after_canlendar:
                Intent mIntent = new Intent(getActivity(), PhysiologicalPeriodActivity.class);
                mIntent.putExtra("isFromAfter", true);
                mIntent.putExtra("currentDate", currentDay);
                startActivityForResult(mIntent, 100);
                break;
            case R.id.home_nextDay:
                String tempDay = DateUtils.getDate(afterToday.getText().toString().trim(), 1);
                afterToday.setText(tempDay.split(" ")[0]);
                if (tempDay.split(" ")[0].equals(currentDay)) {
                    homeWeekday.setText("（今天" + tempDay.split(" ")[1] + ")");
                    homeNextDay.setTextColor(getResources().getColor(R.color.graycolor));
                    homeNextDay.setClickable(false);
                } else {
                    homeWeekday.setText("（" + tempDay.split(" ")[1] + "）");
                    homeNextDay.setTextColor(getResources().getColor(R.color.black1));
                    homeNextDay.setClickable(true);
                }
                afterHelper.showLoading();
                Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString())), this);
                break;
            case R.id.home_preDay:
                String temp = DateUtils.getDate(afterToday.getText().toString().trim(), -1);
                afterToday.setText(temp.split(" ")[0]);
                if (temp.split(" ")[0].equals(currentDay)) {
                    homeWeekday.setText("（今天" + temp.split(" ")[1] + "）");
                    homeNextDay.setTextColor(getResources().getColor(R.color.graycolor));
                    homeNextDay.setClickable(false);
                } else {
                    homeWeekday.setText("（" + temp.split(" ")[1] + "）");
                    homeNextDay.setClickable(true);
                    homeNextDay.setOnClickListener(this);
                    homeNextDay.setTextColor(getResources().getColor(R.color.black1));
                }
                afterHelper.showLoading();
                Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString().trim())), this);
                break;
            case R.id.bt_over_see://查看评价
                intent = new Intent(getActivity(), AfterEvaluate.class);
                if (flag == 1) {
                    intent.putExtra("flag", flag);
                }
                intent.putExtra("time", DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString().trim())));
                startActivity(intent);
                break;
            case R.id.ll_breakfast:
                intent = new Intent(getActivity(), AddLunchActivity.class);
                intent.putExtra("mealType", "20001");
                intent.putExtra("currentDay", DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString())));
                startActivityForResult(intent, 1);
                break;
            case R.id.ll_lunch:
                intent = new Intent(getActivity(), AddLunchActivity.class);
                intent.putExtra("mealType", "20002");
                intent.putExtra("currentDay", DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString())));
                startActivityForResult(intent, 2);
                break;
            case R.id.ll_dinner:
                intent = new Intent(getActivity(), AddLunchActivity.class);
                intent.putExtra("currentDay", DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString())));
                intent.putExtra("mealType", "20003");
                startActivityForResult(intent, 3);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == 10) {
//            Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(currentDay)), this);
//        } else  不知道为什么startActivityForResult这方法就是没有办法回调
        if (resultCode == 20) {//点击日历返回
            String currenStr = data.getStringExtra("current");
            int currentYear = Integer.parseInt(currenStr.split("-")[0]);
            int currentMonth = Integer.parseInt(currenStr.split("-")[1]);
            int currentDay = Integer.parseInt(currenStr.split("-")[2]);
            afterToday.setText(currentYear + "年" + currentMonth + "月" + currentDay + "日");
            if (DateUtils.isToday(currentYear, currentMonth, currentDay)) {
                homeWeekday.setText("(今天 " + DateUtils.getWeekdayOfMonth(currentYear, currentMonth, currentDay) + ")");
                homeNextDay.setTextColor(getResources().getColor(R.color.graycolor));
                homeNextDay.setClickable(false);
            } else {
                homeNextDay.setTextColor(getResources().getColor(R.color.black1));
                homeNextDay.setClickable(true);
                homeWeekday.setText("(" + DateUtils.getWeekdayOfMonth(currentYear, currentMonth, currentDay) + ")");
            }
            afterHelper.showLoading();
            Api.getDietRecord(DateUtils.dateFormatChanged_2(currenStr), this);
        }
    }

    @Subscribe
    public void onEvent(Integer event) {
        flag = 1;
        if (event == 10) {
            afterHelper.showLoading();
            Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString())), this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            afterHelper.showLoading();
            Api.getDietRecord(DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(afterToday.getText().toString().trim())), this);
        }
    }
}
