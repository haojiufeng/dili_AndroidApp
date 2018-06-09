package com.diligroup.view.wheelview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.diligroup.R;

/**
 * Created by hjf on 2016/10/25.
 */
public class MyWheelViewAdapter extends AbstractWheelTextAdapter {
    String[] list;

    public MyWheelViewAdapter(Context context, String[] list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.base_info_layout, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.baseinfo_text);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.length;
    }

    @Override
    public CharSequence getItemText(int index) {
        return list[index] + "";
    }
}
