package com.diligroup.my.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.bean.GetJiaoQinBean;
import com.diligroup.utils.CommonUtils;
import com.diligroup.utils.UserManager;
import com.diligroup.view.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
饮食禁忌adapter
 */
public class OtherRequestAdapter extends BaseAdapter {

    private List<GetJiaoQinBean.ListBean> mList;
    private Context mContext;
    private boolean isOver;//是否目标体重超过了当前体重
    private LayoutInflater mInflater;
    private static HashMap<Integer, Boolean> isSelected;//用来控制checkBox的选中情况
    List<String> selectList;
//    private boolean is_first = true;

    private String currentWeight;//当前体重字符串
    float targetWeight;//目标体重字符串
    int beginWeight = 40;
    OnWeightChangeListener listener;//监听体重变化

    public OtherRequestAdapter(Context context, List<GetJiaoQinBean.ListBean> list, OnWeightChangeListener listener, float targetWeight) {
        mList = list;
        mContext = context;
        this.listener = listener;
        this.targetWeight = targetWeight;
        selectList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setDate(List<GetJiaoQinBean.ListBean> list) {
        mList = list;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.other_request_item, null);
            holder = new ViewHolder(convertView);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_noeat_item);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_noeat_item);
            convertView.setTag(holder);
            constructRuler(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mList.get(position).getDictName());
        holder.foodId = mList.get(position).getCode();
        if (mList.get(position).isChecked()) {
            holder.cb.setChecked(true);
            if (mList.get(position).getCode().contains("250002") || mList.get(position).getDictName().contains("增重") || mList.get(position).getDictName().contains("增肌")) {//增肌，增重
                holder.add_fat_layout.setVisibility(View.GONE);
                holder.tv_zengji.setVisibility(View.VISIBLE);
            } else if (mList.get(position).getCode().contains("250001") || mList.get(position).getDictName().contains("减重") || mList.get(position).getDictName().contains("减脂")) {//减脂，减重
                holder.tv_zengji.setVisibility(View.GONE);
                holder.add_fat_layout.setVisibility(View.VISIBLE);
            } else {
                holder.tv_zengji.setVisibility(View.GONE);
                holder.add_fat_layout.setVisibility(View.GONE);
            }
        } else {
            holder.tv_zengji.setVisibility(View.GONE);
            holder.add_fat_layout.setVisibility(View.GONE);
            holder.cb.setChecked(false);
        }
        currentWeight = UserManager.getInstance().getNowWeight().replace("kg", "");
        holder.tv_weight_now.setText(currentWeight + "kg");
        holder.weightRuler.setSmoothScrollingEnabled(true);
// 初始化减脂文字提示

        if (!TextUtils.isEmpty(currentWeight) && targetWeight != 0) {
            int weeks = (int) (Float.parseFloat(currentWeight) - targetWeight);
            setWeightDate(holder, weeks);
        } else {
            if (UserManager.getInstance().getSex().equals("0")) {
                holder.target_weight.setText("50kg");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.weightRuler.scrollTo(10 * 20 * 10, 0);
                    }
                }, 500);
            } else if (UserManager.getInstance().getSex().equals("1")) {
                holder.target_weight.setText("70kg");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.weightRuler.scrollTo(30 * 20 * 10, 0);//(70-40)*20*10
                    }
                }, 500);
            }
        }
        holder.day_costHeat.setText("550kcal");
        if (holder.weightRuler.getHandler() == null)
            holder.weightRuler.setHandler(new Handler());
        holder.weightRuler.setOnScrollStateChangedListener(new MyHorizontalScrollView.ScrollViewListener() {

            @Override
            public void onScrollChanged(MyHorizontalScrollView.ScrollType scrollType) {
                if (scrollType == MyHorizontalScrollView.ScrollType.IDLE) {
                    final float scrollx = holder.weightRuler.getScrollX() / 20;
                    final float value = scrollx / 10;
//            //目标体重不能超过当前体重
                    if (beginWeight + value > Float.parseFloat(currentWeight)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int temp = ((int) (Float.parseFloat(currentWeight) - 40) * 20 * 10);
                                holder.weightRuler.scrollTo(temp, 0);//(70-40)*20*10
                            }
                        }, 500);
                        listener.getCurrentWeight(Float.parseFloat(currentWeight));
                        setWeightDate(holder,0);//weeks==0;,因为currentWeight==targetWeight
                    }
                }
            }
        });
        holder.weightRuler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        final float scrollx = holder.weightRuler.getScrollX() / 20;
                        final float value = scrollx / 10;
                        //目标体重不能超过当前体重
                        if (beginWeight + value <= Float.parseFloat(currentWeight)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("Value=========", String.valueOf(scrollx));
                                    Log.e("Double==Value=========", String.valueOf(value));
//                                    holder.target_weight.setText(String.valueOf(beginWeight
//                                            + value + "kg"));
                                    targetWeight=beginWeight+value;
                                    if (!TextUtils.isEmpty(currentWeight)) {
                                        int weeks = (int) (Float.parseFloat(currentWeight) - (beginWeight + value));
//                                        holder.suggestedTime.setText(weeks * 2 + "周（" + weeks * 7 * 2 + "）天");
//                                        holder.cost_totalHeat.setText(weeks * 7700 + "kcal");
                                        setWeightDate(holder,weeks);
                                    }
                                    listener.getCurrentWeight(beginWeight + value);
                                }
                            }, 500);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int temp = ((int) (Float.parseFloat(currentWeight) - 40) * 20 * 10);
                                    holder.weightRuler.scrollTo(temp, 0);//(70-40)*20*10
                                }
                            }, 500);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return false;
            }
        });
        return convertView;
    }

    /**
     * 设置减脂中的文字提示信息
     * @param holder
     * @param weeks
     */
    private void setWeightDate(final ViewHolder holder, int weeks) {
        holder.target_weight.setText(targetWeight + "kg");
        holder.tv_weight_now.setText(currentWeight + "kg");
        holder.suggestedTime.setText(weeks * 2 + "周（" + weeks * 7 * 2 + "）天");
        holder.cost_totalHeat.setText(weeks * 7700 + "kcal");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                float temp = ((targetWeight - 40) * 20 * 10);
                holder.weightRuler.scrollTo((int) temp, 0);//(70-40)*20*10

            }
        }, 500);
    }


    public class ViewHolder {
        @Bind(R.id.reduce_fat_layout)
        LinearLayout add_fat_layout;//选择增重时候展示的标尺view
        @Bind(R.id.tv_zengji)
        TextView tv_zengji;//选择减肌时候展示的提示文案
        @Bind(R.id.ll_ruler)
        LinearLayout ll_ruler;
        @Bind(R.id.tv_weight_now)
        TextView tv_weight_now;//当前体重
        @Bind(R.id.weight_target)
        TextView target_weight; //目标体重
        @Bind(R.id.tv_time_need)
        TextView suggestedTime;//建议用时
        @Bind(R.id.tv_cost_total)
        TextView cost_totalHeat;//需减总热量
        @Bind(R.id.tv_day_cost)
        TextView day_costHeat;// 每天需消耗热量
        @Bind(R.id.weightRuler)
        MyHorizontalScrollView weightRuler;// 标尺 scrollView
        public String foodId;
        public TextView tvName;
        public CheckBox cb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //绘制标尺
    private void constructRuler(final ViewHolder holder) {

        final View leftview = LayoutInflater.from(mContext).inflate(
                R.layout.blankhrulerunit, null);
        leftview.setLayoutParams(new ViewGroup.LayoutParams(CommonUtils.getScreenWidth(mContext) / 2,
                ViewGroup.LayoutParams.MATCH_PARENT));
        holder.ll_ruler.addView(leftview);
        for (int i = 0; i < 60; i++) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.hrulerunit, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(200,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            TextView tv = (TextView) view.findViewById(R.id.hrulerunit);
            tv.setText(String.valueOf(beginWeight + i));

            holder.ll_ruler.addView(view);
        }

    }

    public interface OnWeightChangeListener {
        //当前用户选择的目标体重
        float getCurrentWeight(float weight);
    }
}


