package com.example.libnet.converter;


import com.example.libnet.IConverter;

import java.nio.charset.Charset;

/**
 * Created by whr on 2016/10/10.
 * 转换基类
 *
 * @param <T>
 */
public abstract class BaseConverter<T> implements IConverter<T> {
    /**
     * 字符语言
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
