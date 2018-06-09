package com.diligroup.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetAllergyDetailBean;
import com.diligroup.bean.MyItemClickListener;
import com.diligroup.utils.SetItemSelector;

import java.util.List;

/**
 *大分类 adapter
 * Created by Administrator on 2016/8/12.
 */
public class AllergyAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<GetAllergyDetailBean.ListBigBean> mlist;
    MyItemClickListener listener;
    public AllergyAdapter(Context mContext, List<GetAllergyDetailBean.ListBigBean> mlist, MyItemClickListener listener) {
        super();
        this.mContext = mContext;
        this.mlist = mlist;
        this.listener=listener;
        initDate(mlist);
    }

    private void initDate(List<GetAllergyDetailBean.ListBigBean> mlist) {
        if (mlist != null && mlist.size() > 0) {
            selectPosion(0);
        }
    }

    /**
     * 指定哪一个被选中
     *
     * @param position
     */
    public void selectPosion(int position) {
        for (int i = 0; i < mlist.size(); i++) {
            if (i == position) {
                mlist.get(i).setSelected(true);
            } else {
                mlist.get(i).setSelected(false);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.addlunch_child, null);
        return new MyHomeViewHoder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHomeViewHoder viewHoder = (MyHomeViewHoder) holder;
        viewHoder.dishName.setText(mlist.get(position).getAllergyType1());
        if (mlist.get(position).isSelected()) {
            SetItemSelector.setSelector(mlist.get(position).getAllergyType1(), true, viewHoder.dishIcon);
            viewHoder.dishName.setTextColor(mContext.getResources().getColor(R.color.green));
            viewHoder.left.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHoder.rightLine.setVisibility(View.GONE);
        } else {
            SetItemSelector.setSelector(mlist.get(position).getAllergyType1(), false, viewHoder.dishIcon);
            viewHoder.dishName.setTextColor(mContext.getResources().getColor(R.color.black1));
            viewHoder.left.setBackgroundColor(mContext.getResources().getColor(R.color.gray_f0f0f0));
            viewHoder.rightLine.setVisibility(View.VISIBLE);
        }
        viewHoder.left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectPosion(position);
                notifyDataSetChanged();
                listener.onItemClick(view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }
    public class MyHomeViewHoder extends RecyclerView.ViewHolder {
        public ImageView dishIcon;
        public TextView dishName;
        public View rightLine;
        public LinearLayout left;

        public MyHomeViewHoder(View itemView) {
            super(itemView);
            dishIcon = (ImageView) itemView.findViewById(R.id.dish_icon);
            dishName = (TextView) itemView.findViewById(R.id.dish_name);
            rightLine = itemView.findViewById(R.id.right_line);
            left = (LinearLayout) itemView.findViewById(R.id.left);
        }
    }
}
