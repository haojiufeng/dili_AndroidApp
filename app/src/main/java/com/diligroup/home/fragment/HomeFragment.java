package com.diligroup.home.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseFragment;
import com.diligroup.base.Constant;
import com.diligroup.bean.BannerBean;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.bean.MyStickyHeadChangeListener;
import com.diligroup.bean.UpdateVersionBean;
import com.diligroup.dialog.MealChoicePopwindow;
import com.diligroup.dialog.MyLoadingDialog;
import com.diligroup.dialog.UpdateVersionDialog;
import com.diligroup.home.GetAllShop;
import com.diligroup.home.ServiceActivity;
import com.diligroup.home.adapter.HomeRighAdapter;
import com.diligroup.home.adapter.LeftAdapter;
import com.diligroup.my.activity.PhysiologicalPeriodActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.DeviceUtils;
import com.diligroup.utils.FileUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.service.VersionDownload;
import com.diligroup.view.DividerItemDecoration;
import com.diligroup.view.banner.BaseSliderView;
import com.diligroup.view.banner.DefaultSliderView;
import com.diligroup.view.banner.SliderLayout;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Request;

/**
 * Created by hjf on 2016/7/18.
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, RequestManager.ResultCallback, MyItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.slider)
    SliderLayout sliderLayout;
    @Bind(R.id.home_today)
    TextView homeToday;//今天日期
    @Bind(R.id.home_weekday)
    TextView homeWeekday;//今天是哪一天，周几
    @Bind(R.id.home_thisService_evaluation)
    RelativeLayout homeThisServiceEvaluation;//本次服务评价
    @Bind(R.id.home_currend_date)
    RelativeLayout home_currend_date;
    @Bind(R.id.home_right_recycleView)
    RecyclerView home_right_recycleView;
    @Bind(R.id.home_left_listView)
    RecyclerView home_left_listView;
    @Bind(R.id.select_store)
    LinearLayout select_store;//选择门店
    @Bind(R.id.home_store_address)
    TextView home_store_address;//哪一个门店，地址
    @Bind(R.id.meal_choice)
    RelativeLayout meal_choice;//餐别选择
    @Bind(R.id.breakfase_icon)
    ImageView mealIcon;//餐别图标
    @Bind(R.id.breakfase_text)
    TextView mealText;//餐别文本
    @Bind(R.id.topView)
    LinearLayout topView;
    @Bind(R.id.home_head_pull)
    PtrClassicFrameLayout home_head_pull;
    @Bind(R.id.refresh_root_view)
    LinearLayout fresh_root;
    @Bind(R.id.home_error_info)
    TextView home_error_info;//加载异常提示
    @Bind(R.id.home_error_layout)
    LinearLayout home_error_layout;
    private LinearLayout.LayoutParams thisService;//本次服务评价文本布局
    private Intent mIntent;
    private String currentDay;//今天日期字符串
    private ArrayList<String> templateDateList = new ArrayList<>();//门店供应的菜品日期集合
    private int currentClickItem;
    private LeftAdapter adapter;
    HomeRighAdapter righAdapter;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean> dishesTypeList;//左侧成品分类列表
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> rightDishesList = new ArrayList<>();//左侧成品分类列表
    private DividerItemDecoration dividerItemDecoration;

    String currentMealTypeCode;//当前餐别
    private int hour;
    private LinearLayoutManager layoutManager_1;
    int currentLeftPosition;

    String currentHeadCode;//右侧滚动时候，当前sticky的headCode值
    //    private String address;//当前地址
    private String currenStoreId;//门店id
    //    private boolean isFirst = true;//是否是第一次加载数据
    private BannerBean bean;//banner数据bean
//    private ViewSwitcherHelper helper;//统一网络加载页面

    private UpdateProgressBroadcastReceiver receiver;
    private UpdateVersionDialog update_dialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x10) {
                int progress = (Integer) msg.obj;
                if (progress == 95) {
                    update_dialog.setSureText("已完成");
                    update_dialog.dismiss();
                    return;
                }
                update_dialog.setCurrentProgress(progress);
            }
        }
    };
    private MyLoadingDialog dialog;

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_home;
    }

    @Override
    public void setViews() {
        Api.updateVersion(this);

        dialog = new MyLoadingDialog(getActivity());
        dialog.show();
        currentDay = DateUtils.getCurrentDate();
        homeToday.setText(currentDay);
        homeWeekday.setText("(今天 " + DateUtils.getWeekDay() + ")");
        home_store_address.setText(UserManager.getInstance().getStoreAddress() + UserManager.getInstance().getStoreName());

        selectMeal();
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.VERTICAL_LIST);
//        dividerItemDecoration.setHeight(CommonUtils.px2dip(getActivity(), 0.1));
//        leftListView.addItemDecoration(dividerItemDecoration);//垂直列表的分割线
//        leftListView.setHasFixedSize(true);//保持固定大小，提高性能
        CommonUtils.initRerecyelerView(getActivity(), home_left_listView, 1/10);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_left_listView.setLayoutManager(layoutManager);

        CommonUtils.initRerecyelerView(getActivity(), home_right_recycleView, 1/10);
        initDate();

//        CustomerPtrHeader mPtrClassicHeader = new CustomerPtrHeader(getContext(), new CustomerPtrHeader.ListViewLoadListener() {
//            @Override
//            public void refreshBegin(PtrFrameLayout frame) {
//                frame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Api.getBanner(UserManager.getInstance().getStoreId(), "1", HomeFragment.this);
//                    }
//                }, 1800);
//
//            }
//
//            @Override
//            public void refreshComplete(PtrFrameLayout frame) {
//                home_head_pull.refreshComplete();
//            }
//        });
//        home_head_pull.setHeaderView(mPtrClassicHeader);
//        home_head_pull.addPtrUIHandler(mPtrClassicHeader);
//        home_head_pull.setPullToRefresh(true);
        home_head_pull.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                frame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        home_head_pull.refreshComplete();
//                    }
//                }, 1800);
                Api.getBanner(UserManager.getInstance().getStoreId(), "1", HomeFragment.this);
            }
        });
    }

    /**
     * 根据日期选择餐别
     */
    private void selectMeal() {
        hour = DateUtils.getCurrentTime();
        if (hour >= 0 && hour <= 9) {
            select_meal(0);
            currentMealTypeCode = "20001";
        } else if (hour > 9 && hour < 16) {
            select_meal(1);
            currentMealTypeCode = "20002";
        } else {
            select_meal(2);
            currentMealTypeCode = "20003";
        }
    }

    @Override
    public void setListeners() {

        home_currend_date.setOnClickListener(this);

        homeThisServiceEvaluation.setOnClickListener(this);
        select_store.setOnClickListener(this);
        meal_choice.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    home_head_pull.setEnabled(true);
                    home_head_pull.setPullToRefresh(true);
                } else {
                    home_head_pull.setEnabled(false);
                    home_head_pull.setPullToRefresh(false);
                }
            }

        });
        home_right_recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) home_right_recycleView.getLayoutManager()).findFirstVisibleItemPosition();
                if (dishesTypeList == null || dishesTypeList.size() == 0) {
                    return;
                }
                String tempHeadCode = getRightList(dishesTypeList).get(position).getHeaderCode();
                if (!tempHeadCode.equals(currentHeadCode)) {
                    for (int i = 0; i < dishesTypeList.size(); i++) {
                        if (dishesTypeList.get(i).getDishesTypeCode().equals(tempHeadCode)) {
                            home_left_listView.scrollToPosition(i);
                            adapter.selectPosion(i);
                            currentHeadCode = tempHeadCode;
                            currentLeftPosition = i;
                            break;
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        sliderLayout.removeAllSliders();//释放资源
        super.onDestroyView();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initDate() {
        Api.getBanner(UserManager.getInstance().getStoreId(), "1", this);
    }

    /**
     * 初始化轮播图
     */
    private void initBanner(List<BannerBean.ImageUrlBean> imgUrls) {
        sliderLayout.removeAllSliders();
        for (int i = 0; i < imgUrls.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(
                    getActivity(), false);
            sliderView
                    .image(utf8Togb2312(imgUrls.get(i).getImageUrl()))
//                    .image("http://192.168.101.98:8080/test/02.jpg")
                    .error(R.mipmap.home_banner_default)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", "" + i);
            sliderView.setOnSliderClickListener(new MyOnSliderClickListener(i));
            if (sliderLayout != null) {
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
                sliderLayout.addSlider(sliderView);
            }
        }
    }

    public String utf8Togb2312(String str) {
        String data = "";
        try {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c + "".getBytes().length > 1 && c != ':' && c != '/') {
                    data = data + java.net.URLEncoder.encode(c + "", "utf-8");
                } else {
                    data = data + c;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_currend_date:
                mIntent = new Intent(getActivity(), PhysiologicalPeriodActivity.class);
                mIntent.putExtra("isFromHome", true);
                mIntent.putExtra("currentDate", homeToday.getText().toString().trim());
                mIntent.putExtra("dateList", DateUtils.dateList(templateDateList));
                startActivityForResult(mIntent, 10);//传递当前日期
                break;
            case R.id.home_thisService_evaluation:
                mIntent = new Intent(getActivity(), ServiceActivity.class);
                mIntent.putExtra("mealType", currentMealTypeCode);
                mIntent.putExtra("date", DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())));
                startActivity(mIntent);
                break;
            case R.id.select_store:
                mIntent = new Intent(getActivity(), GetAllShop.class);
                startActivityForResult(mIntent, 0x30);
                break;
            case R.id.meal_choice:
                final MealChoicePopwindow popwindow = new MealChoicePopwindow(getActivity(), meal_choice, meal_choice.getWidth(), new MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        select_meal(position);
                    }
                });
                break;
        }
    }

    /**
     * 指定选中哪一个餐别
     *
     * @param position
     */
    private void select_meal(int position) {
        switch (position) {
            case 0:
                mealIcon.setImageResource(R.mipmap.breakfase_icon_normal);
                mealText.setText("早餐");
                currentMealTypeCode = "20001";
                dialog.show();
                Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode, "", "1", this);
                break;
            case 1:
                mealIcon.setImageResource(R.mipmap.lunch_icon_normal);
                mealText.setText("午餐");
                currentMealTypeCode = "20002";
                dialog.show();
                Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode, "", "1", this);
                break;
            case 2:
                mealIcon.setImageResource(R.mipmap.dinner_icon_normal);
                mealText.setText("晚餐");
                currentMealTypeCode = "20003";
                dialog.show();
                Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode, "", "1", this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 20:
                String currenStr = data.getStringExtra("current");
                int currentYear = Integer.parseInt(currenStr.split("-")[0]);
                int currentMonth = Integer.parseInt(currenStr.split("-")[1]);
                int currentDay = Integer.parseInt(currenStr.split("-")[2]);
                homeToday.setText(currentYear + "年" + currentMonth + "月" + currentDay + "日");
                if (DateUtils.isToday(currentYear, currentMonth, currentDay)) {
                   dialog.show();
                    Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), DateUtils.dateFormatChanged_2(currenStr), currentMealTypeCode, "", "1", this);
                    homeWeekday.setText("(今天 " + DateUtils.getWeekdayOfMonth(currentYear, currentMonth, currentDay) + ")");
                } else {
                    homeWeekday.setText("(" + DateUtils.getWeekdayOfMonth(currentYear, currentMonth, currentDay) + ")");
                   dialog.show();
                    Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), DateUtils.dateFormatChanged_2(currenStr), currentMealTypeCode, "", "1", this);
                }
                break;
            case 0x111://GetAllShop界面返回
                currenStoreId = UserManager.getInstance().getStoreId();
                //    设置标题门店
                home_store_address.setText(UserManager.getInstance().getStoreAddress() + UserManager.getInstance().getStoreName());
               dialog.show();
                Api.getBanner(UserManager.getInstance().getStoreId(), "1", HomeFragment.this);
                Api.homeStoreSupplyList(currenStoreId, DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode, "", "1", this);
                break;
        }
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        if (!NetUtils.isMobileConnected(getActivity())) {
            ToastUtil.showLong(getActivity(), "断网了");
            if(fresh_root!=null && home_error_layout!=null && home_error_info!=null){
            fresh_root.setVisibility(View.GONE);
            home_error_layout.setVisibility(View.VISIBLE);
            home_error_info.setText("数据加载失败");
            }
        }
        if (home_head_pull.isRefreshing() && home_head_pull!=null) {
            home_head_pull.refreshComplete();
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {

        if (object != null && action == Action.BANNER) {
            bean = (BannerBean) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                home_head_pull.refreshComplete();
                initBanner(bean.getImageUrl());
            } else {
                LogUtils.i("轮播图接口调失败");
            }
        } else if (null != object && action == Action.GET_HOME_LIST) {
            HomeStoreSupplyList listBean = (HomeStoreSupplyList) object;
            dialog.dismiss();
            if (listBean.getCode().equals(Constant.RESULT_SUCESS)) {
                templateDateList = listBean.getJson().getTmplDateList();
                dishesTypeList = listBean.getJson().getDishesSupplyList();
                if (dishesTypeList == null || (dishesTypeList != null && dishesTypeList.size() <= 0)) {
                    home_right_recycleView.setVisibility(View.INVISIBLE);
                    fresh_root.setVisibility(View.GONE);
                    home_error_layout.setVisibility(View.VISIBLE);
                    switch (Integer.parseInt(currentMealTypeCode)){
                        case 20001:
                            home_error_info.setText("抱歉，门店今天无早餐服务！");
                            break;
                        case 20002:
                            home_error_info.setText("抱歉，门店今天无午餐服务！");
                            break;
                        case 20003:
                            home_error_info.setText("抱歉，门店今天无晚餐服务！");
                            break;
                        default:
                            home_error_info.setText("抱歉，门店今天无对应的餐别服务！");
                            break;
                    }
                    return;
                } else {
                    fresh_root.setVisibility(View.VISIBLE);
                    home_error_layout.setVisibility(View.GONE);
                    home_right_recycleView.setVisibility(View.VISIBLE);
                    currentHeadCode = dishesTypeList.get(0).getDishesSupplyDtlList().get(0).getHeaderCode();
                }
                relayoutAll();//是否需要展示列表中评价文字和服务评价文案
                if (adapter == null) {
                    adapter = new LeftAdapter(getActivity(), dishesTypeList, this);
                    home_left_listView.setAdapter(adapter);
                } else {
                    adapter.setDate(dishesTypeList);
                }
                if (righAdapter == null) {
                    righAdapter = new HomeRighAdapter(getActivity(), dishesTypeList, DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode, new MyStickyHeadChangeListener() {

                        @Override
                        public void headChange(int position, String headId) {
                            if (TextUtils.isEmpty(currentHeadCode)) {
                                currentHeadCode = dishesTypeList.get(0).getDishesSupplyDtlList().get(0).getHeaderCode();
                            }
                        }
                    });
                    home_right_recycleView.setAdapter(righAdapter);
                    final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(righAdapter);
                    layoutManager_1 = new LinearLayoutManager(getActivity());
                    home_right_recycleView.setLayoutManager(layoutManager_1);
                    home_right_recycleView.addItemDecoration(headersDecor);

                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            headersDecor.invalidateHeaders();//解决开始头部文本没有渲染的问题
                        }
                    });
                } else {
                    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> temprightDishesList = getRightList(dishesTypeList);
                    righAdapter.setDate(temprightDishesList,  DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode);
                }
            }else{
                home_right_recycleView.setVisibility(View.INVISIBLE);
                fresh_root.setVisibility(View.GONE);
                home_error_layout.setVisibility(View.VISIBLE);
                home_error_info.setText(listBean.getMessage());
            }
        } else if (object != null && action == Action.UPDTE_VERSION) {
            final UpdateVersionBean versionBean = (UpdateVersionBean) object;
            if (versionBean.getCode().equals(Constant.RESULT_SUCESS)) {
                if (DeviceUtils.getVersionCode(getActivity()) < Integer.parseInt(versionBean.getMap().getVersionCode())) {
                    update_dialog = new UpdateVersionDialog(getActivity(), versionBean.getMap().getDescribe(), new UpdateVersionDialog.OnSureClickedLisener() {
                        @Override
                        public void onSureClick() {
                            receiver = new UpdateProgressBroadcastReceiver();
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(Constant.DOWNLOADMANAGEACTION);
                            getActivity().registerReceiver(receiver, filter);
                            update_dialog.setSureText("下载中……");
                            //                        ToastUtil.showLong(HomeActivity.this, "正在检查更新...");
                            // 开启更新服务UpdateService
                            // 这里为了把update更好模块化，可以传一些updateService依赖的值
                            // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
                            Intent intent = new Intent();
                            intent.putExtra("downloadUrl", versionBean.getMap().getDownloadPath());
//                            intent.putExtra("downloadUrl", "http://192.168.101.98:8080/test/app-debug.apk");
                            intent.putExtra("name", FileUtils.getFileName(versionBean.getMap().getDownloadPath()));
//                            intent.putExtra("name", FileUtils.getFileName("http://192.168.101.98:8080/test/app-debug.apk"));
                            intent.setClass(getActivity(), VersionDownload.class);
                            getActivity().startService(intent);
                        }
                    }, !versionBean.getMap().isForceUpdate());
                    update_dialog.show();
                } else {
                    if (update_dialog != null && update_dialog.isShowing()) {
//                        update_dialog.dismiss();
                        update_dialog.cancel();
                    }
                }
            }
        } else {
//            helper.showError(request, action, this);
//            helper.showNotify("错了错了");
        }
    }

    /**
     * 是否需要展示列表中评价文字和服务评价文案
     */
    public void relayoutAll() {
        try {
            if (DateUtils.compareDate(homeToday.getText().toString(), currentDay) < 0) {//早于现在时间
                homeThisServiceEvaluation.setVisibility(View.VISIBLE);
                listEvaluteInvisible(true);
            } else {
                if (homeWeekday.getText().toString().contains("今天") && mealText.getText().toString().equals("早餐") && hour > 9) {
                    homeThisServiceEvaluation.setVisibility(View.VISIBLE);
                    listEvaluteInvisible(true);
                } else if (homeWeekday.getText().toString().contains("今天") && mealText.getText().toString().equals("午餐") && hour > 16) {
                    homeThisServiceEvaluation.setVisibility(View.VISIBLE);
                    listEvaluteInvisible(true);
                } else {
                    listEvaluteInvisible(false);
                    homeThisServiceEvaluation.setVisibility(View.GONE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列表评价文字是否展示
     */
    private void listEvaluteInvisible(boolean isShow) {
        if (dishesTypeList != null) {
            for (int i = 0; i < dishesTypeList.size(); i++) {
                if (dishesTypeList.get(i).getDishesSupplyDtlList() != null && dishesTypeList.get(i).getDishesSupplyDtlList().size() > 0) {
                    for (int j = 0; j < dishesTypeList.get(i).getDishesSupplyDtlList().size(); j++) {
                        if (isShow) {
                            dishesTypeList.get(i).getDishesSupplyDtlList().get(j).setShow(true);
                        } else {
                            dishesTypeList.get(i).getDishesSupplyDtlList().get(j).setShow(false);
                        }
                    }
                }
            }
        }
        if (righAdapter != null) {
            righAdapter.setDate(getRightList(dishesTypeList), DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(homeToday.getText().toString())), currentMealTypeCode);
        }
    }

    @Override
    public void onItemClick(View view, int position) {//左侧item点击事件
        int count = 0;
        if (dishesTypeList != null) {
            for (int i = 0; i < dishesTypeList.size(); i++) {
                if (position == 0) {
                    home_right_recycleView.scrollToPosition(0);
                    return;
                } else if (i < position && dishesTypeList.get(i).getDishesSupplyDtlList() == null) {
                    continue;
                } else if (i < position && dishesTypeList.get(i).getDishesSupplyDtlList() != null) {
                    count = count + dishesTypeList.get(i).getDishesSupplyDtlList().size();
                    currentLeftPosition = count;
                } else {
                    break;
                }
            }
        }
        home_right_recycleView.scrollToPosition(position);
        ((LinearLayoutManager) home_right_recycleView.getLayoutManager()).scrollToPositionWithOffset(count, 0);
    }

    /**
     * 获取右侧菜品列表
     *
     * @param mList
     * @return
     */
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> getRightList(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean> mList) {
        rightDishesList.clear();
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getDishesSupplyDtlList() != null) {
                    for (int j = 0; j < mList.get(i).getDishesSupplyDtlList().size(); j++) {
                        mList.get(i).getDishesSupplyDtlList().get(j).setHeaderCode(mList.get(i).getDishesTypeCode());
                        mList.get(i).getDishesSupplyDtlList().get(j).setHeaderName(mList.get(i).getDishesTypeName());
                    }
                    rightDishesList.addAll(mList.get(i).getDishesSupplyDtlList());
                }
            }
        }
        return rightDishesList;
    }

    @Override
    public void onRefresh() {
    }

    class MyOnSliderClickListener implements BaseSliderView.OnSliderClickListener {
        int index;

        public MyOnSliderClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onSliderClick(BaseSliderView slider) {
            ToastUtil.showLong(getActivity(), bean.getImageUrl().get(index).getLinkUrl());
        }
    }

    /**
     * 自定义广播接收者，时时接受下载进度值
     */
    class UpdateProgressBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progress", 0);
//            LogUtils.e("接受到的下载的进度===", progress + "");
            Message msg = mHandler.obtainMessage();
            msg.what = 0x10;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
       if(!hidden){
           home_store_address.setText(UserManager.getInstance().getStoreAddress() + UserManager.getInstance().getStoreName());
           dialog.show();
           initDate();
           selectMeal();
       }
    }
}
