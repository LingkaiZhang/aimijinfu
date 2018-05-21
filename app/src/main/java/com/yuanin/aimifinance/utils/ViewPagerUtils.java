package com.yuanin.aimifinance.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager工具类, 用于提供ViewPager相关的公用方法
 */
public class ViewPagerUtils {

    // 获取ViewPager的TabIndicator列表
    public static List<TabIndicatorEntity> getTabIndicator(Integer number) {
        List<TabIndicatorEntity> list = new ArrayList<TabIndicatorEntity>();
        for (int i = 0; i < number; i++) {
            TabIndicatorEntity indicator = new TabIndicatorEntity();
            indicator.type = i;
            list.add(indicator);
        }
        return list;
    }

    /**
     * 变换底部选项卡颜色 应用于登录后主界面
     *
     * @param context
     * @param index
     * @param textViews
     */
    public static void changeTextViewStyle_Main(Context context, int index, List<TextView> textViews) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                TextView tv_text = textViews.get(i);
                tv_text.setTextColor(context.getResources().getColor(R.color.theme_color));
                switch (i) {
                    case 0:
                        Drawable topDrawable = context.getResources().getDrawable(R.mipmap.index_select);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable, null, null);
                        break;
                    case 1:
                        Drawable topDrawable1 = context.getResources().getDrawable(R.mipmap.finance_select);
                        topDrawable1.setBounds(0, 0, topDrawable1.getMinimumWidth(), topDrawable1.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable1, null, null);
                        break;
                    case 2:
                        Drawable topDrawable2 = context.getResources().getDrawable(R.mipmap.mine_select);
                        topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable2, null, null);
                        break;
                }
            } else {
                TextView tv_text = textViews.get(i);
                tv_text.setTextColor(context.getResources().getColor(R.color.text_gray));
                switch (i) {
                    case 0:
                        Drawable topDrawable = context.getResources().getDrawable(R.mipmap.index);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable, null, null);
                        break;
                    case 1:
                        Drawable topDrawable1 = context.getResources().getDrawable(R.mipmap.finance);
                        topDrawable1.setBounds(0, 0, topDrawable1.getMinimumWidth(), topDrawable1.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable1, null, null);
                        break;
                    case 2:
                        Drawable topDrawable2 = context.getResources().getDrawable(R.mipmap.mine);
                        topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                        tv_text.setCompoundDrawables(null, topDrawable2, null, null);
                        break;
                }
            }
        }
    }

    /**
     * 变换底部选项卡颜色 应用于理财界面
     *
     * @param context
     * @param index
     * @param textViews
     */
    public static void changeTextViewStyle_FINANCE(Context context, int index, List<TextView> textViews) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                TextView tv_text = textViews.get(i);
                tv_text.setTextColor(context.getResources().getColor(R.color.theme_color));
            } else {
                TextView tv_text = textViews.get(i);
                tv_text.setTextColor(context.getResources().getColor(R.color.text_black));
            }
        }
    }
}