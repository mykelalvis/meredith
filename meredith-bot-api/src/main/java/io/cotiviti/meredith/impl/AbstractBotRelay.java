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

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import io.cotiviti.meredith.BotRelay;

/**
 * This is a base for a BotRelay. Note that the basic pattern is that an empty
 * "acceptRegexes" is implicitly "Accept all messages" and an empty
 * "rejectRegexes" is "Reject no messages"
 * 
 * The acceptance order is meant to be implementation-specific, but applying
 * Accept first and Reject second is the preferred implementation.
 * 
 * @author mykelalvis
 *
 */
abstract public class AbstractBotRelay extends AbstractBotVerticle implements BotRelay {
	private final List<Pattern> acceptRegexes;
	private final List<Pattern> rejectRegexes;

	public AbstractBotRelay(final List<Pattern> acceptRegexes, final List<Pattern> rejectRegexes) {
		this.acceptRegexes = acceptRegexes;
		this.rejectRegexes = rejectRegexes;
	}

	@Override
	public Optional<List<Pattern>> getRejectRegexes() {
		return Optional.ofNullable(rejectRegexes);
	}

	@Override
	public Optional<List<Pattern>> getAcceptRegexes() {
		return Optional.ofNullable(acceptRegexes);
	}

}
