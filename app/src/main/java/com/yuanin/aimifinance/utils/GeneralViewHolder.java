package com.yuanin.aimifinance.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GeneralViewHolder {

	private Context context;
	private SparseArray<View> views;
	private int position;
	private View convertView;

	public GeneralViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.context = context;
		this.position = position;
		this.views = new SparseArray<View>();
		convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		convertView.setTag(this);
	}

	public static GeneralViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new GeneralViewHolder(context, parent, layoutId, position);
		} else {
			GeneralViewHolder holder = (GeneralViewHolder) convertView.getTag();
			holder.position = position;
			return holder;
		}
	}

	public View getConvertView() {
		return convertView;
	}

	/**
	 * 获取ListView的当前位置
	 * 
	 * @return 当前位置
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 *            ：控件ID
	 * @param text
	 *            ：赋值内容
	 * @return 返回自己是为了方便链式编程写法，不过，一般不太用
	 */
	public GeneralViewHolder setTextForTextView(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public GeneralViewHolder setTextAndTypefaceForTextView(int viewId, String text, Typeface typeface) {
		TextView tv = getView(viewId);
		tv.setText(text);
		tv.setTypeface(typeface);
		return this;
	}

	/**
	 * 设置TextView字体颜色
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param ResourceID
	 *            :资源ID
	 */
	public GeneralViewHolder setColorForTextView(int viewId, int ResourceID) {
		TextView tv = getView(viewId);
		tv.setTextColor(ResourceID);
		return this;
	}

	/**
	 * 设置TextView背景
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param ResourceID
	 *            :资源ID
	 */
	public GeneralViewHolder setBackgroundForTextView(int viewId, int ResourceID) {
		TextView tv = getView(viewId);
		tv.setBackgroundResource(ResourceID);
		return this;
	}

	/**
	 * 设置TextView背景
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param ResourceID
	 *            :资源ID
	 */
	public GeneralViewHolder setDrawableRightForTextView(int viewId, int ResourceID) {
		TextView tv = getView(viewId);
		Drawable drawable = context.getResources().getDrawable(ResourceID);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, drawable, null);
		return this;
	}

	/**
	 * 设置TextView背景
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param ResourceID
	 *            :资源ID
	 */
	public GeneralViewHolder setBackgroundColorForImageView(int viewId, int ResourceID) {
		ImageView imageView = getView(viewId);
		imageView.setBackgroundColor(ResourceID);
		return this;
	}

	/**
	 * 设置EditText的值
	 * 
	 * @param viewId
	 *            ：控件ID
	 * @param text
	 *            ：赋值内容
	 * @return 返回自己是为了方便链式编程写法，不过，一般不太用
	 */
	public GeneralViewHolder setTextForEditText(int viewId, String text) {
		EditText et = getView(viewId);
		et.setText(text);
		return this;
	}

	/**
	 * 设置ImageView资源文件
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param ImageResource
	 *            :资源ID
	 */
	public GeneralViewHolder setImageResourceForImageView(int viewId, int ImageResource) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(ImageResource);
		return this;
	}

	/**
	 * 设置View可见度
	 * 
	 * @param viewId
	 *            :控件ID
	 * @param visibility
	 *            ：可见度
	 */
	public GeneralViewHolder setViewVisibilty(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}

	/**
	 * 设置从远程获取的图片
	 * 
	 * @param viewID
	 *            ：控件ID
	 * @param url
	 *            ：路径地址
	 */

//	public void setImageResourceFromRemote(Context context, int viewID, String url) {
//		ImageView imageView = getView(viewID);
//		BitmapUtils bitmapUtils = new BitmapUtils(context);
//		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);// 默认背景图片
//		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);// 加载失败图片
//		bitmapUtils.display(imageView, url);
//	}
}
