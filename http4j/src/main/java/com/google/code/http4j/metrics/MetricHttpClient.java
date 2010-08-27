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

package com.google.code.http4j.metrics;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.code.http4j.Container;
import com.google.code.http4j.HttpRequest;
import com.google.code.http4j.HttpResponse;
import com.google.code.http4j.impl.BasicHttpClient;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public class MetricHttpClient extends BasicHttpClient {
	
	protected AggregatedMetrics metrics;
	
	public MetricHttpClient() {
		super();
		metrics = createMetrics();
	}
	
	@Override
	protected Container createContainer() {
		return new MetricContainer();
	}
	
	protected AggregatedMetrics createMetrics() {
		return new AggregatedMetrics();
	}
	
	@Override
	protected HttpResponse submit(HttpRequest request, boolean parseEntity)
			throws IOException, URISyntaxException {
		HttpResponse response = super.submit(request, parseEntity);
		metrics.aggregate(ThreadLocalMetrics.getInstance());
		return response;
	}

	public AggregatedMetrics getMetrics() {
		return metrics;
	}
}
