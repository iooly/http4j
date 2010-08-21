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

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.code.http4j.client.HttpClient;
import com.google.code.http4j.client.HttpResponse;
import com.google.code.http4j.client.impl.BasicDnsCache;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class MetricHttpClientTestCase {
	
	/*
	 * While test suite executes, the other cases might also access thread local metrics.
	 * Thus some metrics in that thread might have initialized values.
	 * So we need to test it in separated thread.
	 */
	@Test
	public void testMetricsInSeparatedThread() throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<Boolean> result = service.submit(new Callable<Boolean>(){
			@Override
			public Boolean call() throws Exception {
				try {
					testMetrics();
					return true;
				} catch(Exception e) {
					return false;
				}
			}});
		Assert.assertTrue(result.get());
	}

	@Test
	public void testCacheDns() throws IOException, URISyntaxException {
		MetricHttpClient client = new MetricHttpClient();
		String host = "www.csdn.net";
		InetAddress ip = InetAddress.getByName(host);
		BasicDnsCache.getInstance().cache(host, ip);
		client.head(host);
		Timer dns = ThreadLocalMetrics.getInstance().getDnsTimer();
		Assert.assertEquals(dns.getTimeCost(), 0);
	}
	
	private void testMetrics() throws IOException, URISyntaxException {
		HttpClient client = new MetricHttpClient();
		assertionTimers(false);
		Metrics metrics = ThreadLocalMetrics.getInstance();
		Counter<Long> requestTrafficCounter = metrics.getRequestTrafficCounter();
		Counter<Long> responseTrafficCounter = metrics.getResponseTrafficCounter();
		Assert.assertEquals(requestTrafficCounter.get(), Long.valueOf(0));
		Assert.assertEquals(responseTrafficCounter.get(), Long.valueOf(0));
		HttpResponse response = client.get("http://www.bing.com", false);
		Assert.assertNotNull(response);
		assertionTimers(true);
		long sent = requestTrafficCounter.get(), received = responseTrafficCounter.get();
		System.out.println("Sent :" + sent);
		System.out.println("Received :" + received);
		Assert.assertTrue(sent > 0);
		Assert.assertTrue(received > 0);
	}
	
	private List<Timer> getTimers() {
		List<Timer> timers = new ArrayList<Timer>();
		ThreadLocalMetrics metrics = ThreadLocalMetrics.getInstance();
		timers.add(metrics.getDnsTimer());
		timers.add(metrics.getConnectionTimer());
		timers.add(metrics.getRequestTimer());
		timers.add(metrics.getResponseTimer());
		return timers;
	}
	
	private void assertionTimers(boolean flag) {
		List<Timer> timers = getTimers();
		for(Timer timer: timers) {
			System.out.println(timer.getTimeCost()/1000000 + "ms");
			Assert.assertEquals(timer.getTimeCost() > 0, flag);
		}
	}
}
