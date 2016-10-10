package com.example.libnet.converter.impl;

import com.example.libnet.converter.BaseConverter;

/**
 * Created by whr on 2016/10/10.
 */
public class StringConverter extends BaseConverter<String>{
    @Override
    public byte[] requestBodyConverter(String body) {
        return body.getBytes(UTF_8);
    }
}
