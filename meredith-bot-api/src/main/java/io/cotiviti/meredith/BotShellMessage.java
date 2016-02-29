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

public interface BotShellMessage {
	/**
	 * @return List of Strings that make up the command and any subsequent
	 *         parameters
	 */
	List<String> getCommand();

	/**
	 * @return True if we should merge error output with stdout of the command
	 */
	Boolean isRedirectErrorStream();

	/**
	 * @return True if we should discard all output (i.e. the logging isn't
	 *         important)
	 */
	Boolean isDiscardOutput();

}
