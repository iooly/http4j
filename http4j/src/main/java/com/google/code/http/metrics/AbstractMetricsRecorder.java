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
public abstract class AbstractMetricsRecorder implements MetricsRecorder {

	protected Timer dnsTimer;
	protected Timer connectionTimer;
	protected Timer requestTimer;
	protected Timer responseTimer;
	protected Counter<Long> requestTransportCounter;
	protected Counter<Long> responseTransportCounter;
	protected Counter<Integer> connectionCounter;

	protected AbstractMetricsRecorder() {
		init();
	}
	
	abstract protected void init();

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
	public void reset() {
		dnsTimer.reset();
		connectionTimer.reset();
		requestTimer.reset();
		responseTimer.reset();
		requestTransportCounter.reset();
		responseTransportCounter.reset();
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
	public Counter<Integer> getConnectionCounter() {
		return connectionCounter;
	}

	@Override
	public Metrics captureMetrics() {
		return new BasicMetrics();
	}
	
	protected class BasicMetrics implements Metrics {
		@Override
		public long getDnsLookupCost() {
			return dnsTimer.getDuration();
		}

		@Override
		public long getConnectingCost() {
			return connectionTimer.getDuration();
		}

		@Override
		public long getSendingCost() {
			return requestTimer.getDuration();
		}

		@Override
		public long getWaitingCost() {
			return responseTimer.getStart() - requestTimer.getStop();
		}

		@Override
		public long getReceivingCost() {
			return responseTimer.getDuration();
		}

		@Override
		public long getBytesSent() {
			return requestTransportCounter.get();
		}

		@Override
		public long getBytesReceived() {
			return responseTransportCounter.get();
		}
	}
}
