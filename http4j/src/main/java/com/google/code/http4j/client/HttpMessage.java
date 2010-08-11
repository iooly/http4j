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

package com.google.code.http4j.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface HttpMessage extends Formattable, Http, Serializable {
	
	public List<HttpHeader> getHeaders();
	
	void addHeaders(Collection<HttpHeader> headers);
	
	public void addHeaders(HttpHeader... headers);
	
	public void addHeader(String name, String value);

	void addHeader(HttpHeader header);
}
