package com.github.koendeschacht.jsonrpc4j.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.koendeschacht.jsonrpc4j.JsonRpcClient;
import com.github.koendeschacht.jsonrpc4j.JsonRpcServer;
import com.github.koendeschacht.jsonrpc4j.ProxyUtil;
import com.github.koendeschacht.jsonrpc4j.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocalThreadServer<T> extends Thread implements AutoCloseable {
	private static final Logger logger = LoggerFactory.getLogger(LocalThreadServer.class);
	
	private final InputStream serverInput = new PipedInputStream();
	private final OutputStream serverOutput = new PipedOutputStream();
	private final AtomicBoolean isAlive = new AtomicBoolean(true);
	
	private final JsonRpcServer jsonRpcServer;
	private final Class<?> remoteInterface;
	private final Object service;
	
	public LocalThreadServer(Object service, final Class<?> remoteInterface) {
		this.remoteInterface = remoteInterface;
		this.service = service;
		jsonRpcServer = new JsonRpcServer(new ObjectMapper(), service, remoteInterface);
		start();
	}
	
	public void setRequestInterceptor(RequestInterceptor interceptor) {
		jsonRpcServer.setRequestInterceptor(interceptor);
	}
	
	public T client(Class<T> clazz) throws IOException {
		InputStream clientInput = new PipedInputStream((PipedOutputStream) serverOutput);
		OutputStream clientOutput = new PipedOutputStream((PipedInputStream) serverInput);
		return ProxyUtil.createClientProxy(ClassLoader.getSystemClassLoader(), clazz, new JsonRpcClient(), clientInput, clientOutput);
	}
	
	@Override
	public void run() {
		logger.debug("started local thread server for interface {} and handler {}", remoteInterface, service);
		while (isAlive.get()) {
			try {
				final int available = serverInput.available();
				if (available > 0) {
					jsonRpcServer.handleRequest(serverInput, serverOutput);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void close() throws Exception {
		isAlive.set(false);
	}
}
