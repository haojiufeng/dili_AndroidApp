package com.diligroup.after.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.ButtonClickListener;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.home.FoodDetailsActivity;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.RecordSQLiteOpenHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjf on 2016/7/14.
 * 门店供应自定义通用搜索列表适配器
 */
public class CustomRighSearchAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> customerList;

    private int mBottomCount = 1;//底部View个数
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;

    ButtonClickListener listener;
    boolean clearList;

    public boolean isLocalSQL() {
        return isLocalSQL;
    }

    boolean isLocalSQL;//是否是本地数据库查询

    public CustomRighSearchAdapter(Context mContext, List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> customerList, ButtonClickListener listener, boolean isLocalSQL) {
        super();
        this.mContext = mContext;
        this.customerList = customerList;
        this.listener = listener;
        this.isLocalSQL = isLocalSQL;
    }

    public void setDate(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> customerList, boolean isLocalSQL) {
        this.customerList = customerList;
        this.isLocalSQL = isLocalSQL;
//        if(clearList && customerList.size()>0){
//            customerList.clear();
//            notifyDataSetChanged();
//        }else if(clearList && customerList.size()>0){
//            return;
//        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE_BOTTOM && getContentItemCount() != 0) {
            view = View.inflate(mContext, R.layout.delete_hository, null);
            return new CusBottomViewHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.customer_search_rightlist, null);
            return new CusMyViewHoder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CusBottomViewHolder) {
            CusBottomViewHolder myViewHolder = (CusBottomViewHolder) holder;
        } else {
            final CusMyViewHoder viewHoder = (CusMyViewHoder) holder;
//        StringBuilder tempStr = new StringBuilder();
//        for (int i = 0; i < customerList.get(position).getAccessoriesList().size(); i++) {
//            if (!tempStr.toString().contains("+")) {
//                tempStr.append(customerList.get(position).getAccessoriesList().get(i).getFoodName());
//            } else {
//                tempStr.append("+" + customerList.get(position).getAccessoriesList().get(i).getFoodName());
//            }
//        }
            if (!TextUtils.isEmpty(customerList.get(position).getImagesURL1())) {
                Picasso.with(mContext)
                        .load(customerList.get(position).getImagesURL1())
                        .placeholder(R.mipmap.food_icon_default)
                        .error(R.mipmap.food_icon_default)
                        .into(viewHoder.addlunchRightIcon);
            }
            if (isLocalSQL) {
                viewHoder.addlunchDishesIngredients.setText("配料：" + customerList.get(position).getMainFood());
            } else {
                if (null != customerList.get(position).getAccessoriesList() && customerList.get(position).getAccessoriesList().size() > 0) {
                    viewHoder.addlunchDishesIngredients.setText("配料：" + customerList.get(position).getIngredients().getFoodName() + "+" + customerList.get(position).getAccessoriesList().get(0).getFoodName());
                } else {
                    viewHoder.addlunchDishesIngredients.setText("配料：" + customerList.get(position).getIngredients().getFoodName());
                } //不能用这种方式，这个取的是我本地数据库，没有那么多字段
            }
            if (customerList.get(position).getFoodNums() == 0) {
                viewHoder.addlunchDishesNum.setVisibility(View.GONE);
                viewHoder.addlunchReducedish.setVisibility(View.GONE);
            } else {
                viewHoder.addlunchDishesNum.setVisibility(View.VISIBLE);
                viewHoder.addlunchReducedish.setVisibility(View.VISIBLE);
                viewHoder.addlunchDishesNum.setText(customerList.get(position).getFoodNums() + "");
            }
            viewHoder.addlunchDishesName.setText(customerList.get(position).getDishesName());
//        viewHoder.addlunchRightIcon.setImageURI(Uri.parse(customerList.get(position).getImagesURL()));
            viewHoder.addlunchAdddish.setImageResource(customerList.get(position).getFoodNums() > 0 ? R.mipmap.add_dish_pressed : R.mipmap.add_dishes_normal);
            viewHoder.addlunchAdddish.setOnClickListener(new MyOnClickListener(position, viewHoder));
            viewHoder.addlunchReducedish.setOnClickListener(new MyOnClickListener(position, viewHoder));
            viewHoder.rootView.setOnClickListener(new MyOnClickListener(position, null));
            viewHoder.input_weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!TextUtils.isEmpty(editable) && editable.toString().length() > 0) {
                        customerList.get(position).setWeight(viewHoder.input_weight.getText().toString());
                        LogUtils.i("输入的内容是==" + viewHoder.input_weight.getText().toString());
                        if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
                            if(!((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))){
                                ((AddLunchActivity) mContext).getAddMealList().add(setBean(position));
                            }
                            for (int i = 0; i < ((AddLunchActivity) mContext).getAddMealList().size(); i++) {
                                if (((AddLunchActivity) mContext).getAddMealList().get(i).equals(setBean(position))) {
                                    int num = Integer.parseInt(viewHoder.addlunchDishesNum.getText().toString());
                                    float tempWeight = Float.parseFloat(viewHoder.input_weight.getText().toString());

                                    ((AddLunchActivity) mContext).getAddMealList().get(i).setWeight(tempWeight * num + "");
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < ((AddLunchActivity) mContext).getAddMealList().size(); i++) {
                            if (((AddLunchActivity) mContext).getAddMealList().get(i).equals(setBean(position))) {
                                ((AddLunchActivity) mContext).getAddMealList().get(i).setWeight("");
                            }
                        }
                    }
                }
            });
            if (customerList.get(position).isShowWeight()) {
                viewHoder.weightlayout.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
                    viewHoder.input_weight.setText(viewHoder.input_weight.getText().toString());
                }
                if (customerList.get(position).getWeight() != null && Float.parseFloat(customerList.get(position).getWeight()) > 0.0f) {
                    viewHoder.input_weight.setText(customerList.get(position).getWeight());
                }
            } else {
                viewHoder.weightlayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() == 0 ? 0 : getContentItemCount() + mBottomCount;
    }

    //内容长度
    public int getContentItemCount() {
        return customerList == null ? 0 : customerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mBottomCount != 0 && position >= (dataItemCount)) {//底部View
            return ITEM_TYPE_BOTTOM;
        } else {//内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        int position;
        //        View view;
        CusMyViewHoder viewHoder;

        public MyOnClickListener(int position, CusMyViewHoder viewHoder) {
            this.viewHoder = viewHoder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addlunch_adddish:
                    if (viewHoder != null)
                        CommonUtils.propertyValuesHolder(viewHoder.addlunchDishesNum);
                    if (!((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        ((AddLunchActivity) mContext).addFood(setBean(position), true);
                    } else if (((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        int tempIndex = ((AddLunchActivity) mContext).getAddMealList().indexOf(setBean(position));
                        float tempWeight=Float.parseFloat(TextUtils.isEmpty(setBean(position).getWeight())?"0.0f":setBean(position).getWeight());
                       String temp=((AddLunchActivity) mContext).getAddMealList().get(tempIndex).getWeight();
                        float currentWeight=Float.parseFloat(TextUtils.isEmpty(temp)?"0.0f":temp);
                        ((AddLunchActivity) mContext).getAddMealList().get(tempIndex).setWeight(tempWeight+currentWeight+"");//有的话，累加
                    }
                    customerList.get(position).setShowWeight(true);
                    customerList.get(position).setFoodNums(customerList.get(position).getFoodNums() + 1);
                    //控制界面上变化的每一个元素
                    viewHoder.addlunchDishesNum.setVisibility(View.VISIBLE);
                    viewHoder.addlunchReducedish.setVisibility(View.VISIBLE);
                    viewHoder.weightlayout.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(viewHoder.addlunchDishesNum.getText())) {
                        viewHoder.addlunchDishesNum.setText("1");
                    } else {
                        viewHoder.addlunchDishesNum.setText(Integer.parseInt(viewHoder.addlunchDishesNum.getText().toString()) + 1 + "");
                    }
//                    notifyItemChanged(position);
                    break;
                case R.id.addlunch_reducedish:
                    customerList.get(position).setFoodNums(customerList.get(position).getFoodNums() - 1);
                    viewHoder.addlunchDishesNum.setText(Integer.parseInt(viewHoder.addlunchDishesNum.getText().toString()) - 1 + "");
                    if (customerList.get(position).getFoodNums() <= 0) {
                        viewHoder.addlunchDishesNum.setVisibility(View.GONE);
                        viewHoder.addlunchReducedish.setVisibility(View.GONE);
                        viewHoder.weightlayout.setVisibility(View.GONE);
                        //本条编辑框为“”
                        if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
                            viewHoder.input_weight.setText("");
                        }
                        //数据集中的本条数据为“”
                        if (!TextUtils.isEmpty(customerList.get(position).getWeight())) {
                            customerList.get(position).setWeight("");
                        }
                        ((AddLunchActivity) mContext).deleteFood(setBean(position));
                    } else if(customerList.get(position).getFoodNums() > 0 && !TextUtils.isEmpty(viewHoder.input_weight.getText().toString())){
                        if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
//                            viewHoder.input_weight.setText(viewHoder.input_weight.getText().toString());
                            ((AddLunchActivity) mContext).addFood(setBean(position), false);//false代表把整个集合中已有数据weight值减下来
                        }
                    }
                    break;
                case R.id.root_view:
                    Intent mIntent = new Intent(mContext, FoodDetailsActivity.class);
                    mIntent.putExtra("foodCode", customerList.get(position).getDishesCode());
                    mContext.startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 统一添加生成菜谱接口
     *
     * @param position
     * @return菜品来源（1自定义，2来自门店）
     */
    private AddFoodCompleteBean setBean(int position) {
        AddFoodCompleteBean bean = new AddFoodCompleteBean();
        bean.setWeight(customerList.get(position).getWeight());
        bean.setDishesCode(customerList.get(position).getDishesCode());
        bean.setDishesName(customerList.get(position).getDishesName());
        bean.setImageUrl(customerList.get(position).getImagesURL());
        bean.setImageUrl1(customerList.get(position).getImagesURL1());
        bean.setFoodNums(customerList.get(position).getFoodNums());
        bean.setWayType("1");
        return bean;
    }

    class CusMyViewHoder extends RecyclerView.ViewHolder {
        @Bind(R.id.addlunch_right_icon)
        ImageView addlunchRightIcon;//右侧icon
        @Bind(R.id.addlunch_dishes_name)
        TextView addlunchDishesName;//食物名称
        @Bind(R.id.addlunch_dishes_ingredients)
        TextView addlunchDishesIngredients;//食品配料
        @Bind(R.id.addlunch_grams_num)
        TextView addlunchGramsNum;//克数
        @Bind(R.id.addlunch_reducedish)
        ImageView addlunchReducedish;//“-”
        @Bind(R.id.addlunch_dishes_num)
        TextView addlunchDishesNum;//食物数量
        @Bind(R.id.addlunch_adddish)
        ImageView addlunchAdddish;//“+”
        @Bind(R.id.root_view)
        RelativeLayout rootView;
        @Bind(R.id.input_weight)
        EditText input_weight;
        @Bind(R.id.weightlayout)
        LinearLayout weightlayout;

        public CusMyViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //底部 ViewHolder
    class CusBottomViewHolder extends RecyclerView.ViewHolder {
        public CusBottomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerList.clear();
                    clearList = true;
                    setDate(customerList, false);
                    new RecordSQLiteOpenHelper(mContext).deleteAll(RecordSQLiteOpenHelper.CUSTOMER_TABLE_NAME);
                    listener.clicked();
                }
            });
        }
    }

    public List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> getDate() {
        return customerList;
    }
}

