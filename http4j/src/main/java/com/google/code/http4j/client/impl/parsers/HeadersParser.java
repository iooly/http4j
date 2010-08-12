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

package com.google.code.http4j.client.impl.parsers;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.code.http4j.client.HttpHeader;
import com.google.code.http4j.client.impl.BasicHttpHeader;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class HeadersParser extends AbstractStreamParser<Map<String, HttpHeader>> {

	@Override
	protected Map<String, HttpHeader> parse(byte[] bytes) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		String line = null;
		Map<String, HttpHeader> headerMap = new HashMap<String, HttpHeader>();
		while((line = reader.readLine()) != null) {
			addHeader(headerMap, parseHeader(line));
		}
		return headerMap;
	}
	
	protected void addHeader(Map<String, HttpHeader> headerMap, HttpHeader header) {
		if(null != header) {
			headerMap.put(header.getCanonicalName(), header);
		}
	}

	protected HttpHeader parseHeader(String line) {
		String[] strings = line.split(":");
		return createHeader(strings[0].trim(), strings[1].trim());
	}

	protected HttpHeader createHeader(String name, String value) {
		return new BasicHttpHeader(name, value);
	}

	@Override
	protected int getCapacity() {
		return 1000;
	}

	@Override
	protected byte[] getEndExpression() {
		return HEADERS_END;
	}
}
