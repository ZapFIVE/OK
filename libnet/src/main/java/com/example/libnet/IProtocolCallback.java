package com.example.libnet;

import com.example.libnet.http.EnumProtocolStatus;
import com.example.libnet.http.HttpResponse;

/**
 * Created by whr on 2016/10/9.
 */
public interface IProtocolCallback {
    /**
     * 协议操作回调，解析由上层完成实现，底层无操作
     *
     * @param protocol 本次协议对象
     * @param response   返回类实体
     */
    void onResponse(com.example.libnet.BaseProtocol protocol, HttpResponse response);

    /**
     * 请求出错
     *
     * @param protocol 本次协议对象
     * @param throwable 异常信息
     */
    void onError(com.example.libnet.BaseProtocol protocol, Throwable throwable);

    /**
     * 请求协议的状态变更
     *
     * @param protocol
     * @param status
     */
    void onStatusChange(com.example.libnet.BaseProtocol protocol, EnumProtocolStatus status);
}
