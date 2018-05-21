package com.yuanin.aimifinance.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yuanin.aimifinance.R;

/**
 * @author huangxin
 * @date 2016/11/15
 * @desc
 */

public class PasswordEditText extends EditText implements View.OnFocusChangeListener,
        TextWatcher {
    //EditText右侧的眼睛按钮
    private Drawable mHideDrawable, mShowDrawable, mCurrentDrawable;
    private boolean hasFoucs;
    private boolean isShowPwd = false;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mHideDrawable = getResources().getDrawable(
                R.mipmap.login_hide_pwd);
        mShowDrawable = getResources().getDrawable(
                R.mipmap.login_show_pwd);
        mHideDrawable.setBounds(0, 0, mHideDrawable.getIntrinsicWidth(),
                mHideDrawable.getIntrinsicHeight()+10);

        mCurrentDrawable = mHideDrawable;
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /* *@说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     *  getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     *  getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     *  distance 删除图标顶部边缘到控件顶部边缘的距离
     *  distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    if (isShowPwd) {
                        //设置EditText文本为隐藏的
                        this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mHideDrawable.setBounds(0, 0, mHideDrawable.getIntrinsicWidth(),
                                mHideDrawable.getIntrinsicHeight()+10);
                        mCurrentDrawable = mHideDrawable;
                        setClearIconVisible(true);
                    } else {
                        //设置EditText文本为可见的
                        this.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        mShowDrawable.setBounds(0, 0, mShowDrawable.getIntrinsicWidth(),
                                mShowDrawable.getIntrinsicHeight()+10);
                        mCurrentDrawable = mShowDrawable;
                        setClearIconVisible(true);
                    }
                    isShowPwd = !isShowPwd;
                    this.setSelection(TextUtils.isEmpty(this.getText()) ? 0 : this.length());//光标挪到最后
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mCurrentDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
