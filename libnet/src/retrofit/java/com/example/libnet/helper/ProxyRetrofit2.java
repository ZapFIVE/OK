package com.example.libnet.helper;

import com.example.libnet.BaseProtocol;
import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.INetConfig;
import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by whr on 2016/10/9.
 *
 * 网络代理类 用Retrofit实现
 */
public class ProxyRetrofit2 extends BaseProxyNet{

    /**
     * 回调池
     */
    private final ConcurrentHashMap<String, Call<ResponseBody>> mCallbackCache = new ConcurrentHashMap<>();

    /**
     * 代理协议
     */
    public interface IProxyApi {
        /**
         * 代理Get请求
         *
         * @param url 请求地址
         * @param heads 请求头
         * @param params 请求参数
         * @return 返回数据结果
         */
        @GET
        Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> heads, @QueryMap Map<String, String> params);

        /**
         * 代理POST请求
         *
         * @param url 请求地址
         * @param heads 请求头
         * @param params 请求参数
         * @param body 请求body 对象
         * @return 返回数据结果
         */
        @POST
        Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> heads, @QueryMap Map<String, String> params, @Body byte[] body);

        /**
         * 代理PUT请求
         *
         * @param url 请求地址
         * @param heads 请求头
         * @param params 请求参数
         * @param body 请求body 对象
         * @return 返回数据结果
         */
        @PUT
        Call<ResponseBody> put(@Url String url, @HeaderMap Map<String, String> heads, @QueryMap Map<String, String> params, @Body byte[] body);

        /**
         * 代理PATCH请求
         *
         * @param url 请求地址
         * @param heads 请求头
         * @param params 请求参数
         * @param body 请求body 对象
         * @return 返回数据结果
         */
        @PATCH
        Call<ResponseBody> patch(@Url String url, @HeaderMap Map<String, String> heads, @QueryMap Map<String, String> params, @Body byte[] body);

        /**
         * 代理DELETE请求
         *
         * @param url
         * @param heads
         * @param params
         * @return
         */
        @DELETE
        Call<ResponseBody> delete(@Url String url, @HeaderMap Map<String, String> heads, @QueryMap Map<String, String> params);
    }
    /**
     * 操作执行retrofit单位
     */
    private Retrofit mRetrofit = null;

    /**
     * 构造函数
     *
     * @param config 网络配置类
     */
    public ProxyRetrofit2(INetConfig config) {
        super(config);

        mRetrofit = new Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .build();
    }

    @Override
    public void cancel(BaseProtocol protocol) {
        Call<ResponseBody> call = mCallbackCache.remove(getProtocolCacheKey(protocol));
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            call.cancel();
        }
    }

    @Override
    public void request(final BaseProtocol protocol, HttpRequest request, final BaseProtocolCallback callback) {
        IProxyApi api = mRetrofit.create(IProxyApi.class);
        Call<ResponseBody> call = chooseFromType(api, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 添加表头
                HashMap<String, String> heads = null;
                Headers headers = response.headers();

                if (headers != null && headers.size() != 0) {
                    heads = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        heads.put(headers.name(i), headers.value(i));
                    }
                }
                try {
                    callback.onResponse(protocol, new HttpResponse(response.code(), response.body().bytes(), heads));
                } catch (IOException e) {
                    callback.onError(protocol, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(protocol, t);
            }
        });

        mCallbackCache.put(getProtocolCacheKey(protocol), call);
    }

    /**
     * 根据请求类型决定
     *
     * @param api IProxyApi 实例
     * @param request HttpRequest 实例
     * @return 请求执行对象 Call
     */
    private Call<ResponseBody> chooseFromType(IProxyApi api, HttpRequest request) {
        Call<ResponseBody> call = null;

        switch (request.getRequest()) {
            case GET:
                call = api.get(request.getUrl(), request.getHeadMaps(), request.getParamMaps());
                break;

            case POST:
                call = api.post(request.getUrl(), request.getHeadMaps(), request.getParamMaps(), request.getBody());
                break;

            case PUT:
                call = api.put(request.getUrl(), request.getHeadMaps(), request.getParamMaps(), request.getBody());
                break;

            case PATCH:
                call = api.patch(request.getUrl(), request.getHeadMaps(), request.getParamMaps(), request.getBody());
                break;

            case DELETE:
                call = api.delete(request.getUrl(), request.getHeadMaps(), request.getParamMaps());
                break;

            default:
                break;
        }

        return call;
    }
}
