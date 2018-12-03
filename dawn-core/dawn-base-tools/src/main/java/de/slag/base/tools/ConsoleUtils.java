package de.slag.base.tools;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConsoleUtils {

	private static final Log LOG = LogFactory.getLog(ConsoleUtils.class);

	public static String determineUserHome() {
		return System.getProperty("user.home");
	}

	public static String runConsoleCommand(final String command) {
		try {
			return runInternal(command);
		} catch (final IOException e) {
			throw new RuntimeException("error execute command: " + command, e);
		}
	}

	// TODO: error bzw. return handling. bisher geht alles auf error.
	private static String runInternal(final String command) throws IOException {
		final Process exec = Runtime.getRuntime().exec(command);
		while (exec.isAlive()) {
			LOG.debug("process alive, sleep.");
			SleepUtils.sleepFor(50);
		}

		final String error = readInput(exec.getErrorStream());
		if(StringUtils.isNotBlank(error)) {
			throw new IOException(error);
		}
		return readInput(exec.getInputStream());
	}	
	

	private static String readInput(final InputStream errorStream) throws IOException {
		final StringBuilder errorBuffer = new StringBuilder();
		while (true) {
			final int read = errorStream.read();
			if (read == -1) {
				break;
			}
			final char c = (char) read;
			errorBuffer.append(c);
		}
		return errorBuffer.toString();
	}

}
