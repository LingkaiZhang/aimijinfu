package com.yuanin.aimifinance.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yuanin.aimifinance.R;


/**
 * @author markmjw
 * @date 2013-10-08
 */
public class XFooterView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;


    private View mLayout;

    private int mState = STATE_NORMAL;

    public XFooterView(Context context) {
        super(context);
        initView(context);
    }

    public XFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.vw_footer, null);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mLayout);

//        progressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

//        Animation mRotateUpAnim = new RotateAnimation(0.0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        mRotateUpAnim.setDuration(1000);
//        mRotateUpAnim.setFillAfter(false);
//        mRotateUpAnim.setRepeatCount(-1);
//        ivLoading.startAnimation(mRotateUpAnim);
    }

    /**
     * Set footer view state
     *
     * @param state
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_READY
     */
    public void setState(int state) {
        if (state == mState) return;

        if (state != STATE_LOADING) {
//            ivLoading.clearAnimation();
        }
        switch (state) {
            case STATE_NORMAL:
//                if (mState == STATE_LOADING) {
//                    ivLoading.clearAnimation();
//                }
                break;

            case STATE_READY:
//                if (mState != STATE_READY) {
//                    ivLoading.clearAnimation();
//                }
                break;

            case STATE_LOADING:
                break;
        }

        mState = state;
    }

    /**
     * Set footer view bottom margin.
     *
     * @param margin
     */
    public void setBottomMargin(int margin) {
        if (margin < 0) return;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = margin;
        mLayout.setLayoutParams(lp);
    }

    /**
     * Get footer view bottom margin.
     *
     * @return
     */
    public int getBottomMargin() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

}
