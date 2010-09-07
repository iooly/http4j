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

package com.google.code.http.metrics;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class AggregatedMetricsRecorder extends AbstractMetricsRecorder {

	public AggregatedMetricsRecorder() {
		super();
	}

	public void aggregate(MetricsRecorder metrics) {
		((AggregatedTimer) connectionTimer).aggregate(metrics.getConnectionTimer());
		((AggregatedTimer) dnsTimer).aggregate(metrics.getDnsTimer());
		((AggregatedTimer) requestTimer).aggregate(metrics.getRequestTimer());
		((AggregatedTimer) responseTimer).aggregate(metrics.getResponseTimer());
		((AggregatedCounter<Long>) requestTransportCounter).aggregate(metrics.getRequestTransportCounter());
		((AggregatedCounter<Long>) responseTransportCounter).aggregate(metrics.getResponseTransportCounter());
	}

	@Override
	protected Counter<Long> createLongCounter() {
		return new AtomicLongCounter();
	}
	
	@Override
	protected Counter<Integer> createIntegerCounter() {
		return new AtomicIntegerCounter();
	}

	@Override
	protected Timer createTimer() {
		return new AggregatedNanoSecondTimer();
	}
}
