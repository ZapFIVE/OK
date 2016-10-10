package com.example.libnet.cache;

/**
 * Created by whr on 2016/10/10.
 *
 * 缓存控制接口
 */
public interface ICacheControl {

    /**
     * 缓存文件大小
     *
     * @return
     */
    long size();
    /**
     * 清空缓存
     */
    void clear();
}
