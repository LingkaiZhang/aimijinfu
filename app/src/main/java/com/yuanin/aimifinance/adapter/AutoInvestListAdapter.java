package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.ModifyAutoInvestActivity;
import com.yuanin.aimifinance.entity.AutoInvestEntity;
import com.yuanin.aimifinance.inter.IDeleteCallBack;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangxin
 * @desc 自动投资
 */
public class AutoInvestListAdapter extends GeneralAdapter<AutoInvestEntity> {
    private IDeleteCallBack callBack;
    private List<SwipeMenuLayout> swipeLayouts = new ArrayList<>();

    public AutoInvestListAdapter(List<AutoInvestEntity> list, Context context, IDeleteCallBack callBack) {
        super(context, list, R.layout.adapter_auto_invest);
        this.callBack = callBack;
    }

    public void closeSwipe() {
        for (int i = 0; i < swipeLayouts.size(); i++) {
            swipeLayouts.get(i).smoothClose();
        }
    }

    @Override
    public void convert(GeneralViewHolder holder, final AutoInvestEntity data) {
        holder.setTextForTextView(R.id.tvName, data.getRemark());
        swipeLayouts.add((SwipeMenuLayout) holder.getView(R.id.swipeLayout));
        if (data.getMin_unit().equals(data.getMax_unit())) {
            if (data.getMin_unit().equals("0")) {
                holder.setTextForTextView(R.id.tvTime, "不限");
            } else {
                holder.setTextForTextView(R.id.tvTime, data.getMin_unit() + "个月");
            }
        } else {
            holder.setTextForTextView(R.id.tvTime, data.getMin_unit() + "个月~" + data.getMax_unit() + "个月");
        }

        if (data.getMin_annual().equals(data.getMax_annual())) {
            if (data.getMin_annual().equals("0")) {
                holder.setTextForTextView(R.id.tvRate, "不限");
            } else {
                holder.setTextForTextView(R.id.tvRate, data.getMin_annual() + "%");
            }
        } else {
            holder.setTextForTextView(R.id.tvRate, data.getMin_annual() + "%~" + data.getMax_annual() + "%");
        }


        LinearLayout llMoney = holder.getView(R.id.llMoney);
        if (Integer.parseInt(data.getAll_amount()) == 1) {
            llMoney.setVisibility(View.GONE);
            holder.setTextForTextView(R.id.tvType, "账户余额全部出借");
        } else {
            llMoney.setVisibility(View.VISIBLE);
            holder.setTextForTextView(R.id.tvMoney, data.getAmount() + "元");
            holder.setTextForTextView(R.id.tvType, "固定单笔出借金额");
        }
        if (Integer.parseInt(data.getIs_red()) == 1) {
            holder.setTextForTextView(R.id.tvRedPacket, "使用");
        } else {
            holder.setTextForTextView(R.id.tvRedPacket, "不使用");
        }

        final ToggleButton toggleBtn = holder.getView(R.id.toggleBtn);
        if (Integer.parseInt(data.getStatus()) == 0) {
            toggleBtn.setChecked(true);
        } else {
            toggleBtn.setChecked(false);
        }
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleBtn.isChecked()) {
                    callBack.onDelete(data, 0);
                } else {
                    callBack.onDelete(data, 2);
                }
            }
        });
        Button btnDelete = holder.getView(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDelete(data, 1);
            }
        });

        LinearLayout llMain = holder.getView(R.id.llMain);
        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifyAutoInvestActivity.class);
                intent.putExtra("data", data);
                context.startActivity(intent);
            }
        });
    }
}
