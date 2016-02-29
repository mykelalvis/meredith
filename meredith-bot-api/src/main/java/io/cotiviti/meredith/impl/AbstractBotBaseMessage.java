/**
 * Copyright (C) 2015 Cotiviti Labs (nexgen.admin@cotiviti.io)
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
package io.cotiviti.meredith.impl;

import io.cotiviti.meredith.BotBaseMessage;
import io.vertx.core.json.JsonObject;

public abstract class AbstractBotBaseMessage implements BotBaseMessage {
	private final JsonObject payload;
	private final String from;
	private final String to;

	public AbstractBotBaseMessage(JsonObject payload, String from, String to) {
		this.payload = payload;
		this.from = from;
		this.to = to;
	}

	@Override
	public JsonObject getPayload() {
		return payload;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

}
