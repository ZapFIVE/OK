package com.example.libnet.helper;

import com.example.libnet.BaseProtocol;
import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.INetConfig;
import com.example.libnet.IProxyNet;
import com.example.libnet.http.HttpRequest;

/**
 * Created by whr on 2016/10/9.
 *
 * 网络代理基类
 */
public abstract class BaseProxyNet implements IProxyNet{

    /**
     * 构造函数
     *
     * @param config 网络配置类
     */
    public BaseProxyNet(INetConfig config) {
    }

    @Override
    public void cancel(BaseProtocol protocol) {

    }

    @Override
    public void request(BaseProtocol protocol, HttpRequest request, BaseProtocolCallback callback) {

    }

    /**
     * 获取缓存的Key值
     *
     * @param protocol 协议类
     * @return 缓存的key值
     */
    public String getProtocolCacheKey(BaseProtocol protocol) {
        return protocol.getClass().getName() + "$" + protocol.hashCode();
    }
}
