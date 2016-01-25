package io.cotiviti.meredith;

import io.vertx.core.json.JsonObject;

public interface BotBaseMessage {

	JsonObject getPayload();

	String getFrom();

	String getTo();

}
