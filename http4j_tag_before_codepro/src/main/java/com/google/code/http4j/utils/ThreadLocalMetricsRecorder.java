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

package com.google.code.http4j.utils;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 * 
 */
public class ThreadLocalMetricsRecorder implements MetricsRecorder {

	protected static final ThreadLocal<ThreadLocalMetricsRecorder> local = new ThreadLocal<ThreadLocalMetricsRecorder>();

	protected Timer blockingTimer;
	protected Timer dnsTimer;
	protected Timer connectionTimer;
	protected Timer requestTimer;
	protected Timer responseTimer;
	protected Timer sslTimer;
	protected Counter<Long> requestTransportCounter;
	protected Counter<Long> responseTransportCounter;

	protected ThreadLocalMetricsRecorder() {
		init();
		local.set(this);
	}

	public static ThreadLocalMetricsRecorder getInstance() {
		ThreadLocalMetricsRecorder recorder = local.get();
		return recorder == null ? new ThreadLocalMetricsRecorder() : recorder;
	}

	protected void init() {
		blockingTimer = new NanoSecondTimer();
		dnsTimer = new NanoSecondTimer();
		connectionTimer = new NanoSecondTimer();
		requestTimer = new NanoSecondTimer();
		responseTimer = new NanoSecondTimer();
		sslTimer = new NanoSecondTimer();
		requestTransportCounter = new LongCounter();
		responseTransportCounter = new LongCounter();
	}

	@Override
	public Timer getBlockingTimer() {
		return blockingTimer;
	}

	@Override
	public Timer getDnsTimer() {
		return dnsTimer;
	}

	public Timer getConnectionTimer() {
		return connectionTimer;
	}

	@Override
	public Timer getRequestTimer() {
		return requestTimer;
	}

	@Override
	public Timer getResponseTimer() {
		return responseTimer;
	}

	@Override
	public Counter<Long> getRequestTransportCounter() {
		return requestTransportCounter;
	}

	@Override
	public Counter<Long> getResponseTransportCounter() {
		return responseTransportCounter;
	}

	@Override
	public Timer getSslTimer() {
		return sslTimer;
	}

	@Override
	public void reset() {
		dnsTimer.reset();
		connectionTimer.reset();
		requestTimer.reset();
		responseTimer.reset();
		sslTimer.reset();
		requestTransportCounter.reset();
		responseTransportCounter.reset();
	}

	@Override
	public Metrics captureMetrics() {
		return new BasicMetrics(blockingTimer.getDuration(), dnsTimer
				.getDuration(), connectionTimer.getDuration(), requestTimer
				.getDuration(), responseTimer.getStart()
				- requestTimer.getStop(), responseTimer.getDuration(), sslTimer
				.getDuration(), requestTransportCounter.get(),
				responseTransportCounter.get());
	}
}
