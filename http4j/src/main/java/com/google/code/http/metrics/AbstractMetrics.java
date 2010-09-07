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
public abstract class AbstractMetrics implements Metrics {
	
	protected long connectingCost;
	protected long dnsLookupCost;
	protected long receivingCost;
	protected long sendingCost;
	protected long waitingCost;
	protected long bytesSent;
	protected long bytesReceived;
	
	public AbstractMetrics(MetricsRecorder recorder) {
		connectingCost = getTimeCost(recorder.getConnectionTimer());
		dnsLookupCost = getTimeCost(recorder.getDnsTimer());
		receivingCost = getTimeCost(recorder.getResponseTimer());
		sendingCost = getTimeCost(recorder.getRequestTimer());
		waitingCost = calculateWaiting(recorder.getRequestTimer(), recorder.getResponseTimer());
		bytesSent = recorder.getRequestTransportCounter().get().longValue();
		bytesReceived = recorder.getResponseTransportCounter().get().longValue();
	}
	
	protected long getTimeCost(Timer timer) {
		return timer.getDuration();
	}
	
	protected long calculateWaiting(Timer requestTimer, Timer responseTimer) {
		long stop = responseTimer.getStart();
		long start = requestTimer.getStop();
		return stop - start;
	}

	@Override
	public long getConnectingCost() {
		return connectingCost;
	}

	@Override
	public long getDnsLookupCost() {
		return dnsLookupCost;
	}

	@Override
	public long getReceivingCost() {
		return receivingCost;
	}

	@Override
	public long getSendingCost() {
		return sendingCost;
	}

	@Override
	public long getWaitingCost() {
		return waitingCost;
	}
	
	@Override
	public long getBytesSent() {
		return bytesSent;
	}
	
	@Override
	public long getBytesReceived() {
		return bytesReceived;
	}
}
