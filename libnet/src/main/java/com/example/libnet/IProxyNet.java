package com.example.libnet;


import com.example.libnet.http.HttpRequest;

/**
 * Created by whr on 2016/10/9.
 *
 * 代理操作网络请求
 */
public interface IProxyNet {

    /**
     * 取消请求
     * @param protocol    协议实例
     */
    void cancel(com.example.libnet.BaseProtocol protocol);

    /**
     * 发起请求
     *
     * @param protocol 协议实例
     * @param request  请求类型
     * @param callback 请求回调
     */
    void request(com.example.libnet.BaseProtocol protocol, HttpRequest request, com.example.libnet.IProtocolCallback callback);
}
