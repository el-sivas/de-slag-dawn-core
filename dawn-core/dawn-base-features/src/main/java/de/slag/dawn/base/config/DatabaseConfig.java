package de.slag.dawn.base.config;

public interface DatabaseConfig extends SlagConfig, Comparable<DatabaseConfig> {

	String getDriverClass();

	String getUrl();

	String getUsername();

	String getPassword();

	String getDialect();

	@Override
	// FIXME
	default int compareTo(DatabaseConfig arg0) {		
		return getUrl().compareTo(arg0.getUrl());
	}
}