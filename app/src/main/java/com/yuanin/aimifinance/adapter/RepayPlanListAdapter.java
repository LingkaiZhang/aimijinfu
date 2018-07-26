package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.RepayEntity;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/21
 * @time 14:03
 * @desc 投资记录list
 */
public class RepayPlanListAdapter extends GeneralAdapter<RepayEntity> {

    public RepayPlanListAdapter(List<RepayEntity> list, Context context) {
        super(context, list, R.layout.adapter_repay_plan);
    }

    @Override
    public void convert(GeneralViewHolder holder, RepayEntity data) {
        holder.setTextForTextView(R.id.tvAccount, FmtMicrometer.format6(data.getPaymentprice()));
        holder.setTextForTextView(R.id.tvDate, data.getPaymentdate());
        holder.setTextForTextView(R.id.tvStatus, data.getStatus());
        holder.setTextForTextView(R.id.tvCount,data.getPeriodqty());
        TextView tvStatus = holder.getView(R.id.tvStatus);
        tvStatus.setTextColor(Color.parseColor(data.getColor()));
    }
}
