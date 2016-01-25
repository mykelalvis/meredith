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
package io.cotiviti.meredith.redis;

import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotClassification;
import io.cotiviti.meredith.impl.AbstractBrainVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

@Component
@SpringVerticle(springConfig = MeredithRedisBrainConfig.class)
public class MeredithRedisBrain extends AbstractBrainVerticle implements InitializingBean {
	@Autowired
	public String brainChannel;

	@Autowired
	public String redisHost = "127.0.0.1";

	public RedisClient brain;

	public MeredithRedisBrain() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String host;
		JsonObject c = Vertx.currentContext().config();
		host = c.getString("host");
		host = host == null ? redisHost : host;
		brain = RedisClient.create(vertx, new RedisOptions().setHost(host));

	}

	@Override
	public String getClassification() {
		return BotClassification.BRAIN;
	}

	protected Handler<Message<BotBrainMessage>> handleBrainMessage() {
		return msg -> {
			switch (msg.body().getType()) {
			case GET:
				brain.get(msg.body().getKey(), s -> {
					if (s.succeeded())
						msg.reply(s.result());
					else
						msg.fail(GET_FAIL, "failed to get " + msg.body());
				});
				break;
			case PUT:
				brain.set(msg.body().getKey(), msg.body().getValue().orElse(null), s -> {
					if (!s.succeeded()) {
						msg.fail(PUT_FAIL, s.cause().getMessage());
					}
				});
				break;
			case DEL:
				brain.del(msg.body().getKey(), s -> {
					if (!s.succeeded())
						msg.fail(REMOVE_FAIL, s.cause().getMessage());
				});
				break;
			case EXISTS:
				brain.exists(msg.body().getKey(), s -> {
					if (s.succeeded())
						msg.reply(s.result() == 1L);
					else
						msg.fail(EXISTS_FAIL, s.cause().getMessage());
				});
				break;
			}
		};
	}

}
