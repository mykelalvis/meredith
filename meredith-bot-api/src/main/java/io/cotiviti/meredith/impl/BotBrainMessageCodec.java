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
