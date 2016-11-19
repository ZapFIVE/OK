package com.example.libnet.http;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by whr on 2016/10/9.
 *
 * 请求类实体
 */
public class HttpRequest implements Parcelable {
    /**
     * 请求类型
     */
    private final EnumRequest mRequest;
    /**
     * 请求地址
     */
    private final String mUrl;

    /**
     * 请求表头列表
     */
    private final HashMap<String, String> mHeadMaps = new HashMap<>();
    /**
     * 请求参数列表
     */
    private final HashMap<String, String> mParamMaps = new HashMap<>();

    /**
     * 请求body 对象
     */
    private final byte[] mBody;

    /**
     * 构造参数
     *
     * @param request 请求类型
     * @param url 请求地址
     * @param heads 请求表头列表
     * @param params 请求参数列表
     * @param body 请求body 对象
     */
    private HttpRequest(EnumRequest request, String url, HashMap<String, String> heads, HashMap<String, String> params, byte[] body) {
        mRequest = request;
        mUrl = url;

        mHeadMaps.clear();
        if (heads != null) {
            mHeadMaps.putAll(heads);
        }

        mParamMaps.clear();
        if (params != null) {
            mParamMaps.putAll(params);
        }
        mBody = body;
    }

    public EnumRequest getRequest() {
        return mRequest;
    }

    public String getUrl() {
        return mUrl;
    }

    public HashMap<String, String> getHeadMaps() {
        return mHeadMaps;
    }

    public HashMap<String, String> getParamMaps() {
        return mParamMaps;
    }

    public byte[] getBody() {
        return mBody;
    }



    /**
     * 构造器{@link HttpRequest}.
     */
    public static final class Builder {
        /**
         * 请求类型
         */
        private EnumRequest mRequest;
        /**
         * 请求地址
         */
        private String mUrl;

        /**
         * 请求表头列表
         */
        private HashMap<String, String> mHeadMaps = null;
        /**
         * 请求参数列表
         */
        private HashMap<String, String> mParamMaps = null;

        /**
         * 请求body对象
         */
        private byte[] mBody = null;

        /**
         * 构造器
         *
         * @param request 请求类型
         * @param url 请求地址
         */
        public Builder(EnumRequest request, String url) {
            mRequest = request;
            mUrl = url;
        }

        /**
         * 设置 请求表头列表
         *
         * @param heads 请求表头
         * @return Builder实例
         */
        public Builder setHeadMaps(HashMap<String, String> heads) {
            mHeadMaps = heads;

            return this;
        }

        /**
         * 设置请求参数列表
         *
         * @param params 请求参数列表
         * @return Builder实例
         */
        public Builder setParamMaps(HashMap<String, String> params) {
           mParamMaps = params;

            return this;
        }

        /**
         * 设置请求body对象
         * @param body 请求body对象
         * @return Builder实例
         */
        public Builder setBody(byte[] body) {
            mBody = body;

            return this;
        }

        /**
         * 生成 {@link HttpRequest}实例
         *
         * @return 生成 {@link HttpRequest}实例
         */
        public HttpRequest build() {
            return new HttpRequest(mRequest, mUrl, mHeadMaps, mParamMaps, mBody);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRequest == null ? -1 : this.mRequest.ordinal());
        dest.writeString(this.mUrl);
        dest.writeSerializable(this.mHeadMaps);
        dest.writeSerializable(this.mParamMaps);
        dest.writeByteArray(this.mBody);
    }

    protected HttpRequest(Parcel in) {
        int tmpMRequest = in.readInt();
        this.mRequest = tmpMRequest == -1 ? null : EnumRequest.values()[tmpMRequest];
        this.mUrl = in.readString();
        HashMap<String, String> heads = (HashMap<String, String>) in.readSerializable();
        HashMap<String, String> params = (HashMap<String, String>) in.readSerializable();

        mHeadMaps.clear();
        if (heads != null) {
            mHeadMaps.putAll(heads);
        }

        mParamMaps.clear();
        if (params != null) {
            mParamMaps.putAll(params);
        }
        this.mBody = in.createByteArray();
    }

    public static final Creator<HttpRequest> CREATOR = new Creator<HttpRequest>() {
        @Override
        public HttpRequest createFromParcel(Parcel source) {
            return new HttpRequest(source);
        }

        @Override
        public HttpRequest[] newArray(int size) {
            return new HttpRequest[size];
        }
    };
}
