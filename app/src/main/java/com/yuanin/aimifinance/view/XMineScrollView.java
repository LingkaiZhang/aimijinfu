package com.yuanin.aimifinance.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.yuanin.aimifinance.R;


/**
 * @author markmjw
 * @date 2013-10-08
 */
public class XMineScrollView extends ScrollView implements OnScrollListener {
//    private static final String TAG = "XScrollView";

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;

    // when pull up >= 50px
    private final static int PULL_LOAD_MORE_DELTA = 50;

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;

    // used for scroll back
    private Scroller mScroller;
    // user's scroll listener
    private OnScrollListener mScrollListener;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    // the interface to trigger refresh and load more.
    private IXScrollViewListener mListener;

    private LinearLayout mLayout;
    private LinearLayout mContentLayout;

    private XMineHeaderView mHeader;
    // header view content, use it to calculate the Header's height. And hide it when disable pull refresh.
    private RelativeLayout mHeaderContent;
    //    private TextView mHeaderTime;
    private int mHeaderHeight;


    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private boolean mPullLoading = false;
    private boolean isNeedLan = true;

    public XMineScrollView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XMineScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XMineScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    public void setIsNeedLan(boolean isNeedLan) {
        this.isNeedLan = isNeedLan;
    }

    private void initWithContext(Context context) {
        mLayout = (LinearLayout) View.inflate(context, R.layout.vw_xscrollview_layout, null);
        mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);

        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XScrollView need the scroll event, and it will dispatch the event to user's listener (as a proxy).
        this.setOnScrollListener(this);

        // init header view
        mHeader = new XMineHeaderView(context);
        mHeaderContent = (RelativeLayout) mHeader.findViewById(R.id.header_content);
        LinearLayout headerLayout = (LinearLayout) mLayout.findViewById(R.id.header_layout);
        headerLayout.addView(mHeader);


        // 获取头部高度
        ViewTreeObserver vto = mHeaderContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mHeaderHeight = mHeaderContent.getHeight();
            }
        });

        ViewTreeObserver observer = mHeader.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
//                    mHeaderHeight = mHeaderContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
        this.addView(mLayout);
    }

    /**
     * Set the content ViewGroup for XScrollView.
     *
     * @param content
     */
    public void setContentView(ViewGroup content) {
        if (mLayout == null) {
            return;
        }

        if (mContentLayout == null) {
            mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);
        }

        if (mContentLayout.getChildCount() > 0) {
            mContentLayout.removeAllViews();
        }
        mContentLayout.addView(content);
    }

    /**
     * Set the content View for XScrollView.
     *
     * @param content
     */
    public void setView(View content) {
        if (mLayout == null) {
            return;
        }

        if (mContentLayout == null) {
            mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);
        }
        mContentLayout.addView(content);
    }

    /**
     * Enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;

        // disable, hide the content
        mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }


    /**
     * Set listener.
     *
     * @param listener
     */
    public void setIXScrollViewListener(IXScrollViewListener listener) {
        mListener = listener;
    }

    /**
     * Auto call back refresh.
     */
    public void autoRefresh() {
        mHeader.setVisibleHeight(mHeaderHeight);
        mPullRefreshing = true;
        refresh();
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());
        // scroll to top each time
        post(new Runnable() {
            @Override
            public void run() {
                XMineScrollView.this.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void resetHeaderHeight() {
        int height = mHeader.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderHeight) {
            finalHeight = mHeaderHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if (isTop() && (mHeader.getVisibleHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();

                }
                break;

            default:
                // reset
                mLastY = -1;
                resetHeaderOrBottom();
                break;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }


    private void resetHeaderOrBottom() {
        if (isTop()) {
            // invoke refresh
            if (mEnablePullRefresh && mHeader.getVisibleHeight() > mHeaderHeight) {
                mPullRefreshing = true;
                refresh();
            }
            resetHeaderHeight();
        }
    }

    private boolean isTop() {
        return getScrollY() <= 0 || mHeader.getVisibleHeight() > mHeaderHeight || mContentLayout.getTop() > 0;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLL_BACK_HEADER) {
                mHeader.setVisibleHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // send to user's listener
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void refresh() {
        if (mEnablePullRefresh && null != mListener) {
            mListener.onRefresh();
        }
    }

    /**
     * You can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    /**
     * Implements this interface to get refresh/load more event.
     */
    public interface IXScrollViewListener {
        void onRefresh();

        void onLoadMore();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isNeedLan) {
            boolean isLan = false;
            if (mLastY == -1) {
                mLastY = ev.getRawY();
            }
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = ev.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    isLan = Math.abs(deltaY) > 10;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;

                case MotionEvent.ACTION_UP:
                    break;
            }
            return isLan;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
