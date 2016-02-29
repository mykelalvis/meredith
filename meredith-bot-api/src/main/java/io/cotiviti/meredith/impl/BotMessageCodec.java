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

import io.cotiviti.meredith.BotEvent;
import io.cotiviti.meredith.BotMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class BotMessageCodec extends AbstractBotBaseMessageCodec<BotMessage> {
	@Override
	public void encodeToWire(Buffer buffer, BotMessage s) {
		JsonObject obj = new JsonObject();
		obj.put(PAYLOAD, s.getPayload().encode());
		obj.put(FROM, s.getFrom());
		obj.put(TO, s.getTo());
		obj.put(TYPE, s.getEvent().name());
		String enc = obj.encode();
		buffer.appendInt(enc.length());
		buffer.appendString(enc);
	}

	@Override
	public BotMessage decodeFromWire(int pos, Buffer buffer) {
		int len = buffer.getInt(pos);
		String s = buffer.getString(pos += 4, pos += len);
		JsonObject a = new JsonObject(s);
		return new BotMessageImpl(a.getJsonObject(PAYLOAD), a.getString(FROM), a.getString(TO),
				BotEvent.of(a.getString(TYPE)));
	}

}
