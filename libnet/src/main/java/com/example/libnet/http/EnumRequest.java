package com.example.libnet.http;

/**
 * Created by whr on 2016/10/8.
 *
 * 请求枚举
 */
public enum  EnumRequest {
    /**
     * GET（SELECT）：从服务器取出资源（一项或多项）。
     */
    GET,
    /**
     * POST（CREATE）：在服务器新建一个资源。
     */
    POST,
    /**
     * PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
     */
    PUT,
    /**
     * PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
     */
    PATCH,
    /**
     * DELETE（DELETE）：从服务器删除资源。
     */
    DELETE,
}
