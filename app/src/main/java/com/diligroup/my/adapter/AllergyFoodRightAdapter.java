package com.diligroup.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetAllergyDetailBean;

import java.util.List;

/**
 * Created by hjf on 2016/8/29.
 */
public class AllergyFoodRightAdapter extends BaseAdapter {
    List<GetAllergyDetailBean.ListBigBean.ListBean> foodList;
    Context mContext;
    LayoutInflater mInflater;
//    String selectedName;
    public AllergyFoodRightAdapter(Context mContext, List<GetAllergyDetailBean.ListBigBean.ListBean> foodList) {
        this.foodList = foodList;
        this.mContext=mContext;
//        this.selectedName=selectedName;
        mInflater = LayoutInflater.from(mContext);
    }
    public void setDate( List<GetAllergyDetailBean.ListBigBean.ListBean> foodList){
        this.foodList=foodList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foodList == null ? 0 : foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_food_detail, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_nzw_name);
            holder.food_Check = (CheckBox) convertView.findViewById(R.id.cb_item_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if(selectedName!=null && selectedName.length()>1){
//        for(int i=0;i<selectedName.split(",").length;i++){
//            if(selectedName.split(",")[i].equals(foodList.get(position).getAllergyName())){
//                foodList.get(position).setSelected(true);
//            }
//        }
//
//        }
        if(foodList.get(position).isSelected()){
            holder.food_Check.setChecked(true);
        }else {
            holder.food_Check.setChecked(false);
        }
        holder.title.setText(foodList.get(position).getAllergyName());
//        holder.id = String.valueOf(foodList.get(position).getId());
        return convertView;
    }
    //ViewHolder静态类
    static class ViewHolder {
        public TextView title;
        public CheckBox food_Check;
//        public String id;
    }
}
