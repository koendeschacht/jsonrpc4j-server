package com.github.koendeschacht.jsonrpc4j.server;

import com.github.koendeschacht.jsonrpc4j.JsonRpcMultiServer;
import com.github.koendeschacht.jsonrpc4j.JsonRpcParam;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

import static com.github.koendeschacht.jsonrpc4j.JsonRCPBasicServerProtocol.RESULT;
import static com.github.koendeschacht.jsonrpc4j.util.Util.*;
import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class MultiServiceTest {
	
	private static final String serviceName = "Test";
	@Mock(type = MockType.NICE)
	private ServiceInterfaceWithParamNameAnnotation mockService;
	private JsonRpcMultiServer multiServer;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	@Before
	public void setup() {
		multiServer = new JsonRpcMultiServer();
		multiServer.addService(serviceName, mockService, ServiceInterfaceWithParamNameAnnotation.class);
		byteArrayOutputStream = new ByteArrayOutputStream();
	}
	
	@Test
	public void callMethodExactNumberOfParametersNamed() throws Exception {
		EasyMock.expect(mockService.testMethod(param2)).andReturn("success");
		EasyMock.replay(mockService);
		
		multiServer.handleRequest(messageWithMapParamsStream(serviceName + JsonRpcMultiServer.DEFAULT_SEPARATOR + "testMethod", param1, param2), byteArrayOutputStream);
		
		assertEquals("success", decodeAnswer(byteArrayOutputStream).get(RESULT).textValue());
	}
	
	public interface ServiceInterfaceWithParamNameAnnotation {
		String testMethod(@JsonRpcParam("param1") String param1);
	}
	
}
