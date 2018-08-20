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
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.activity.LoginRegisterActivity;
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

        if (entity.getBorrowAmountName() != null) {
            HeadViewHolder headViewHolder = null;
            if (convertView == null) {
                headViewHolder = new HeadViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_finance_debt_product_item, null);
                headViewHolder.tvName = (TextView) convertView
                        .findViewById(R.id.tvName);
                headViewHolder.tvRate = (TextView) convertView
                        .findViewById(R.id.tvRate);
                headViewHolder.tvTime = (TextView) convertView
                        .findViewById(R.id.tvTime);
                headViewHolder.btnStatus = (TextView) convertView
                        .findViewById(btnStatus);
                headViewHolder.tvUnit = (TextView) convertView
                        .findViewById(R.id.tvUnit);
                headViewHolder.tvLeaveMoney = (TextView) convertView
                        .findViewById(R.id.tvLeaveMoney);
                headViewHolder.interestRates = (TextView) convertView
                        .findViewById(R.id.interestRates);
                headViewHolder.interestRatesLogo = (TextView) convertView
                        .findViewById(R.id.interestRatesLogo);
                headViewHolder.interestFirstThenCost = (TextView) convertView
                        .findViewById(R.id.interestFirstThenCost);
                headViewHolder.equalityCorpusAndInterest = (TextView) convertView
                        .findViewById(R.id.equalityCorpusAndInterest);
                headViewHolder.tvZheRangXiShuo = (TextView) convertView
                        .findViewById(R.id.tvZheRangXiShuo);

                convertView.setTag(headViewHolder);
            } else {
                headViewHolder = (HeadViewHolder) convertView.getTag();
            }

            headViewHolder.tvName.setText(entity.getBorrowAmountName());
            headViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
            headViewHolder.tvTime.setText(entity.getDays());
            //mainViewHolder.tvUnit.setText(entity.getUnit());
            headViewHolder.tvLeaveMoney.setText("剩余" + FmtMicrometer.format6(entity.getDueCapital()) + "元");
            headViewHolder.btnStatus.setText(entity.getStatus());
            headViewHolder.tvZheRangXiShuo.setText(entity.getDiscountRate());

            //TODO  显示还款方式代码
            if (entity.getRepayMethod().contains("先息后本")) {
                headViewHolder.equalityCorpusAndInterest.setVisibility(View.GONE);
                headViewHolder.interestFirstThenCost.setVisibility(View.VISIBLE);
            } else if (entity.getRepayMethod().contains("等额本息")) {
                headViewHolder.interestFirstThenCost.setVisibility(View.GONE);
                headViewHolder.equalityCorpusAndInterest.setVisibility(View.VISIBLE);
            }

            if (entity.getIsbuy() == 1) {
                headViewHolder.btnStatus.setBackgroundResource(R.drawable.selector_index_button);
                headViewHolder.btnStatus.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(mContext, DebtProductDetailActivity.class);
                            intent.putExtra("entityID", entity.getBorrowTransferId());
                            intent.putExtra("productName",entity.getBorrowAmountName());
                            mContext.startActivity(intent);
                        }
                    }
                });
            } else {
                headViewHolder.btnStatus.setBackgroundResource(R.mipmap.index_buy_btn_noclick);
                headViewHolder.btnStatus.setOnClickListener(null);
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
                mainViewHolder.ivTypeLogo = (ImageView) convertView
                        .findViewById(R.id.iv_type_logo);

                convertView.setTag(mainViewHolder);
            } else {
                mainViewHolder = (MainViewHolder) convertView.getTag();
            }
            mainViewHolder.tvName.setText(entity.getTitle());
//            mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
            mainViewHolder.tvTime.setText(entity.getTerm());
            mainViewHolder.tvUnit.setText(entity.getUnit());

            mainViewHolder.tvLeaveMoney.setText("剩余" + FmtMicrometer.format6(entity.getAmount()) + "元");
            mainViewHolder.btnStatus.setText(entity.getStatusname());

            //TODO  显示还款方式代码
            if (entity.getRepay_method().contains("先息后本")) {
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.GONE);
                mainViewHolder.interestFirstThenCost.setVisibility(View.VISIBLE);
            } else if (entity.getRepay_method().contains("等额本息")) {
                mainViewHolder.interestFirstThenCost.setVisibility(View.GONE);
                mainViewHolder.equalityCorpusAndInterest.setVisibility(View.VISIBLE);
            }

            //TODO   显示产品类型图标
            if (entity.getTypename() != null) {
                if (entity.getTypename().contains("车")) {
                    mainViewHolder.ivTypeLogo.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), AppUtils.getBitmap(mContext, R.mipmap.car_loan)) );
                } else if (entity.getTypename().contains("经")) {
                    mainViewHolder.ivTypeLogo.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), AppUtils.getBitmap(mContext, R.mipmap.manage_loan_blue)) );
                } else if (entity.getTypename().contains("信")) {
                    mainViewHolder.ivTypeLogo.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), AppUtils.getBitmap(mContext, R.mipmap.credit_loan_yellow)) );
                }
            } else {
                mainViewHolder.ivTypeLogo.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), AppUtils.getBitmap(mContext, R.mipmap.car_loan)) );
            }


            //TODO  加息功能代码
            if (entity.getOrgannual() == null || entity.getExtannual() == null) {
                mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
                mainViewHolder.interestRates.setVisibility(View.GONE);
                mainViewHolder.interestRatesLogo.setVisibility(View.GONE);
            } else {

                if (Double.valueOf(entity.getExtannual()) > 0 && Double.valueOf(entity.getOrgannual()) > 0 ) {
//                    mainViewHolder.interestRates.setText("+" + entity.getExtannual() + "%");
                    mainViewHolder.interestRates.setText("+" + String.format("%.2f", Double.valueOf(entity.getExtannual())).toString() + "%");
                    mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getOrgannual())));
                    mainViewHolder.interestRates.setVisibility(View.VISIBLE);
                    mainViewHolder.interestRatesLogo.setVisibility(View.GONE);
                } else {
                    mainViewHolder.tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(entity.getAnnual())));
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
                            mContext.startActivity(new Intent(mContext, LoginRegisterActivity.class));
                        } else if (StaticMembers.HK_STATUS != 1) {
                            //刷新弹框状态
                            EventMessage eventMessage2 = new EventMessage();
                            eventMessage2.setType(EventMessage.POPUWINDOWN_FINANCE_PRODUCT);
                            EventBus.getDefault().post(eventMessage2);
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
        TextView tvName;
        TextView tvRate;
        TextView tvTime;
        TextView btnStatus;
        TextView tvUnit;
        TextView tvLeaveMoney;
        TextView tvZheRangXiShuo;

        TextView interestRatesLogo;
        TextView interestRates;

        TextView interestFirstThenCost;
        TextView equalityCorpusAndInterest;
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
