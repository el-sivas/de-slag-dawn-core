package de.slag.core.logic.feature;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SlagDevelopment {

	private static final Log LOG = LogFactory.getLog(SlagDevelopment.class);

	private static final String SLAG_DEVELOPMENT_ENABLED = "SlagDevelopmentEnabled";

	private static SlagDevelopment instance;

	public static SlagDevelopment instance() {
		if (instance == null) {
			instance = new SlagDevelopment();
		}
		return instance;
	}

	public static boolean isEnabled() {
		final String property = System.getProperty(SLAG_DEVELOPMENT_ENABLED);
		final boolean devEnabled = BooleanUtils.isTrue(Boolean.valueOf(property));
		if (devEnabled) {
			LOG.warn(SLAG_DEVELOPMENT_ENABLED);
		}
		return devEnabled;
	}

	public boolean getEnabled() {
		return isEnabled();
	}

}
