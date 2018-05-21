package com.yuanin.aimifinance.adapter;

import android.content.Context;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.AutoInvestRecordEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 */
public class AutoInvestRecordListAdapter extends GeneralAdapter<AutoInvestRecordEntity> {

    public AutoInvestRecordListAdapter(List<AutoInvestRecordEntity> list, Context context) {
        super(context, list, R.layout.adapter_auto_invest_record);
    }

    @Override
    public void convert(GeneralViewHolder holder, final AutoInvestRecordEntity data) {
        holder.setTextForTextView(R.id.tvName, data.getRemark());
        holder.setTextForTextView(R.id.tvProject, data.getProject_name());
        holder.setTextForTextView(R.id.tvMoney, data.getAmount() + "元");
        holder.setTextForTextView(R.id.tvDate, data.getCreated());
    }
}
