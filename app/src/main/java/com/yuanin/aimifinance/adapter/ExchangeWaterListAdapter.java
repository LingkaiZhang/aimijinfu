package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.FundsWaterEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 11:41
 * @desc 交易流水list
 */
public class ExchangeWaterListAdapter extends GeneralAdapter<FundsWaterEntity> {
    private Context mContext;

    public ExchangeWaterListAdapter(Context context, List<FundsWaterEntity> list) {
        super(context, list, R.layout.adapter_exchange_water_item);
        this.mContext = context;
    }

    @Override
    public void convert(GeneralViewHolder holder, FundsWaterEntity entity) {
        TextView tvTitleDate = holder.getView(R.id.tvTitleDate);
        LinearLayout llMain = holder.getView(R.id.llMain);
        if (entity.getStyle() == 1) {
            tvTitleDate.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
            holder.setTextForTextView(R.id.tvTitleDate, entity.getCreated());
        } else {
            tvTitleDate.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
            holder.setTextForTextView(R.id.tvType, entity.getType());
            holder.setTextForTextView(R.id.tvStatus, entity.getStatus());
            TextView tvStatus = holder.getView(R.id.tvStatus);
            if (entity.getStatus().equals(mContext.getResources().getString(R.string.funds_water_finish))) {
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            } else if (entity.getStatus().equals(mContext.getResources().getString(R.string.funds_water_no_finish))) {
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            } else if (entity.getStatus().equals(mContext.getResources().getString(R.string.funds_water_chuli))) {
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.man_biao));
            }
            holder.setTextForTextView(R.id.tvMoney, entity.getMoney() + "元");
            holder.setTextForTextView(R.id.tvDate, entity.getCreated());
        }
    }
}
