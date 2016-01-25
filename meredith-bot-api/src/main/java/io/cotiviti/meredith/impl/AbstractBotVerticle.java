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
import io.cotiviti.meredith.BotControlMessage;
import io.cotiviti.meredith.BotInitializable;
import io.cotiviti.meredith.BotMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

/**
 * AbstractBotVerticle should respond to control messages on BotChannel.CONTROL
 * 
 * @author malvis
 *
 */
public abstract class AbstractBotVerticle extends AbstractVerticle implements BotInitializable {
	private final Logger log = LoggerFactory.getLogger(AbstractBotVerticle.class);

	public AbstractBotVerticle() {
		super();
		this.botInitialize();
	}

	abstract public String getClassification();

	@Override
	public void start() throws Exception {
		super.start();
		this.botStart();
	}

	@Override
	public void stop() throws Exception {
		this.botStop();
		super.stop();
	}

	@Override
	protected void finalize() throws Throwable {
		this.botDestroy();
		super.finalize();
	}

	private Handler<Message<JsonObject>> controlHandler() {
		return msg -> {
			// TODO What do we do with control messages?
			JsonObject x = msg.body();
		};
	}

	private Handler<AsyncResult<Void>> controlCompletionHandler() {
		return res -> {
			if (res.succeeded()) {
				// TODO registration propagated
				log.info("Control channel propagated for " + this.context.deploymentID());
				// TODO This needs to register the consumers for messages now
			} else {
				log.error("Control channel propagation failed for " + this.context.deploymentID());
			}
		};
	}

	public void start(Future<Void> future) throws Exception {
		EventBus eb = vertx.eventBus();
		eb.registerDefaultCodec(BotMessage.class, new BotMessageCodec());
		eb.registerDefaultCodec(BotBrainMessage.class, new BotBrainMessageCodec());
		eb.registerDefaultCodec(BotControlMessage.class, new BotControlMessageCodec());
		Context ctx = vertx.getOrCreateContext();

		MessageConsumer<JsonObject> consumer = eb.consumer(BotChannel.CONTROL_OB, controlHandler());
		consumer.completionHandler(controlCompletionHandler());

		JsonObject o = new JsonObject();
		o.put("id", ctx.deploymentID());
		o.put("classification", getClassification());
		eb.send(BotChannel.CONTROL_IB, o, ar -> {
			if (!ar.succeeded()) {
				log.error("Registration with Meredith didn't succeed");
				future.fail(ar.cause());
			} else {
				log.info("Registered as " + ctx.deploymentID() + " as a " + o.getString("classification"));
				try {
					this.start();
					future.complete();
				} catch (Exception e) {
					future.fail(e);
				}
			}
		});
	}

}
