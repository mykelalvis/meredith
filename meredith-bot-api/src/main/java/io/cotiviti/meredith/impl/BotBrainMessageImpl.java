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

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotBrainMessageType;
import io.vertx.core.json.JsonObject;

public class BotBrainMessageImpl implements BotBrainMessage {
	private final BotBrainMessageType type;
	private final String key;
	private final Optional<String> value;

	public static Optional<BotBrainMessage> fromJson(JsonObject j) {
		BotBrainMessage b = null;
		Optional<Integer> t1 = Optional.ofNullable(j.getInteger(BotBrainMessageType.BRAIN_MESSAGE_TYPE_FIELD));
		Optional<String> k = Optional.ofNullable(j.getString(BotBrainMessage.KEY_FIELD));
		Optional<String> v = Optional.ofNullable(j.getString(BotBrainMessage.VALUE_FIELD));
		if (t1.isPresent() && k.isPresent())
			b = new BotBrainMessageImpl(BotBrainMessageType.values()[t1.get()], k.get(), v);
		return Optional.ofNullable(b);
	}

	public BotBrainMessageImpl(BotBrainMessageType type, String key, Optional<String> value) {
		this.type = type;
		this.key = key;
		this.value = value;
	}

	@Override
	public BotBrainMessageType getType() {
		return type;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Optional<String> getValue() {
		return value;
	}

}
