package com.yuanin.aimifinance.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.TopBarInfo;


public class TopBarUtil {
    private static TextView titleTextView;

    /**
     * @param view 所include的公共布局
     * @param tbi  封装的需要的属性对象
     */
    public static void setTopBarInfo(View view, TopBarInfo tbi) {
        // 初始化各种控件
        ImageView imageView = (ImageView) view.findViewById(R.id.imgview_back);
        ImageView rightImageView = (ImageView) view
                .findViewById(R.id.imgview_right);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setVisibility(View.VISIBLE);
        // 中间显示文字设置
        titleTextView.setText(tbi.getTxtStr());

        if (tbi.getLeftBtnListener() != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(tbi.getLeftBtnListener());

            if (tbi.getLeftPicId() != 0) {
                imageView.setImageResource(tbi.getLeftPicId());
            }
        } else {
            imageView.setVisibility(View.GONE);
        }

        if (tbi.getRightClickListener() != null) {
            rightImageView.setVisibility(View.VISIBLE);
            rightImageView.setOnClickListener(tbi.getRightClickListener());
            if (tbi.getRightPicId() != 0) {
                rightImageView.setImageResource(tbi.getRightPicId());
            }
        } else {
            rightImageView.setVisibility(View.GONE);
        }
    }

    public static void setTopBarInfoWithRightText(View view, TopBarInfo tbi) {
        // 初始化各种控件
        ImageView imageView = (ImageView) view.findViewById(R.id.imgview_back);
        TextView rightImageView = (TextView) view
                .findViewById(R.id.imgview_right);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setVisibility(View.VISIBLE);
        // 中间显示文字设置
        titleTextView.setText(tbi.getTxtStr());

        if (tbi.getLeftBtnListener() != null) {
            imageView.setOnClickListener(tbi.getLeftBtnListener());
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (tbi.getRightClickListener() != null) {
            rightImageView.setText(tbi.getBottomStr());
            rightImageView.setOnClickListener(tbi.getRightClickListener());
        } else {
            rightImageView.setVisibility(View.GONE);
        }
    }

    // 点击事件公共类
    public class AlwaysUseListener implements OnClickListener {
        private Context con;

        public AlwaysUseListener(Context con) {
            this.con = con;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // 返回的点击事件
                case R.id.imgview_back:
                    Animation animation = AnimationUtils.loadAnimation(con, R.anim.top_title_exit);
                    animation.setFillAfter(true);
                    titleTextView.startAnimation(animation);
                    ((Activity) con).finish();
                    break;
            }
        }

    }
}
