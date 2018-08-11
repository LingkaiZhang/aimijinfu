package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.BuyRegularActivity;
import com.yuanin.aimifinance.activity.DebtProductDetailActivity;
import com.yuanin.aimifinance.activity.LoginRegisterActivity;
import com.yuanin.aimifinance.entity.DebtFinanceProductEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.FinanceProductEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.yuanin.aimifinance.R.id.btnStatus;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 14:47
 * @desc 资金流水list
 */
public class DebtFinanceProductListAdapter extends BaseAdapter {
    private List<DebtFinanceProductEntity> dataList;
    private Context mContext;

    public DebtFinanceProductListAdapter(Context mContext, List<DebtFinanceProductEntity> dataList) {
        super();
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        dataList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final DebtFinanceProductEntity entity = dataList.get(position);
            MainViewHolder mainViewHolder;
            if (convertView == null) {
                mainViewHolder = new MainViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_finance_debt_product_item, null);
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
                mainViewHolder.ivTypeLogo = (ImageView) convertView
                        .findViewById(R.id.iv_type_logo);

                convertView.setTag(mainViewHolder);
            } else {
                mainViewHolder = (MainViewHolder) convertView.getTag();
            }
            mainViewHolder.tvName.setText(entity.getBorrowAmountName());
//            mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
            mainViewHolder.tvTime.setText(entity.getDays());
            //mainViewHolder.tvUnit.setText(entity.getUnit());
            mainViewHolder.tvLeaveMoney.setText("剩余" + FmtMicrometer.format6(entity.getDueCapital()) + "元");
            mainViewHolder.btnStatus.setText(entity.getStatus());

            //TODO  显示还款方式代码
            if (entity.getRepayMethod().contains("先息后本")) {
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.GONE);
                mainViewHolder.interestFirstThenCost.setVisibility(View.VISIBLE);
            } else if (entity.getRepayMethod().contains("等额本息")) {
                mainViewHolder.interestFirstThenCost.setVisibility(View.GONE);
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.VISIBLE);
            }

            if (entity.getIsbuy().equals("1") ) {
                mainViewHolder.btnStatus.setBackgroundResource(R.drawable.selector_index_button);
                mainViewHolder.btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StaticMembers.IS_NEED_LOGIN) {
                            mContext.startActivity(new Intent(mContext, LoginRegisterActivity.class));
                        } else if (StaticMembers.HK_STATUS != 1) {
                            //刷新弹框状态
                            EventMessage eventMessage2 = new EventMessage();
                            eventMessage2.setType(EventMessage.POPUWINDOWN_FINANCE_PRODUCT);
                            EventBus.getDefault().post(eventMessage2);
                        } else {

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

                    Intent intent = new Intent(mContext, DebtProductDetailActivity.class);
                    intent.putExtra("entityID", entity.getBorrowTransferId());
                    intent.putExtra("productName",entity.getBorrowAmountName());
                    mContext.startActivity(intent);
                }
            });

        return convertView;
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
        ImageView ivTypeLogo;
    }
}
