package com.example.libnet.sample;

import android.support.annotation.NonNull;

import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.IConverter;
import com.example.libnet.converter.impl.StringConverter;
import com.example.libnet.http.EnumRequest;
import com.example.libnet.sample.net.TestBaseProtocol;

/**
 * Created by whr on 2016/10/10.
 * 百度Api中找了一个POST的
 * http://apistore.baidu.com/apiworks/servicedetail/2563.html
 */
public class KeyExtraProtocol extends TestBaseProtocol<String> {

    @Override
    public String getPath() {
        return "http://apis.baidu.com/showapi_open_bus/key_extra/key_ex";
    }

    @NonNull
    @Override
    public IConverter getConverterStrategy() {
        return new StringConverter();
    }

    /**
     * @param apiKey
     * @param text
     * @param callback
     */
    public void request(String apiKey, String text, BaseProtocolCallback callback) {
        putHead("apikey", apiKey);
        putHead("Content-Type", "application/x-www-form-urlencoded");
        putParams("text", text);
        request(EnumRequest.POST, getPath(), callback);
    }
}
