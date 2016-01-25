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
import org.springframework.stereotype.Component;

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotClassification;
import io.cotiviti.meredith.impl.AbstractBrainVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

@Component
@SpringVerticle(springConfig = LocalMappedBrainConfig.class)
public class LocalMappedBrain extends AbstractBrainVerticle implements InitializingBean {

	@Override
	public String getClassification() {
		return BotClassification.BRAIN;
	}

	protected Handler<Message<BotBrainMessage>> handleBrainMessage() {
		return msg -> {
			BotBrainMessage j = msg.body();
			SharedData sd = vertx.sharedData();
			sd.<String, String> getClusterWideMap(getBrainChannel(), res -> {
				if (!res.succeeded())
					msg.fail(NO_MAP_FAIL, res.cause().getMessage());
				AsyncMap<String, String> map = res.result();
				switch (j.getType()) {
				case GET:
					map.get(j.getKey(), s -> {
						if (s.succeeded())
							msg.reply(s.result());
						else
							msg.fail(GET_FAIL, "failed to get " + j);
					});
					break;
				case PUT:
					map.put(j.getKey(), j.getValue().orElse(null), s -> {
						if (!s.succeeded())
							msg.fail(PUT_FAIL, s.cause().getMessage());
					});
					break;
				case DEL:
					map.remove(j.getKey(), s -> {
						if (!s.succeeded())
							msg.fail(REMOVE_FAIL, s.cause().getMessage());
					});
					break;
				case EXISTS:
					map.get(j.getKey(), s -> {
						if (s.succeeded())
							msg.reply(true);
						else
							msg.reply(false);
					});
					break;
				}
			});
		};
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}
}