package com.diligroup.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.Constant;
import com.diligroup.bean.GetFoodDetailsBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.net.RequestManager;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ViewSwitcherHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;


/**
 * Created by Administrator on 2016/7/22
 */
public class FoodDetailsActivity extends AppCompatActivity implements RequestManager.ResultCallback {
    @Bind(R.id.iv_delish_food)
    ImageView iv_food;
    @Bind(R.id.tv_food_name)
    TextView tv_foodName;
    @Bind(R.id.tv_zhuliao)
    TextView tv_zhuliao;
    @Bind(R.id.tv_fuliao)
    TextView tv_fuliao;
    @Bind(R.id.tv_tiaoliao)
    TextView tv_tiaoliao;
    @Bind(R.id.tv_shiyongyou)
    TextView tv_shiyongyou;
    @Bind(R.id.tv_nengliang)
    TextView tv_nengliang;//能量
    @Bind(R.id.tv_tshhw)
    TextView tv_tshhw;//碳水化合物
    @Bind(R.id.tv_daibanzhis)
    TextView tv_daibanzhis;//蛋白质
    @Bind(R.id.tv_zhifangs)
    TextView tv_zhifangs;//脂肪
    @Bind(R.id.details_rootview)
    LinearLayout details_rootview;
    @Bind(R.id.fuliao_layout)
    LinearLayout fuliao_layout;//食用油布局
    @Bind(R.id.tiaoliao_layout)
    LinearLayout tiaoliao_layout;//食用油布局
    @Bind(R.id.shiyongyou_layout)
    LinearLayout shiyongyou_layout;//食用油布局
    private ViewSwitcherHelper myHelper;

    //    @Bind(R.id.ib_goback)
//    ImageButton iv_back;
    @OnClick(R.id.ib_goback)
    public void goBack() {
        this.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        ButterKnife.bind(this);
        myHelper = new ViewSwitcherHelper(this, details_rootview);
        myHelper.showLoading();
        String code = getIntent().getStringExtra("foodCode");
        Api.getFoodDetails(code, this);
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if (action == Action.GET_DETAILS && object != null) {
            GetFoodDetailsBean foodDetailsBean = (GetFoodDetailsBean) object;
            if (foodDetailsBean.getCode().equals(Constant.RESULT_SUCESS)) {
                myHelper.showContent();
                if (null != foodDetailsBean) {
                    tv_foodName.setText(foodDetailsBean.getDishesName());
                    tv_zhuliao.setText(foodDetailsBean.getMainFoodName());
                    if (!TextUtils.isEmpty(foodDetailsBean.getAccessorysName())) {
                        tv_fuliao.setText(foodDetailsBean.getAccessorysName());
                    } else {
                        fuliao_layout.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(foodDetailsBean.getSeasoningsName())) {
                        tv_tiaoliao.setText(foodDetailsBean.getSeasoningsName());
                    } else {
                        tiaoliao_layout.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(foodDetailsBean.getOilName())) {
                        tv_shiyongyou.setText(foodDetailsBean.getOilName());
                    } else {
                        shiyongyou_layout.setVisibility(View.GONE);
                    }
                    tv_nengliang.setText(String.valueOf(foodDetailsBean.getEnergyKC() + "kal"));
                    tv_tshhw.setText(String.valueOf(foodDetailsBean.getCarbohydrates()) + "g");
                    tv_daibanzhis.setText(String.valueOf(foodDetailsBean.getProtein()) + "g");
                    tv_zhifangs.setText(String.valueOf(foodDetailsBean.getFat()) + "g");
                    Picasso.with(this)
                            .load(foodDetailsBean.getImagesURL())
                            .error(R.mipmap.food_detail_default)
                            .placeholder(R.mipmap.food_detail_default)
                            .into(iv_food);
                } else {
                    myHelper.showNotify("数据加载失败，请稍后重试");
                }
            }
        }
    }
}
