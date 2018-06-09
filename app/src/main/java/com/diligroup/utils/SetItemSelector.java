package com.diligroup.utils;

import android.widget.ImageView;

import com.diligroup.R;

/**
 * Created by hjf on 2016/8/31.
 */
public class SetItemSelector {

    public static void setSelector(String foodName, boolean isClicked, ImageView iv_icon) {
        if (foodName.equals("谷类")) {

            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_gulei_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_gulei_normal);
            }
        } else if (foodName.equals("豆类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_dou_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_dou_normal);
            }
        } else if (foodName.equals("蔬菜类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_shucai_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_shucai_normal);
            }
        } else if (foodName.equals("水果类")) {

            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_fruit_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_fruit_normal);
            }
        } else if (foodName.equals("坚果类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_jianguo_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_jianguo_normal);
            }

        } else if (foodName.equals("乳制品类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_milk_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_milk_normal);
            }
        } else if (foodName.equals("蛋类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_egg_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_egg_normal);
            }
        } else if (foodName.equals("鱼类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_fish_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_fish_normal);
            }
        } else if (foodName.equals("海鲜类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_seafood_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_seafood_normal);
            }
        } else if (foodName.equals("调料类")) {
            if (isClicked) {
                iv_icon.setImageResource(R.drawable.iv_tiaoliao_pressed);
            } else {
                iv_icon.setImageResource(R.drawable.iv_tiaoliao_normal);
            }
        }
    }
}
