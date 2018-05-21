package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.entity.HistoryItemEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 11:41
 * @desc 往期项目list
 */
public class HistoryItemListAdapter extends GeneralAdapter<HistoryItemEntity> {

    public HistoryItemListAdapter(List<HistoryItemEntity> list, Context context) {
        super(context, list, R.layout.adapter_history_item);
    }

    @Override
    public void convert(GeneralViewHolder holder, final HistoryItemEntity data) {
        holder.setTextForTextView(R.id.tvTitle, data.getProject_name());
        holder.setTextForTextView(R.id.tvDate, data.getBuylastdate());
        holder.setTextForTextView(R.id.tvMoney, data.getAmount());
        holder.setTextForTextView(R.id.tvRate, AppUtils.formatDouble("#.00", Double.valueOf(data.getAnnual())));
        holder.setTextForTextView(R.id.tvPeople, data.getQty() + "");
        TextView tvStatus = holder.getView(R.id.tvStatus);
        switch (data.getStatus()) {
            case 3:
                tvStatus.setText("已满标");
                break;
            case 4:
                tvStatus.setText("已流标");
                break;
            case 5:
                tvStatus.setText("还款中");
                break;
            case 6:
                tvStatus.setText("即将到期");
                break;
            case 7:
                tvStatus.setText("已还款");
                break;
            case 8:
                tvStatus.setText("已逾期");
                break;
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.isShowLastItem = false;
                Intent intent = new Intent(context, FinanceProductDetailActivity.class);
                intent.putExtra("where", 4);
                intent.putExtra("entityID", data.getId());
                context.startActivity(intent);
            }
        });
    }
}
