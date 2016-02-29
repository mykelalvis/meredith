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
package io.cotiviti.meredith.slack;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import io.cotiviti.meredith.BotChannel;
import io.cotiviti.meredith.BotClassification;
import io.cotiviti.meredith.BotEvent;
import io.cotiviti.meredith.impl.AbstractBotRelay;
import io.cotiviti.meredith.impl.BotMessageImpl;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class MeredithSlackRelay extends AbstractBotRelay {
	private final Logger log = LoggerFactory.getLogger(MeredithSlackRelay.class);
	private SlackSession session;
	private String token;

	public MeredithSlackRelay(final String token, List<Pattern> acceptRegexes, List<Pattern> rejectRegexes) {
		super(acceptRegexes, rejectRegexes);
		this.token = token;
	}

	// TODO Move this into the abstract classes?
	public void botStart() {
		session = SlackSessionFactory.createWebSocketSlackSession(token);
		try {
			session.connect();
		} catch (IOException e1) {
			log.warn("{}", e1);
		}
		EventBus eb = vertx.eventBus();
		MessageConsumer<JsonObject> c = eb.consumer(BotChannel.INBOUNDMESSAGES, handleInboundSlackMessage());
		// TODO Do I need to make this registration async?  I think that I must not...
		session.addMessagePostedListener((e, s) -> eb.publish(BotChannel.OUTBOUNDMESSAGES,
				new BotMessageImpl(new JsonObject(e.getJsonSource().toJSONString()), e.getSender().getId(),
						e.getChannel().getId(), translateEvent(e))));
	}

	private Handler<Message<JsonObject>> handleInboundSlackMessage() {
		return msg -> {
			log.info("Handling " + msg.body().encodePrettily());
			// TODO Decide if this goes to a user or a channel
			//  If User
			//		session.sendMessageToUser(userName, message, attachment);
			//  If Channel
			//		session.sendMessage(channel, message, attachment, chatConfiguration)
			// TODO Is there some form of attachment?
		};
	}

	@Override
	public void botStop() {
		super.botStop();
		try {
			this.session.disconnect();
		} catch (IOException | NullPointerException e) {
			log.error("Error stopping MeredithSlackAdapter:  {} ", e);
		}
	}

	@Override
	public String getClassification() {
		return BotClassification.RELAY;
	}

	private static Optional<BotEvent> translateEvent(SlackMessagePosted e) {
		BotEvent evt = BotEvent.UNKNOWN;
		SlackEventType x = e.getEventType();
		// Yeah, I probably could've used a regex to make these...
		if (x != null)
			switch (x) {
			case SLACK_CHANNEL_ARCHIVED:
				evt = BotEvent.CHANNEL_ARCHIVED;
				break;
			case SLACK_CHANNEL_CREATED:
				evt = BotEvent.CHANNEL_CREATED;
				break;
			case SLACK_CHANNEL_DELETED:
				evt = BotEvent.CHANNEL_DELETED;
				break;
			case SLACK_CHANNEL_RENAMED:
				evt = BotEvent.CHANNEL_RENAMED;
				break;
			case SLACK_CHANNEL_UNARCHIVED:
				evt = BotEvent.CHANNEL_UNARCHIVED;
				break;
			case SLACK_GROUP_JOINED:
				evt = BotEvent.GROUP_JOINED;
				break;
			case SLACK_MESSAGE_DELETED:
				evt = BotEvent.MESSAGE_DELETED;
				break;
			case SLACK_MESSAGE_UPDATED:
				evt = BotEvent.MESSAGE_UPDATED;
				break;
			case SLACK_MESSAGE_POSTED:
				evt = BotEvent.MESSAGE_POSTED;
				break;
			case SLACK_REPLY:
				evt = BotEvent.REPLY;
				break;
			case SLACK_CONNECTED:
				evt = BotEvent.CONNECTED;
				break;
			case REACTION_ADDED:
				evt = BotEvent.REACTION_ADDED;
				break;
			case REACTION_REMOVED:
				evt = BotEvent.REACTION_REMOVED;
				break;
			case UNKNOWN:
				evt = BotEvent.UNKNOWN;
				break;

			}
		return Optional.of(evt);
	}

}
