package com.diligroup.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.diligroup.R;
import com.diligroup.base.AppManager;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.base.DiliApplication;
import com.diligroup.bean.GetCityCode;
import com.diligroup.bean.GetShopBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.other.LocationService;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;
import com.diligroup.utils.ViewSwitcherHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Request;

/**
 * 根据位置获取 本市门店 list
 * Created by Kevin on 2016/7/11.
 */
public class GetCityShopActivity extends BaseActivity implements CloudListener, BDLocationListener, View.OnClickListener {
    private static final String LTAG = GetCityShopActivity.class.getSimpleName();
    private final int SDK_PERMISSION_REQUEST = 127;
    LocationService locationService;
    //    List<CloudPoiInfo> cloudPoiInfoList;
//    List<ShopInfosBean> shopInfoList;
    List<GetShopBean.StoreCustListBean> shopList;
    @Bind(R.id.lv_list_shop)
    ListView shopListView;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.comm_title)
    TextView tv_title;
    private int shopId;//门店id
    private String shopName;//门店名称
    private String storeAdrress;//门店地址
    private ViewSwitcherHelper myHelper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.getshop_activity;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {
        ToastUtil.showShort(GetCityShopActivity.this, "网络异常请检查连接.....");

    }

    @Override
    protected void onStart() {
        getPersimmions();
        Intent intent = new Intent();
        UserManager.getInstance().setStoreAddress("1111");
        UserManager.getInstance().setStoreName("222");
        UserManager.getInstance().setStoreId(String.valueOf("333"));
        setResult(0x111, intent);
        GetCityShopActivity.this.finish();
        super.onStart();
    }

    private void getLocation() {
        locationService = ((DiliApplication) getApplication()).locationService;
        locationService.registerListener(this);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    @Override
    protected void initViewAndData() {
        tv_title.setText("选择本市门店");
        myHelper = new ViewSwitcherHelper(this, shopListView);
        AppManager.getAppManager().addActivity(this);
        iv_back.setOnClickListener(this);
        if (NetUtils.isMobileConnected(this)) {
            getLocation();
            myHelper.showLoading();
        } else {
            ToastUtil.showLong(this, "哎呀，断网了");
            return;
        }
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (shopList != null) {
                    shopId = shopList.get(position).getStoreId();
                    String temp = shopList.get(position).getCityName().contains("市辖区") ? shopList.get(position).getProvinceName() : shopList.get(position).getCityName();
                    storeAdrress = temp + shopList.get(position).getDistrictName();
                    shopName = shopList.get(position).getName();
//                    HashMap<String,String> map=new HashMap<String, String>();
//                    map.put("storeId",shopId+"");
//                    Api.updataUserInfo(map,GetCityShopActivity.this);
                    Intent intent = new Intent();
                    UserManager.getInstance().setStoreAddress(storeAdrress);
                    UserManager.getInstance().setStoreName(shopName);
                    UserManager.getInstance().setStoreId(String.valueOf(shopId));
                    setResult(0x111, intent);
                    GetCityShopActivity.this.finish();
                }
            }
        });

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
            String city = bdLocation.getCity();
            LogUtils.e(city);
            Api.getCityCode(city, this);
            locationService.unregisterListener(this);
            locationService.stop();
        } else {
            myHelper.showNotify("获取位置失败");
            ToastUtil.showLong(this, "获取位置失败");
        }
    }

    @Override
    protected void onStop() {
        if (locationService != null) {
            locationService.unregisterListener(this); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        if (result != null && result.poiList != null && result.poiList.size() > 0) {
            Log.d(LTAG, "onGetSearchResult, result length: " + result.poiList.size());
        }
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }

    @Override
    public void onError(Request request, Action action, Exception e) {
        if (!NetUtils.isMobileConnected(this)) {
            myHelper.showError(request, action, this);
        } else {
            myHelper.showNotify("获取数据失败，请稍后重试");
        }
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.Get_CityCode && object != null) {
            GetCityCode cityBean = (GetCityCode) object;
            if (cityBean.getCode().equals(Constant.RESULT_SUCESS)) {
                Map map = new HashMap();
                if (cityBean.getDis().getParentCode() != null) {
                    map.put("cityCode", cityBean.getDis().getParentCode());
                }
                map.put("cityCode", cityBean.getDis().getSortCode());
                Api.getShopNearBy(map, this);
            } else {
                ToastUtil.showLong(this, cityBean.getMessage());
            }
        } else if (action == Action.GET_SHOP_NEARBY && object != null) {
            GetShopBean shopBean = (GetShopBean) object;
            if (shopBean.getCode().equals(Constant.RESULT_SUCESS)) {
                myHelper.showContent();
                shopList = shopBean.getStoreCustList();
                if (null!=shopList && shopList.size() > 0) {
                    shopListView.setAdapter(new ShopListAdapter());
                } else {
                    Map<String, String> map = new HashMap();
                    map.put("isDefault", "1");
                    Api.getShopNearBy(map, this);
                }

            } else {
                myHelper.showNotify("数据获取失败，请稍后再试");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (shopId == 0 && !TextUtils.isEmpty(UserManager.getInstance().getStoreId())) {
                    return;
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
//禁止系统返回键
    }

    public class ShopListAdapter extends BaseAdapter {

        LayoutInflater mInflater;

        ShopListAdapter() {
            mInflater = LayoutInflater.from(GetCityShopActivity.this);
        }

        @Override
        public int getCount() {
            return shopList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_shop, null);
                holder.shop_name = (TextView) convertView.findViewById(R.id.tv_shopname);
                holder.shop_details = (TextView) convertView.findViewById(R.id.tv_shop_details);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.shop_name.setText(shopList.get(position).getName());
            holder.shop_details.setText("地址：" + shopList.get(position).getAddress());
            return convertView;
        }
    }

    class ViewHolder {
        TextView shop_name;
        TextView shop_details;
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }
}
