package com.diligroup.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetJiaoQinBean;

import java.util.ArrayList;
import java.util.List;
/*
饮食禁忌adapter
 */
public class ListitemAdapter extends BaseAdapter {

	private List<GetJiaoQinBean.ListBean> mList;
	private Context mContext;
	private LayoutInflater mInflater;
//	private static HashMap<Integer,Boolean> isSelected;//用来控制checkBox的选中情况
	List<String>  selectList;
//	String[] tabooName;//饮食禁忌code码
	public ListitemAdapter(Context context,List<GetJiaoQinBean.ListBean> list){
		mList = list;
		mContext = context;
//		if(!TextUtils.isEmpty(tabooName)){
//		this.tabooName=tabooName.split(",");
//		}
		selectList=new ArrayList<>();
		mInflater = LayoutInflater.from(context);
	}
	public void setDate(List<GetJiaoQinBean.ListBean> list){
		mList=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.jiaoqing_item, null);
            holder.tvName = (TextView)convertView.findViewById(R.id.tv_noeat_item);
			holder.cb= (CheckBox) convertView.findViewById(R.id.cb_noeat_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();  
        }  
           
        holder.tvName.setText(mList.get(position).getDictName());
		holder.foodId=mList.get(position).getCode();
//		if(null!=tabooName && tabooName.length>0){
//			for(int i=0;i<tabooName.length;i++){
//				if(tabooName[i].indexOf(mList.get(position).getDictName())!=-1){
//					mList.get(position).setChecked(true);
//				}
//			}
//		}
		if(mList.get(position).isChecked()){
			holder.cb.setChecked(true);
		}else{
			holder.cb.setChecked(false);
		}
		return convertView;
	}
	public class ViewHolder{
		public String foodId;
		public  TextView tvName;
		public CheckBox cb;
	};
}


