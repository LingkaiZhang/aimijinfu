package com.yuanin.aimifinance.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class GeneralAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> datas;
	protected LayoutInflater inflater;
	protected int layoutId;

	public GeneralAdapter(Context context, List<T> datas, int layoutId) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.datas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获取PantoViewHolder实例
		GeneralViewHolder holder = GeneralViewHolder.getInstance(context, convertView, parent, layoutId, position);
		// 给控件赋值
		convert(holder, getItem(position));
		// 返回View
		return holder.getConvertView();
	}

	public abstract void convert(GeneralViewHolder holder, T data);

}
