package com.example.libnet.exception.net;

/**
 * Created by kinger on 2016/11/7.
 *
 * 没网络
 */
public class NoNetException extends RuntimeException {

    private static final long serialVersionUID = -2266424646452371926L;

    public NoNetException() {
        super();
    }

    public NoNetException(String message) {
        super(message);
    }
}
