package com.yong.volleyok;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yong.volleyok.okhttp.OkHttp3Stack;
import com.yong.volleyok.request.NetRequest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * <b>Project:</b> com.yong.volleyok.http <br>
 * <b>Create Date:</b> 2016/4/23 <br>
 * <b>Author:</b> qingyong <br>
 * <b>Description:</b> 请求的具体实现类 <br>
 */
public class HttpClient implements IHttpClient {

    private final RequestQueue mRequestQueue;
    private final Context mContext;

    public HttpClient(Context context) {
        mContext = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // FIXME 信任所有证书，该实现存在风险
        allowAllSSLOkHttp(builder);
        OkHttpClient client = builder.build();

        mRequestQueue = Volley.newRequestQueue(context,
                new OkHttp3Stack(client));
    }
    @Override
    public Request netRequest(HttpRequest httpRequest, HttpListener<NetworkResponse> listener) {
        NetRequest request = new NetRequest(httpRequest, listener);
        mRequestQueue.add(request);
        return request;
    }

    /**
     * FIXME 信任所有证书，该实现存在风险
     * https://gist.github.com/shibenli/8f28871972c15dc46ddb3f86556ee117
     *
     * @param builder
     */
    public static void allowAllSSLOkHttp(OkHttpClient.Builder builder) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
            }, new SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
