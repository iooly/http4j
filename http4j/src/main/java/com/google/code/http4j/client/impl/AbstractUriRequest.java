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

import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public abstract class AbstractUriRequest extends AbstractHttpRequest {

	private static final long serialVersionUID = -8545690642344763108L;
	
	public AbstractUriRequest(String _url) throws MalformedURLException,
			UnknownHostException {
		super(_url);
	}

	@Override
	public boolean hasEntity() {
		return false;
	}
	
	@Override
	protected String formatBody() {
		return "";
	}

	@Override
	protected String getURI() {
		return parameters.isEmpty() ? getPath() : calculateURI();
	}
}