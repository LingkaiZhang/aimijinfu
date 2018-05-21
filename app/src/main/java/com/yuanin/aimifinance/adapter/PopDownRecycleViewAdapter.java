package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuanin.aimifinance.R;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 13:16
 * @desc 下拉poplist
 */
public class PopDownRecycleViewAdapter extends RecyclerView.Adapter<PopDownRecycleViewAdapter.MyViewHolder> {
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private int index;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;

        public MyViewHolder(View view) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
        }

    }

    public PopDownRecycleViewAdapter(List<String> datas, Context context) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    public void setCurrentSelect(int index) {
        this.index = index;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_down_pop, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tvTime.setText(mDatas.get(position));
        if (holder.getPosition() == index) {
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        } else {
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.text_light_gray));
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
