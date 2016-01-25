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
