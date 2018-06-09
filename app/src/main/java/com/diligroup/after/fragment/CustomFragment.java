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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.after.adapter.CustomRighSearchAdapter;
import com.diligroup.after.adapter.CustomeRighAdapter;
import com.diligroup.after.adapter.StoreSuppyLeftAdapter;
import com.diligroup.base.BaseFragment;
import com.diligroup.base.Constant;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.ButtonClickListener;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.CostomerCategory;
import com.diligroup.bean.CustomerSearchResultBean;
import com.diligroup.bean.FindFoodByCategory;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.RecordSQLiteOpenHelper;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;
import com.diligroup.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
public class CustomFragment extends BaseFragment implements View.OnClickListener, StoreSuppyLeftAdapter.OnGetView, AdapterView.OnItemClickListener, RequestManager.ResultCallback, ButtonClickListener, PullToRefreshBase.OnRefreshListener2 {
    @Bind(R.id.custome_input_search_dishes)
    EditText customeInputSearchDishes;
    @Bind(R.id.addlunche_custome_delete)
    ImageView addluncheCustomeDelete;
    @Bind(R.id.cuostomer_recyclerView)
    PullToRefreshListView cuostomerRecyclerView;
    @Bind(R.id.customer_left_listView)
    ListView customer_left_listView;
    @Bind(R.id.costom_search_list)
    RecyclerView costom_search_list;//自定义搜索列表
    @Bind(R.id.customer_normallayout)
    LinearLayout customer_normallayout;//默认展示自定义列表
    @Bind(R.id.costom_searchlayout)
    LinearLayout customer_search_layout;//自定义搜索列表
    @Bind(R.id.cus_nearsearch)
    TextView cus_nearsearch;
    @Bind(R.id.customer_margin)
    View custoer_margin;//自定义切换布局时候间隔
    @Bind(R.id.customer_complete_add)
    RelativeLayout customer_complete_add;//添加完成按钮
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mAllList = new ArrayList<>();
    List<CostomerCategory.ListBean> leftList = new ArrayList<CostomerCategory.ListBean>();
    private int currentClickItem;//当前选中的左侧的index
    private StoreSuppyLeftAdapter leftAdapter;
    CustomRighSearchAdapter searchAdapter;
    int currentIndex = 0;//当前左侧选中索引
    private String whichDay;
    private String mealType;

    private RecordSQLiteOpenHelper cusopenHelper;
    CustomeRighAdapter rightAdapter;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> nearSearchList;//最近搜索列表
    private RecyclerView.LayoutManager searchLayyoutManager;
    long preTime;//文本改变，上次请求时间
    boolean isFirst = true;//是否是第一次加载
    int currrrentPage = 1;
    private ViewSwitcherHelper helper;
    private int tatalCount;

    @Override
    public int getLayoutXml() {
        return R.layout.fragment_customer;
    }

    @Override
    public void setViews() {
        Api.getCustomerFoodList(this);
//        mRefreshLayout.setDelegate(this);//为刷新设置代理
        helper = new ViewSwitcherHelper(getActivity(), cuostomerRecyclerView);
        helper.showLoading();
        searchLayyoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST);
        costom_search_list.addItemDecoration(dividerItemDecoration);//垂直列表的分割线
        costom_search_list.setHasFixedSize(true);//保持固定大小，提高性能
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        costom_search_list.setLayoutManager(layoutManager);
    }

    @Override
    public void setListeners() {
        addluncheCustomeDelete.setOnClickListener(this);
        customeInputSearchDishes.setOnClickListener(this);
        customer_complete_add.setOnClickListener(this);

        customer_left_listView.setOnItemClickListener(this);
        cuostomerRecyclerView.setOnRefreshListener(this);
        cuostomerRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);

        cusopenHelper = new RecordSQLiteOpenHelper(getActivity());
        customeInputSearchDishes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //获取焦点然后展示搜索最近的菜品
                if (hasFocus) {
                    customer_normallayout.setVisibility(View.GONE);
                    customer_search_layout.setVisibility(View.VISIBLE);
                    custoer_margin.setVisibility(View.GONE);
                    //展示最近搜索列表
                    nearSearchList = cusopenHelper.getALllFoods(RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME);
                    if (nearSearchList != null && nearSearchList.size() > 0) {
                        fillSearchDate(nearSearchList, true);
                    }
                }
            }
        });
        customeInputSearchDishes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                customer_search_layout.setVisibility(View.VISIBLE);
                customer_normallayout.setVisibility(View.GONE);
                try {
                    if (!TextUtils.isEmpty(editable.toString())) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - preTime > 5) {
                            Api.customer_search(Constant.cityCode, editable.toString(), CustomFragment.this);
                            preTime = currentTime;
                        }
                    } else {
                        cus_nearsearch.setVisibility(View.VISIBLE);
                        nearSearchList = cusopenHelper.getALllFoods(RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME);
                        fillSearchDate(nearSearchList, true);
//                        if (cus_nearsearch != null) {
//                            cus_nearsearch.setVisibility(View.VISIBLE);
//                        }
//                        if (nearSearchList.size() > 0 && null != searchLayyoutManager.getChildAt(nearSearchList.size() - 1))
//                            searchLayyoutManager.getChildAt(nearSearchList.size() - 1).setVisibility(View.VISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Fragment newInstance(String mealType, String whichDay) {
        Bundle args = new Bundle();
        args.putString("mealType", mealType);
        args.putString("whichDay", whichDay);
        CustomFragment fragmnent = new CustomFragment();
        fragmnent.setArguments(args);
        return fragmnent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            mealType = getArguments().getString("mealType");
            whichDay = DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(getArguments().getString("whichDay")));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addlunche_custome_delete:
                customeInputSearchDishes.setText("");
                break;
            case R.id.custome_input_search_dishes:
                customeInputSearchDishes.setFocusable(true);
                break;
            case R.id.customer_complete_add:
                List<AddFoodCompleteBean> addMealList = ((AddLunchActivity) getActivity()).getAddMealList();
                for(int i=addMealList.size()-1;i>=0;i--){
                    if((TextUtils.isEmpty(addMealList.get(i).getWeight()) && addMealList.get(i).getWayType().equals("1"))|| (!TextUtils.isEmpty(addMealList.get(i).getWeight()) && addMealList.get(i).getWeight().equals("0.0"))){
                        ToastUtil.showLong(getActivity(), "请输入您选择的菜品的重量");
                        return;
//                        addMealList.remove(addMealList.get(i));
                    }
                }
                if (addMealList.size()==0) {
                    ToastUtil.showLong(getActivity(), "你还没有添加任何菜品哟");
                    return;
                }
                String json = new Gson().toJson(addMealList);
                Api.addFoodComplete(whichDay, UserManager.getInstance().getUserId(), mealType, json, this);
                if (searchAdapter != null) {
                    //遍历搜索结果列表，存入数据库
                    for (int i = 0; i < searchAdapter.getDate().size(); i++) {
                        if (searchAdapter.getDate().get(i).getFoodNums() != 0) {
                            HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean foodBean = cusopenHelper.selectFood(searchAdapter.getDate().get(i).getDishesName(), RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME);
                            if (foodBean != null) {
                                cusopenHelper.updateFood(searchAdapter.getDate().get(i), RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME, searchAdapter.isLocalSQL());
                            } else {
                                cusopenHelper.addFood(searchAdapter.getDate().get(i), RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME, searchAdapter.isLocalSQL());
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View getView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(getActivity(), R.layout.addlunch_child, null);
        }
        ImageView dishIcon = (ImageView) convertView.findViewById(R.id.dish_icon);
        TextView dishName = (TextView) convertView.findViewById(R.id.dish_name);
        View rightLine = convertView.findViewById(R.id.right_line);
        dishName.setText(leftList.get(position).getDictName());

        if (position == currentClickItem) {
            CommonUtils.showCategoryIcon(Integer.parseInt(leftList.get(position).getCode()), dishIcon, true);
            dishName.setTextColor(getActivity().getResources().getColor(R.color.green));
            convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            rightLine.setVisibility(View.GONE);
        } else {
            CommonUtils.showCategoryIcon(Integer.parseInt(leftList.get(position).getCode()), dishIcon, false);
            dishName.setTextColor(getActivity().getResources().getColor(R.color.black1));
            convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.common_backgroud));
            rightLine.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        currentClickItem = position;
        isFirst = true;
        currrrentPage = 1;
        leftAdapter.notifyDataSetChanged();
        if (leftList != null && leftList.size() > 0) {
            helper.showLoading();
            Api.findFoodByCategoryId(currrrentPage, mealType, leftList.get(position).getCode(), this);
        }
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
        if (object != null && action == Action.GET_COSTOMER_FOODLIST) {//左侧列表
            CostomerCategory bean = (CostomerCategory) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                leftList = bean.getList();
                if (mealType.equals("20001") && leftList != null) {//早餐没有热菜大荤
                    for (int i = 0; i < leftList.size(); i++) {
                        if (leftList.get(i).getCode().equals("10007")) {
                            leftList.remove(leftList.get(i));
                            break;
                        }
                    }
                }
                Api.findFoodByCategoryId(1, mealType, leftList.get(0).getCode(), this);
                leftAdapter = new StoreSuppyLeftAdapter(getActivity(), leftList, this);
                customer_left_listView.setAdapter(leftAdapter);
            }
        } else if (object != null && action == Action.CUSTOMER_SEARCH) {//自定义搜索
            CustomerSearchResultBean resultBean = (CustomerSearchResultBean) object;
            if (resultBean.getCode().equals(Constant.RESULT_SUCESS)) {
                List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> resultList = resultBean.getDishesLibList();
                if (resultList.size() > 0) {
//                    cus_nearsearch.setVisibility(View.VISIBLE);
                    initAdapter(resultList);
                } else {
//                    searchAdapter.setDate(resultList, false);
                    initAdapter(resultList);
                    ToastUtil.showLong(getActivity(), "没有找到你要搜索的菜品哦");
                }
            } else {
                initAdapter(resultBean.getDishesLibList());
//                searchAdapter.setDate(resultBean.getDishesLibList(), false);
                ToastUtil.showLong(getActivity(), "没有找到你要搜索的菜品哦");
            }
            //隐藏最近搜索和清空文字
            cus_nearsearch.setVisibility(View.GONE);
        } else if (object != null && action == Action.CUSTOMER_FIND_BY_CATEGORYID) {
            FindFoodByCategory rightFoodBean = (FindFoodByCategory) object;
            if (rightFoodBean.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                if (rightFoodBean != null) {
                    tatalCount = rightFoodBean.getTotalCount();
                }
                if (cuostomerRecyclerView.isRefreshing()) {
                    cuostomerRecyclerView.onRefreshComplete();
                }
                if (isFirst) {
                    mAllList = rightFoodBean.getDishesLibList();
                } else {
                    mAllList.addAll(rightFoodBean.getDishesLibList());
                }
                if (rightAdapter == null) {
                    rightAdapter = new CustomeRighAdapter(getActivity(), mAllList);
                    cuostomerRecyclerView.setAdapter(rightAdapter);
                } else {
                    rightAdapter.setDate(mAllList);
                }
            } else {
                helper.showNotify(rightFoodBean.getMessage());
            }
        } else if (object != null && action == Action.ADD_FOOD_COMPLETE) {
            CommonBean commonBean = (CommonBean) object;
            if (commonBean.getCode().equals(Constant.RESULT_SUCESS)) {
                //添加菜品成功，回传页面
//                getActivity().setResult(10,new Intent());
                EventBus.getDefault().post(Integer.parseInt("10"));
                if(getActivity()!=null)
                getActivity().finish();
            } else {
                ToastUtil.showLong(getActivity(), "添加菜品失败");
            }

        }
    }

    private void initAdapter(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> resultList) {
        if (searchAdapter == null) {
            searchAdapter = new CustomRighSearchAdapter(getActivity(), resultList, this, false);
            costom_search_list.setAdapter(searchAdapter);
        } else if (resultList!=null && resultList.size() > 0 && searchAdapter != null) {
            searchAdapter.setDate(resultList, false);
        }
    }

    @Override
    public void clicked() {//点击了清空搜索历史回调
//        searchLayyoutManager.getChildAt(nearSearchList.size()).setVisibility(View.GONE);

        customeInputSearchDishes.setText("");
    }

    private void fillSearchDate(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList, boolean isLoacalSql) {
        CommonUtils.initRerecyelerView(getActivity(), costom_search_list, 1);

        costom_search_list.setLayoutManager(searchLayyoutManager);
        if (searchAdapter == null) {
            searchAdapter = new CustomRighSearchAdapter(getActivity(), mList, this, isLoacalSql);
            costom_search_list.setAdapter(searchAdapter);
        } else {
            searchAdapter.setDate(mList, isLoacalSql);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Api.findFoodByCategoryId(1, mealType, leftList.get(currentClickItem).getCode(), this);
        isFirst = true;
        currrrentPage = 1;
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (currrrentPage * 20 >= tatalCount) {
            ToastUtil.showLong(getActivity(), "没有更多数据了");
            cuostomerRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (cuostomerRecyclerView.isRefreshing()) {
                        cuostomerRecyclerView.onRefreshComplete();
                    }
                }
            }, 1000);
            return;
        }
        currrrentPage = currrrentPage + 1;
        isFirst = false;
        Api.findFoodByCategoryId(currrrentPage, mealType, leftList.get(currentClickItem).getCode(), this);
    }
}
