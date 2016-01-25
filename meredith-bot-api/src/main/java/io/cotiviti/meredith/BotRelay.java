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

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Relays all matching messages to the Meredith processor
 * 
 * @author mykel.alvis@gmail.com
 *
 */
public interface BotRelay extends BotInitializable {

	/**
	 * Returns Patterns that are used to reject a message. This is usually
	 * nothing, since you don't reject messages, but you might want to ignore
	 * some on a particular case-by-case basis
	 * 
	 * If a message matches ANY of the RejectRegexes, then the message will
	 * never be relayed to Meredith
	 * 
	 * @return
	 */
	Optional<List<Pattern>> getRejectRegexes();

	/**
	 * Returns Patterns used to determing if a message gets listened to or not
	 * By default, you should listen to every message, but you might want to
	 * filter some out
	 * 
	 * If a message matches ANY of the AcceptRegexes, it is relayed
	 * 
	 * @return
	 */
	Optional<List<Pattern>> getAcceptRegexes();
}
