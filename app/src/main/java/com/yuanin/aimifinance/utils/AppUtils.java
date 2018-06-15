package com.yuanin.aimifinance.utils;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.HKRegisterWebActivity;
import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.activity.OpenAccountActivity;
import com.yuanin.aimifinance.activity.WebViewActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.dialog.ShareDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
import com.yuanin.aimifinance.entity.InviteFriendTopEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.ShareContentEntity;
import com.yuanin.aimifinance.entity.ShareGridEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 15:09
 * @desc 通用工具类
 */
public class AppUtils {

    private static GeneralDialog safeDialog;

    public static String getToken(JSONObject obj) {
        Iterator<String> iterator = obj.keys();
        List<String> list = new ArrayList<String>();
        String[] arrays = new String[obj.length()];
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = list.get(i);
        }
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(arrays, com);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arrays.length; i++) {
            try {
                if (obj.get(arrays[i]) instanceof JSONObject) {
                    String str = getToken((JSONObject) obj.get(arrays[i]));
                    sb.append(str);
                } else if ((obj.get(arrays[i]) instanceof String)) {
                    sb.append(obj.get(arrays[i]));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * // 显示右上角pop
     *
     * @param mRightPopView pop布局
     * @return
     */

    public static PopupWindow createPop(View mRightPopView, int style) {
        final PopupWindow mRightPop = new PopupWindow(mRightPopView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mRightPop.setAnimationStyle(style);
        mRightPop.setFocusable(false);
        return mRightPop;
    }

    public static PopupWindow createHKPop(View popView, final Context context) {
        final PopupWindow mPop = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mPop.setAnimationStyle(R.style.SignPopupWindowAnimation);
        mPop.setFocusable(false);
        ImageView ivClose = (ImageView) popView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        Button btnGo = (Button) popView.findViewById(R.id.btnGo);
        if (StaticMembers.HK_STATUS == 0) {
            btnGo.setText("立即开户");
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPop.dismiss();
                    context.startActivity(new Intent(context, OpenAccountActivity.class));
                }
            });
        } else if (StaticMembers.HK_STATUS == 2) {
            btnGo.setText("立即激活");
            btnGo.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             JSONObject obj = AppUtils.getPublicJsonObject(true);
                                             try {
                                                 obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                                                 obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_ACTIVATE_UESR);
                                                 String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                                                 obj.put(ParamsKeys.TOKEN, token);
                                                 obj.remove(ParamsKeys.KEY);
                                             } catch (JSONException e) {
                                                 e.printStackTrace();
                                             }
                                             Type mType = new TypeToken<ReturnResultEntity<HKRegisterEntity>>() {
                                             }.getType();
                                             NetUtils.request(context, obj, mType, new IHttpRequestCallBack() {
                                                         @Override
                                                         public void onStart() {

                                                         }

                                                         @Override
                                                         public void onFinish() {

                                                         }

                                                         @Override
                                                         public void onSuccess(Object object) {
                                                             ReturnResultEntity<HKRegisterEntity> entity = (ReturnResultEntity<HKRegisterEntity>) object;
                                                             if (entity.isSuccess(context)) {
                                                                 if (entity.isNotNull()) {
                                                                     Intent intent = new Intent(context, HKRegisterWebActivity.class);
                                                                     intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                                                                     context.startActivity(intent);
                                                                     mPop.dismiss();
                                                                 }
                                                             } else {
                                                                 AppUtils.showToast(context, entity.getRemark());
                                                             }
                                                         }

                                                         @Override
                                                         public void onFailure() {
                                                             AppUtils.showConectFail(context);
                                                         }
                                                     }
                                             );
                                         }
                                     }

            );
        }
        return mPop;
    }


    public static PopupWindow createQNPop(View popView, final Context context) {
        final PopupWindow mPop = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mPop.setAnimationStyle(R.style.SignPopupWindowAnimation);
        mPop.setFocusable(false);
        ImageButton ivClose = (ImageButton) popView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        Button btnGo = (Button) popView.findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(context, WebViewActivity.class);
                intent4.putExtra(ParamsKeys.TYPE, ParamsValues.QUESTION_NAIRE);
                intent4.putExtra(ParamsKeys.USER_ID,StaticMembers.USER_ID);
                context.startActivity(intent4);
                mPop.dismiss();
            }
        });

        return mPop;
    }

    public static PopupWindow createSLPop(View popView, final Context context ) {
        final PopupWindow mPop = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mPop.setAnimationStyle(R.style.SignPopupWindowAnimation);
        mPop.setFocusable(false);
        ImageButton ivClose = (ImageButton) popView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });

        return mPop;
    }


    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 保存数据至SharedPreferences
     */
    public static void save2SharedPreferences(Context context, String fileName, String dataName, String data) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(dataName, data);
        editor.apply();
    }

    /**
     * 获取数据从SharedPreferences
     */
    public static String getFromSharedPreferences(Context context, String fileName, String dataName) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String data = sharedPreferences.getString(dataName, "");
        return data;
    }

    /**
     * 删除SharedPreferences数据
     */
    public static void deleteSharedPreferences(Context context, String fileName) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 隐藏电话号码
     *
     * @param phoneNumber
     * @return
     */
    public static String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }

    /**
     * 判断edit是否输入内容
     */
    public static boolean isEditTextInputSome(EditText edit) {
        return edit.getText().toString().trim().length() > 0;
    }

    /**
     * 显示toast
     */
    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 显示toast
     */
    public static void showMiddleToast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示toast
     */
    public static void showConectFail(final Context context) {
        if (context != null) {
            AppUtils.showToast(context, context.getResources().getString(R.string.connect_server_fail));
        }
    }
    /**
     * 显示toast2
     */
    public static void showConectFail2(final Context context) {
        if (context != null) {
//            AppUtils.showToast(context, context.getResources().getString(R.string.connect_server_fail));
            safeDialog = new GeneralDialog(context, true, "提示", "您的账号已与服务器断开连接,您可以检查网络或重新登录进行重试.", "取消", "重新登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    safeDialog.dismiss();

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    safeDialog.dismiss();
                    AppUtils.exitLogin(context);
                    context.startActivity(new Intent(context, LoginActivity.class));

                }
            });

        }
    }

    public static void showNoMore(Context context) {
        if (context != null) {
            AppUtils.showToast(context, context.getResources().getString(R.string.connect_no_more));
        }
    }

    public static void showRequestFail(Context context) {
        if (context != null) {
            AppUtils.showToast(context, context.getResources().getString(R.string.connect_request_fail));
        }
    }

    public static void initBooleanData(Context context) {
        //是否储存登录信息
        String userid = AppUtils.getFromSharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_USERID);
        if (userid.length() > 0) {
            String mobile = AppUtils.getFromSharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_MOBILE);
            String login_token = AppUtils.getFromSharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOKEN);
            StaticMembers.IS_NEED_LOGIN = false;
            StaticMembers.USER_ID = userid;
            StaticMembers.MOBILE = mobile;
            StaticMembers.LOGIN_TOKEN = login_token;
            StaticMembers.RSA_MOBILE = rsaEncode(context, mobile);
            StaticMembers.TOTAL_MONEY = AppUtils.getFromSharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOTAL);
        } else {
            StaticMembers.IS_NEED_LOGIN = true;
        }
        //导航页
        String guideStr = AppUtils.getFromSharedPreferences(context, ParamsKeys.GUIDE_FILE, ParamsKeys.GUIDE_KEY);
        StaticMembers.IS_NEED_GUDIE = !guideStr.equals(ParamsValues.GUIDE_VALUE);
        //手势密码
        String gesturePwd = AppUtils.getFromSharedPreferences(context, ParamsKeys.GESTURE_SHARE_FILE_NAME,
                ParamsKeys.GESTURE_SHARE_DATA_NAME);
        StaticMembers.IS_NEED_GUSTURE_PWD = gesturePwd.length() > 0;
        //指纹密码
        String fingerStr = AppUtils.getFromSharedPreferences(context, ParamsKeys.FINGER_PWD_SHARE_FILE_NAME,
                ParamsKeys.FINGER_PWD_SHARE_DATA_NAME);
        StaticMembers.IS_NEED_FINGER_PWD = fingerStr.equals("1");
    }

    //rsa加密
    public static String rsaEncode(Context context, String source) {
        String afterencrypt = "";
        try {
            // 从文件中得到公钥
            InputStream inPublic = context.getResources().getAssets().open("aimilicai_public.pem");
            PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
            // 加密
            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            afterencrypt = Base64.encodeToString(encryptByte, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        afterencrypt = afterencrypt.replaceAll("\n", "");
        return afterencrypt;
    }

    //退出登录
    public static void exitLogin(Context context) {
        save2SharedPreferences(context, ParamsKeys.LOGIN_MBOILE_FILE, ParamsKeys.LOGIN_MBOILE, StaticMembers.MOBILE);
        deleteSharedPreferences(context, ParamsKeys.GESTURE_SHARE_FILE_NAME);
        deleteSharedPreferences(context, ParamsKeys.LOGIN_FILE);
        deleteSharedPreferences(context, ParamsKeys.FINGER_PWD_SHARE_FILE_NAME);
        deleteSharedPreferences(context,ParamsKeys.USER_INFO_FILE);
        initBooleanData(context);
        //刷新首页登录状态
        EventMessage eventMessage2 = new EventMessage();
        eventMessage2.setType(EventMessage.UPDATE_INDEX_LOGIN);
        EventBus.getDefault().post(eventMessage2);
    }

    // 启动其他程序
    public static void runApp(Context context, String packageName) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List apps = pManager.queryIntentActivities(
                    resolveIntent, 0);

            ResolveInfo ri = (ResolveInfo) apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入数据到SD卡中
     */
    public static void writeData2SDCard(String path, InputStream data) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(path);
            byte buffer[] = new byte[2 * 1024];          //每次写2K数据
            int temp;
            while ((temp = data.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();    //关闭数据流操作
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 四舍五入
     *
     * @param d
     * @param len
     * @return
     */

    //0.7083
    public static double round(double d, int len) {     // 进行四舍五入
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.
                ROUND_DOWN).doubleValue();
    }



    /**
     * 加法
     *
     * @param a
     * @param b
     * @return
     */
    public static double add(double a, double b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.add(b2).doubleValue();
    }

    /**
     * 减法
     *
     * @param a
     * @param b
     * @return
     */
    public static double subtract(double a, double b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 乘法
     *
     * @param a
     * @param b
     * @return
     */
    public static double multiply(double a, double b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除法
     *
     * @param a
     * @param b
     * @return
     */
    public static double divide(double a, double b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算应还总利息
     *
     * @param amount       < 借款总金额 >
     * @param qty          还款时间 < 12个月、40天 >
     * @param unit         < 单位;m表示月;d表示天;如果unit为d只能到期还本付息 >
     * @param repay_method < 还款方式;1表示先息后本、2表示等额本息、3表示到期还本付息 >
     * @param annual       年化收益 < 不带%;如:7.5 >
     */
    public static double calcInterest(double amount, double annual, int qty, String unit, int repay_method) {

        //总利息
        double totalinterest = 0;
        if (repay_method != 2) {
            //先息后本、到期还本付息

            //一年利息
            totalinterest = divide(multiply(amount, annual), 100);

            //如果按月来计算
            if (unit.equals("个月")) {
                //一个月利息
                totalinterest = divide(totalinterest, 12);
            } else if (unit.equals("天")) {
                //按天来计算(一年按360天来计算利息)
                //一天利息
                totalinterest = divide(totalinterest, 360);
            }
            //投资的利息
            totalinterest = multiply(totalinterest, qty);
        } else {
            //等额本息
            //每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            annual = divide(annual, 100);
            double monthannual = divide(annual, 12);//月利率
            //获得基数
            double base = 0;
            for (int i = 1; i <= qty; ++i) {
                if (i == 1) {
                    base = add(1, monthannual);
                } else {
                    base = multiply(base, add(1, monthannual));
                }
            }
            double monthamount = divide(multiply(multiply(amount, monthannual), base), subtract(base, 1));

            //总的应还利息(月供额乘以期数-本金)
            totalinterest = subtract(multiply(monthamount, qty), amount);
        }
        totalinterest = round(totalinterest, 2);
        return totalinterest;
    }

    public static void shareInviteFrient(InviteFriendTopEntity inviteFriendTopEntity, Context context, List<ShareGridEntity> shareList, int gridNum) {
        if (inviteFriendTopEntity != null) {
            ShareContentEntity shareContentEntity = new ShareContentEntity();
            shareContentEntity.setImageUrl(inviteFriendTopEntity.getSharelogo());
            shareContentEntity.setText(inviteFriendTopEntity.getSharedescript());
            shareContentEntity.setTitleUrl(inviteFriendTopEntity.getShareurlurl());
            shareContentEntity.setTitle(inviteFriendTopEntity.getSharetitle());
            shareContentEntity.setUrl(inviteFriendTopEntity.getShareurlurl());
            new ShareDialog(context, shareContentEntity, shareList, gridNum);
        } else {
            showToast(context, "获取分享信息失败，请重试");
        }
    }

    public static Object fail2SetData(String key) {
        Object entity = StaticMembers.aCache.getAsObject(key);
        return entity;
    }

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static boolean isExistBoolean(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String formatDouble(String format, double num) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(num);
    }

    //获取图片资源转化位图方法
    public static Bitmap getBitmap(Context context, int resID) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resID);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        return bm;
    }

    public static void openApk(Context context, String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String formatToDay(long ms) {//将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = "" + day;
        String strHour = "" + hour;
        String strMinute = "" + minute;
        String strSecond = "" + second;
        return strDay + "天" + strHour + "小时" + strMinute + "分" + strSecond + "秒";
    }

    public static JSONObject getPublicJsonObject(boolean isNeedID) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ParamsKeys.KEY, ParamsValues.KEY);
            obj.put(ParamsKeys.APPID, ParamsValues.APP_ID);
            obj.put(ParamsKeys.VERSION_NUM, ParamsValues.VERSION_CODE);
            if (isNeedID) {
                obj.put(ParamsKeys.USER_ID, StaticMembers.USER_ID);
                obj.put(ParamsKeys.LOGIN_TOKEN, StaticMembers.LOGIN_TOKEN);
                obj.put(ParamsKeys.MOBILE, StaticMembers.RSA_MOBILE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        long totalBlocks;
        long availableBlocks;
        // 由于API18（Android4.3）以后getBlockSize过时并且改为了getBlockSizeLong
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    public static void openKeyboard(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }


    public static void closeKeyboard(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void setCurrentDateToTopTitle(View topView) {
        TextView tvDay = (TextView) topView.findViewById(R.id.tvDay);
        TextView tvMonth = (TextView) topView.findViewById(R.id.tvMonth);
        TextView tvWeek = (TextView) topView.findViewById(R.id.tvWeek);
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        tvDay.setText(AppUtils.dateToStr(date, "dd"));
        if (AppUtils.dateToStr(date, "M").equals("1")) {
            tvMonth.setText("一月");
        } else if (AppUtils.dateToStr(date, "M").equals("2")) {
            tvMonth.setText("二月");
        } else if (AppUtils.dateToStr(date, "M").equals("3")) {
            tvMonth.setText("三月");
        } else if (AppUtils.dateToStr(date, "M").equals("4")) {
            tvMonth.setText("四月");
        } else if (AppUtils.dateToStr(date, "M").equals("5")) {
            tvMonth.setText("五月");
        } else if (AppUtils.dateToStr(date, "M").equals("6")) {
            tvMonth.setText("六月");
        } else if (AppUtils.dateToStr(date, "M").equals("7")) {
            tvMonth.setText("七月");
        } else if (AppUtils.dateToStr(date, "M").equals("8")) {
            tvMonth.setText("八月");
        } else if (AppUtils.dateToStr(date, "M").equals("9")) {
            tvMonth.setText("九月");
        } else if (AppUtils.dateToStr(date, "M").equals("10")) {
            tvMonth.setText("十月");
        } else if (AppUtils.dateToStr(date, "M").equals("11")) {
            tvMonth.setText("十一月");
        } else if (AppUtils.dateToStr(date, "M").equals("12")) {
            tvMonth.setText("十二月");
        }
        tvWeek.setText(AppUtils.dateToStr(date, "EEE"));
    }


    //检查网络设置跳转页面
    public static void checkNetwork (Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }


//    /**
//     * 获取版本号
//     *
//     * @return 当前应用的版本号
//     */
//    public static String getVersion(Context context) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//            String version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未知";
//        }
//    }
}
