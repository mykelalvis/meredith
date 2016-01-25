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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.cotiviti.meredith.BotEvent;
import io.cotiviti.meredith.BotMessage;
import io.vertx.core.json.JsonObject;

public class BotMessageImplTest {

	private BotMessage message;
	private String testPayloadString = UUID.randomUUID().toString();
	private JsonObject testPayload = new JsonObject("{ \"x\" : \"" + testPayloadString + "\"}");
	private String testFrom = UUID.randomUUID().toString();
	private String testTo = UUID.randomUUID().toString();
	private Optional<BotEvent> testBotEvent = Optional
			.of(BotEvent.values()[new Random().nextInt(BotEvent.values().length)]);

	@Before
	public void setUp() throws Exception {
		this.message = new BotMessageImpl(this.testPayload, this.testFrom, this.testTo, testBotEvent);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBotMessageImpl() {
		assertNotNull("Message must have created", this.message);
	}

	@Test
	public void testGetPayload() {
		assertEquals("Get correct payload", message.getPayload(), this.testPayload);
	}

	@Test
	public void testGetFrom() {
		assertEquals("Get correct From", message.getFrom(), this.testFrom);
	}

	@Test
	public void testGetTo() {
		assertEquals("Get correct To", message.getTo(), this.testTo);
	}

	@Test
	public void testGetEvent() {
		assertEquals("Get correct Event", message.getEvent(), this.testBotEvent.get());
	}

}
