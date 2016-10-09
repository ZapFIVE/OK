package com.example.libnet.sample;

import com.example.libnet.BaseProtocolCallback;
import com.example.libnet.http.EnumRequest;
import com.example.libnet.sample.net.TestBaseProtocol;


/**
 * Created by whr on 2016/10/8.
 *
 * https://developer.github.com/v3/search/#search-repositories
 * GET /search/repositories
 *
 * https://api.github.com/search/repositories?q=tetris+language:assembly&sort=stars&order=desc
 *
 */
public class GitHubSearchProtocol extends TestBaseProtocol {

    /**
     * Sort type
     */
    public static final String TYPE_SORT_STARS = "stars";
    public static final String TYPE_SORT_FORKS = "forks";
    public static final String TYPE_SORT_UPDATED = "updated";

    /**
     * order type
     */
    public static final String TYPE_ORDER_ASC = "asc";
    public static final String TYPE_ORDER_DESC = "desc";

    @Override
    public String getPath() {
        return "/search/repositories";
    }

    @Override
    public boolean isUIResponse() {
        return true;
    }

    /**
     * 请求数据
     *
     * @param query
     * @param sort
     * @param order
     */
    public void request(String query, String sort, String order, BaseProtocolCallback callback) {
        putParams("q", query);
        putParams("sort", sort);
        putParams("order", order);

        request(EnumRequest.GET, callback);
    }
}
