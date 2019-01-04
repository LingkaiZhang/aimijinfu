package com.yuanin.aimifinance.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
public class MyInvestListAdapter extends GeneralAdapter<MyInvestEntity> {
    private int select;
    private AlertDialog dialog;
    private LinearLayout llMain;
    private TextView tvOrderDetail, tvProjectDetail, tvClose;

    public MyInvestListAdapter(Context context, List<MyInvestEntity> list) {
        super(context, list, R.layout.adapter_my_invest);
    }

    public void setCurrentSelect(int select) {
        this.select = select;
    }

    @Override
    public void convert(GeneralViewHolder holder, final MyInvestEntity data) {
        LinearLayout llLeave = holder.getView(R.id.llLeave);
        TextView tvStatus = holder.getView(R.id.tvStatus);
        holder.setTextForTextView(R.id.tvName, data.getProject_name());
        holder.setTextForTextView(R.id.tvInvestMoney, FmtMicrometer.format6(data.getAmount()));
        holder.setTextForTextView(R.id.tvEarn, FmtMicrometer.format6(data.getInterest()));
        holder.setTextForTextView(R.id.tvStartDate, data.getCreated());
        holder.setTextForTextView(R.id.tvEndDate, data.getExpire_time());
        TextView tvEndDateTitle = holder.getView(R.id.tvEndDateTitle);
        ImageView ivPrintMark = holder.getView(R.id.ivPrintMark);

        switch (select) {
            case 1:
                tvEndDateTitle.setText("到期时间:");
                llLeave.setVisibility(View.VISIBLE);
                holder.setTextForTextView(R.id.tvLeaveDay, data.getDays_num());
                if (data.getStatus() == 3) {
                    holder.setTextForTextView(R.id.tvStatus, "已满标");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.man_biao));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_print));
                }else if (data.getStatus() == 13) {
                // holder.setTextForTextView(R.id.tvStatus, "提前结清");
                    holder.setTextForTextView(R.id.tvStatus, "续借");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.prepayment));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.prepayment_print));
                } else if (data.getStatus() == 8) {
                    // holder.setTextForTextView(R.id.tvStatus, "提前结清");
                    holder.setTextForTextView(R.id.tvStatus, "催收中");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.prepayment));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.prepayment_print));
                } else {
                    holder.setTextForTextView(R.id.tvStatus, "还款中");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.jin_xing));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_print));
                }

                break;
            case 2:
                tvEndDateTitle.setText("截止时间:");
                llLeave.setVisibility(View.GONE);

                if (data.getStatus() == 3) {
                    holder.setTextForTextView(R.id.tvStatus, "已满标");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.man_biao));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_print));
                } else {
                    holder.setTextForTextView(R.id.tvStatus, "募集中");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.jin_xing));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_print));
                }

                break;
            case 3:
                tvEndDateTitle.setText("流标时间:");
                llLeave.setVisibility(View.GONE);
                holder.setTextForTextView(R.id.tvStatus, "已流标");
                tvStatus.setTextColor(context.getResources().getColor(R.color.liu_biao));
                ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.gray_print));
                break;
            case 4:
                tvEndDateTitle.setText("到期时间:");
                llLeave.setVisibility(View.GONE);
                //提前结清
                if (data.getStatus() == 13) {
                   // holder.setTextForTextView(R.id.tvStatus, "提前结清");
                    holder.setTextForTextView(R.id.tvStatus, "续借");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.prepayment));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.prepayment_print));
                } else if (data.getStatus() == 8) {
                // holder.setTextForTextView(R.id.tvStatus, "提前结清");
                holder.setTextForTextView(R.id.tvStatus, "催收中");
                tvStatus.setTextColor(context.getResources().getColor(R.color.prepayment));
                ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.prepayment_print));
                } else {
                    holder.setTextForTextView(R.id.tvStatus, "已还款");
                    tvStatus.setTextColor(context.getResources().getColor(R.color.huan_kuan));
                    ivPrintMark.setImageDrawable(context.getResources().getDrawable(R.drawable.huankuan_print));
                }
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
                        //if (data.getIsTransferBid() == 0) {
                            Intent intent = new Intent(context, FinanceProductDetailActivity.class);
                            intent.putExtra("where", 1);
                            intent.putExtra("entityID", data.getId());
                            context.startActivity(intent);
                       /* } else {
                            Intent intent = new Intent(context, DebtProductDetailActivity.class);
                            intent.putExtra("where", 1);
                            intent.putExtra("entityID", data.getId());
                            context.startActivity(intent);
                        }
*/
                        dialog.dismiss();
                    }
                });
                tvOrderDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderFormActivity.class);
                        intent.putExtra("entityID", data.getInvest_id());
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
