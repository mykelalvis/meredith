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

import java.util.Optional;

import io.cotiviti.meredith.BotEvent;
import io.cotiviti.meredith.BotMessage;
import io.vertx.core.json.JsonObject;

public class BotMessageImpl extends AbstractBotBaseMessage implements BotMessage {
	private final BotEvent event;

	public BotMessageImpl(JsonObject payload, String from, String to, Optional<BotEvent> event) {
		super(payload, from, to);
		this.event = event.orElse(BotEvent.UNKNOWN);
	}

	public BotEvent getEvent() {
		return event;
	}
}
