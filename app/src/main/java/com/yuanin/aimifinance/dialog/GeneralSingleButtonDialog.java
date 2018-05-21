package com.yuanin.aimifinance.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
public class GeneralSingleButtonDialog {
    private Context context;
    private AlertDialog dialog;
    private View view;
    private TextView tvTips, tvTitle;
    private Button btnConfirm;
    private LinearLayout llMain;


    public GeneralSingleButtonDialog(Context context, boolean isCancelable, String title, String tips, String btnStr, View.OnClickListener listener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_genaral_single_button, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(isCancelable);
        dialog.setView(new EditText(context));
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 非常重要：设置对话框弹出的位置
        window.setContentView(view);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        initViews();
        tvTitle.setText(title);
        tvTips.setText(tips);
        btnConfirm.setText(btnStr);
        btnConfirm.setOnClickListener(listener);
    }


    private void initViews() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llMain.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        llMain.setLayoutParams(lp);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }

    public void onDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }
}
