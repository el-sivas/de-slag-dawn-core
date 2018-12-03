package de.slag.base.tools;

import java.lang.management.ManagementFactory;
import java.util.Date;

public class SystemUtils {
	
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	
	public static Date getStartTime() {
		return new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
	}

}
