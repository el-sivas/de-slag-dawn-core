package de.slag.base.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SleepUtils {

	private static final Log LOG = LogFactory.getLog(SleepUtils.class);

	public static void sleepFor(int sleeptimeInMs) {
		LOG.info("sleep for (ms):" + sleeptimeInMs);
		try {
			Thread.sleep(sleeptimeInMs);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
