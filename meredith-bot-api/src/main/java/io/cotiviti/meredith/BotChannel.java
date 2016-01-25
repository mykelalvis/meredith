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

public interface BotChannel {
	/**
	 * Channel that Meredith uses to control the verticles
	 */
	String CONTROL_OB = "meredith.control.ob";
	/**
	 * The channel that all verticles use to respond to Meredith
	 */
	String CONTROL_IB = "meredith.control.ib";
	/** Inbound messages (from the chat system) */
	String INBOUNDMESSAGES = "meredith.inbound.messages";
	/** Outbound messages (to the chat system) */
	String OUTBOUNDMESSAGES = "meredith.outbound.messages";
	/**
	 * The brain listens on a single channel and will only respond
	 * point-to-point
	 */
	String BRAIN_MESSAGE_CHANNEL = "meredith.brain";

	String SHELL_WORKER_CHANNEL = "meredith.worker.shell";
	String REGISTRATIONMESSAGES = "meredith.processing.registartions";

	public String getChannel();
}
