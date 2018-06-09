package com.diligroup.after.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.home.FoodDetailsActivity;
import com.diligroup.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hjf on 2016/7/14.
 * 自定义菜品适配器
 */
public class CustomeRighAdapter extends BaseAdapter {
    Context mContext;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList;
    public CustomeRighAdapter(Context mContext, List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setDate(List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final MyViewHoder myViewHoder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.addlunch_rightlist, null);
            myViewHoder = new MyViewHoder();
            myViewHoder.addlunchRightIcon = (ImageView) convertView.findViewById(R.id.addlunch_right_icon);
            myViewHoder.addlunchDishesName = (TextView) convertView.findViewById(R.id.addlunch_dishes_name);
            myViewHoder.addlunchDishesIngredients = (TextView) convertView.findViewById(R.id.addlunch_dishes_ingredients);

            myViewHoder.addlunchGramsNum = (TextView) convertView.findViewById(R.id.addlunch_grams_num);
            myViewHoder.addlunchReducedish = (ImageView) convertView.findViewById(R.id.addlunch_reducedish);
            myViewHoder.addlunchAdddish = (ImageView) convertView.findViewById(R.id.addlunch_adddish);
            myViewHoder.addlunchDishesNum = (TextView) convertView.findViewById(R.id.addlunch_dishes_num);

            myViewHoder.weightlayout = convertView.findViewById(R.id.weightlayout);
            myViewHoder.root_view = convertView.findViewById(R.id.root_view);
            myViewHoder.input_weight = (EditText) convertView.findViewById(R.id.input_weight);
            convertView.setTag(myViewHoder);
        } else {
            myViewHoder = (MyViewHoder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(mList.get(position).getImagesURL1())) {
            Picasso.with(mContext)
                    .load(mList.get(position).getImagesURL1())
                    .error(R.mipmap.food_icon_default)
                    .into(myViewHoder.addlunchRightIcon);
        }
        myViewHoder.addlunchDishesName.setText(mList.get(position).getDishesName());
        if (mList.get(position).getAccessoriesList().size() > 0) {
            myViewHoder.addlunchDishesIngredients.setText("配料：" + mList.get(position).getIngredients().getFoodName() + "+" + mList.get(position).getAccessoriesList().get(0).getFoodName());
        } else {
            myViewHoder.addlunchDishesIngredients.setText("配料：" + mList.get(position).getIngredients().getFoodName());
        }
        if (mList.get(position).getFoodNums() > 0) {
            myViewHoder.addlunchDishesNum.setVisibility(View.VISIBLE);
            myViewHoder.addlunchReducedish.setVisibility(View.VISIBLE);
            myViewHoder.addlunchDishesNum.setText(mList.get(position).getFoodNums() + "");
        } else {
            myViewHoder.addlunchDishesNum.setVisibility(View.GONE);
            myViewHoder.addlunchReducedish.setVisibility(View.GONE);
        }
        myViewHoder.addlunchAdddish.setOnClickListener(new MyOnClickListener(position, myViewHoder));
        myViewHoder.addlunchReducedish.setOnClickListener(new MyOnClickListener(position, myViewHoder));
        myViewHoder.root_view.setOnClickListener(new MyOnClickListener(position, myViewHoder));
        myViewHoder.input_weight.setText(mList.get(position).getWeight());


        myViewHoder.input_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    myViewHoder.input_weight.addTextChangedListener(new MyTextWatcher(position,myViewHoder));
//                    LogUtils.i("当前position==" + position+"=="+hasFocus);
                }else{
//                    LogUtils.i("当前position==" + position+"=="+hasFocus);
                }
            }
        });
        myViewHoder.input_weight.setTag(position);
        if (mList.get(position).isShowWeight()) {
            myViewHoder.weightlayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(myViewHoder.input_weight.getText().toString())) {
                myViewHoder.input_weight.setText(myViewHoder.input_weight.getText().toString());
            }
            if (!TextUtils.isEmpty(mList.get(position).getWeight()) && Float.parseFloat(mList.get(position).getWeight()) > 0.0) {
                myViewHoder.input_weight.setText(mList.get(position).getWeight());
            }
        } else {
            myViewHoder.weightlayout.setVisibility(View.GONE);
        }

        return convertView;
    }
    class MyViewHoder {
        ImageView addlunchRightIcon;//右侧icon
        TextView addlunchDishesName;//食物名称
        TextView addlunchDishesIngredients;//食品配料

        TextView addlunchGramsNum;//克数
        ImageView addlunchReducedish;//“-”
        TextView addlunchDishesNum;//食物数量

        ImageView addlunchAdddish;//“+”
        View root_view;
        View weightlayout;
        EditText input_weight;
    }
    class MyTextWatcher implements TextWatcher {
        int position;
        MyViewHoder myViewHoder;

        public MyTextWatcher(int position, MyViewHoder myViewHoder) {
            this.position = position;
            this.myViewHoder = myViewHoder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable) && editable.toString().length() > 0) {
//                LogUtils.i("输入的内容是==" + myViewHoder.input_weight.getText().toString());
//                LogUtils.i("当前position==" + position+"=="+myViewHoder.input_weight.isActivated());
//                LogUtils.i("当前position==" + position+"=="+myViewHoder.input_weight.isInEditMode());
                mList.get(position).setWeight(myViewHoder.input_weight.getText().toString());
                if (!TextUtils.isEmpty(myViewHoder.input_weight.getText().toString())) {
                    if (!((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        ((AddLunchActivity) mContext).getAddMealList().add(setBean(position));
                    }
                    for (int i = 0; i < ((AddLunchActivity) mContext).getAddMealList().size(); i++) {
                        if (((AddLunchActivity) mContext).getAddMealList().get(i).equals(setBean(position))) {
                            int num = Integer.parseInt(TextUtils.isEmpty(myViewHoder.addlunchDishesNum.getText().toString()) ? "0" : myViewHoder.addlunchDishesNum.getText().toString());
                            float tempWeight = Float.parseFloat(myViewHoder.input_weight.getText().toString());
                            ((AddLunchActivity) mContext).getAddMealList().get(i).setWeight(tempWeight * num + "");
                        }
                    }
                }
            } else {//编辑框为空情况
                for (int i = 0; i < ((AddLunchActivity) mContext).getAddMealList().size(); i++) {
                    if (((AddLunchActivity) mContext).getAddMealList().get(i).equals(setBean(position))) {
                        ((AddLunchActivity) mContext).getAddMealList().get(i).setWeight("");
                    }
                }
            }
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        int position;
        MyViewHoder viewHolder;

        public MyOnClickListener(int position, MyViewHoder viewHolder) {
            this.position = position;
//            this.view = view;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addlunch_adddish:
                    if (viewHolder.addlunchDishesNum != null)
                        CommonUtils.propertyValuesHolder(viewHolder.addlunchDishesNum);
                    //不管编辑框是否为空，添加进去，后续改变输入值，然后做对应的set
                    if (!((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        ((AddLunchActivity) mContext).getAddMealList().add(setBean(position));
                    } else if (((AddLunchActivity) mContext).getAddMealList().contains(setBean(position))) {
                        int tempIndex = ((AddLunchActivity) mContext).getAddMealList().indexOf(setBean(position));
                        float tempWeight = Float.parseFloat(TextUtils.isEmpty(setBean(position).getWeight()) ? "0.0f" : setBean(position).getWeight());
                        String temp = ((AddLunchActivity) mContext).getAddMealList().get(tempIndex).getWeight();
                        float currentWeight = Float.parseFloat(TextUtils.isEmpty(temp) ? "0.0f" : temp);
                        ((AddLunchActivity) mContext).getAddMealList().get(tempIndex).setWeight(tempWeight + currentWeight + "");//有的话，累加
                    }
                    mList.get(position).setFoodNums(mList.get(position).getFoodNums() + 1);
                    viewHolder.addlunchDishesNum.setText(mList.get(position).getFoodNums()+"");
                    mList.get(position).setShowWeight(true);
                    viewHolder.addlunchReducedish.setVisibility(View.VISIBLE);
                    viewHolder.addlunchDishesNum.setVisibility(View.VISIBLE);
                    viewHolder.weightlayout.setVisibility(View.VISIBLE);
//                    viewHolder.input_weight.setFocusable(true);
//                    if (TextUtils.isEmpty(viewHolder.addlunchDishesNum.getText())) {
//                        viewHolder.addlunchDishesNum.setText("1");
//                    } else {
//                        viewHolder.addlunchDishesNum.setText(Integer.parseInt(viewHolder.addlunchDishesNum.getText().toString()) + 1 + "");
//                    }
                    break;
                case R.id.addlunch_reducedish:
                    mList.get(position).setFoodNums(mList.get(position).getFoodNums() - 1);
                    viewHolder.addlunchDishesNum.setText(Integer.parseInt(viewHolder.addlunchDishesNum.getText().toString()) - 1 + "");
                    if (mList.get(position).getFoodNums() > 0 && !TextUtils.isEmpty(viewHolder.input_weight.getText().toString())) {//修改weight值就行
                        ((AddLunchActivity) mContext).addFood(setBean(position), false);
                    } else if (mList.get(position).getFoodNums() <= 0) {
                        viewHolder.addlunchReducedish.setVisibility(View.GONE);
                        viewHolder.addlunchDishesNum.setVisibility(View.GONE);
                        viewHolder.weightlayout.setVisibility(View.GONE);
                        mList.get(position).setShowWeight(false);
                        viewHolder.input_weight.setText("");
                        if (!TextUtils.isEmpty(mList.get(position).getWeight())) {
                            mList.get(position).setWeight("");
                        }
                        ((AddLunchActivity) mContext).deleteFood(setBean(position));
                    }
                    break;
                case R.id.root_view://右侧item的点击事件，展示编辑框
                    Intent mIntent = new Intent(mContext, FoodDetailsActivity.class);
                    mIntent.putExtra("foodCode", mList.get(position).getDishesCode());
                    mContext.startActivity(mIntent);
                    break;
//
                default:
                    break;
            }
        }
    }

    private AddFoodCompleteBean setBean(int position) {
        AddFoodCompleteBean bean = new AddFoodCompleteBean();
        bean.setWeight(mList.get(position).getWeight());
        bean.setDishesCode(mList.get(position).getDishesCode());
        bean.setDishesName(mList.get(position).getDishesName());
        bean.setImageUrl(mList.get(position).getImagesURL());
        bean.setImageUrl1(mList.get(position).getImagesURL1());
        bean.setFoodNums(mList.get(position).getFoodNums());
        bean.setWayType("1");
        return bean;
    }
}