package com.diligroup.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RecyclerView.Adapter的基类（数据集为List）
 * Created by Zhang on 2016/1/6.
 */
public abstract class BaseListAdapter<T,T1> extends BaseAdapter {
    protected Context mContext ;
    protected List<T> mDatas;
    protected List<T1> mEntity;
    protected BaseListAdapter(Context context) {
        mContext = context;

    }
    protected BaseListAdapter(Context context,List<T> mDatas) {
        mContext = context;
       this.mDatas = mDatas;

    }
    protected BaseListAdapter(Context context,List<T> mDatas,List<T1> mEntity) {
        mContext = context;
        this.mDatas = mDatas;
        this.mEntity = mEntity;
    }

    protected BaseListAdapter() {
    }


    /***
     * 带泛型的简单名称的findViewById
     *
     * @param view
     * @param resId
     * @param <T>
     * @return
     */
    protected <T> T $(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    protected  <T extends View> T inflateView(int resId) {
        return (T) LayoutInflater.from(mContext).inflate(resId, null);
    }


    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //在Adapter里CRUD就不需要在外部notifyDataSetChanged了。
    public void setData(List<T> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public void setData(T[] data) {
        mDatas = Arrays.asList(data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<T>()) : mDatas;
    }

    public void addData(List<T> data) {
        if (mDatas != null && data != null && !data.isEmpty()) {
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addItem(T obj) {
        if (mDatas != null) {
            mDatas.add(obj);
        }
        notifyDataSetChanged();
    }

    public void addItem(int pos, T obj) {
        if (mDatas != null) {
            mDatas.add(pos, obj);
        }
        notifyDataSetChanged();
    }

    public void removeItem(Object obj) {
        mDatas.remove(obj);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

}

