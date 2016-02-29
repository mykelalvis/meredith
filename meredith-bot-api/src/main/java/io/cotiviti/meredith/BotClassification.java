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

public interface BotClassification {
	public static final String CONTROL = "meredith.control";
	public static final String RELAY = "meredith.relay";
	public static final String BRAIN = "meredith.brain";
	public static final String CUSTOM = "meredith.custom";
	public static final String AUTOMATION_CONTROLLER = "meredith.automation";
	public static final String GENERIC_WORKER = "meredith.worker.generic";
	public static final String SHELL_WORKER = "meredith.worker.shell";
	public static final String PROCESSOR = "meredith.processor";
}
