package com.diligroup.after.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetDietRecordBean;
import com.diligroup.utils.StringUtils;
import com.diligroup.view.stickyListView.StickyListHeadersAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加三餐首页list adapter
 * Created by hjf on 2016/8/28.
 */
public class AfterFragmentAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    Context mContext;
    List<GetDietRecordBean.MealBean> mlist = new ArrayList<>();

    public AfterFragmentAdapter(Context mContext, GetDietRecordBean bean) {
        this.mContext = mContext;
        initList(bean);
    }

    public void setDate(GetDietRecordBean bean) {
//        this.mlist = mlist;
        initList(bean);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
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
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.food_after_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_food_name);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tv_Kcal = (TextView) convertView.findViewById(R.id.tv_food_kcal);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_after_icon);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(mlist.get(position).getImageUrl1())) {
            Picasso.with(mContext)
                    .load(mlist.get(position).getImageUrl1())
                    .error(R.mipmap.food_icon_default)
                    .into(holder.iv_icon);
        }
        holder.tv_name.setText(mlist.get(position).getDishesName());
        if (TextUtils.isEmpty(mlist.get(position).getWeight()) || (!TextUtils.isEmpty(mlist.get(position).getWeight())&&  Float.parseFloat(mlist.get(position).getWeight())==0.0)) {
            holder.tv_num.setText("适量");
            holder.tv_Kcal.setVisibility(View.GONE);
        } else {
            holder.tv_Kcal.setVisibility(View.VISIBLE);
            holder.tv_num.setText(mlist.get(position).getWeight() + "g");
            holder.tv_Kcal.setText(mlist.get(position).getEnergyKc() + "kcal");
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeadViewHolder headViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.after_header, null);
            headViewHolder = new HeadViewHolder();
            headViewHolder.after_mealname = (TextView) convertView.findViewById(R.id.after_mealname);
            headViewHolder.tv_total_kcal = (TextView) convertView.findViewById(R.id.tv_total_kcal);
            convertView.setTag(headViewHolder);
        } else {
            headViewHolder = (HeadViewHolder) convertView.getTag();
        }
        switch (mlist.get(position).getMealType()) {
            case 1://
                headViewHolder.after_mealname.setText("早餐");
                headViewHolder.tv_total_kcal.setText(StringUtils.DecimalTwoFloat(mornTotalKal) + "kcal");
                break;
            case 2://午餐
                headViewHolder.after_mealname.setText("午餐");
                headViewHolder.tv_total_kcal.setText(StringUtils.DecimalTwoFloat(afterTtotalKal) + "kcal");
                break;
            case 3://晚餐
                headViewHolder.after_mealname.setText("晚餐");
                headViewHolder.tv_total_kcal.setText(StringUtils.DecimalTwoFloat(evenTotalKal) + "kcal");
                break;
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mlist.get(position).getMealType();
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_num;
        ImageView iv_icon;
        TextView tv_Kcal;
    }

    class HeadViewHolder {
        TextView after_mealname;//早，中，晚餐
        TextView tv_total_kcal;//总热量
    }

    float mornTotalKal = 0;//早餐total能量
    float afterTtotalKal = 0;//午餐
    float evenTotalKal = 0;//晚餐

    /**
     * 整合集合
     *
     * @param dietRecordBean
     * @return
     */
    private List<GetDietRecordBean.MealBean> initList(GetDietRecordBean dietRecordBean) {
        mlist.clear();
        mornTotalKal = 0.0f;//早餐total能量
        afterTtotalKal = 0.0f;//午餐
        evenTotalKal = 0.0f;//晚餐;
        if (dietRecordBean.getMorn().size() > 0) {
            for (int i = 0; i < dietRecordBean.getMorn().size(); i++) {
                mornTotalKal += Float.parseFloat(dietRecordBean.getMorn().get(i).getEnergyKc());
            }
            mlist.addAll(dietRecordBean.getMorn());
        }
        if (dietRecordBean.getAfternoon().size() > 0) {
            for (int i = 0; i < dietRecordBean.getAfternoon().size(); i++) {
                afterTtotalKal += Float.parseFloat(dietRecordBean.getAfternoon().get(i).getEnergyKc());
//                dietRecordBean.getAfternoon().get(i).setTotalKal(totalKal + "");
            }
            mlist.addAll(dietRecordBean.getAfternoon());
        }
        if (dietRecordBean.getEven().size() > 0) {
            for (int i = 0; i < dietRecordBean.getEven().size(); i++) {
                evenTotalKal += Float.parseFloat(dietRecordBean.getEven().get(i).getEnergyKc());
            }
            mlist.addAll(dietRecordBean.getEven());
        }
        return mlist;
    }

}
