package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.BuyRegularActivity;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.entity.IndexProductEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 11:41
 * @desc 优选list
 */
public class IndexProductListAdapter extends GeneralAdapter<IndexProductEntity> {

    public IndexProductListAdapter(List<IndexProductEntity> list, Context context) {
        super(context, list, R.layout.adapter_index_product_item);
    }

    @Override
    public void convert(GeneralViewHolder holder, final IndexProductEntity data) {
        View topLL = holder.getView(R.id.topLL);
        if (holder.getPosition() == 0) {
            topLL.setVisibility(View.GONE);
        } else {
            topLL.setVisibility(View.VISIBLE);
        }
//        holder.setTextForTextView(R.id.tvRate, AppUtils.formatDouble("#.00", Double.valueOf(data.getAnnual())));
        holder.setTextForTextView(R.id.tvTime, data.getTerm());
        holder.setTextForTextView(R.id.tvUnit, data.getUnit());
        holder.setTextForTextView(R.id.tvName, data.getTitle());
        holder.setTextForTextView(R.id.tvLeaveMoney, "剩余" + data.getAmount() + "元");
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.isShowLastItem = false;
                if (data.getType() == 2 || data.getType() == 3) {
                    StaticMembers.isShowLastItem = false;
                } else {
                    StaticMembers.isShowLastItem = true;
                }
                Intent intent = new Intent(context, FinanceProductDetailActivity.class);
                intent.putExtra("entityID", data.getId());
                context.startActivity(intent);
            }
        });
        TextView btnStatus = holder.getView(R.id.btnStatus);
        holder.setTextForTextView(R.id.btnStatus, data.getStatusname());
        //TODO   显示产品类型图标
        ImageView ivTypeLogo = holder.getView(R.id.iv_type_logo);
        if (data.getTypename().contains("车")) {
            ivTypeLogo.setImageDrawable(new BitmapDrawable(context.getResources(), AppUtils.getBitmap(context, R.mipmap.car_loan)) );
        } else if (data.getTypename().contains("经")) {
            ivTypeLogo.setImageDrawable(new BitmapDrawable(context.getResources(), AppUtils.getBitmap(context, R.mipmap.manage_loan)) );
        } else if (data.getTypename().contains("信")) {
            ivTypeLogo.setImageDrawable(new BitmapDrawable(context.getResources(), AppUtils.getBitmap(context, R.mipmap.credit_loan)) );
        }

        //TODO   显示还款方式
        TextView equalityCorpusAndInterest = holder.getView(R.id.equalityCorpusAndInterest);
        TextView interestFirstThenCost = holder.getView(R.id.interestFirstThenCost);
        if (data.getRepay_method().contains("先息后本")) {
            interestFirstThenCost.setVisibility(View.VISIBLE);
            equalityCorpusAndInterest.setVisibility(View.GONE);
        }else if (data.getRepay_method().contains("等额本息")) {
            interestFirstThenCost.setVisibility(View.GONE);
            equalityCorpusAndInterest.setVisibility(View.VISIBLE);
        }

        //TODO   首页产品列表加息显示
        TextView interestRates = holder.getView(R.id.interestRates);
        TextView interestRatesLogo = holder.getView(R.id.interestRatesLogo);

        if (data.getOrgannual() == null || data.getExtannual() == null) {
            holder.setTextForTextView(R.id.tvRate, AppUtils.formatDouble("#.0", Double.valueOf(data.getAnnual())));
            interestRates.setVisibility(View.GONE);
            interestRatesLogo.setVisibility(View.GONE);
        } else {

            if (Double.valueOf(data.getExtannual()) > 0 && Double.valueOf(data.getOrgannual()) > 0) {
                interestRates.setText("+" + String.format("%.1f", Double.valueOf(data.getExtannual())).toString() + "%");
                holder.setTextForTextView(R.id.tvRate, AppUtils.formatDouble("#.0", Double.valueOf(data.getOrgannual())));
                interestRates.setVisibility(View.VISIBLE);
                interestRatesLogo.setVisibility(View.GONE);
            } else {
                holder.setTextForTextView(R.id.tvRate, AppUtils.formatDouble("#.0", Double.valueOf(data.getAnnual())));
                interestRates.setVisibility(View.GONE);
                interestRatesLogo.setVisibility(View.GONE);
            }
        }

        if (data.getIsbuy() == 1) {
            btnStatus.setBackgroundResource(R.drawable.selector_index_button);
            btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticMembers.IS_NEED_LOGIN) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        StaticMembers.isShowLastItem = false;
                        Intent intent = new Intent(context, BuyRegularActivity.class);
                        intent.putExtra("entityID", data.getId());
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            btnStatus.setBackgroundResource(R.mipmap.index_buy_btn_noclick);
            btnStatus.setOnClickListener(null);
        }
    }
}
