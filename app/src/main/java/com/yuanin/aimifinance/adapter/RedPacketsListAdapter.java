package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.RedPacketsEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 15:41
 * @desc 红包list
 */
public class RedPacketsListAdapter extends GeneralAdapter<RedPacketsEntity> {

    public RedPacketsListAdapter(List<RedPacketsEntity> list, Context context) {
        super(context, list, R.layout.adapter_red_packets);
    }

    @Override
    public void convert(GeneralViewHolder holder, RedPacketsEntity data) {
        holder.setTextForTextView(R.id.tvMoney, String.valueOf(data.getAmount()));
        LinearLayout rlMain = holder.getView(R.id.rlMain);
        TextView tvMinMoneyTitle = holder.getView(R.id.tvMinMoneyTitle);
        TextView tvMinMoney = holder.getView(R.id.tvMinMoney);
        TextView tvMoney = holder.getView(R.id.tvMoney);
        TextView tvMoneyTitle = holder.getView(R.id.tvMoneyTitle);
        TextView tvStartDateDate = holder.getView(R.id.tvStartDateDate);
        TextView tvEndDate = holder.getView(R.id.tvEndDate);
        TextView tvTo = holder.getView(R.id.tvTo);
        TextView tvRule = holder.getView(R.id.tvRule);

        switch (data.getStatus()) {
            //未使用
            case 0:
                rlMain.setBackgroundResource(R.mipmap.red_packets_no_use_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.theme_color));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.theme_color));
                tvMinMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_black));
                tvMinMoney.setTextColor(context.getResources().getColor(R.color.text_black));
                tvRule.setTextColor(context.getResources().getColor(R.color.text_black));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_black));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_black));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_black));
                break;
            //已使用
            case 1:
                rlMain.setBackgroundResource(R.mipmap.red_packets_use_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMinMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvMinMoney.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvRule.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                break;
            //已过期
            case 2:
                rlMain.setBackgroundResource(R.mipmap.red_packets_out_date_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMinMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvMinMoney.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvRule.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                break;
        }
        holder.setTextForTextView(R.id.tvStartDateDate, data.getStartdate());
        holder.setTextForTextView(R.id.tvEndDate, data.getExpirydate());
        holder.setTextForTextView(R.id.tvRule, data.getContent());
        holder.setTextForTextView(R.id.tvMinMoney, String.valueOf(data.getMin_invest_amount()) + context.getResources().getString(R.string.rmb));
    }
}
