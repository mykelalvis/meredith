package io.cotiviti.meredith;

public interface BotControlMessage extends BotBaseMessage {
	public final static String NAME_KEY = "meredith.name";

	BotControlEvent getControlEvent();
}
