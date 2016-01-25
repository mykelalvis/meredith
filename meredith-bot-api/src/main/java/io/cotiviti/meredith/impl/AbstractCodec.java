package io.cotiviti.meredith.impl;

import io.vertx.core.eventbus.MessageCodec;

abstract public class AbstractCodec<M> implements MessageCodec<M, M> {
	@Override
	public String name() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public M transform(M s) {
		return s;
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
