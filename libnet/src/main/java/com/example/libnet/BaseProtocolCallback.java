package com.example.libnet;

import com.example.libnet.http.HttpResponse;

/**
 * Created by whr on 2016/10/8.
 *
 * 协议操作的回调
 */
public abstract class BaseProtocolCallback {
    /**
     * 协议操作回调，解析由上层完成实现，底层无操作
     *
     * @param protocol 本次协议对象
     * @param response   返回类实体
     */
    public abstract void onResponse(BaseProtocol protocol, HttpResponse response);

    /**
     * 请求出错
     *
     * @param protocol 本次协议对象
     * @param throwable 异常信息
     */
    public abstract void onError(BaseProtocol protocol, Throwable throwable);
}
