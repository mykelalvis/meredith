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
