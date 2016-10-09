package com.yong.volleyok;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;

/**
 * <b>Project:</b> com.yong.volleyok <br>
 * <b>Create Date:</b> 2016/4/23 <br>
 * <b>Author:</b> qingyong <br>
 * <b>Description:</b> 请求 <br>
 */
public interface IHttpClient {

    /**
     * net请求
     *
     * @param httpRequest
     * @param listener
     * @return
     */
    Request netRequest(HttpRequest httpRequest, HttpListener<NetworkResponse> listener);
}
