package com.example.libnet.helper;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.libnet.BaseProtocol;
import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.INetConfig;
import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;
import com.yong.volleyok.HttpClient;
import com.yong.volleyok.HttpListener;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by whr on 2016/10/9.
 */
public class ProxyVolley extends BaseProxyNet{
    /**
     * 回调池
     */
    private final ConcurrentHashMap<String, Request> mCallbackCache = new ConcurrentHashMap<>();

    /**
     * 请求队列
     */
    private HttpClient mClient;

    public ProxyVolley(INetConfig config) {
        super(config);
        mClient = new HttpClient(config.getContext());
    }

    @Override
    public void cancel(BaseProtocol protocol) {
        Request call = mCallbackCache.remove(getProtocolCacheKey(protocol));
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public int chooseFromType(HttpRequest request) {
        int method = -1;
        switch (request.getRequest()) {
            case GET:
                method = Request.Method.GET;
                break;
            case POST:
                method = Request.Method.POST;
                break;
            case PUT:
                method = Request.Method.PUT;
                break;
            case PATCH:
                method = Request.Method.PATCH;
                break;
            case DELETE:
                method = Request.Method.DELETE;
                break;

            default:
                break;
        }

        return method;
    }

    @Override
    public void request(final BaseProtocol protocol, final HttpRequest request, final BaseProtocolCallback callback) {
        com.yong.volleyok.HttpRequest httpRequest = new com.yong.volleyok.HttpRequest.Builder(request.getUrl())
            .setMethod(chooseFromType(request))
            .addheader(request.getHeadMaps())
            .addParam(request.getParamMaps())
            .build();
        Request call = mClient.netRequest(httpRequest, new HttpListener<NetworkResponse>() {
            @Override
            public void onSuccess(NetworkResponse result) {
                callback.onResponse(protocol, new HttpResponse(result.statusCode, result.data, new HashMap<>(result.headers)));
            }

            @Override
            public void onError(VolleyError error) {
                callback.onError(protocol, error);
            }
        });

        mCallbackCache.put(getProtocolCacheKey(protocol), call);
    }
}
