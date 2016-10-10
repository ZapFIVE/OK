package com.example.libnet.converter.impl;

import com.example.libnet.converter.BaseConverter;
import com.google.gson.Gson;

/**
 * Created by whr on 2016/10/9.
 */
public class GsonConverter extends BaseConverter {
    /**
     * 默认HEAD
     */
    public static final String DEFAULT_HEAD_KEY = "Content-type";
    public static final String DEFAULT_HEAD_VALUE = "application/json; charset=UTF-8";

    @Override
    public byte[] requestBodyConverter(Object body) {
        Gson gson = new Gson();
        return gson.toJson(body).getBytes(UTF_8);
    }
}
