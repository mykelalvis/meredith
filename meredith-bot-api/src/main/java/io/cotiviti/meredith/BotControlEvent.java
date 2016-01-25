package io.cotiviti.meredith;

import java.util.Optional;

public enum BotControlEvent {
	STOP, START, SCALE, PING, PONG, DEREGISTER, REGISTER_BRAIN, UNKNOWN;
	public static Optional<BotControlEvent> of(String be) {
		BotControlEvent bev = null;
		if (be != null)
			try {
				bev = BotControlEvent.valueOf(be);
			} catch (IllegalArgumentException | NullPointerException n) {
				// Do nothing, i.e. bev = null;
				bev = BotControlEvent.UNKNOWN;
			}
		return Optional.of(bev);
	}
}
