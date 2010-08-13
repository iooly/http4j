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

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class StatusCodes {

	public static final int OK = 200;

	public static final int NO_CONTENT = 204;

	public static final int RESET_CONTENT = 205;

	public static final int NOT_MODIFIED = 304;

	public static boolean hasEntity(int status) {
		return status >= OK 
			&& status != NO_CONTENT 
			&& status != RESET_CONTENT
			&& status != NOT_MODIFIED;
	}
}
