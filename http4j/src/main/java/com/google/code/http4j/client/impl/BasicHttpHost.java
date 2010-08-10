package com.google.code.http4j.client.impl;


import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.code.http4j.client.HttpHost;
import com.google.code.http4j.client.impl.utils.URLFormatter;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class BasicHttpHost implements HttpHost {
	
	protected String protocol;

	protected int port;
	
	protected InetAddress inetAddress;

	public BasicHttpHost(String protocol, String host, int port) throws UnknownHostException {
		this(protocol, host, port, null);
	}
	
	public BasicHttpHost(String protocol, String host, int port, byte[] address) throws UnknownHostException {
		this.protocol = protocol;
		this.port = port;
		this.inetAddress = processDNSLookup(host, address);
	}
	
	protected InetAddress processDNSLookup(String host, byte[] address) throws UnknownHostException {
		return null == address ? InetAddress.getByName(host) : InetAddress.getByAddress(address);
	}

	@Override
	public InetAddress getInetAddress() {
		return inetAddress;
	}
	
	@Override
	public String getHostName() {
		return inetAddress.getHostName();
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String toString() {
		return URLFormatter.buildURLString(protocol, inetAddress.getHostName(), port, "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inetAddress == null) ? 0 : inetAddress.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicHttpHost other = (BasicHttpHost) obj;
		if (inetAddress == null) {
			if (other.inetAddress != null)
				return false;
		} else if (!inetAddress.equals(other.inetAddress))
			return false;
		if (port != other.port)
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		return true;
	}
}
