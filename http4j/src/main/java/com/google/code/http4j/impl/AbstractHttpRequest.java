/**
 * Copyright (C) 2010 Zhang, Guilin <guilin.zhang@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.http4j.impl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.google.code.http4j.HeaderNames;
import com.google.code.http4j.Http;
import com.google.code.http4j.HttpHost;
import com.google.code.http4j.HttpParameter;
import com.google.code.http4j.HttpRequest;
import com.google.code.http4j.impl.utils.URLFormatter;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public abstract class AbstractHttpRequest extends AbstractHttpMessage implements
		HttpRequest {

	private static final long serialVersionUID = 7128951961154306746L;

	protected String encoding;
	protected URI uri;
	protected final HttpHost host;
	protected List<HttpParameter> parameters;

	public AbstractHttpRequest(String _url) throws MalformedURLException,
			UnknownHostException, URISyntaxException {
		this(_url, Http.DEFAULT_CHARSET);
	}

	public AbstractHttpRequest(String _url, String encoding) throws MalformedURLException,
			UnknownHostException, URISyntaxException {
		super();
		this.encoding = encoding;
		URL url = URLFormatter.format(_url);
		uri = url.toURI();
		host = createHttpHost(url.getProtocol(), url.getHost(), url.getPort());
		addDefaultHeaders();
		initParameters(url.getQuery());
	}

	protected void addDefaultHeaders() {
		addHeader(HeaderNames.HOST, uri.getAuthority());
		addHeader(HeaderNames.USER_AGENT, Http.DEFAULT_USER_AGENT);
	}

	@Override
	public void addParameter(String name, String... values) {
		for (String value : values) {
			addParameters(createHttpParameter(name, value));
		}
	}

	@Override
	public void addParameters(HttpParameter... _parameters) {
		for (HttpParameter parameter : _parameters) {
			this.parameters.add(parameter);
		}
	}

	/**
	 * Default behavior, post request should override this method and
	 * {@link #formatBody()}
	 * 
	 * @return
	 */
	protected String calculateURI() {
		return new StringBuilder(getPath()).append("?")
				.append(formatParameters()).toString();
	}

	protected HttpHost createHttpHost(String protocol, String name, int port)
			throws UnknownHostException {
		return new BasicHttpHost(protocol, name, port);
	}

	protected HttpParameter createHttpParameter(String name, String value) {
		return new BasicHttpParameter(name, value);
	}

	@Override
	public String format() {
		StringBuilder message = new StringBuilder(formatRequestLine());
		message.append(Http.CRLF).append(formatHeaders());
		message.append(Http.CRLF).append(formatBody());
		return message.toString();
	}

	abstract protected String formatBody();

	protected String formatParameters() {
		StringBuilder message = new StringBuilder();
		for (HttpParameter parameter : parameters) {
			message.append("&").append(parameter.getName());
			message.append("=").append(encode(parameter.getValue()));
		}
		return message.length() > 0 ? message.substring(1) : "";
	}

	protected String encode(String string) {
		if(null == encoding) {
			return string;
		}
		try {
			return URLEncoder.encode(string, encoding);
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}

	protected String formatRequestLine() {
		return new StringBuilder(getName()).append(Http.BLANK_CHAR)
				.append(getUriString()).append(Http.BLANK_CHAR)
				.append(Http.DEFAULT_HTTP_VERSION).toString();
	}

	@Override
	public HttpHost getHost() {
		return host;
	}

	abstract protected String getName();

	protected String getPath() {
		return uri.getPath();
	}

	@Override
	public URI getUri() {
		return uri;
	}

	abstract protected String getUriString();

	protected void initParameters(String queryString) {
		parameters = new LinkedList<HttpParameter>();
		if (null != queryString) {
			String[] nameValuePairs = queryString.split("&");
			for (String nameValuePair : nameValuePairs) {
				String[] nameAndValue = nameValuePair.split("=");
				addParameter(nameAndValue[0], nameAndValue[1]);
			}
		}
	}
}
