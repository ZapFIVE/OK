package com.example.libnet.sample.net;

import com.example.libnet.IConverter;
import com.google.gson.Gson;

import java.nio.charset.Charset;

/**
 * Created by whr on 2016/10/9.
 */
public class GsonConverter implements IConverter {
    /**
     * 默认HEAD
     */
    public static final String DEFAULT_HEAD_KEY = "Content-type";
    public static final String DEFAULT_HEAD_VALUE = "application/json; charset=UTF-8";
    /**
     * 字符语言
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public byte[] requestBodyConverter(Object body) {
        Gson gson = new Gson();
        return gson.toJson(body).getBytes(UTF_8);
    }
}
