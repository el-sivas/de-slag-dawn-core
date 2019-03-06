package de.slag.dawn.core.persist.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.slag.root.base.DatabaseConfig;
import de.slag.root.base.SlagConfigSupport;

public class HibernateTest {
	
	@Test
	public void test() {
		final DatabaseConfig dbConfig = SlagConfigSupport.getDefault(DatabaseConfig.class);
		Map<String,String> config = new HashMap<>();
		config.put("hibernate.connection.driver_class", dbConfig.getDriverClass());
		config.put("hibernate.connection.url", dbConfig.getUrl());
		
		config.put("hibernate.connection.username", dbConfig.getUsername());
		config.put("hibernate.connection.password", dbConfig.getPassword());
		config.put("hibernate.dialect", dbConfig.getDialect());
		config.put("hibernate.connection.pool_size", dbConfig.getUrl());
		
		
		

			
	}

}
