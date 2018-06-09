package com.diligroup.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetShopBean;
import com.diligroup.bean.MyItemClickListener;

import java.util.List;

/**
 * Created by hjf on 2016/8/28.
 * x
 */
public class SelectAllShopAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<GetShopBean.StoreCustListBean> mList;
    MyItemClickListener listener;
    private int currentPosition;//当前选中的position

    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    //上拉加载更多状态-默认为0
    private int load_more_status=0;

    public SelectAllShopAdapter(Context mContext, List<GetShopBean.StoreCustListBean> mList, MyItemClickListener listener) {
        super();
        this.mContext = mContext;
        this.mList = mList;
        this.listener = listener;
    }

    public void setDate(List<GetShopBean.StoreCustListBean> mlist) {
        this.mList = mlist;
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        if (viewType == TYPE_ITEM) {
            view = View.inflate(mContext, R.layout.item_shop, null);
            return new MyHomeViewHoder(view);
//        }
//        } else {
//            view = View.inflate(mContext, R.layout.recycler_load_more_layout, null);
//            return new MyFooterViewHoder(view);
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHomeViewHoder) {
            MyHomeViewHoder viewHoder = (MyHomeViewHoder) holder;
            viewHoder.shopName.setText(mList.get(position).getName());
            viewHoder.shopDetails.setText("地址：" + mList.get(position).getAddress());
            viewHoder.rootView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    listener.onItemClick(view, position);
                }
            });
        }else if(holder instanceof MyFooterViewHoder){
            MyFooterViewHoder footerViewHoder= (MyFooterViewHoder) holder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                    footerViewHoder.loadMore.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footerViewHoder.loadMore.setText("正在加载更多数据...");
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyHomeViewHoder extends RecyclerView.ViewHolder {
        public TextView shopName;
        public TextView shopDetails;
        public View rootView;

        public MyHomeViewHoder(View itemView) {
            super(itemView);
            shopName = (TextView) itemView.findViewById(R.id.tv_shopname);
            shopDetails = (TextView) itemView.findViewById(R.id.tv_shop_details);
            rootView = itemView.findViewById(R.id.select_all_root);
        }
    }

    public class MyFooterViewHoder extends RecyclerView.ViewHolder {
        public TextView loadMore;
        public MyFooterViewHoder(View itemView) {
            super(itemView);
            loadMore = (TextView) itemView.findViewById(R.id.recycler_loadmore);
        }
    }
    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     * @param status
     */
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }
}
