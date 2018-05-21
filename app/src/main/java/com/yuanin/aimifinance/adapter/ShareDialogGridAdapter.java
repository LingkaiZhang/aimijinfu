package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.ShareGridEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 13:16
 * @desc 分享dialog grid
 */
public class ShareDialogGridAdapter extends GeneralAdapter<ShareGridEntity> {

    public ShareDialogGridAdapter(List<ShareGridEntity> list, Context context) {
        super(context, list, R.layout.adapter_dialog_share);
    }

    @Override
    public void convert(GeneralViewHolder holder, ShareGridEntity data) {
        TextView tv = holder.getView(R.id.tvItem);
        tv.setText(data.getTextName());
        Drawable drawable = context.getResources().getDrawable(data.getIconId());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null, drawable, null, null);
    }
}
