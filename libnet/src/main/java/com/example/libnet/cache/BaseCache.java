package com.example.libnet.cache;

import com.example.libnet.ICache;
import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;

/**
 * Created by whr on 2016/10/10.
 *
 * Cache操作基类
 */
public abstract class BaseCache implements ICache, ICacheControl{
    @Override
    public void put(HttpRequest key, HttpResponse value) {

    }

    @Override
    public HttpResponse get(HttpRequest key) {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void clear() {

    }
}
