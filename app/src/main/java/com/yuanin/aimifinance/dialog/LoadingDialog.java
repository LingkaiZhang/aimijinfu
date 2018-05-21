package com.yuanin.aimifinance.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.widget.ImageView;

import com.yuanin.aimifinance.R;


public class LoadingDialog extends Dialog {


    private boolean cancelable;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int theme, boolean cancelable, OnCancelListener cancelListener) {
        super(context, theme);
        this.cancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_loading_have_cover);
        final ImageView imageView = (ImageView) findViewById(R.id.imageLoading);
        ((Animatable) imageView.getDrawable()).start();
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(false);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Animatable) imageView.getDrawable()).stop();
            }
        });
    }
}
