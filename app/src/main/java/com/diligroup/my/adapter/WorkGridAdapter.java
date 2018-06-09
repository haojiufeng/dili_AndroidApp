package com.diligroup.my.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseListAdapter;
import com.diligroup.bean.GetJobBean;

import java.util.List;

/**
 * Created by hjf on 2016/8/25.
 */
public class WorkGridAdapter extends BaseListAdapter {
    public WorkGridAdapter(Context mContext, List<GetJobBean.ListBean> jobList) {
        super(mContext,jobList);
    }
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,R.layout.grid_item, null);
            holder.tv_work = (TextView) convertView.findViewById(R.id.tv_work_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GetJobBean.ListBean bean= (GetJobBean.ListBean) mDatas.get(position);
            holder.tv_work.setText(bean.getProfName());

        if(bean.isSelected()){
            holder.tv_work.setTextColor(mContext.getResources().getColor(R.color.title_color));
        }else{
            holder.tv_work.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return convertView;
    }
    public class ViewHolder {
        public TextView tv_work;
    }
}
