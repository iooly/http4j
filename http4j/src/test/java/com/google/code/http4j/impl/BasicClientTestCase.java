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

package com.google.code.http4j.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.code.http4j.Response;
import com.google.code.http4j.utils.Metrics;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public final class BasicClientTestCase {
	
	private BasicClient client;
	
	@BeforeClass
	public void beforeClass() {
		client = new BasicClient();
	}
	
	@Test
	public void noConnectionPool() throws InterruptedException, IOException, URISyntaxException {
		client.noConnectionPool();
		Response response = client.get("http://www.baidu.com/");
		Metrics metrics = response.getMetrics();
		Assert.assertFalse(metrics.isCachedConnection());
	}
	
	@Test(dependsOnMethods = "noConnectionPool")
	public void noDNSCache() throws InterruptedException, IOException, URISyntaxException {
		client.noDNSCache();
		Response response = client.get("http://www.baidu.com/");
		Metrics metrics = response.getMetrics();
		Assert.assertTrue(metrics.getDnsLookupCost() > 0);
		Response response2 = client.get("http://www.baidu.com/img/baidu_logo.gif");
		Metrics metrics2 = response2.getMetrics();
		Assert.assertFalse(metrics2.equals(metrics));
		Assert.assertTrue(metrics2.getDnsLookupCost() > 0);
	}
}
