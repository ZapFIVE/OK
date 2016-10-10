package com.example.libnet.exception.opt;

/**
 * Created by whr on 2016/10/10.
 *
 * 没有设置缓存策略
 */
public class NullPointerCacheException extends RuntimeException{
    private static final long serialVersionUID = -4895373817050597704L;

    public NullPointerCacheException() {
        super();
    }

    public NullPointerCacheException(String message) {
        super(message);
    }
}
