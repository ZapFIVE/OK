package com.yong.volleyok.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yong.volleyok.HttpListener;
import com.yong.volleyok.HttpRequest;

/**
 * Created by whr on 2016/10/9.
 */
public class NetRequest extends RequestWrapper<NetworkResponse> {

    public NetRequest(HttpRequest httpRequest, HttpListener<NetworkResponse> listener) {
        super(httpRequest, listener);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }
}
