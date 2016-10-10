package com.example.libnet.http;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by whr on 2016/10/9.
 * 返回类实体
 */
public class HttpResponse implements Parcelable {

    /**
     * httpCode码 {@link HttpStatus}实例
     */
    private final int mStatusCode;
    /**
     * 数据，可能为空
     */
    private final byte[] mData;
    /**
     * 请求头
     */
    private final HashMap<String, String> mHeaders;

    /**
     * 构造参数
     *
     * @param statusCode httpCode码 {@link HttpStatus}实例
     * @param data 数据，可能为空
     * @param headers 请求头
     */
    public HttpResponse(int statusCode, byte[] data, HashMap<String, String> headers) {
        this.mStatusCode = statusCode;
        this.mData = data;
        this.mHeaders = headers;
    }

    /**
     * httpCode码 {@link HttpStatus}实例
     *
     * @return httpCode码 {@link HttpStatus}实例
     */
    public int getStatusCode() {
        return mStatusCode;
    }

    /**
     * 数据，可能为空
     *
     * @return 数据，可能为空
     */
    public byte[] getData() {
        return mData;
    }

    /**
     * 请求头
     *
     * @return 请求头
     */
    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStatusCode);
        dest.writeByteArray(this.mData);
        dest.writeSerializable(this.mHeaders);
    }

    protected HttpResponse(Parcel in) {
        this.mStatusCode = in.readInt();
        this.mData = in.createByteArray();
        this.mHeaders = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<HttpResponse> CREATOR = new Parcelable.Creator<HttpResponse>() {
        @Override
        public HttpResponse createFromParcel(Parcel source) {
            return new HttpResponse(source);
        }

        @Override
        public HttpResponse[] newArray(int size) {
            return new HttpResponse[size];
        }
    };
}
