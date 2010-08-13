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

package com.google.code.http4j.client.impl;

import com.google.code.http4j.client.HttpResponse;
import com.google.code.http4j.client.StatusLine;


/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {

	private static final long serialVersionUID = 7490197120431297469L;
	
	protected StatusLine statusLine;
	
	protected String entity;
	
	public BasicHttpResponse(StatusLine statusLine, String entity) {
		this.statusLine = statusLine;
		this.entity = entity;
	}

	@Override
	public String getEntity() {
		return entity;
	}

	@Override
	public StatusLine getStatusLine() {
		return statusLine;
	}

	@Override
	public String format() {
		StringBuilder message = new StringBuilder(formatStatusLine());
		message.append(CRLF).append(formatHeaders());
		message.append(CRLF).append(CRLF);
		message = null == entity ? message : message.append(formatEntity());
		return message.toString();
	}

	protected String formatEntity() {
		// TODO encode
		return entity;
	}

	protected String formatStatusLine() {
		return statusLine.format();
	}

	@Override
	public String toString() {
		return format();
	}
}
