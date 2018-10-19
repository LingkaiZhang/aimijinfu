package com.yuanin.aimifinance.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.utils.StaticMembers;


/**
 * @author huangxin
 * @date 2015/11/10
 * @desc 通用dialog
 */
public class DebtConfirmPayDialog {
    private AlertDialog dialog;
    private View view;
    private TextView tvTitle, tvBalance, tvPrincipal, tvAmountPayable, tvProspectiveEarnings, tvReceptServiceCharge;
    private Button btnCancel, btnConfirm;
    private LinearLayout llMain;
    private View viewLineOne;


    public DebtConfirmPayDialog(Context context, boolean isCancelable, String title,
                                String balabce, String principal,String receptServiceCharge, String amountPayable, String prospectiveEarnings,
                                String leftStr, String rigthStr, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_debt_buy_comfirm, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(isCancelable);
        dialog.setView(new EditText(context));
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 非常重要：设置对话框弹出的位置
        window.setContentView(view);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        initViews();
        if (title.length() > 0) {
            tvTitle.setText(title);
        }  else {
            tvTitle.setVisibility(View.GONE);
            viewLineOne.setVisibility(View.GONE);
        }

        btnCancel.setText(leftStr);
        btnConfirm.setText(rigthStr);
        tvBalance.setText(balabce);
        tvPrincipal.setText(principal);
        tvReceptServiceCharge.setText(receptServiceCharge);
        tvAmountPayable.setText(amountPayable);
        tvProspectiveEarnings.setText(prospectiveEarnings);
        btnCancel.setOnClickListener(leftListener);
        btnConfirm.setOnClickListener(rightListener);
    }


    private void initViews() {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        viewLineOne = view.findViewById(R.id.view_line_one);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvPrincipal = view.findViewById(R.id.tvPrincipal);
        tvReceptServiceCharge = view.findViewById(R.id.tvReceptServiceCharge);
        tvAmountPayable = view.findViewById(R.id.tvAmountPayable);
        tvProspectiveEarnings = view.findViewById(R.id.tvProspectiveEarnings);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llMain.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        llMain.setLayoutParams(lp);
    }

    public void dismiss() {
        dialog.dismiss();
    }


}
