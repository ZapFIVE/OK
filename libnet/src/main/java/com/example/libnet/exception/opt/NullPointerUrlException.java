package com.example.libnet.exception.opt;

/**
 * Created by whr on 2016/10/10.
 *
 * 没有设置BaseUrl
 */
public class NullPointerUrlException extends RuntimeException {
    private static final long serialVersionUID = -3372478990122620533L;

    public NullPointerUrlException() {
        super();
    }

    public NullPointerUrlException(String message) {
        super(message);
    }
}
