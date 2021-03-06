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
 */
public interface Metrics {

	long getBlockingCost();// acquire connection permit cost
	
	long getDnsLookupCost();

	long getConnectingCost();

	long getSendingCost();

	long getWaitingCost();// sending --|-- waiting --|-- receiving

	long getReceivingCost();
	
	long getBytesSent();
	
	long getBytesReceived();
	
	long getSslHandshakeCost();
	
	void setParentMetrics(Metrics sourceMetrics);// return this
	
	Metrics getParentMetrics();// used while redirection happens
}
