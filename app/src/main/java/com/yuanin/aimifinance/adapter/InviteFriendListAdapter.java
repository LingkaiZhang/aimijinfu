package com.yuanin.aimifinance.adapter;

import android.content.Context;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.InviteFriendEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/21
 * @time 14:03
 * @desc 投资记录list
 */
public class InviteFriendListAdapter extends GeneralAdapter<InviteFriendEntity> {

    public InviteFriendListAdapter(List<InviteFriendEntity> list, Context context) {
        super(context, list, R.layout.adapter_invest_record);
    }

    @Override
    public void convert(GeneralViewHolder holder, InviteFriendEntity data) {
        holder.setTextForTextView(R.id.tvInvestAccount, data.getMobile());
        holder.setTextForTextView(R.id.tvInvestMoney, data.getAmount() + "元");
        holder.setTextForTextView(R.id.tvInvestTime, data.getCreated());
    }
}
