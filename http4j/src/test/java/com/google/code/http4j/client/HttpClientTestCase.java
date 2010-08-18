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


import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.code.http4j.client.impl.BasicHttpClient;
import com.google.code.http4j.client.impl.HttpHead;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class HttpClientTestCase {
	
	private HttpClient client;
	
	@BeforeClass
	public void setUp() {
		client = new BasicHttpClient();
	}
	
	@Test
	public void testExecute() throws IOException {
		HttpRequest request = new HttpHead("http://www.google.com");
		byte[] response = client.execute(request);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length > 0);
	}
	
	@Test
	public void testSubmit() throws IOException {
		HttpRequest request = new HttpHead("http://www.google.com");
		HttpResponse response = client.submit(request);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getStatusLine());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertTrue(response.getHeaders().size() > 0);
		Assert.assertNull(response.getEntity());
	}
	
	@Test
	public void testHead() throws IOException {
		HttpResponse response = client.head("http://www.google.com");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getStatusLine());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertTrue(response.getHeaders().size() > 0);
		Assert.assertNull(response.getEntity());
	}
	
	@Test
	public void testExecuteHead() throws IOException {
		byte[] response = client.executeHead("http://www.google.com");
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length > 0);
	}
	
	@Test
	public void testGet() throws IOException {
		HttpResponse response = client.get("http://www.google.com");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getStatusLine());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertTrue(response.getHeaders().size() > 0);
		Assert.assertNotNull(response.getEntity());
	}
	
	@Test
	public void testExecuteGet() throws IOException {
		byte[] response = client.executeGet("http://www.google.com");
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length > 0);
	}
	
	@Test
	public void testPost() throws IOException {
		HttpResponse response = client.post("http://www.javaeye.com/login?name=http4j&password=http4j");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getStatusLine());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertTrue(response.getHeaders().size() > 0);
		Assert.assertNotNull(response.getEntity());
	}
	
	@Test
	public void testExecutePost() throws IOException {
		byte[] response = client.executePost("http://www.javaeye.com/login?name=http4j&password=http4j");
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length > 0);
	}
}
