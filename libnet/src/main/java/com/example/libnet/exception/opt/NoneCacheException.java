package com.example.libnet.exception.opt;

/**
 * Created by whr on 2016/10/10.
 *
 * 没有缓存
 */
public class NoneCacheException extends RuntimeException {
    private static final long serialVersionUID = 8134192853791080241L;

    public NoneCacheException() {
        super();
    }

    public NoneCacheException(String message) {
        super(message);
    }
}
