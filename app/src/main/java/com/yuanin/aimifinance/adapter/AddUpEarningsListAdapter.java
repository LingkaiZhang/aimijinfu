package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.AddUpEarningsEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/19
 * @time 16:25
 * @desc 累计收益list
 */
public class AddUpEarningsListAdapter extends GeneralAdapter<AddUpEarningsEntity> {

    public AddUpEarningsListAdapter(List<AddUpEarningsEntity> list, Context context) {
        super(context, list, R.layout.adapter_add_up_earnings);
    }

    @Override
    public void convert(GeneralViewHolder holder, AddUpEarningsEntity data) {
        holder.setTextForTextView(R.id.tvName, data.getProject_name());
        TextView tvStatus = holder.getView(R.id.tvStatus);
        switch (data.getStatus()) {
            case 0:
                tvStatus.setText("已逾期");
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_gray));
                break;
            case 1:
                tvStatus.setText("待还款");
                tvStatus.setTextColor(context.getResources().getColor(R.color.theme_color));
                break;
            case 2:
                tvStatus.setText("即将到期");
                tvStatus.setTextColor(context.getResources().getColor(R.color.add_up_green));
                break;
            case 3:
                tvStatus.setText("已还款");
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_gray));
                break;
                //TODO  提前结清
            case 4:
                tvStatus.setText("提前结清");
                tvStatus.setTextColor(context.getResources().getColor(R.color.prepayment));
                break;
        }
        if (data.getPeriodqty().length() != 0) {
            holder.setTextForTextView(R.id.tvTime, data.getPeriodqty() + "期/" + data.getTerm() + "期");
        } else {
            holder.setTextForTextView(R.id.tvTime, "");
        }
        holder.setTextForTextView(R.id.tvMoney, String.valueOf(data.getInterest()));
        holder.setTextForTextView(R.id.tvDate, data.getRepay_time() + "到期");
    }
}
