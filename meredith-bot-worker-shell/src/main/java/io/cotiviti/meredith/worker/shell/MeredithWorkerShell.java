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
package io.cotiviti.meredith.worker.shell;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotChannel;
import io.cotiviti.meredith.BotClassification;
import io.cotiviti.meredith.BotControlMessage;
import io.cotiviti.meredith.BotMessage;
import io.cotiviti.meredith.BotShellMessage;
import io.cotiviti.meredith.impl.AbstractBotVerticle;
import io.cotiviti.meredith.impl.BotBrainMessageCodec;
import io.cotiviti.meredith.impl.BotControlMessageCodec;
import io.cotiviti.meredith.impl.BotMessageCodec;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.redis.RedisClient;

@Component
@SpringVerticle(springConfig = MeredithWorkerShellConfig.class)
public class MeredithWorkerShell extends AbstractBotVerticle implements InitializingBean {
	@Autowired
	public String brainChannel;

	@Autowired
	public String redisHost = "127.0.0.1";

	public RedisClient brain;

	public MeredithWorkerShell() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//		String host;
		//		JsonObject c = Vertx.currentContext().config();
		//		host = c.getString("host");
		//		host = host == null ? redisHost : host;
		//		brain = RedisClient.create(vertx, new RedisOptions().setHost(host));
		//
	}

	@Override
	public void botStart() {
		super.botStart(); // Should do nothing
		EventBus eb = vertx.eventBus();
		eb.registerDefaultCodec(BotMessage.class, new BotMessageCodec());
		eb.registerDefaultCodec(BotBrainMessage.class, new BotBrainMessageCodec());
		eb.registerDefaultCodec(BotControlMessage.class, new BotControlMessageCodec());
		Context ctx = vertx.getOrCreateContext();

		MessageConsumer<BotShellMessage> consumer = eb.consumer(BotChannel.SHELL_WORKER_CHANNEL);
		consumer.handler(handleShellCommandMessage());
	}

	@Override
	public String getClassification() {
		return BotClassification.SHELL_WORKER;
	}

	protected Handler<Message<BotShellMessage>> handleShellCommandMessage() {
		return msg -> {
			BotShellMessage m = msg.body();
			Runtime r = Runtime.getRuntime();
			ProcessBuilder pb = new ProcessBuilder();
			pb.command(m.getCommand());
			Map<String, String> env = pb.environment();
			env.put("VAR1", "myValue");
			env.remove("OTHERVAR");
			env.put("VAR2", env.get("VAR1") + "suffix");
			pb.directory(new File("myDir"));
			File log = new File("log");
			pb.redirectErrorStream(m.isRedirectErrorStream());
			pb.redirectOutput(Redirect.appendTo(log));
			Process p;
			try {
				p = pb.start();

				// TODO Do something here instead of checking output
				assert pb.redirectInput() == Redirect.PIPE;
				assert pb.redirectOutput().file() == log;
				assert p.getInputStream().read() == -1;
			} catch (IOException e) {
				msg.fail(-1, e.getMessage());
			}
		};
	}

}
