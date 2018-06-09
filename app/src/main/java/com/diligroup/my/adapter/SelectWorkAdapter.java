package com.diligroup.my.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseListAdapter;
import com.diligroup.bean.GetJobBean;
import com.diligroup.utils.LogUtils;
import com.diligroup.view.MyGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjf on 2016/9/1.
 */
public class SelectWorkAdapter extends BaseListAdapter {
    List<GetJobBean.ListBean> bean;

    String jobType;//用户选中的传递过来的职业信息
    String jobName;//职业名称
    SelectedChangeListener listener;

    public void setCurrentItem(String jobType, String jobName) {
        if (currentItem == null) {
            currentItem = new GetJobBean.ListBean();
            currentItem.setLaborCode(jobType);
            currentItem.setDictName(jobName);
        } else {
            currentItem.setLaborCode(jobType);
            currentItem.setDictName(jobName);
        }
    }

    GetJobBean.ListBean currentItem;//当前用户的职业
    WorkGridAdapter adapter;

    public SelectWorkAdapter(Context mContext, List<List<GetJobBean.ListBean>> workList, String jobType, String jobCode, SelectedChangeListener listener) {
        super(mContext, workList);
        this.jobType = jobType;
        this.jobName = jobCode;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder myViewHolder;

        if (contentView == null) {
            contentView = View.inflate(mContext, R.layout.my_select_work_item, null);
            myViewHolder = new ViewHolder(contentView);
            contentView.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) contentView.getTag();
        }

        List<GetJobBean.ListBean> beanList = (List<GetJobBean.ListBean>) mDatas.get(position);
        if (beanList.get(0).getDictName().contains("轻")) {
            myViewHolder.my_work_worktype.setText("轻体力");
            myViewHolder.my_work_remind.setText("75%时间坐或站立;25%时间站着活动");
        } else if (beanList.get(position).getDictName().contains("中")) {
            myViewHolder.my_work_worktype.setText("中等体力");
            myViewHolder.my_work_remind.setText("25%时间坐或站立;75%时间特殊活动");
        } else if (beanList.get(position).getDictName().contains("重")) {
            myViewHolder.my_work_remind.setText("40%时间坐或站立;60%时间特殊活动");
            myViewHolder.my_work_worktype.setText("重体力");
        }
        if (beanList.get(0).getLaborCode().equals(jobType)) {
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).getProfName().equals(jobName)) {
                    beanList.get(i).setSelected(true);
                }
            }
        }
//        if (adapter == null) {//不能复用，不然，只会展示第一条数据
            adapter = new WorkGridAdapter(mContext, (List<GetJobBean.ListBean>) mDatas.get(position));
            myViewHolder.myWorkGridView.setAdapter(adapter);
        myViewHolder.myWorkGridView.setOnItemClickListener(new MyOnItemClickListener(position));
        return contentView;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        int selectIndex;

        public MyOnItemClickListener(int selectIndex) {
            this.selectIndex = selectIndex;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            LogUtils.i("当前选中item==", ((List<GetJobBean.ListBean>) mDatas.get(selectIndex)).get(position).getProfName());
            for (int i = 0; i < ((List<GetJobBean.ListBean>) mDatas.get(selectIndex)).size(); i++) {
                if (i == position) {
                    ((List<GetJobBean.ListBean>) mDatas.get(selectIndex)).get(i).setSelected(true);
                    currentItem = ((List<GetJobBean.ListBean>) mDatas.get(selectIndex)).get(position);
                    listener.getCurrentItem(currentItem);
                    jobName =currentItem.getCode();
                } else {
                    ((List<GetJobBean.ListBean>) mDatas.get(selectIndex)).get(i).setSelected(false);
                    for (int g = 0; g < mDatas.size(); g++) {
                        if (g != selectIndex) {
                            for (int j = 0; j < ((List<GetJobBean.ListBean>) mDatas.get(g)).size(); j++) {
                                ((List<GetJobBean.ListBean>) mDatas.get(g)).get(j).setSelected(false);
                            }
                        }
                    }
                }
            }
            SelectWorkAdapter.this.notifyDataSetChanged();
        }
    }

    class ViewHolder {
        @Bind(R.id.gv_work)
        MyGridView myWorkGridView;//
        @Bind(R.id.my_work_remind)
        TextView my_work_remind;//对于不同程度劳动力的温馨提示语
        @Bind(R.id.my_work_worktype)
        TextView my_work_worktype;//劳动类型，中，轻，重，

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface SelectedChangeListener {
        void getCurrentItem(GetJobBean.ListBean bean);
    }
}
