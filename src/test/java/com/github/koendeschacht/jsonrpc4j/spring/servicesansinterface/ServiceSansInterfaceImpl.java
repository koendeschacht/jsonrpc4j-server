package com.github.koendeschacht.jsonrpc4j.spring.servicesansinterface;


import com.github.koendeschacht.jsonrpc4j.JsonRpcService;

/**
 * <p>Unlike the {@link com.github.koendeschacht.jsonrpc4j.spring.service.Service} /
 * {@link com.github.koendeschacht.jsonrpc4j.integration.StreamServerTest.ServiceImpl} example, this case has no interface
 * so the bean has the @JsonRpcService directly into the implementation.  This setup worked
 * in jsonrpc4j 1.1, but failed in 1.2.</p>
 */

@JsonRpcService("ServiceSansInterface")
public class ServiceSansInterfaceImpl {
}
