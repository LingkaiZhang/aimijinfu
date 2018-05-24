package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.BuyRegularActivity;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.entity.FinanceProductEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.List;

import static com.yuanin.aimifinance.R.id.btnStatus;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 14:47
 * @desc 资金流水list
 */
public class FinanceProductListAdapter extends BaseAdapter {
    private List<FinanceProductEntity> dataList;
    private Context mContext;
    private static final int TYPE_COUNT = 2;//item类型的总数

    public FinanceProductListAdapter(Context mContext, List<FinanceProductEntity> dataList) {
        super();
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getStyle();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FinanceProductEntity entity = dataList.get(position);
        if (entity.getStyle() == 1) {
            HeadViewHolder headViewHolder = null;
            if (convertView == null) {
                headViewHolder = new HeadViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_finance_product_head, null);
                headViewHolder.tvType = (TextView) convertView
                        .findViewById(R.id.tvType);
                headViewHolder.topLL = (LinearLayout) convertView
                        .findViewById(R.id.topLL);
                convertView.setTag(headViewHolder);
            } else {
                headViewHolder = (HeadViewHolder) convertView.getTag();
            }
            if (position == 0) {
                headViewHolder.topLL.setVisibility(View.GONE);
            } else {
                headViewHolder.topLL.setVisibility(View.VISIBLE);
            }
            Drawable drawable = mContext.getResources().getDrawable(entity.getType());
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            headViewHolder.tvType.setCompoundDrawables(drawable, null, null, null);
            headViewHolder.tvType.setText(entity.getTerm());
        } else {
            MainViewHolder mainViewHolder = null;
            if (convertView == null) {
                mainViewHolder = new MainViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_finance_product_item, null);
                mainViewHolder.tvName = (TextView) convertView
                        .findViewById(R.id.tvName);
                mainViewHolder.tvRate = (TextView) convertView
                        .findViewById(R.id.tvRate);
                mainViewHolder.tvTime = (TextView) convertView
                        .findViewById(R.id.tvTime);
                mainViewHolder.btnStatus = (TextView) convertView
                        .findViewById(btnStatus);
                mainViewHolder.tvUnit = (TextView) convertView
                        .findViewById(R.id.tvUnit);
                mainViewHolder.tvLeaveMoney = (TextView) convertView
                        .findViewById(R.id.tvLeaveMoney);
                mainViewHolder.interestRates = (TextView) convertView
                        .findViewById(R.id.interestRates);
                mainViewHolder.interestRatesLogo = (TextView) convertView
                        .findViewById(R.id.interestRatesLogo);
                mainViewHolder.interestFirstThenCost = (TextView) convertView
                        .findViewById(R.id.interestFirstThenCost);
                mainViewHolder.equalityCorpusAndInterest = (TextView) convertView
                        .findViewById(R.id.equalityCorpusAndInterest);

                convertView.setTag(mainViewHolder);
            } else {
                mainViewHolder = (MainViewHolder) convertView.getTag();
            }
            mainViewHolder.tvName.setText(entity.getTitle());
//            mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
            mainViewHolder.tvTime.setText(entity.getTerm());
            mainViewHolder.tvUnit.setText(entity.getUnit());
            mainViewHolder.tvLeaveMoney.setText("剩余" + entity.getAmount() + "元");
            mainViewHolder.btnStatus.setText(entity.getStatusname());

            //TODO  显示还款方式代码
            if (entity.getRepay_method().contains("先息后本")) {
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.GONE);
                mainViewHolder.interestFirstThenCost.setVisibility(View.VISIBLE);
            } else if (entity.getRepay_method().contains("等额本息")) {
                mainViewHolder.interestFirstThenCost.setVisibility(View.GONE);
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.VISIBLE);
            }

            //TODO  加息功能代码
            if (entity.getOrgannual() == null || entity.getExtannual() == null) {
                mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.0", Double.valueOf(entity.getAnnual())));
                mainViewHolder.interestRates.setVisibility(View.GONE);
                mainViewHolder.interestRatesLogo.setVisibility(View.GONE);
            } else {

                if (Double.valueOf(entity.getExtannual()) > 0 && Double.valueOf(entity.getOrgannual()) > 0 ) {
//                    mainViewHolder.interestRates.setText("+" + entity.getExtannual() + "%");
                    mainViewHolder.interestRates.setText("+" + String.format("%.1f", Double.valueOf(entity.getExtannual())).toString() + "%");
                    mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.0", Double.valueOf(entity.getOrgannual())));
                    mainViewHolder.interestRates.setVisibility(View.VISIBLE);
                    mainViewHolder.interestRatesLogo.setVisibility(View.VISIBLE);
                } else {
                    mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.0", Double.valueOf(entity.getAnnual())));
                    mainViewHolder.interestRates.setVisibility(View.GONE);
                    mainViewHolder.interestRatesLogo.setVisibility(View.GONE);
                }
            }




            if (entity.getIsbuy() == 1) {
                mainViewHolder.btnStatus.setBackgroundResource(R.drawable.selector_index_button);
                mainViewHolder.btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StaticMembers.IS_NEED_LOGIN) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        } else {
                            StaticMembers.isShowLastItem = false;
                            Intent intent = new Intent(mContext, BuyRegularActivity.class);
                            intent.putExtra("entityID", entity.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            } else {
                mainViewHolder.btnStatus.setBackgroundResource(R.mipmap.index_buy_btn_noclick);
                mainViewHolder.btnStatus.setOnClickListener(null);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StaticMembers.isShowLastItem = false;
                    if (entity.getType() == 2 || entity.getType() == 3) {
                        StaticMembers.isShowLastItem = false;
                    } else {
                        StaticMembers.isShowLastItem = true;
                    }
                    Intent intent = new Intent(mContext, FinanceProductDetailActivity.class);
                    intent.putExtra("entityID", entity.getId());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class HeadViewHolder {
        TextView tvType;
        LinearLayout topLL;
    }

    class MainViewHolder {
        TextView tvName;
        TextView tvRate;
        TextView tvTime;
        TextView btnStatus;
        TextView tvUnit;
        TextView tvLeaveMoney;

        TextView interestRatesLogo;
        TextView interestRates;

        TextView interestFirstThenCost;
        TextView equalityCorpusAndInterest;
    }
}
