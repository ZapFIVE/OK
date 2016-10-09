package com.example.libnet.sample.net;

import com.example.libnet.BaseProtocol;
import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.http.HttpResponse;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by whr on 2016/10/9.
 *
 * 具体的解析类
 */
public abstract class TestBaseProtocolCallback<T> extends BaseProtocolCallback{
    /**
     * 字符语言
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public void onResponse(BaseProtocol protocol, HttpResponse response) {
        Gson gson = new Gson();
        try {
            Type superclass = this.getClass().getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            Type t = parameterized.getActualTypeArguments()[0];
            //将json转成泛型的类
            T result = gson.fromJson(new String(response.getData(), UTF_8), t);
            onSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            onError(protocol, e);
        }
    }

    @Override
    public void onError(BaseProtocol protocol, Throwable throwable) {
        onFail(-1);
    }

    /**
     * 请求成功
     *
     * @param data
     */
    public void onSuccess(T data) {

    }

    /**
     * 请求失败
     *
     * @param errorCode
     */
    public void onFail(int errorCode) {

    }
}
