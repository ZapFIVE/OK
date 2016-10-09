package com.example.libnet.cache;

/**
 * Created by whr on 2016/10/8.
 * 缓存接口类
 * @param <K> 唯一标识
 * @param <V> 缓存数据
 */
public interface ICache<K,V> {

    /**
     * 获取缓存
     *
     * @param key 唯一标识
     * @return 缓存数据
     */
    V get(K key);

    /**
     * 存放缓存
     *
     * @param key 唯一标识
     * @param value 缓存数据
     */
    void put(K key, V value);
}
