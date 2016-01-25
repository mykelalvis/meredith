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
package io.cotiviti.meredith;

import java.util.Optional;

public enum BotEvent {
	CHANNEL_ARCHIVED, CHANNEL_CREATED, CHANNEL_DELETED, CHANNEL_RENAMED, CHANNEL_UNARCHIVED, GROUP_JOINED, MESSAGE_DELETED, MESSAGE_UPDATED, MESSAGE_POSTED, REPLY, CONNECTED, REACTION_ADDED, REACTION_REMOVED, UNKNOWN;

	public static Optional<BotEvent> of(String be) {
		BotEvent bev = null;
		if (be != null)
			try {
				bev = BotEvent.valueOf(be);
			} catch (IllegalArgumentException | NullPointerException n) {
				// Do nothing, i.e. bev = null;
			}
		return Optional.ofNullable(bev);
	}
}