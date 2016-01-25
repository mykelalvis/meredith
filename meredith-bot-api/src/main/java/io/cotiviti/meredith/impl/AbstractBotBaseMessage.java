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
