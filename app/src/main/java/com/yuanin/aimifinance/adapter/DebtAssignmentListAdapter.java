package com.yuanin.aimifinance.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.DebtProductDetailActivity;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.activity.OrderFormActivity;
import com.yuanin.aimifinance.entity.DebtAssignmentEntity;
import com.yuanin.aimifinance.entity.MyInvestEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 11:41
 * @desc 我的投资list
 */
public class DebtAssignmentListAdapter extends GeneralAdapter<DebtAssignmentEntity> {
    private int select;
    private AlertDialog dialog;
    private LinearLayout llMain;
    private TextView tvOrderDetail, tvProjectDetail, tvClose;

    public DebtAssignmentListAdapter(Context context, List<DebtAssignmentEntity> list) {
        super(context, list, R.layout.adapter_debt_invest_list);
    }

    public void setCurrentSelect(int select) {
        this.select = select;
    }

    @Override
    public void convert(GeneralViewHolder holder, final DebtAssignmentEntity data) {
        LinearLayout llLeave = holder.getView(R.id.llLeave);
        TextView tvStatus = holder.getView(R.id.tvStatus);
        holder.setTextForTextView(R.id.tvName, data.getBorrowAmountName());

        TextView tvEndDateTitle = holder.getView(R.id.tvEndDateTitle);
        TextView tvFirst = holder.getView(R.id.tvFirst);
        TextView tvSecond = holder.getView(R.id.tvSecond);
        TextView tvThird = holder.getView(R.id.tvThird);
        TextView tvInvestMoney = holder.getView(R.id.tvInvestMoney);
        holder.setTextForTextView(R.id.tvEarn, FmtMicrometer.format6(data.getDiscountRate()));
        TextView tvStartDate = holder.getView(R.id.tvStartDate);


        switch (select) {
            case 1:

                break;
            case 2:
                tvFirst.setText("债权价值(元)");
                tvSecond.setText("折让系数(%)");
                tvThird.setText("转让本金(元)");
                tvInvestMoney.setText(FmtMicrometer.format6(data.getDueAmount()));
                tvStartDate.setText(FmtMicrometer.format6(data.getDueCapital()));
                break;
            case 3:
                tvFirst.setText("债权价值(元)");
                tvSecond.setText("折让系数(%)");
                tvThird.setText("剩余金额(元)");
                tvInvestMoney.setText(FmtMicrometer.format6(data.getDueAmount()));
                tvStartDate.setText(FmtMicrometer.format6(data.getDueCapital()));
                break;
            case 4:
                tvFirst.setText("债权价值(元)");
                tvSecond.setText("折让系数(%)");
                tvThird.setText("实际到账金额(元)");
                tvInvestMoney.setText(FmtMicrometer.format6(data.getDueAmount()));
                tvStartDate.setText(FmtMicrometer.format6(data.getAmount()));
                break;
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_my_invest_bottom, null);
                    llMain = (LinearLayout) view.findViewById(R.id.llMain);
                    tvProjectDetail = (TextView) view.findViewById(R.id.tvProjectDetail);
                    tvOrderDetail = (TextView) view.findViewById(R.id.tvOrderDetail);
                    tvClose = (TextView) view.findViewById(R.id.tvClose);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llMain.getLayoutParams();
                    lp.width = StaticMembers.SCREEN_WIDTH;
                    llMain.setLayoutParams(lp);
                    dialog = new AlertDialog.Builder(context).create();
                    dialog.setCancelable(true);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
                    window.setContentView(view);
                    window.setWindowAnimations(R.style.BottomDialog); // 添加动画
                } else {
                    dialog.show();
                }
                tvProjectDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StaticMembers.isShowLastItem = false;
                        Intent intent = new Intent(context, DebtProductDetailActivity.class);
                        intent.putExtra("where", 1);
                        intent.putExtra("entityID", data.getBorrowTransferId());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                tvOrderDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderFormActivity.class);
                        intent.putExtra("entityID", data.getOrderId());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}

