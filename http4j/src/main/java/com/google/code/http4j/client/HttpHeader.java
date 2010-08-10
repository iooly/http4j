package com.google.code.http4j.client;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public interface HttpHeader {
	
	String getName();

	String getValue();
	
	String toCanonicalString();
}
