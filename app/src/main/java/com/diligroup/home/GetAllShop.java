package com.diligroup.home;

import android.os.FileObserver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.bean.GetShopBean;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.home.adapter.SelectAllShopAdapter;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Request;

/**
 * 用户手动 选择 门店地址
 * Created by hjf 2016-08-28.
 */
public class GetAllShop extends BaseActivity implements MyItemClickListener {
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.select_shopname)
    TextView currentShopName;//当前门店名称
    @Bind(R.id.selectshop_details_address)
    TextView currentDetailAddress;//当前门店详细地址
    @Bind(R.id.selectshop_all_list)
    RecyclerView selectshop_all_list;//选择所有门店list
    @Bind(R.id.current_shop_layout)
    RelativeLayout current_shop_layout;//当前门店布局
    @Bind(R.id.allshop_head_pull)
    PtrClassicFrameLayout allShopHeadPull;
    @Bind(R.id.allshop_rootview)
    LinearLayout allshop_rootview;//
    private SelectAllShopAdapter adapter;
    private List<GetShopBean.StoreCustListBean> mlist;//所有门店列表
    private List<GetShopBean.StoreCustListBean> alllist;//所有门店列表
    private GetShopBean.StoreCustListBean currentShopBean;//当前门店bean；
    private LinearLayoutManager recyclerLayout;
    int currentPage = 1;//当前页数
    boolean isFirst = true;//是否是第一次加载数据
    private ViewSwitcherHelper helper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.select_shop;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("选择门店");
        ivBack.setVisibility(View.VISIBLE);
        FileObserver observer;
        helper = new ViewSwitcherHelper(this, allshop_rootview);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CommonUtils.initRerecyelerView(this, selectshop_all_list, 0);
        recyclerLayout = new LinearLayoutManager(this);
        selectshop_all_list.setLayoutManager(recyclerLayout);
        helper.showLoading();
        Api.getAllShop(1, this);

        allShopHeadPull.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return checkContentCanbePullDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Api.getAllShop(1, GetAllShop.this);
                    }
                }, 1800);
            }
        });
        current_shop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = currentShopBean.getCityName().contains("市辖区") ? currentShopBean.getProvinceName() : currentShopBean.getCityName();
                saveShopInfo(currentShopBean.getStoreId() + "", temp + currentShopBean.getDistrictName(), currentShopBean.getName());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("storeId", currentShopBean.getStoreId() + "");
                Api.updataUserInfo(map, GetAllShop.this);
            }
        });
        selectshop_all_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && lastVisibleItem > 20) {
                    adapter.changeMoreStatus(SelectAllShopAdapter.LOADING_MORE);
                    isFirst = false;
                    Api.getAllShop(currentPage, GetAllShop.this);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = recyclerLayout.findLastVisibleItemPosition();
            }
        });
    }

    private boolean checkContentCanbePullDown(PtrFrameLayout frame, View content, View header) {
        /**
         * 最终判断，判断第一个子 View 的 top 值</br>
         * 如果第一个子 View 有 margin，则当 top==子 view 的 marginTop+content 的 paddingTop 时，表示在最顶部，返回 true，可以下拉</br>
         * 如果没有 margin，则当 top==content 的 paddinTop 时，表示在最顶部，返回 true，可以下拉
         */
        View child = selectshop_all_list.getChildAt(0);
        if (child != null) {
            ViewGroup.LayoutParams glp = child.getLayoutParams();
            int top = child.getTop();
            if (glp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) glp;
                return top == mlp.topMargin + selectshop_all_list.getPaddingTop();
            } else {
                return top == selectshop_all_list.getPaddingTop();
            }
        }
            return false;
    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        helper.showError(request, action, this);
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (null != object && action == Action.GET_SHOP_NEARBY) {
            GetShopBean bean = (GetShopBean) object;
            if (bean.getCode().equals(Constant.RESULT_SUCESS)) {
                helper.showContent();
                mlist = bean.getStoreCustList();
                if (isFirst) {
                    if (allShopHeadPull.isRefreshing()) {
                        allShopHeadPull.refreshComplete();
                    }
                    alllist = mlist;
                    for (int i = 0; i < alllist.size(); i++) {
                        if (alllist.get(i).getIsDefault().equals("1")) {
                            currentShopName.setText("[ 当前 ] " + alllist.get(i).getName());
                            currentDetailAddress.setText("地址：" + alllist.get(i).getAddress());
                            currentShopBean = alllist.get(i);
                            alllist.remove(alllist.get(i));
                            break;
                        }
                    }
                } else {
                    alllist.addAll(mlist);
                }
                if (adapter == null) {
                    adapter = new SelectAllShopAdapter(GetAllShop.this, alllist, this);
                    selectshop_all_list.setAdapter(adapter);
                } else {
                    adapter.setDate(alllist);
                }
                currentPage = currentPage + 1;
            } else {
                helper.showError(request, action, this);
            }
        } else if (object != null && action == Action.UPDATA_USERINFO) {
            if (((CommonBean) object).getCode().equals(Constant.RESULT_SUCESS)) {
                setResult(0x111);
                finish();
            }
        }
    }

    //选择某一个门店
    @Override
    public void onItemClick(View view, int position) {
        String temp = mlist.get(position).getCityName().contains("市辖区") ? mlist.get(position).getProvinceName() : mlist.get(position).getCityName();
        saveShopInfo(mlist.get(position).getStoreId() + "", temp + mlist.get(position).getDistrictName(), mlist.get(position).getName());

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("storeId", mlist.get(position).getStoreId() + "");
        Api.updataUserInfo(map, GetAllShop.this);

    }

    /**
     * 保存门店信息
     *
     * @param storeID
     * @param storeAdrress
     * @param storeName
     */
    public void saveShopInfo(String storeID, String storeAdrress, String storeName) {
        UserManager.getInstance().setStoreId(storeID);
        UserManager.getInstance().setStoreAddress(storeAdrress);
        UserManager.getInstance().setStoreName(storeName);
    }
}
