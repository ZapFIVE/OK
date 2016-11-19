package com.example.libnet;

import com.example.libnet.http.HttpRequest;

/**
 * Created by kinger on 2016/11/7.
 *
 * 协议的拦截器
 */
public interface IInterceptor {
    /**
     * 在处理请求之前拦截 它、 长用于测试.
     *
     * @param protocol
     * @param request
     * @param callback
     * @return
     */
    void request(final BaseProtocol protocol, final HttpRequest request, final IProtocolCallback callback);
}
