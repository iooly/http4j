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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.http4j.client.Connection;
import com.google.code.http4j.client.Http;
import com.google.code.http4j.client.HttpHost;
import com.google.code.http4j.client.impl.utils.IOUtils;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 * 
 */
public abstract class AbstractConnection implements Connection {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected HttpHost host;

	public AbstractConnection(HttpHost host) {
		this.host = host;
	}

	@Override
	public HttpHost getHost() {
		return host;
	}

	protected SocketAddress getSocketAddress(HttpHost host)
			throws UnknownHostException {
		int port = host.getPort();
		port = (port < 0) ? (host.getProtocol().equalsIgnoreCase(
				Http.PROTOCOL_HTTP) ? 80 : 443) : port;
		return new InetSocketAddress(host.getInetAddress(), port);
	}
	
	@Override
	public byte[] read() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1 << 14);
		ByteBuffer extended = ByteBuffer.allocate(1 << 15);
		while (read(buffer) == buffer.capacity()) {
			// Increasing buffer's capacity reduces the chance to get here
			extended = ensureSpace(buffer, extended);
			IOUtils.transfer(buffer, extended);
		}
		byte[] result = IOUtils.extract(extended.position() == 0 ? buffer : extended);
		logger.debug("HTTP Response <<\r\n{}", new String(result));
		return result;
	}
	
	abstract protected int read(ByteBuffer buffer) throws IOException;

	protected ByteBuffer ensureSpace(ByteBuffer src, ByteBuffer dest) {
		return dest.remaining() < src.position() ? IOUtils.extendBuffer(dest): dest;
	}
}
