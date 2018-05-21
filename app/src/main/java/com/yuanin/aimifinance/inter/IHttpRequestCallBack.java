package com.yuanin.aimifinance.inter;

public interface IHttpRequestCallBack {
    //请求开始的回调方法
    void onStart();

    //请求结束的回调方法
    void onFinish();

    //请求成功的回调方法
    void onSuccess(Object object);

    //请求失败的回调方法
    void onFailure();
}
