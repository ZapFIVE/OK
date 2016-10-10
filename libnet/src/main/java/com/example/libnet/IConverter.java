package com.example.libnet;

/**
 * Created by whr on 2016/10/9.
 *
 * 转换类
 * @param <T>
 */
public interface IConverter<T> {
    /**
     * 请求数据转换
     *
     * @param body 请求body
     * @return 字节数组
     */
    byte[] requestBodyConverter(T body);
}
