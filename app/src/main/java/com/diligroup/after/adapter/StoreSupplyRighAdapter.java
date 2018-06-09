package com.diligroup.after.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.after.AddLunchActivity;
import com.diligroup.bean.AddFoodCompleteBean;
import com.diligroup.bean.HomeStoreSupplyList;
import com.diligroup.home.FoodDetailsActivity;
import com.diligroup.utils.CommonUtils;
import com.diligroup.view.stickyListView.StickyListHeadersAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjf on 2016/7/14.
 */
public class StoreSupplyRighAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    Context mContext;
    List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean> mList;
    private List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean.DishesSupplyDtlListBean> rightDishesList = new ArrayList<>();//所有右侧成品分类列表

    public StoreSupplyRighAdapter(Context mContext, List<HomeStoreSupplyList.JsonBean.DishesSupplyListBean> mList) {
        super();
        this.mContext = mContext;
        this.mList = mList;
        initListDate();
    }

    private void initListDate() {
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
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeadViewHolder headViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.stickview_home_head, null);
            headViewHolder = new HeadViewHolder();
            headViewHolder.sticklist_headtext = (TextView) convertView.findViewById(R.id.sticklist_headtext);
            headViewHolder.top_head_line = convertView.findViewById(R.id.top_head_line);
            convertView.setTag(headViewHolder);
        } else {
            headViewHolder = (HeadViewHolder) convertView.getTag();
        }
        headViewHolder.sticklist_headtext.setText(rightDishesList.get(position).getHeaderName());
        headViewHolder.top_head_line.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        if (position != rightDishesList.size()) {
            return Long.parseLong(rightDishesList.get(position).getHeaderCode());
        }
        return Long.parseLong(rightDishesList.get(position - 1).getHeaderCode());
    }

    @Override
    public int getCount() {
        return rightDishesList == null ? 0 : rightDishesList.size();
    }

    @Override
    public Object getItem(int i) {
        return rightDishesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHoder myViewHoder;
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
            myViewHoder.root_view = (RelativeLayout) convertView.findViewById(R.id.root_view);
            convertView.setTag(myViewHoder);
        } else {
            myViewHoder = (MyViewHoder) convertView.getTag();
        }
        Picasso.with(mContext)
                .load(rightDishesList.get(position).getImagesURL1())
                .error(R.mipmap.food_icon_default)
                .into(myViewHoder.addlunchRightIcon);
        StringBuilder tempStr = new StringBuilder();
//        for (int i = 0; i < rightDishesList.get(position).getAccessoriesList().size(); i++) {
//            if (TextUtils.isEmpty(tempStr.toString())) {
//                tempStr.append(rightDishesList.get(position).getAccessoriesList().get(i).getFoodName());
//            } else {
//                tempStr.append("+" + rightDishesList.get(position).getAccessoriesList().get(i).getFoodName());
//            }
//        }
        if (rightDishesList.get(position).getAccessoriesList().size() > 0) {
            myViewHoder.addlunchDishesIngredients.setText("配料：" + rightDishesList.get(position).getIngredients().getFoodName() + "+" + rightDishesList.get(position).getAccessoriesList().get(0).getFoodName());
        } else {
            myViewHoder.addlunchDishesIngredients.setText("配料：" + rightDishesList.get(position).getIngredients().getFoodName());
        }
        myViewHoder.addlunchDishesName.setText(rightDishesList.get(position).getDishesName());
        myViewHoder.addlunchAdddish.setImageResource(rightDishesList.get(position).getFoodNums() > 0 ? R.mipmap.add_dish_pressed : R.mipmap.add_dishes_normal);

        if (rightDishesList.get(position).getFoodNums() > 0) {
            myViewHoder.addlunchDishesNum.setVisibility(View.VISIBLE);
            myViewHoder.addlunchReducedish.setVisibility(View.VISIBLE);
            myViewHoder.addlunchDishesNum.setText(rightDishesList.get(position).getFoodNums() + "");
        } else {
            myViewHoder.addlunchDishesNum.setVisibility(View.INVISIBLE);
            myViewHoder.addlunchReducedish.setVisibility(View.INVISIBLE);
        }

        myViewHoder.addlunchAdddish.setOnClickListener(new MyOnClickListener(position, myViewHoder.addlunchDishesNum));
        myViewHoder.addlunchReducedish.setOnClickListener(new MyOnClickListener(position, null));
        myViewHoder.root_view.setOnClickListener(new MyOnClickListener(position, null));
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
        View root_view;//布局
//
//
//        public MyViewHoder() {
//            ButterKnife.bind(this);
//        }
    }

    class HeadViewHolder {
        TextView sticklist_headtext;
        View top_head_line;
    }

    class MyOnClickListener implements View.OnClickListener {
        int position;
        View view;

        public MyOnClickListener(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addlunch_adddish:
                    if (view != null)
                        CommonUtils.propertyValuesHolder(view);
                    rightDishesList.get(position).setFoodNums(rightDishesList.get(position).getFoodNums() + 1);
                    ((AddLunchActivity) mContext).addFood(setBean(position), true);
                    notifyDataSetChanged();
                    break;
                case R.id.addlunch_reducedish:
                    rightDishesList.get(position).setFoodNums(rightDishesList.get(position).getFoodNums() - 1);
                    if (rightDishesList.get(position).getFoodNums() > 1) {//修改weight值就行
                        ((AddLunchActivity) mContext).addFood(setBean(position), false);
                    } else {
                        ((AddLunchActivity) mContext).deleteFood(setBean(position));
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.root_view:
                    Intent mIntent = new Intent(mContext, FoodDetailsActivity.class);
                    mIntent.putExtra("foodCode", rightDishesList.get(position).getDishesCode());
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
     * @return
     */
    private AddFoodCompleteBean setBean(int position) {
        AddFoodCompleteBean bean = new AddFoodCompleteBean();
        bean.setWeight(rightDishesList.get(position).getWeight());
        bean.setDishesCode(rightDishesList.get(position).getDishesCode());
        bean.setDishesName(rightDishesList.get(position).getDishesName());
        bean.setImageUrl(rightDishesList.get(position).getImagesURL());
        bean.setImageUrl1(rightDishesList.get(position).getImagesURL1());
        bean.setWayType("2");
        return bean;
    }
}