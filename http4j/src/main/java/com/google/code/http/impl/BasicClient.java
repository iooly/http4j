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

package com.google.code.http.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.code.http.Client;
import com.google.code.http.ConnectionCache;
import com.google.code.http.CookieCache;
import com.google.code.http.DnsCache;
import com.google.code.http.Request;
import com.google.code.http.RequestExecutor;
import com.google.code.http.Response;
import com.google.code.http.impl.conn.ConnectionPool;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class BasicClient implements Client {

	protected final ConnectionCache connectionCache;

	protected final CookieCache cookieCache;

	protected final ResponseParser responseParser;

	public BasicClient() {
		connectionCache = createConnectionCache();
		cookieCache = createCookieCache();
		responseParser = createResponseParser();
	}

	protected ResponseParser createResponseParser() {
		return new ResponseParser();
	}

	protected CookieCache createCookieCache() {
		return new CookieStoreAdapter();
	}

	protected ConnectionCache createConnectionCache() {
		return new ConnectionPool();
	}

	@Override
	public Response submit(Request request) throws InterruptedException,
			IOException {
		RequestExecutor executor = new BasicRequestExecutor(connectionCache, cookieCache, responseParser);
		return executor.execute(request);
	}
	
	@Override
	public Response get(String url) throws InterruptedException, IOException, URISyntaxException {
		return submit(new Get(url));
	}
	
	@Override
	public Response post(String url) throws InterruptedException, IOException, URISyntaxException {
		return submit(new Post(url));
	}
	
	@Override
	protected void finalize() throws Throwable {
		cookieCache.clear();
		DnsCache.clear();
		connectionCache.shutdown();
		super.finalize();
	}
}
