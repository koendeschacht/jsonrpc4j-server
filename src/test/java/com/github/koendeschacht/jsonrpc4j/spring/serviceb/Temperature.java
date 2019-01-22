package com.github.koendeschacht.jsonrpc4j.spring.serviceb;


import com.github.koendeschacht.jsonrpc4j.JsonRpcService;

@JsonRpcService(
		"api/temperature" // note the absence of a leading slash
)
public interface Temperature {
	
	@SuppressWarnings("unused")
	Integer currentTemperature();
	
}
