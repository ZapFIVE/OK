package com.example.libnet;

import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;

/**
 * Created by whr on 2016/10/10.
 *
 * 缓存接口
 */
public interface ICache {
    /**
     * 获取缓存
     *
     * @param key 缓存的key
     * @return
     */
    HttpResponse get(HttpRequest key);

    /**
     * 设置缓存
     *
     * @param key   缓存的key
     * @param value 缓存的值
     */
    void put(HttpRequest key, HttpResponse value);
}
