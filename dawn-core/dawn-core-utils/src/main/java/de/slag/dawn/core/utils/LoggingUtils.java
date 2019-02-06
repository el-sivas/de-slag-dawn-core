package de.slag.dawn.core.utils;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class LoggingUtils {
	
	private static final Log LOG = LogFactory.getLog(LoggingUtils.class);
	
	public static void activateLogging() {
		activateLogging(Level.INFO, "de.slag");
		LOG.info("logging activated");
	}

	public static void activateLogging(final Level info, final String loggerName) {
		Configurator.setLevel(loggerName, info);
	}
	

	
	

}
