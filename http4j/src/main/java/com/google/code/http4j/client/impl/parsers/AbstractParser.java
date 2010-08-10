package com.google.code.http4j.client.impl.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.google.code.http4j.client.Http;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public abstract class AbstractParser<T> implements Parser<T>, Http {

	private ByteBuffer buffer;
	

	protected AbstractParser() {
		resetBuffer();
	}

	abstract protected int getCapacity();

	abstract protected T parse(byte[] source) throws IOException;

	abstract protected byte[] getEndExpression();

	@Override
	public T parse(InputStream in) throws IOException {
		fillBuffer(in);
		T t = parse(compress());
		resetBuffer();
		return t; 
	}

	private void resetBuffer() {
		buffer = ByteBuffer.allocate(getCapacity());
	}
	
	private void fillBuffer(InputStream in) throws IOException {
		byte b;
		byte[] end = getEndExpression();
		int matched = 0, total = end.length;
		while (matched < total && (b = (byte) in.read()) != -1) {
			if (!buffer.hasRemaining()) {
				extendBuffer();
			}
			buffer.put(b);
			matched = b == end[matched] ? matched + 1 : 0;
		}
		buffer.flip();
	}
	
	private byte[] compress() {
		return new String(buffer.array()).trim().getBytes();
	}

	private void extendBuffer() {
		ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() << 1);
		newBuffer.put(buffer);
		buffer = newBuffer;
	}
}
