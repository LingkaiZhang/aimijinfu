package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.LinearLayout;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.entity.InvestRecordEntity;
import com.yuanin.aimifinance.entity.SmartInvestProductListEntity;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author lingkai  星期四 2019/4/25
 * @version :
 * @name :
 */
public class SmartInvestProductListAdapter extends GeneralAdapter<SmartInvestProductListEntity> {

    Typeface PingFangSC_Regular;
    Typeface PingFangSC_Medium;
    Context mContext;

    public SmartInvestProductListAdapter(List<SmartInvestProductListEntity> list, Context context) {
        super(context, list, R.layout.adapter_smart_invest_list);
        this.mContext = context;
        PingFangSC_Regular = Typeface.createFromAsset(context.getAssets(),"苹方黑体-准-简.ttf");
        PingFangSC_Medium = Typeface.createFromAsset(context.getAssets(),"苹方黑体-中粗-简.ttf");
    }

    @Override
    public void convert(GeneralViewHolder holder, SmartInvestProductListEntity data) {
        holder.setTextAndTypefaceForTextView(R.id.tvInvestAccount, data.getProjectName(),PingFangSC_Medium);
        holder.setTextAndTypefaceForTextView(R.id.tvInvestMoney, FmtMicrometer.format6(data.getAmount()),PingFangSC_Regular);
        holder.setTextAndTypefaceForTextView(R.id.tvInvestTime, data.getTerm() + "个月",PingFangSC_Regular);

        LinearLayout itemView = holder.getView(R.id.llItem);
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FinanceProductDetailActivity.class);
            intent.putExtra("entityID", data.getBorrowAmountId());
            mContext.startActivity(intent);
        });
    }
}
