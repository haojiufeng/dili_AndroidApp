package com.diligroup.after.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.after.adapter.RighSearchAdapter;
import com.diligroup.after.adapter.StoreSupplyRighAdapter;
import com.diligroup.after.adapter.StoreSuppyLeftAdapter;
import com.diligroup.base.BaseFragment;
import com.diligroup.base.Constant;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.ButtonClickListener;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.bean.StoreSupplySearchBean;
import com.diligroup.home.adapter.LeftAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.RecordSQLiteOpenHelper;
import com.diligroup.utils.SpeechUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.view.stickyListView.StickyListHeadersListView;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by hjf on 2016/7/13.
 */
public class StoreSupplyFragment extends BaseFragment implements View.OnClickListener, StoreSuppyLeftAdapter.OnGetView, AdapterView.OnItemClickListener, RequestManager.ResultCallback, MyItemClickListener, ButtonClickListener {
    @Bind(R.id.left_listView)
    RecyclerView leftListView;
    @Bind(R.id.right_recyclerView)
    StickyListHeadersListView rightRecyclerView;
    @Bind(R.id.input_search_dishes)
    EditText input_search_dishes;//
    @Bind(R.id.complete_add)
    LinearLayout complete_add;
    @Bind(R.id.store_supply_delete)
    ImageButton store_supply_delete;
    @Bind(R.id.speech_input)
    ImageView speech_input;//语音输入
    @Bind(R.id.storesupply_line)
    View storesupply_line;//切换搜索时候的间隔线
    @Bind(R.id.store_supply_normallayout)
    LinearLayout store_supply_normallayout;//默认列表布局
    @Bind(R.id.store_supply_searchlayout)
    LinearLayout store_supply_searchlayout;//搜索布局
    @Bind(R.id.near_search)
    TextView near_searchnear_search;//搜索文字布局
    //    @Bind(R.id.store_edit_parent)
//    LinearLayout store_edit_parent;//编辑框父布局
    @Bind(R.id.storesupply_search_list)
    RecyclerView storesupply_search_list;//门店供应搜索列表
    private int currentClickItem;
    private LeftAdapter adapter;
    RighSearchAdapter searchAdapter;//搜索适配器
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean> dishesTypeList;//左侧成品分类列表
    private StoreSupplyRighAdapter rightAdapter;
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> rightDishesList;//左侧成品分类列表

    RecordSQLiteOpenHelper openHelper;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> nearSearchList;//最近搜索列表
    private RecyclerView.LayoutManager searchLayyoutManager;
    private String whichDay;//添加的是哪一天的菜品
    private String mealType;
    private ViewSwitcherHelper helper;
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> searchResultList;//搜索出来的结果list

    // 语音听写UI
    private RecognizerDialog mIatDialog;
    private SpeechUtils speechUtils;

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_storesupply;
    }

    @Override
    public void setViews() {
        CommonUtils.initRerecyelerView(getActivity(), leftListView, 1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        leftListView.setLayoutManager(layoutManager);

        openHelper = new RecordSQLiteOpenHelper(getActivity());
        CommonUtils.initRerecyelerView(getActivity(), storesupply_search_list, 1);
        searchLayyoutManager = new LinearLayoutManager(getActivity());
        storesupply_search_list.setLayoutManager(searchLayyoutManager);

        helper = new ViewSwitcherHelper(getActivity(), store_supply_normallayout);
        helper.showLoading();
        Api.homeStoreSupplyList(UserManager.getInstance().getStoreId(), whichDay, mealType, "", "1", this);
        speechUtils = new SpeechUtils(getActivity());
        speechUtils.setParam();
    }

    @Override
    public void setListeners() {
        store_supply_delete.setOnClickListener(this);
        speech_input.setOnClickListener(this);
        complete_add.setOnClickListener(this);
        rightRecyclerView.setOnItemClickListener(this);
        input_search_dishes.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获取焦点然后展示搜索最近的菜品
                if (hasFocus) {
                    store_supply_normallayout.setVisibility(View.GONE);
                    store_supply_searchlayout.setVisibility(View.VISIBLE);
                    storesupply_line.setVisibility(View.GONE);
                    //展示最近搜索列表
                    nearSearchList = openHelper.getALllFoods(RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME);
                    if (nearSearchList != null && nearSearchList.size() > 0) {
                        fillSearchDate(nearSearchList, true);
                    }
                }
            }
        });
        input_search_dishes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //实时根据文字改变，搜索菜品库中菜品
                    near_searchnear_search.setVisibility(View.VISIBLE);
                    store_supply_normallayout.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        Api.storeSupplySearch(UserManager.getInstance().getStoreId(), whichDay, s.toString(), mealType, StoreSupplyFragment.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    nearSearchList = openHelper.getALllFoods(RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME);
                    if (nearSearchList != null && nearSearchList.size() > 0)
                        fillSearchDate(nearSearchList, true);
//                        searchLayyoutManager.getChildAt(nearSearchList.size() - 1).setVisibility(View.VISIBLE);
                }
            }
        });
        rightRecyclerView.setOnStickyHeaderChangedListener(new StickyListHeadersListView.OnStickyHeaderChangedListener() {
            @Override
            public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
                for (int i = 0; i < dishesTypeList.size(); i++) {
                    if (Long.parseLong(dishesTypeList.get(i).getDishesTypeCode()) == headerId) {
                        leftListView.scrollToPosition(i);
                        adapter.selectPosion(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public Fragment newInstance(String mealType, String whichDay) {
        Bundle args = new Bundle();
        args.putString("mealType", mealType);
        args.putString("whichDay", whichDay);
        StoreSupplyFragment fragment = new StoreSupplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            mealType = getArguments().getString("mealType");
            whichDay = DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(getArguments().getString("whichDay")));
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        // 退出时释放连接
        speechUtils.speechDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_search_dishes:
                input_search_dishes.setFocusable(true);
                break;
            case R.id.complete_add:
                List<AddFoodCompleteBean> addMealList = ((AddLunchActivity) getActivity()).getAddMealList();
                for(int i=addMealList.size()-1;i>=0;i--){
                    if((TextUtils.isEmpty(addMealList.get(i).getWeight()) && addMealList.get(i).getWayType().equals("1"))|| (!TextUtils.isEmpty(addMealList.get(i).getWeight()) && addMealList.get(i).getWeight().equals("0.0"))){
                        ToastUtil.showLong(getActivity(), "自定义菜品中，有菜品没有输入重量哟");
                        return;
                    }
                }
                if (addMealList.size() != 0) {
                    String json = new Gson().toJson(addMealList);
                    Api.addFoodComplete(whichDay, UserManager.getInstance().getUserId(), mealType, json, this);
                } else {
                    ToastUtil.showLong(getActivity(), "亲：你还没有选择要添加餐别呢");
                    return;
                }
                if (searchAdapter != null) {
                    //遍历搜索结果列表，存入数据库,生成浏览历史记录
                    for (int i = 0; i < searchAdapter.getDate().size(); i++) {
                        if (searchAdapter.getDate().get(i).getFoodNums() != 0) {
                            HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean foodBean = openHelper.selectFood(searchAdapter.getDate().get(i).getDishesName(), RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME);
                            if (foodBean != null) {
                                openHelper.updateFood(searchAdapter.getDate().get(i), RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME, searchAdapter.isLocalSQL());
                            } else {
                                openHelper.addFood(searchAdapter.getDate().get(i), RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME, searchAdapter.isLocalSQL());
                            }
                        }
                    }
                }
                break;
            case R.id.store_supply_delete:
                input_search_dishes.setText("");
                break;
            case R.id.speech_input:
                mIatDialog = new RecognizerDialog(getActivity(), new InitListener() {
                    @Override
                    public void onInit(int code) {
                        if (code != ErrorCode.SUCCESS) {
                            ToastUtil.showLong(getActivity(),"初始化失败，错误码：" + code);
                        }
                    }
                });
                mIatDialog.setListener(mRecognizerDialogListener);
                mIatDialog.show();
               ToastUtil.showLong(getActivity(),"请开始说话");
                break;
            default:
                break;
        }
    }
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            input_search_dishes.requestFocus();
            input_search_dishes.setText(speechUtils.saveResult(results));
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
         ToastUtil.showLong(getActivity(),"识别出错了");
        }

    };
    @Override
    public View getView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(getActivity(), R.layout.addlunch_child, null);
        }
        ImageView dishIcon = (ImageView) convertView.findViewById(R.id.dish_icon);
        TextView dishName = (TextView) convertView.findViewById(R.id.dish_name);

        if (position == currentClickItem) {
            dishName.setTextColor(getActivity().getResources().getColor(R.color.green));
            convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        } else {
            dishName.setTextColor(getActivity().getResources().getColor(R.color.black1));
            convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.common_backgroud));
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentClickItem = position;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Request request, Action action, Exception e) {

        if (!NetUtils.isMobileConnected(getActivity())) {
            helper.showError(request, action, this);
        } else {
            helper.showNotify("加载数据失败，请稍后重试");
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (null != object && action == Action.GET_HOME_LIST) {
            HomeStoreSupplyList listBean = (HomeStoreSupplyList) object;
            if (listBean.getCode().equals(Constant.RESULT_SUCESS)) {
                if (listBean.getJson().getDishesSupplyList() == null || (listBean.getJson().getDishesSupplyList()!=null)&& listBean.getJson().getDishesSupplyList().size() == 0) {
                    switch (Integer.parseInt(mealType)) {
                        case 20001:
                            helper.showNotify("抱歉，门店今天无早餐服务！");
                            break;
                        case 20002:
                            helper.showNotify("抱歉，门店今天无午餐服务！");
                            break;
                        case 20003:
                            helper.showNotify("抱歉，门店今天无晚餐服务！");
                            break;
                        default:
                            helper.showNotify("抱歉，门店今天无对应的餐别服务！");
                            break;
                    }
                    return;
                }
                helper.showContent();
                dishesTypeList = listBean.getJson().getDishesSupplyList();
                adapter = new LeftAdapter(getActivity(), dishesTypeList, this);
                leftListView.setAdapter(adapter);

                rightAdapter = new StoreSupplyRighAdapter(getActivity(), dishesTypeList);
                rightRecyclerView.setAdapter(rightAdapter);
            } else {
                helper.showNotify("加载数据失败，请稍后重试");
                ToastUtil.showLong(getActivity(), listBean.getMessage());
            }
        } else if (null != object && action == Action.STORESUPPLY_SEARCH) {
            StoreSupplySearchBean listBean = (StoreSupplySearchBean) object;
            if (listBean.getCode().equals(Constant.RESULT_SUCESS)) {
                CommonUtils.initRerecyelerView(getActivity(), storesupply_search_list, 1);
                if (listBean.getJson().getDishesSupplyList() != null && listBean.getJson().getDishesSupplyList().size() > 0) {
                    if (listBean.getJson().getDishesSupplyList().get(0).getDishesSupplyDtlList() != null && listBean.getJson().getDishesSupplyList().get(0).getDishesSupplyDtlList().size() > 0)
                        searchResultList = listBean.getJson().getDishesSupplyList().get(0).getDishesSupplyDtlList();
                    fillSearchDate(searchResultList, false);
                } else {
                    searchResultList=new ArrayList<>();
                    fillSearchDate(searchResultList,false);
                    ToastUtil.showLong(getActivity(), "不好意思，没有搜到你想要的菜品！");
                }
                near_searchnear_search.setVisibility(View.GONE);
            }
        } else if (object != null && action == Action.ADD_FOOD_COMPLETE) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                //添加菜品成功，回传页面
                EventBus.getDefault().post(Integer.parseInt("10"));
                getActivity().finish();
            } else {
                ToastUtil.showLong(getActivity(), "添加菜品失败");
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {//左侧点击右侧联动
        int count = 0;
        for (int i = 0; i < dishesTypeList.size(); i++) {
            if (position == 0) {
                rightRecyclerView.setSelection(0);
                return;
            } else if (i < position && dishesTypeList.get(i).getDishesSupplyDtlList() == null) {
                continue;
            } else if (i < position && dishesTypeList.get(i).getDishesSupplyDtlList() != null) {
                count += dishesTypeList.get(i).getDishesSupplyDtlList().size();
            } else {
                break;
            }
        }
        rightRecyclerView.setSelection(count);
    }

    private void fillSearchDate(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList, boolean isLocalSQL) {
        if (searchAdapter == null) {
            searchAdapter = new RighSearchAdapter(getActivity(), mList, this, isLocalSQL);
            storesupply_search_list.setAdapter(searchAdapter);
        } else {
            searchAdapter.setDate(mList, isLocalSQL);
        }
    }

    /**
     * 清空历史搜索点击
     */
    @Override
    public void clicked() {
//        searchLayyoutManager.getChildAt(nearSearchList.size()).setVisibility(View.GONE);
        input_search_dishes.setText("");
    }
}
