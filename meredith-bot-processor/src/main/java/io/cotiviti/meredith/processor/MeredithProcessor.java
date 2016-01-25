package io.cotiviti.meredith.processor;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cotiviti.meredith.BotChannel;
import io.cotiviti.meredith.BotClassification;
import io.cotiviti.meredith.impl.AbstractBotVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class MeredithProcessor extends AbstractBotVerticle {
	private final Logger log = LoggerFactory.getLogger(MeredithProcessor.class);
	private MessageConsumer<JsonObject> consumer;
	private ConcurrentHashMap<String, String> registrations = new ConcurrentHashMap<>();
	private MessageConsumer<JsonObject> registrar;

	@Override
	public String getClassification() {
		return BotClassification.PROCESSOR;
	}

	@Override
	public void botStart() {
		super.botStart();
		EventBus eb = vertx.eventBus();
		this.registrar = eb.consumer(BotChannel.REGISTRATIONMESSAGES, processRegistrationMessages());
		this.consumer = eb.consumer(BotChannel.OUTBOUNDMESSAGES, processChatMessages());
	}

	private Handler<Message<JsonObject>> processRegistrationMessages() {
		return msg -> {
			log.info("Processing registration message " + msg.body().encodePrettily());
			// TODO Figure out what we do when someone registers
		};
	}

	private Handler<Message<JsonObject>> processChatMessages() {
		return msg -> {
			log.info("Processing " + msg.body().encodePrettily());
			JsonObject o = msg.body();
			String type = o.getString("type");
			String t = this.registrations.get(type);
			// How will I know where to send which messages
			// Currently, all we process are simple command-based messages
		};
	}

	@Override
	public void botStop() {
		registrar.unregister(regres -> {
			if (regres.succeeded()) {
				log.info("Deregistration of registrar succeeded");
				consumer.unregister(res -> {
					if (res.succeeded()) {
						log.info("Deregistration succeeded");
						// TODO unreg consumer succeded
					} else {
						log.error("{}", res.cause());
						// TODO failed
					}
				});
			} else {
				log.error("Deregistration of registration failed due to {}", regres.cause());
			}
		});
		super.botStop();
	}
}
