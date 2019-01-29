package com.yuanin.aimifinance.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.dialog.LoadingDialog;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.inter.IXUtilsDownloadFileCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;


/**
 * @author huangxin
 * @date 2015/10/16
 * @time 16:38
 * @desc 网络请求工具类
 */
public class NetUtils {
    /**
     * 将参数封装成名字叫obj的Json数据，传给服务器（外层封装）
     * 根据现在接口的传输要求，客户端需要封装两次参数，第一次是封装成请求数据需要的字段参数，第二次是将参数封装成名字叫obj的Json
     *
     * @param obj ：第一次封装的请求数据需要的字段参数
     * @return xutils的参数对象
     */
    public static RequestParams initParams(JSONObject obj) {
        RequestParams params = new RequestParams(ParamsValues.NET_URL);
        try {
            if (null != obj) {
                // 需去除转义符
                params.addBodyParameter("obj", obj.toString().replace("\"[", "[").replace("\\", "").replace("]\"", "]"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("入参", params.toString());
        return params;
    }

    public static void request(final JSONObject obj, final Type mType, final IHttpRequestCallBack httpRequestCallBack) {
        // 封装参数
        RequestParams params = initParams(obj);
        x.http().post(params, new Callback.ProgressCallback<String>() {

            @Override
            public void onStarted() {
                httpRequestCallBack.onStart();
            }

            @Override
            public void onSuccess(final String result) {
                try {
                    LogUtils.d("出参", result);
                    Object object = new Gson().fromJson(result, mType);
                    httpRequestCallBack.onSuccess(object);
                } catch (JsonSyntaxException e) {
                    uploadErrorMessage(result, e.toString(), obj.toString());
                    httpRequestCallBack.onFailure();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpRequestCallBack.onFailure();
            }

            @Override
            public void onFinished() {
                httpRequestCallBack.onFinish();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onWaiting() {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }
        });
    }


    public static void request(JSONObject obj) {
        // 封装参数
        RequestParams params = initParams(obj);
        // 请求
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void request(final JSONObject obj, final Type mType, int timeOut, final IHttpRequestCallBack httpRequestCallBack) {
        // 封装参数
        RequestParams params = initParams(obj);
        params.setConnectTimeout(timeOut);
        params.setMaxRetryCount(0);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                try {
                    LogUtils.d("出参", result);
                    Object object = new Gson().fromJson(result, mType);
                    httpRequestCallBack.onSuccess(object);
                } catch (JsonSyntaxException e) {
                    uploadErrorMessage(result, e.toString(), obj.toString());
                    httpRequestCallBack.onFailure();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpRequestCallBack.onFailure();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                httpRequestCallBack.onFinish();
            }
        });
    }


    public static void request(final Context context, final JSONObject obj, final Type mType, final IHttpRequestCallBack httpRequestCallBack) {
        final LoadingDialog ld = new LoadingDialog(context, R.style.loading_dialog, true, null);
        // 封装参数
        RequestParams params = initParams(obj);
        final Callback.Cancelable cancelable = x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onStarted() {
                ld.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onSuccess(final String result) {
                try {
                    LogUtils.d("出参", result);
                    Object object = new Gson().fromJson(result, mType);
                    httpRequestCallBack.onSuccess(object);
                } catch (JsonSyntaxException e) {
                    uploadErrorMessage(result, e.toString(), obj.toString());
                    httpRequestCallBack.onFailure();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpRequestCallBack.onFailure();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (ld.isShowing()) {
                    ld.dismiss();
                }
            }

            @Override
            public void onWaiting() {
            }

        });
        ld.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelable.cancel();
                ld.dismiss();
            }
        });
    }


    public static Callback.Cancelable downloadFile(Context context, String url, String path, final IXUtilsDownloadFileCallBack ixUtilsDownloadFileCallBack) {
        // 封装参数
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        Callback.Cancelable cancelable = x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onStarted() {
                ixUtilsDownloadFileCallBack.onStart();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                ixUtilsDownloadFileCallBack.onLoading((int) total, (int) current);
            }

            @Override
            public void onSuccess(final File result) {
                ixUtilsDownloadFileCallBack.onSuccess();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("hx",ex.getMessage());
                ixUtilsDownloadFileCallBack.onFailure();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                ixUtilsDownloadFileCallBack.onFinish();
            }

            @Override
            public void onWaiting() {
            }
        });
        return cancelable;
    }

    private static void uploadErrorMessage(String result, String error, String params) {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_ERROR_MSG);
            obj.put(ParamsKeys.RESULT, result);
            obj.put(ParamsKeys.ERROR, error);
            obj.put(ParamsKeys.PARAMS, params);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        request(obj);
    }
}