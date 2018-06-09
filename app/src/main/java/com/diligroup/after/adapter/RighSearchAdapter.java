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

import com.diligroup.after.AddLunchActivity;
import com.diligroup.home.FoodDetailsActivity;
import com.diligroup.R;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.ButtonClickListener;
import com.diligroup.bean.HomeStoreSupplyList;
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
public class RighSearchAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList;

    private int mBottomCount = 1;//底部View个数
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    ButtonClickListener listener;
//    boolean isStoreSupply;//是否是门店供应查询
    public boolean isLocalSQL() {
        return isLocalSQL;
    }

    boolean isLocalSQL;//是否是本地数据库查询

    public RighSearchAdapter(Context mContext, List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList, ButtonClickListener listener, boolean isLocalSQL) {
        super();
        this.mContext = mContext;
        this.mList = mList;
        this.listener = listener;
        this.isLocalSQL = isLocalSQL;
    }

    public void setDate(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList, boolean isLocalSQL) {
        this.mList = mList;
        this.isLocalSQL = isLocalSQL;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE_BOTTOM && getContentItemCount() != 0) {
            view = View.inflate(mContext, R.layout.delete_hository, null);
            return new BottomViewHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.customer_search_rightlist, null);
            return new MyViewHoder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BottomViewHolder) {
            BottomViewHolder myViewHolder = (BottomViewHolder) holder;
        } else {
            final MyViewHoder viewHoder = (MyViewHoder) holder;
//        StringBuilder tempStr = new StringBuilder();
//        for (int i = 0; i < mList.get(position).getAccessoriesList().size(); i++) {
//            if (!tempStr.toString().contains("+")) {
//                tempStr.append(mList.get(position).getAccessoriesList().get(i).getFoodName());
//            } else {
//                tempStr.append("+" + mList.get(position).getAccessoriesList().get(i).getFoodName());
//            }
//        }
            if (!TextUtils.isEmpty(mList.get(position).getImagesURL1())) {
                Picasso.with(mContext)
                        .load(mList.get(position).getImagesURL1())
                        .placeholder(R.mipmap.food_icon_default)
                        .error(R.mipmap.food_icon_default)
                        .into(viewHoder.addlunchRightIcon);
            }
            if (isLocalSQL) {
                viewHoder.addlunchDishesIngredients.setText("配料：" + mList.get(position).getMainFood());
            } else {
                if (null != mList.get(position).getAccessoriesList() && mList.get(position).getAccessoriesList().size() > 0) {
                    viewHoder.addlunchDishesIngredients.setText("配料：" + mList.get(position).getIngredients().getFoodName() + "+" + mList.get(position).getAccessoriesList().get(0).getFoodName());
                } else {
                    viewHoder.addlunchDishesIngredients.setText("配料：" + mList.get(position).getIngredients().getFoodName());
                } //不能用这种方式，这个取的是我本地数据库，没有那么多字段
            }
            if (mList.get(position).getFoodNums() == 0) {
                viewHoder.addlunchDishesNum.setVisibility(View.GONE);
                viewHoder.addlunchReducedish.setVisibility(View.GONE);
            } else {
                viewHoder.addlunchDishesNum.setVisibility(View.VISIBLE);
                viewHoder.addlunchReducedish.setVisibility(View.VISIBLE);
                viewHoder.addlunchDishesNum.setText(mList.get(position).getFoodNums() + "");
            }
            viewHoder.addlunchDishesName.setText(mList.get(position).getDishesName());
//        viewHoder.addlunchRightIcon.setImageURI(Uri.parse(mList.get(position).getImagesURL()));
            viewHoder.addlunchAdddish.setImageResource(mList.get(position).getFoodNums() > 0 ? R.mipmap.add_dish_pressed : R.mipmap.add_dishes_normal);
            viewHoder.addlunchAdddish.setOnClickListener(new MyOnClickListener(position, viewHoder.addlunchDishesNum));
            viewHoder.addlunchReducedish.setOnClickListener(new MyOnClickListener(position, viewHoder.addlunchReducedish));
            viewHoder.rootView.setOnClickListener(new MyOnClickListener(position, null));

//            viewHoder.input_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    LogUtils.i("输入的内容是==" + viewHoder.input_weight.getText().toString());
//                    if (!hasFocus && !TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
//                        viewHoder.input_weight.setText(viewHoder.input_weight.getText().toString());
//                        bean.setWeight(viewHoder.input_weight.getText().toString());
//                        bean.setDishesCode(mList.get(position).getDishesCode());
//                        bean.setDishesName(mList.get(position).getDishesName());
//                        bean.setImageUrl(mList.get(position).getImagesURL());
//                        bean.setWayType("1");
//                        if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
//                            mList.get(position).setWeight(viewHoder.input_weight.getText().toString());
//                            ((AddLunchActivity) mContext).addFood(bean);
//                        }
//                    }
//                }
//            });
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
                        LogUtils.i("输入的内容是==" + viewHoder.input_weight.getText().toString());
//                if (!TextUtils.isEmpty(myViewHoder.input_weight.getText().toString())) {
                        for (int i = 0; i < ((AddLunchActivity) mContext).getAddMealList().size(); i++) {
                            if (((AddLunchActivity) mContext).getAddMealList().get(i).equals(setBean(position))) {
                                ((AddLunchActivity) mContext).getAddMealList().get(i).setWeight(viewHoder.input_weight.getText().toString());
                            }
                        }
                    }
//            }
                }
            });
            if (mList.get(position).isShowWeight()) {
                viewHoder.weightlayout.setVisibility(View.VISIBLE);
//                if (!viewHoder.input_weight.hasFocus()) {
//                    viewHoder.input_weight.setFocusable(true);
//                    viewHoder.input_weight.requestFocus();
//                }
                if (!TextUtils.isEmpty(viewHoder.input_weight.getText().toString())) {
                    viewHoder.input_weight.setText(viewHoder.input_weight.getText().toString());
                }

            } else {
                viewHoder.weightlayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount()==0?0:getContentItemCount() + mBottomCount;
    }

    //内容长度
    public int getContentItemCount() {
        return mList == null ? 0 : mList.size();
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
        View view;

        public MyOnClickListener(int position, View view) {
            this.view = view;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addlunch_adddish:
                    if (view != null)
                        CommonUtils.propertyValuesHolder(view);
                    if (!((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        ((AddLunchActivity) mContext).addFood(setBean(position),true);
                    } else {
                        int tempIndex = ((AddLunchActivity) mContext).getAddMealList().indexOf(setBean(position));
                        ((AddLunchActivity) mContext).getAddMealList().get(tempIndex).setWeight(setBean(position).getWeight() + ((AddLunchActivity) mContext).getAddMealList().get(tempIndex).getWeight());//有的话，累加
                    }
//                    if (!isStoreSupply) {
//                        mList.get(position).setShowWeight(true);
//                    }
                    mList.get(position).setFoodNums(mList.get(position).getFoodNums() + 1);
                    notifyDataSetChanged();
                    break;
                case R.id.addlunch_reducedish:
                    mList.get(position).setFoodNums(mList.get(position).getFoodNums() - 1);
                    mList.get(position).setWeight(mList.get(position).getWeight());
                    ((AddLunchActivity) mContext).deleteFood(setBean(position));
                    notifyDataSetChanged();
                    break;
                case R.id.root_view:
                    Intent mIntent = new Intent(mContext, FoodDetailsActivity.class);
                    mIntent.putExtra("foodCode", mList.get(position).getDishesCode());
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
        bean.setWeight(mList.get(position).getWeight());
        bean.setDishesCode(mList.get(position).getDishesCode());
        bean.setDishesName(mList.get(position).getDishesName());
        bean.setImageUrl(mList.get(position).getImagesURL());
        bean.setImageUrl1(mList.get(position).getImagesURL1());
        bean.setWayType("2");
        return bean;
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
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

        public MyViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //底部 ViewHolder
    class BottomViewHolder extends RecyclerView.ViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.clear();
                    notifyDataSetChanged();
                    new RecordSQLiteOpenHelper(mContext).deleteAll(RecordSQLiteOpenHelper.STORESUPPLY_TABLE_NAME);
                    listener.clicked();
                }
            });
        }
    }

    public List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> getDate() {
        return mList;
    }
}

