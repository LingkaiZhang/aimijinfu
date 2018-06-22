package com.yuanin.aimifinance.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.RepayCalenderEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.UserAccountEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewActivity extends BaseActivity
        implements CalendarView.OnYearChangeListener,
        CalendarView.OnDateSelectedListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;

    private int mYear;
    CalendarLayout mCalendarLayout;

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvWaitFund)
    private TextView tvWaitFund;
    @ViewInject(R.id.tvAleardyFund)
    private TextView tvAleardyFund;
    @ViewInject(R.id.tvNewLoan)
    private TextView tvNewLoan;

    private int currentYear = 0;
    private int currentMouth = 0;
    private Calendar mCalendar;
    private Context context = CalendarViewActivity.this;
    private List<RepayCalenderEntity> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        x.view().inject(this);
        initTopBar(getString(R.string.loan_calendar),toptitleView, true);
        initView();

    }



    private void initData() {
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int curDay = mCalendarView.getCurDay();
        schemes.add(getSchemeCalendar(year, month, curDay, 0, "假"));
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                String day = mList.get(i).getDate().split("-")[2];
                int mday = Integer.parseInt(day);
                schemes.add(getSchemeCalendar(mCalendar.getYear(),mCalendar.getMonth(),mday,0,""));
            }
        }
        mCalendarView.setSchemeDate(schemes);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    private void initView() {
        setMIUIStatusBarDarkMode();
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });

        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        mCalendar = calendar;
//        Toast.makeText(this,"点击了:" + calendar.getMonth() + "月" + calendar.getDay() + "日",Toast.LENGTH_LONG).show();
        if (currentYear == calendar.getYear() && currentMouth == calendar.getMonth()) {
            setDateinfo(mList);
        } else {
            currentYear = calendar.getYear();
            currentMouth = calendar.getMonth();
            requestdata(calendar);
        }

    }

    private void requestdata(Calendar calendar) {
        String date = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay() ;

        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_USER_REPAY_CALENDER);
            obj.put("date",date);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<RepayCalenderEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<RepayCalenderEntity> entity = (ReturnResultEntity<RepayCalenderEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        mList = entity.getData();
                        setDateinfo(mList);
                    } else {
                        //当月没有出借信息
                        if(mList != null) {
                            mList.clear();
                        }
                        setDateinfo(mList);
                        AppUtils.showToast(CalendarViewActivity.this, entity.getRemark());
                    }
                } else {
                    AppUtils.showToast(CalendarViewActivity.this, entity.getRemark());
                }
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(CalendarViewActivity.this);
            }
        });
    }

    private void setDateinfo(List<RepayCalenderEntity> mList) {
        tvWaitFund.setText("0.00");
        tvAleardyFund.setText("0.00");
        tvNewLoan.setText("0.00");
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                String date = mList.get(i).getDate();
                String[] split = date.split("-");
                if (Integer.parseInt(split[2]) == mCalendar.getDay() && Integer.parseInt(split[1]) == mCalendar.getMonth()) {
                    tvWaitFund.setText(mList.get(i).getReceiving());
                    tvAleardyFund.setText(mList.get(i).getReceived());
                    tvNewLoan.setText(mList.get(i).getNewCapital());
                }
            }
        }
        initData();
    }

    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private void setMIUIStatusBarDarkMode() {
        if (true) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
