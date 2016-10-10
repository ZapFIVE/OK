package com.example.libnet.exception.opt;

/**
 * Created by whr on 2016/10/10.
 *
 * 无法生成正确的网络代理类
 */
public class IllegalLibNetException extends RuntimeException {
    private static final long serialVersionUID = -6119118012307340223L;

    public IllegalLibNetException() {
        super();
    }

    public IllegalLibNetException(String message) {
        super(message);
    }
}
