package com.example.libnet.sample.net;

import com.example.libnet.BaseProtocol;

/**
 * Created by whr on 2016/10/8.
 *
 * 测试 使用，基类工程
 */
public abstract class TestBaseProtocol<T> extends BaseProtocol<T> {
    public TestBaseProtocol() {
        super(NetConfig.INSTANCE);
    }

}
