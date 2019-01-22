package com.github.koendeschacht.jsonrpc4j;

import com.github.koendeschacht.jsonrpc4j.spring.rest.JsonRpcRestClient;

public class ProxyTestUtil extends ProxyUtil {

    public static <T> T createClientProxy(Class<T> clazz, JsonRpcRestClient client) {
        return createClientProxy(clazz.getClassLoader(), clazz, client);
    }

}
