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

public class GeneralNoTitleDialog {

    private AlertDialog dialog;
    private View view;
    private TextView tvTips;
    private Button btnCancel, btnConfirm;
    private LinearLayout llMain;


    public GeneralNoTitleDialog(Context context, boolean isCancelable, String tips, String leftStr, String rigthStr, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_genaral_notitle, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(isCancelable);
        dialog.setView(new EditText(context));
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 非常重要：设置对话框弹出的位置
        window.setContentView(view);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        initViews();
        tvTips.setText(tips);
        btnCancel.setText(leftStr);
        btnConfirm.setText(rigthStr);
        btnCancel.setOnClickListener(leftListener);
        btnConfirm.setOnClickListener(rightListener);
    }


    private void initViews() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llMain.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        llMain.setLayoutParams(lp);
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
