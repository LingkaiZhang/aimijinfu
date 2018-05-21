package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.WebViewActivity;
import com.yuanin.aimifinance.entity.NoticeEntity;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/21
 * @time 14:03
 * @desc 热点资讯list
 */
public class NoticeListAdapter extends GeneralAdapter<NoticeEntity> {

    public NoticeListAdapter(List<NoticeEntity> list, Context context) {
        super(context, list, R.layout.adapter_notice_list);
    }

    @Override
    public void convert(GeneralViewHolder holder, final NoticeEntity data) {
        holder.setTextForTextView(R.id.tvDate, data.getIssucedate());
        holder.setTextForTextView(R.id.tvTitle, data.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("entityID", data.getId());
                intent.putExtra(ParamsKeys.TYPE, ParamsValues.NOTICE_DETAIL);
                context.startActivity(intent);
            }
        });
    }
}
