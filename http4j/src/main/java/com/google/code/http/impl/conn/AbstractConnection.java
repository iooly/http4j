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

package com.google.code.http.impl.conn;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.code.http.Connection;
import com.google.code.http.Host;
import com.google.code.http.utils.IOUtils;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public abstract class AbstractConnection implements Connection {
	
	protected Host host;
	
	public AbstractConnection(Host host) {
		this.host = host;
	}
	
	abstract protected int read(ByteBuffer buffer) throws IOException;
	
	@Override
	public byte[] read() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1 << 17);
		ByteBuffer extended = ByteBuffer.allocate(1 << 18);
		while (read(buffer) == buffer.capacity()) {
			// Increasing buffer's capacity reduces the chance to get here
			extended = ensureSpace(buffer, extended);
			IOUtils.transfer(buffer, extended);
		}
		return IOUtils.extract(extended.position() == 0 ? buffer : extended);
	}
	
	protected ByteBuffer ensureSpace(ByteBuffer src, ByteBuffer dest) {
		return dest.remaining() < src.position() ? IOUtils.extendBuffer(dest): dest;
	}

	@Override
	public Host getHost() {
		return host;
	}
}
