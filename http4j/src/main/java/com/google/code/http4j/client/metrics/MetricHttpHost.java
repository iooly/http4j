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

package com.google.code.http4j.client.metrics;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.code.http4j.client.impl.BasicHttpHost;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public class MetricHttpHost extends BasicHttpHost {
	
	public MetricHttpHost(String _host) throws UnknownHostException {
		this(PROTOCOL_HTTP, _host, -1);
	}

	public MetricHttpHost(String protocol, String _host, int port)
			throws UnknownHostException {
		this(protocol, _host, port, null);
	}

	public MetricHttpHost(String protocol, String _host, int port, byte[] address)
			throws UnknownHostException {
		super(protocol, _host, port, address);
	}
	
	public InetAddress lookupDNS(String _host, byte[] address) throws UnknownHostException {
		Timer timer = ThreadLocalMetrics.getInstance().getDNSTimer();
		timer.startTimer();
		InetAddress ip = super.lookupDNS(_host, address);
		timer.stopTimer();
		return ip;
	}
}
