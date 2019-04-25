package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.graphics.Typeface;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.InvestRecordEntity;
import com.yuanin.aimifinance.utils.FmtMicrometer;
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

    Typeface PingFangSC_Regular;

    public InvestRecordListAdapter(List<InvestRecordEntity> list, Context context) {
        super(context, list, R.layout.adapter_invest_record);
        PingFangSC_Regular= Typeface.createFromAsset(context.getAssets(),"苹方黑体-准-简.ttf");
    }

    @Override
    public void convert(GeneralViewHolder holder, InvestRecordEntity data) {
        holder.setTextAndTypefaceForTextView(R.id.tvInvestAccount, data.getMobile(),PingFangSC_Regular);
        holder.setTextAndTypefaceForTextView(R.id.tvInvestMoney, FmtMicrometer.format6(data.getAmount()),PingFangSC_Regular);
        holder.setTextAndTypefaceForTextView(R.id.tvInvestTime, data.getCreated(),PingFangSC_Regular);
    }
}
