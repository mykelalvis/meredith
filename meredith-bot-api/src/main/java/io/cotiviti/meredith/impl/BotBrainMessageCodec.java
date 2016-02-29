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

import java.util.Optional;

import io.cotiviti.meredith.BotBrainMessage;
import io.cotiviti.meredith.BotBrainMessageType;
import io.vertx.core.buffer.Buffer;

public class BotBrainMessageCodec extends AbstractCodec<BotBrainMessage> {
	@Override
	public void encodeToWire(Buffer buffer, BotBrainMessage s) {
		buffer.appendInt(s.getType().ordinal());
		buffer.appendInt(s.getKey().length());
		int vLen = (s.getValue().isPresent()) ? s.getValue().get().length() : -1;
		buffer.appendInt(vLen);
		buffer.appendString(s.getKey());
		if (s.getValue().isPresent())
			buffer.appendString(s.getValue().get());
	}

	@Override
	public BotBrainMessage decodeFromWire(int pos, Buffer buffer) {
		int cPos = pos;
		int type = buffer.getInt(cPos);
		int len = buffer.getInt(cPos += 4);
		int len2 = buffer.getInt(cPos += 4);
		String key = buffer.getString(cPos += 4, cPos += len);
		String y = null;
		if (len2 != -1)
			y = buffer.getString(cPos, cPos += len2);
		return new BotBrainMessageImpl(BotBrainMessageType.values()[type], key, Optional.ofNullable(y));
	}

}
