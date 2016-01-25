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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotChannel;
import io.cotiviti.meredith.BotControlEvent;
import io.cotiviti.meredith.BotControlMessage;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public abstract class AbstractBrainVerticle extends AbstractBotVerticle {
	public static final int EXISTS_FAIL = -999;
	public static final int GET_FAIL = -998;
	public static final String BRAIN_VAL = "value";
	public static final String BRAIN_KEY = "key";
	public static final int PUT_FAIL = -997;
	public static final int REMOVE_FAIL = -996;
	public static final int NO_MAP_FAIL = -995;
	private final Logger log = LoggerFactory.getLogger(AbstractBrainVerticle.class);

	public void botStart() {
		EventBus eb = vertx.eventBus();
		MessageConsumer<BotBrainMessage> c = eb.<BotBrainMessage> consumer(BotChannel.BRAIN_MESSAGE_CHANNEL,
				handleBrainMessage());
		JsonObject payload = new JsonObject();
		payload.put(BotControlMessage.NAME_KEY, this.getClass().getCanonicalName());

		BotControlMessage m = new BotControlMessageImpl(payload, BotControlEvent.REGISTER_BRAIN);
		eb.send(BotChannel.CONTROL_IB, m, rep -> {
			if (!rep.succeeded()) {
				log.error("Error registering with Meredith {} ", rep.cause());
				// TODO something bad happened.  Maybe we die here.
			}
		});
	}

	abstract protected Handler<Message<BotBrainMessage>> handleBrainMessage();

	protected String getBrainChannel() {
		return this.getClass().getCanonicalName();
	}
}
