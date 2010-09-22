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

import java.io.IOException;
import java.io.OutputStream;

import com.google.code.http4j.Connection;
import com.google.code.http4j.ConnectionManager;
import com.google.code.http4j.CookieCache;
import com.google.code.http4j.Request;
import com.google.code.http4j.RequestExecutor;
import com.google.code.http4j.Response;
import com.google.code.http4j.metrics.ThreadLocalMetricsRecorder;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class BasicRequestExecutor implements RequestExecutor {
	
	protected final ConnectionManager connectionManager;

	protected final CookieCache cookieCache;

	protected final ResponseParser responseParser;

	public BasicRequestExecutor(ConnectionManager connectionCache, CookieCache cookieCache, ResponseParser responseParser) {
		this.connectionManager = connectionCache;
		this.cookieCache = cookieCache;
		this.responseParser = responseParser;
	}

	@Override
	public Response execute(Request request) throws InterruptedException, IOException {
		ThreadLocalMetricsRecorder.getInstance().reset();
		Connection connection = connectionManager.acquire(request.getHost());
		try {
			send(connection, request);
			Response response = retrieveResponse(connection);
			cookieCache.set(request.getURI(), response.getHeaders());
			return response;
		} catch (IOException e) {
			connection.close();
			throw e;
		}
	}

	protected Response retrieveResponse(Connection connection) throws IOException {
		Response response = responseParser.parse(connection.getInputStream());
		ThreadLocalMetricsRecorder.getInstance().getResponseTimer().stop();
		connection.setReusable(response.getStatusLine().keepAlive());
		connectionManager.release(connection);
		response.setMetrics(ThreadLocalMetricsRecorder.getInstance().captureMetrics());
		return response;
	}

	protected void send(Connection connection, Request request) throws IOException {
		addCookie(request);
		OutputStream out = connection.getOutputStream();
		out.write(request.toMessage());
		out.flush();
		ThreadLocalMetricsRecorder.getInstance().getRequestTimer().stop();
	}

	protected void addCookie(Request request) {
		String cookie = cookieCache.get(request.getURI());
		if (null != cookie) {
			request.setCookie(cookie);
		}
	}
}
