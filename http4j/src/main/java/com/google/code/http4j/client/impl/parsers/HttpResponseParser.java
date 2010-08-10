package com.google.code.http4j.client.impl.parsers;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.code.http4j.client.HttpHeader;
import com.google.code.http4j.client.HttpResponse;
import com.google.code.http4j.client.StatusLine;
import com.google.code.http4j.client.impl.BasicHttpResponse;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public abstract class HttpResponseParser implements Parser<HttpResponse>{
	
	protected StatusLine statusLine;
	
	protected Map<String, HttpHeader> headerMap;
	
	protected String entity;
	
	public HttpResponse parse(InputStream in) throws IOException {
		statusLine = parseStatusLine(in);
		headerMap = parseHeaders(in);
		entity = parseEntity(in);
		return createHttpResponse();
	}

	protected HttpResponse createHttpResponse() {
		HttpResponse response = new BasicHttpResponse(statusLine, entity);
		response.addHeaders(headerMap.values());
		return response;
	}

	protected StatusLine parseStatusLine(InputStream in) throws IOException {
		return new StatusLineParser().parse(in);
	}
	
	protected Map<String, HttpHeader> parseHeaders(InputStream in) throws IOException {
		return new HeadersParser().parse(in);
	}

	abstract protected String parseEntity(InputStream in);
}
