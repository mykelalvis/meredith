package io.cotiviti.meredith.impl;

public abstract class AbstractBotBaseMessageCodec<M> extends AbstractCodec<M> {
	public static final String TYPE = "_eventType";
	public static final String TO = "_to";
	public static final String FROM = "_from";
	public static final String PAYLOAD = "_payload";

}
