package com.yuanin.aimifinance.adapter;

import android.content.Context;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.InvestRecordEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/21
 * @time 14:03
 * @desc 出借记录list
 */
public class InvestRecordListAdapter extends GeneralAdapter<InvestRecordEntity> {

    public InvestRecordListAdapter(List<InvestRecordEntity> list, Context context) {
        super(context, list, R.layout.adapter_invest_record);
    }

    @Override
    public void convert(GeneralViewHolder holder, InvestRecordEntity data) {
        holder.setTextForTextView(R.id.tvInvestAccount, data.getMobile());
        holder.setTextForTextView(R.id.tvInvestMoney, data.getAmount());
        holder.setTextForTextView(R.id.tvInvestTime, data.getCreated());
    }
}
